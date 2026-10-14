package org.example.practicas.practica4;

import org.example.estructuradedatos.PilaEnlazada;

/**
 * Comprueba que (), [] y {} estén balanceados y no cruzados.
 * Cada apertura se apila; cada cierre desapila y exige su pareja.
 */
public class ValidadorSintactico {

    public void validar(String expresion) {
        if (expresion == null || expresion.isBlank()) {
            throw new ExpresionInvalidaException("La expresión está vacía.");
        }
        PilaEnlazada<Character> aperturas = new PilaEnlazada<>();
        for (int i = 0; i < expresion.length(); i++) {
            char simbolo = expresion.charAt(i);
            if (Tokenizador.esApertura(simbolo)) {
                aperturas.apilar(simbolo);
            } else if (Tokenizador.esCierre(simbolo)) {
                if (aperturas.estaVacia()) {
                    throw new ExpresionInvalidaException("Cierre '" + simbolo + "' sin apertura.");
                }
                char apertura = aperturas.desapilar();
                if (!pareja(apertura, simbolo)) {
                    throw new ExpresionInvalidaException(
                            "Delimitadores cruzados: '" + apertura + "' no cierra con '" + simbolo + "'.");
                }
            }
        }
        if (!aperturas.estaVacia()) {
            throw new ExpresionInvalidaException("Apertura '" + aperturas.cima() + "' sin cierre.");
        }
    }

    private static boolean pareja(char apertura, char cierre) {
        return (apertura == '(' && cierre == ')')
                || (apertura == '[' && cierre == ']')
                || (apertura == '{' && cierre == '}');
    }
}
