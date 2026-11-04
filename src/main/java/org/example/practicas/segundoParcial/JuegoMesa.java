package org.example.practicas.segundoParcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

import org.example.estructuradedatos.ListaCircular;

/**
 * Partida. Los turnos son una lista circular: quien juega pasa al siguiente
 * y quien se queda sin dinero sale del anillo.
 */
public class JuegoMesa {

    private static final int MINIMO_JUGADORES = 2;
    private static final int MAXIMO_JUGADORES = 6;

    private final ListaCircular<Jugador> turnos;
    private final Tablero tablero;
    private final MazoSuerte mazo;
    private final RegistroBanco banco;
    private final Random dados;

    public JuegoMesa(Random dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Los dados no pueden ser nulos.");
        }
        this.dados = dados;
        this.turnos = new ListaCircular<>();
        this.tablero = new Tablero();
        this.mazo = new MazoSuerte();
        this.banco = new RegistroBanco();
    }

    public void jugar(BufferedReader entrada) {
        leerJugadores(entrada);
        System.out.println();
        System.out.println("Cada jugador empieza con $" + Jugador.DINERO_INICIAL + ".");
        System.out.println("El tablero es un anillo. Pasar por Salida paga $" + Tablero.BONO_VUELTA + ".");
        tablero.mostrar(turnos);

        while (turnos.tamano() > 1) {
            Jugador actual = turnos.obtener(0);
            System.out.println();
            System.out.println("Turno de " + actual.getNickname() + ". " + describirEspera());
            System.out.println("  " + actual);
            boolean jugo = false;
            while (!jugo) {
                mostrarOpciones();
                switch (leerLinea(entrada).trim()) {
                    case "1" -> jugo = tirarYMover(entrada, actual);
                    case "2" -> tablero.mostrar(turnos);
                    case "3" -> banco.mostrarUltimo();
                    case "4" -> mostrarJugadores();
                    default -> System.out.println("  Opción no válida.");
                }
            }
            if (!actual.tieneDinero()) {
                turnos.eliminar(actual);
                System.out.println("  " + actual.getNickname() + " se quedó sin dinero y sale de los turnos.");
            } else if (turnos.tamano() > 1) {
                turnos.rotar();
            }
        }

        System.out.println();
        if (turnos.estaVacia()) {
            System.out.println("No queda nadie con dinero.");
        } else {
            System.out.println(turnos.obtener(0).getNickname() + " es el último con dinero y gana.");
        }
    }

    private boolean tirarYMover(BufferedReader entrada, Jugador actual) {
        int primero = dados.nextInt(6) + 1;
        int segundo = dados.nextInt(6) + 1;
        int pasos = primero + segundo;
        System.out.println("  Dados: " + primero + " + " + segundo + " = " + pasos + ".");
        int vueltas = 0;
        for (int i = 0; i < pasos; i++) {
            if (actual.avanzar().getTipo() == Casilla.SALIDA) {
                vueltas++;
            }
        }
        if (vueltas > 0) {
            int bono = vueltas * Tablero.BONO_VUELTA;
            actual.cobrar(bono);
            System.out.println("  Pasa por Salida y cobra $" + bono + ".");
        }
        System.out.println("  Cae en " + actual.getCasilla().getNumero() + " " + actual.getCasilla().getNombre() + ".");
        resolver(entrada, actual, true);
        return true;
    }

    private void resolver(BufferedReader entrada, Jugador actual, boolean puedeSacarCarta) {
        if (!actual.tieneDinero()) {
            return;
        }
        Casilla casilla = actual.getCasilla();
        switch (casilla.getTipo()) {
            case Casilla.PROPIEDAD -> resolverPropiedad(entrada, actual, casilla);
            case Casilla.IMPUESTO -> cobrarMulta(actual, casilla.getNombre(), casilla.getImpuesto());
            case Casilla.SUERTE -> {
                if (puedeSacarCarta) {
                    aplicarCarta(entrada, actual);
                }
            }
            default -> {
            }
        }
    }

    private void resolverPropiedad(BufferedReader entrada, Jugador actual, Casilla casilla) {
        if (!casilla.tieneDueno()) {
            ofrecerCompra(entrada, actual, casilla);
            return;
        }
        if (casilla.getDueno() == actual) {
            ofrecerCasa(entrada, actual, casilla);
            return;
        }
        int renta = casilla.rentaActual();
        int pago = actual.pagar(renta);
        casilla.getDueno().cobrar(pago);
        System.out.println("  Paga $" + pago + " de renta a " + casilla.getDueno().getNickname() + ".");
        if (!actual.tieneDinero()) {
            System.out.println("  No le alcanzó para la renta.");
        }
    }

    private void ofrecerCompra(BufferedReader entrada, Jugador actual, Casilla casilla) {
        if (!actual.puedePagar(casilla.getPrecio())) {
            System.out.println("  " + casilla.getNombre() + " cuesta $" + casilla.getPrecio()
                    + " y no puede comprarla sin quedarse en cero.");
            return;
        }
        System.out.print("  ¿Comprar " + casilla.getNombre() + " por $" + casilla.getPrecio() + "? (s/n): ");
        System.out.flush();
        if (!esSi(leerLinea(entrada))) {
            return;
        }
        actual.pagar(casilla.getPrecio());
        casilla.asignarDueno(actual);
        String detalle = casilla.getNombre() + " a " + actual.getNickname() + " por $" + casilla.getPrecio();
        banco.anotar(new Asiento(Asiento.VENTA, detalle));
        System.out.println("  Comprada. " + actual.getNickname() + " queda con $" + actual.getDinero() + ".");
    }

    private void ofrecerCasa(BufferedReader entrada, Jugador actual, Casilla casilla) {
        if (!casilla.puedeConstruir()) {
            System.out.println("  " + casilla.getNombre() + " ya tiene " + Casilla.MAXIMO_CASAS + " casas.");
            return;
        }
        if (!actual.puedePagar(Casilla.COSTO_CASA)) {
            System.out.println("  Construir una casa cuesta $" + Casilla.COSTO_CASA + " y no le alcanza.");
            return;
        }
        System.out.print("  ¿Construir una casa en " + casilla.getNombre() + " por $" + Casilla.COSTO_CASA + "? (s/n): ");
        System.out.flush();
        if (!esSi(leerLinea(entrada))) {
            return;
        }
        actual.pagar(Casilla.COSTO_CASA);
        casilla.construirCasa();
        String detalle = casilla.getNombre() + " por " + actual.getNickname()
                + ". Renta $" + casilla.rentaActual();
        banco.anotar(new Asiento(Asiento.CASA, detalle));
        System.out.println("  Casa lista. Renta actual $" + casilla.rentaActual() + ".");
    }

    private void aplicarCarta(BufferedReader entrada, Jugador actual) {
        CartaSuerte carta = mazo.tomar();
        System.out.println("  Carta superior: " + carta.getTexto() + ".");
        System.out.println("  La carta va al fondo. Ahora arriba está: " + mazo.superior().getTexto() + ".");
        switch (carta.getEfecto()) {
            case CartaSuerte.RETROCEDER -> {
                for (int i = 0; i < carta.getMagnitud(); i++) {
                    actual.retroceder();
                }
                System.out.println("  Retrocede a " + actual.getCasilla().getNumero()
                        + " " + actual.getCasilla().getNombre() + ".");
                resolver(entrada, actual, false);
            }
            case CartaSuerte.AVANZAR -> {
                for (int i = 0; i < carta.getMagnitud(); i++) {
                    if (actual.avanzar().getTipo() == Casilla.SALIDA) {
                        actual.cobrar(Tablero.BONO_VUELTA);
                        System.out.println("  Pasa por Salida y cobra $" + Tablero.BONO_VUELTA + ".");
                    }
                }
                System.out.println("  Avanza a " + actual.getCasilla().getNumero()
                        + " " + actual.getCasilla().getNombre() + ".");
                resolver(entrada, actual, false);
            }
            case CartaSuerte.MULTA -> cobrarMulta(actual, "carta de suerte", carta.getMagnitud());
            case CartaSuerte.COBRAR -> {
                actual.cobrar(carta.getMagnitud());
                System.out.println("  Cobra $" + carta.getMagnitud() + ". Ahora tiene $" + actual.getDinero() + ".");
            }
            default -> {
            }
        }
    }

    private void cobrarMulta(Jugador actual, String motivo, int monto) {
        int pago = actual.pagar(monto);
        banco.anotar(new Asiento(Asiento.MULTA, motivo + " a " + actual.getNickname() + " por $" + pago));
        System.out.println("  Multa de $" + pago + " por " + motivo + ".");
        if (!actual.tieneDinero()) {
            System.out.println("  Se quedó sin dinero al pagar la multa.");
        }
    }

    private void mostrarOpciones() {
        System.out.println("  1  Tirar los dados");
        System.out.println("  2  Ver tablero");
        System.out.println("  3  Ver último registro del banco");
        System.out.println("  4  Ver jugadores");
        System.out.print("Opción: ");
        System.out.flush();
    }

    private void mostrarJugadores() {
        for (Jugador jugador : turnos) {
            System.out.println("  " + jugador);
        }
    }

    private String describirEspera() {
        if (turnos.tamano() <= 1) {
            return "Nadie espera.";
        }
        StringBuilder texto = new StringBuilder("Después siguen:");
        boolean esQuienJuega = true;
        for (Jugador jugador : turnos) {
            if (esQuienJuega) {
                esQuienJuega = false;
                continue;
            }
            texto.append(' ').append(jugador.getNickname());
        }
        return texto.toString();
    }

    private void leerJugadores(BufferedReader entrada) {
        int cantidad = leerCantidad(entrada);
        for (int i = 1; i <= cantidad; i++) {
            Jugador jugador = new Jugador(leerNickname(entrada, i));
            jugador.fijarFicha(tablero.fichaEnSalida());
            turnos.insertarAlFinal(jugador);
        }
    }

    private int leerCantidad(BufferedReader entrada) {
        while (true) {
            System.out.print("¿Cuántos jugadores? (" + MINIMO_JUGADORES + " a " + MAXIMO_JUGADORES + "): ");
            System.out.flush();
            int cantidad = leerEntero(entrada);
            if (cantidad >= MINIMO_JUGADORES && cantidad <= MAXIMO_JUGADORES) {
                return cantidad;
            }
            System.out.println("  Tiene que ser un número entre " + MINIMO_JUGADORES
                    + " y " + MAXIMO_JUGADORES + ".");
        }
    }

    private String leerNickname(BufferedReader entrada, int orden) {
        while (true) {
            System.out.print("Nickname del jugador " + orden + ": ");
            System.out.flush();
            String nickname = leerLinea(entrada).trim();
            if (nickname.isEmpty()) {
                System.out.println("  El nickname no puede estar vacío.");
                continue;
            }
            if (nicknameRepetido(nickname)) {
                System.out.println("  Ese nickname ya está en la partida.");
                continue;
            }
            return nickname;
        }
    }

    private boolean nicknameRepetido(String nickname) {
        for (Jugador jugador : turnos) {
            if (jugador.getNickname().equalsIgnoreCase(nickname)) {
                return true;
            }
        }
        return false;
    }

    private static boolean esSi(String linea) {
        String respuesta = linea.trim();
        return respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si")
                || respuesta.equalsIgnoreCase("sí");
    }

    private int leerEntero(BufferedReader entrada) {
        try {
            return Integer.parseInt(leerLinea(entrada).trim());
        } catch (NumberFormatException ignorado) {
            return -1;
        }
    }

    private String leerLinea(BufferedReader entrada) {
        try {
            String linea = entrada.readLine();
            if (linea == null) {
                throw new IllegalStateException("Se cerró la entrada.");
            }
            descartarEnterDuplicado(entrada);
            return linea;
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo leer la consola.", error);
        }
    }

    private void descartarEnterDuplicado(BufferedReader entrada) throws IOException {
        try {
            Thread.sleep(50);
        } catch (InterruptedException interrumpido) {
            Thread.currentThread().interrupt();
        }
        if (!entrada.ready()) {
            return;
        }
        entrada.mark(2);
        int extra = entrada.read();
        if (extra != '\n' && extra != '\r') {
            entrada.reset();
            return;
        }
        if (!entrada.ready()) {
            return;
        }
        entrada.mark(1);
        int par = entrada.read();
        boolean esPar = (extra == '\r' && par == '\n') || (extra == '\n' && par == '\r');
        if (!esPar) {
            entrada.reset();
        }
    }
}
