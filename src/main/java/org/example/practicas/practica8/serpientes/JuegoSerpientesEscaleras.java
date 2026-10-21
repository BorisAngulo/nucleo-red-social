package org.example.practicas.practica8.serpientes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

import org.example.estructuradedatos.ListaCircular;

/**
 * Partida de serpientes y escaleras.
 * Los turnos viven en una lista circular: la cabeza es quien juega.
 * Si sigue en partida, rotar lo pasa al final. Si gana, sale del anillo.
 */
public class JuegoSerpientesEscaleras {

    private static final int MINIMO_JUGADORES = 2;
    private static final int MAXIMO_JUGADORES = 6;

    private final Tablero tablero;
    private final Random dado;
    private ListaCircular<Jugador> turnos;

    public JuegoSerpientesEscaleras(Random dado) {
        if (dado == null) {
            throw new IllegalArgumentException("El dado no puede ser nulo.");
        }
        this.tablero = new Tablero();
        this.dado = dado;
    }

    public void jugar(BufferedReader entrada) {
        leerJugadores(entrada);
        System.out.println();
        System.out.println("Tablero. ↑ escalera, ↓ serpiente. La meta es la celda " + Tablero.META + ".");
        tablero.mostrar(turnos);
        System.out.println("Para tirar el dado pulsa Enter.");

        Jugador ganador = null;
        while (ganador == null) {
            Jugador actual = turnos.obtener(0);
            System.out.println();
            System.out.println("Turno de " + actual.getNickname() + ". " + describirEspera());
            System.out.println("Enter para tirar el dado.");
            System.out.flush();
            leerLinea(entrada);
            ganador = jugarTurno(actual);
            tablero.mostrar(turnos);
            if (ganador == null) {
                turnos.rotar();
            } else {
                turnos.eliminarAlInicio();
            }
        }
        System.out.println();
        System.out.println(ganador.getNickname() + " llegó a la celda " + Tablero.META + " y gana la partida.");
    }

    private Jugador jugarTurno(Jugador jugador) {
        int tiro = dado.nextInt(6) + 1;
        int destino = jugador.getCeldaPosicion() + tiro;
        System.out.println("  " + jugador.getNickname() + " saca " + tiro + ".");
        if (destino > Tablero.META) {
            System.out.println("  Necesita un tiro exacto para llegar a " + Tablero.META
                    + ". Se queda en la celda " + jugador.getCeldaPosicion() + ".");
            return null;
        }
        jugador.setCeldaPosicion(destino);
        System.out.println("  Avanza a la celda " + destino + ".");
        Celda celda = tablero.obtener(destino);
        if (celda.tieneEfecto()) {
            String causa = celda.getEfecto() == Celda.ESCALERA ? "escalera" : "serpiente";
            jugador.setCeldaPosicion(celda.getNumeroEfecto());
            System.out.println("  Hay una " + causa + " hacia la celda " + celda.getNumeroEfecto() + ".");
        }
        if (jugador.getCeldaPosicion() == Tablero.META) {
            jugador.setEstado(false);
            return jugador;
        }
        return null;
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
        turnos = new ListaCircular<>();
        for (int i = 1; i <= cantidad; i++) {
            turnos.insertarAlFinal(new Jugador(leerNickname(entrada, i)));
        }
    }

    private int leerCantidad(BufferedReader entrada) {
        while (true) {
            System.out.print("¿Cuántos jugadores? (" + MINIMO_JUGADORES + " a " + MAXIMO_JUGADORES + "): ");
            System.out.flush();
            String linea = leerLinea(entrada).trim();
            try {
                int cantidad = Integer.parseInt(linea);
                if (cantidad >= MINIMO_JUGADORES && cantidad <= MAXIMO_JUGADORES) {
                    return cantidad;
                }
            } catch (NumberFormatException ignorado) {
                cantidadInvalida();
                continue;
            }
            System.out.println("  Tiene que ser un número entre " + MINIMO_JUGADORES + " y " + MAXIMO_JUGADORES + ".");
        }
    }

    private void cantidadInvalida() {
        System.out.println("  Eso no es un número.");
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

    /**
     * Lee una línea y descarta el salto extra que algunas consolas agregan al mismo Enter.
     * Así un Enter es un solo turno.
     */
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
