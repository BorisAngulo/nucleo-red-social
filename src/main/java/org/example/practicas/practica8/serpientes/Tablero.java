package org.example.practicas.practica8.serpientes;

import org.example.estructuradedatos.ListaCircular;

/** Tablero de 10×10. La celda 1 está abajo a la izquierda y la 100 arriba a la derecha. */
public class Tablero {

    public static final int LADO = 10;
    public static final int META = LADO * LADO;

    private final Celda[][] celdas;

    public Tablero() {
        celdas = new Celda[LADO][LADO];
        for (int numero = 1; numero <= META; numero++) {
            int[] coordenada = coordenadas(numero);
            celdas[coordenada[0]][coordenada[1]] = new Celda(numero);
        }
        colocarEscaleras();
        colocarSerpientes();
    }

    public Celda obtener(int numero) {
        if (numero < 1 || numero > META) {
            throw new IllegalArgumentException("La celda " + numero + " no está en el tablero.");
        }
        int[] coordenada = coordenadas(numero);
        return celdas[coordenada[0]][coordenada[1]];
    }

    public void mostrar(ListaCircular<Jugador> jugadores) {
        for (int fila = LADO - 1; fila >= 0; fila--) {
            StringBuilder linea = new StringBuilder();
            for (int columna = 0; columna < LADO; columna++) {
                linea.append(texto(celdas[fila][columna], jugadores));
            }
            System.out.println(linea);
        }
    }

    private static String texto(Celda celda, ListaCircular<Jugador> jugadores) {
        String marca = switch (celda.getEfecto()) {
            case Celda.ESCALERA -> celda.getNumero() + "↑" + celda.getNumeroEfecto();
            case Celda.SERPIENTE -> celda.getNumero() + "↓" + celda.getNumeroEfecto();
            default -> Integer.toString(celda.getNumero());
        };
        String ocupantes = "";
        for (Jugador jugador : jugadores) {
            if (jugador.getCeldaPosicion() == celda.getNumero()) {
                ocupantes = ocupantes.isEmpty()
                        ? jugador.getNickname()
                        : ocupantes + "," + jugador.getNickname();
            }
        }
        if (!ocupantes.isEmpty()) {
            marca = marca + " " + ocupantes;
        }
        return String.format(" %-12.12s", marca);
    }

    private void colocarEscaleras() {
        escalera(4, 14);
        escalera(9, 31);
        escalera(20, 38);
        escalera(28, 84);
        escalera(40, 59);
        escalera(51, 67);
        escalera(63, 81);
        escalera(71, 91);
    }

    private void colocarSerpientes() {
        serpiente(17, 7);
        serpiente(54, 34);
        serpiente(62, 19);
        serpiente(64, 60);
        serpiente(87, 24);
        serpiente(93, 73);
        serpiente(95, 75);
        serpiente(98, 78);
    }

    private void escalera(int origen, int destino) {
        obtener(origen).colocarEfecto(Celda.ESCALERA, destino);
    }

    private void serpiente(int origen, int destino) {
        obtener(origen).colocarEfecto(Celda.SERPIENTE, destino);
    }

    private static int[] coordenadas(int numero) {
        int fila = (numero - 1) / LADO;
        int columna = (numero - 1) % LADO;
        return new int[] {fila, columna};
    }
}
