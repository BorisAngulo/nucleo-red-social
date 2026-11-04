package org.example.practicas.segundoParcial;

import org.example.estructuradedatos.ListaCircular;
import org.example.estructuradedatos.ListaDobleCircular;

/**
 * Las casillas forman un anillo: la última enlaza con Salida
 * y cada una conoce la anterior y la siguiente.
 */
public class Tablero {

    public static final int BONO_VUELTA = 100;

    private final ListaDobleCircular<Casilla> casillas;

    public Tablero() {
        casillas = new ListaDobleCircular<>();
        casillas.insertarAlFinal(Casilla.salida(1));
        casillas.insertarAlFinal(Casilla.propiedad(2, "Avenida", 120, 25));
        casillas.insertarAlFinal(Casilla.suerte(3));
        casillas.insertarAlFinal(Casilla.impuesto(4, "Impuesto", 60));
        casillas.insertarAlFinal(Casilla.propiedad(5, "Calle Real", 140, 30));
        casillas.insertarAlFinal(Casilla.suerte(6));
        casillas.insertarAlFinal(Casilla.propiedad(7, "Plaza", 160, 35));
        casillas.insertarAlFinal(Casilla.impuesto(8, "Aduana", 80));
        casillas.insertarAlFinal(Casilla.propiedad(9, "Parque", 100, 20));
        casillas.insertarAlFinal(Casilla.suerte(10));
        casillas.insertarAlFinal(Casilla.propiedad(11, "Estación", 180, 40));
        casillas.insertarAlFinal(Casilla.propiedad(12, "Mercado", 130, 28));
    }

    public ListaDobleCircular<Casilla>.Posicion fichaEnSalida() {
        return casillas.marcarInicio();
    }

    public void mostrar(ListaCircular<Jugador> jugadores) {
        System.out.println("Tablero (anillo):");
        for (Casilla casilla : casillas) {
            StringBuilder fichas = new StringBuilder();
            for (Jugador jugador : jugadores) {
                if (jugador.getCasilla() == casilla) {
                    if (fichas.length() > 0) {
                        fichas.append(", ");
                    }
                    fichas.append(jugador.getNickname());
                }
            }
            String marca = fichas.length() == 0 ? "" : "  <- " + fichas;
            System.out.println("  " + casilla + marca);
        }
    }
}
