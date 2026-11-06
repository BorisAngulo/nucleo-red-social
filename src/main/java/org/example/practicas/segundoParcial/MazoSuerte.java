package org.example.practicas.segundoParcial;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Mazo sobre una lista simple. La cabeza es la carta superior.
 * Al usarla se quita del frente y se inserta al final, que es el fondo.
 */
public class MazoSuerte {

    private final ListaEnlazada<CartaSuerte> cartas;

    public MazoSuerte() {
        cartas = new ListaEnlazada<>();
        cartas.insertarAlFinal(new CartaSuerte("Retrocede 3 casillas", CartaSuerte.RETROCEDER, 3));
        cartas.insertarAlFinal(new CartaSuerte("El banco te paga $120", CartaSuerte.COBRAR, 120));
        cartas.insertarAlFinal(new CartaSuerte("Paga una multa de $100", CartaSuerte.MULTA, 100));
        cartas.insertarAlFinal(new CartaSuerte("Retrocede 1 casilla", CartaSuerte.RETROCEDER, 1));
        cartas.insertarAlFinal(new CartaSuerte("Avanza 2 casillas", CartaSuerte.AVANZAR, 2));
        cartas.insertarAlFinal(new CartaSuerte("Paga $70 por reparaciones", CartaSuerte.MULTA, 70));
    }

    public CartaSuerte tomar() {
        CartaSuerte carta = cartas.eliminarAlInicio();
        cartas.insertarAlFinal(carta);
        return carta;
    }

    public CartaSuerte superior() {
        return cartas.obtener(0);
    }
}
