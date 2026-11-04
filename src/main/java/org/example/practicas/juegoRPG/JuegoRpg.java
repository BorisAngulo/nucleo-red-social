package org.example.practicas.juegoRPG;

import java.io.BufferedReader;
import java.io.IOException;

import org.example.estructuradedatos.ListaCircular;

/**
 * Combate por turnos. Los jugadores vivos forman una lista circular:
 * la cabeza actúa y, si sigue en pie, rotar la pasa al final.
 * Quien se queda sin vida sale del anillo.
 */
public class JuegoRpg {

    private static final int MINIMO_JUGADORES = 2;
    private static final int MAXIMO_JUGADORES = 6;

    private final ListaCircular<Jugador> turnos;
    private final Historial historial;

    public JuegoRpg() {
        this.turnos = new ListaCircular<>();
        this.historial = new Historial();
    }

    public void jugar(BufferedReader entrada) {
        leerJugadores(entrada);
        System.out.println();
        System.out.println("Combate. Vida " + Jugador.VIDA_INICIAL + ", ataque " + Jugador.ATAQUE_INICIAL + ".");
        System.out.println("Cada quien empieza con una poción, una piedra de fuerza y una bomba.");
        mostrarEstado();

        while (turnos.tamano() > 1) {
            Jugador actual = turnos.obtener(0);
            System.out.println();
            System.out.println("Turno de " + actual.getNickname() + ". " + describirEspera());
            System.out.println("  " + actual);
            boolean actuo = false;
            while (!actuo) {
                mostrarOpciones();
                switch (leerLinea(entrada).trim()) {
                    case "1" -> actuo = atacar(entrada, actual);
                    case "2" -> actuo = usarItem(entrada, actual);
                    case "3" -> actual.getInventario().mostrar();
                    case "4" -> historial.mostrar();
                    case "5" -> mostrarEstado();
                    default -> System.out.println("  Opción no válida.");
                }
            }
            if (turnos.tamano() > 1 && actual.sigueEnPie()) {
                turnos.rotar();
            }
        }

        Jugador ganador = turnos.estaVacia() ? null : turnos.obtener(0);
        System.out.println();
        if (ganador == null) {
            System.out.println("No queda nadie en pie.");
        } else {
            String cierre = ganador.getNickname() + " gana el combate.";
            System.out.println(cierre);
            historial.registrar(cierre);
        }
        System.out.println();
        System.out.println("Historial de la partida");
        historial.mostrar();
    }

    private boolean atacar(BufferedReader entrada, Jugador actual) {
        Jugador objetivo = elegirObjetivo(entrada, actual);
        if (objetivo == null) {
            return false;
        }
        objetivo.recibirDano(actual.getAtaque());
        String suceso = actual.getNickname() + " ataca a " + objetivo.getNickname()
                + " y le quita " + actual.getAtaque() + ". Vida de " + objetivo.getNickname()
                + ": " + objetivo.getPuntosVida() + ".";
        anotar(suceso);
        if (!objetivo.sigueEnPie()) {
            sacarDelAnillo(objetivo);
        }
        return true;
    }

    private boolean usarItem(BufferedReader entrada, Jugador actual) {
        Inventario inventario = actual.getInventario();
        if (inventario.estaVacio()) {
            System.out.println("  No te quedan items.");
            return false;
        }
        inventario.mostrar();
        System.out.print("  Número del item (0 cancela): ");
        System.out.flush();
        int posicion = leerEntero(entrada);
        if (posicion == 0) {
            return false;
        }
        Item item = inventario.obtener(posicion);
        if (item == null) {
            System.out.println("  No hay un item en esa posición.");
            return false;
        }
        if (item.getEfecto() == Item.DANO) {
            Jugador objetivo = elegirObjetivo(entrada, actual);
            if (objetivo == null) {
                return false;
            }
            inventario.quitar(posicion);
            objetivo.recibirDano(item.getMagnitud());
            String suceso = actual.getNickname() + " usa " + item.getNombre() + " contra "
                    + objetivo.getNickname() + " y le quita " + item.getMagnitud()
                    + ". Vida de " + objetivo.getNickname() + ": " + objetivo.getPuntosVida() + ".";
            anotar(suceso);
            if (!objetivo.sigueEnPie()) {
                sacarDelAnillo(objetivo);
            }
            return true;
        }
        inventario.quitar(posicion);
        if (item.getEfecto() == Item.CURAR) {
            actual.curar(item.getMagnitud());
            anotar(actual.getNickname() + " usa " + item.getNombre() + " y recupera "
                    + item.getMagnitud() + ". Vida: " + actual.getPuntosVida() + ".");
        } else {
            actual.aumentarAtaque(item.getMagnitud());
            anotar(actual.getNickname() + " usa " + item.getNombre() + " y su ataque sube a "
                    + actual.getAtaque() + ".");
        }
        return true;
    }

    private void sacarDelAnillo(Jugador caido) {
        turnos.eliminar(caido);
        anotar(caido.getNickname() + " se quedó sin vida y sale del anillo.");
    }

    private Jugador elegirObjetivo(BufferedReader entrada, Jugador actual) {
        int disponibles = turnos.tamano() - 1;
        if (disponibles <= 0) {
            System.out.println("  No hay a quién atacar.");
            return null;
        }
        System.out.println("  ¿A quién?");
        int numero = 1;
        for (Jugador jugador : turnos) {
            if (jugador == actual) {
                continue;
            }
            System.out.println("  " + numero + ". " + jugador.getNickname()
                    + " (vida " + jugador.getPuntosVida() + ")");
            numero++;
        }
        System.out.print("  Número (0 cancela): ");
        System.out.flush();
        int elegido = leerEntero(entrada);
        if (elegido == 0) {
            return null;
        }
        numero = 1;
        for (Jugador jugador : turnos) {
            if (jugador == actual) {
                continue;
            }
            if (numero == elegido) {
                return jugador;
            }
            numero++;
        }
        System.out.println("  Ese número no está en la lista.");
        return null;
    }

    private void mostrarEstado() {
        System.out.println("En pie:");
        for (Jugador jugador : turnos) {
            System.out.println("  " + jugador);
        }
    }

    private void mostrarOpciones() {
        System.out.println("  1  Atacar");
        System.out.println("  2  Usar item");
        System.out.println("  3  Ver inventario");
        System.out.println("  4  Ver historial");
        System.out.println("  5  Ver estado");
        System.out.print("Opción: ");
        System.out.flush();
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

    private void anotar(String suceso) {
        System.out.println("  " + suceso);
        historial.registrar(suceso);
    }

    private void leerJugadores(BufferedReader entrada) {
        int cantidad = leerCantidad(entrada);
        for (int i = 1; i <= cantidad; i++) {
            Jugador jugador = new Jugador(leerNickname(entrada, i));
            equipar(jugador);
            turnos.insertarAlFinal(jugador);
        }
    }

    private void equipar(Jugador jugador) {
        Inventario inventario = jugador.getInventario();
        inventario.agregar(new Item("Poción", Item.CURAR, 12));
        inventario.agregar(new Item("Piedra de fuerza", Item.AUMENTAR_ATAQUE, 3));
        inventario.agregar(new Item("Bomba", Item.DANO, 9));
    }

    private int leerCantidad(BufferedReader entrada) {
        while (true) {
            System.out.print("¿Cuántos jugadores se enfrentan? (" + MINIMO_JUGADORES
                    + " a " + MAXIMO_JUGADORES + "): ");
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
