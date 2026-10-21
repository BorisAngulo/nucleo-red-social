package org.example.practicas.practica8.serpientes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.estructuradedatos.ColaArreglo;

/**
 * Partida de serpientes y escaleras.
 * Los turnos salen de una cola circular: quien juega se desencola y,
 * si sigue en partida, vuelve a encolarse al final.
 */
public class JuegoSerpientesEscaleras {

    private static final int MINIMO_JUGADORES = 2;
    private static final int MAXIMO_JUGADORES = 6;

    private final Tablero tablero;
    private final Random dado;
    private ColaArreglo<Jugador> turnos;
    private List<Jugador> jugadores;

    public JuegoSerpientesEscaleras(Random dado) {
        if (dado == null) {
            throw new IllegalArgumentException("El dado no puede ser nulo.");
        }
        this.tablero = new Tablero();
        this.dado = dado;
    }

    public void jugar(BufferedReader entrada) {
        jugadores = leerJugadores(entrada);
        turnos = new ColaArreglo<>(jugadores.size());
        for (Jugador jugador : jugadores) {
            turnos.encolar(jugador);
        }
        System.out.println();
        System.out.println("Tablero. ↑ escalera, ↓ serpiente. La meta es la celda " + Tablero.META + ".");
        tablero.mostrar(jugadores);
        System.out.println("Para tirar el dado pulsa Enter.");

        Jugador ganador = null;
        while (ganador == null) {
            Jugador actual = turnos.desencolar();
            if (!actual.isEstado()) {
                continue;
            }
            System.out.println();
            System.out.println("Turno de " + actual.getNickname() + ". " + describirEspera());
            System.out.println("Enter para tirar el dado.");
            System.out.flush();
            leerLinea(entrada);
            ganador = jugarTurno(actual);
            if (actual.isEstado()) {
                turnos.encolar(actual);
            }
            tablero.mostrar(jugadores);
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
        if (turnos.estaVacia()) {
            return "Nadie espera.";
        }
        StringBuilder texto = new StringBuilder("Después siguen:");
        for (Jugador jugador : turnos) {
            texto.append(' ').append(jugador.getNickname());
        }
        return texto.toString();
    }

    private List<Jugador> leerJugadores(BufferedReader entrada) {
        int cantidad = leerCantidad(entrada);
        List<Jugador> alta = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            alta.add(new Jugador(leerNickname(entrada, i, alta)));
        }
        return alta;
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

    private String leerNickname(BufferedReader entrada, int orden, List<Jugador> yaIngresados) {
        while (true) {
            System.out.print("Nickname del jugador " + orden + ": ");
            System.out.flush();
            String nickname = leerLinea(entrada).trim();
            if (nickname.isEmpty()) {
                System.out.println("  El nickname no puede estar vacío.");
                continue;
            }
            boolean repetido = false;
            for (Jugador jugador : yaIngresados) {
                if (jugador.getNickname().equalsIgnoreCase(nickname)) {
                    repetido = true;
                    break;
                }
            }
            if (repetido) {
                System.out.println("  Ese nickname ya está en la partida.");
                continue;
            }
            return nickname;
        }
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
