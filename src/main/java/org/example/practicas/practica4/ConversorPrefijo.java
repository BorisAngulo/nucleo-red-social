package org.example.practicas.practica4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.estructuradedatos.PilaEnlazada;

/**
 * Infija a prefija en tres pasos: se invierte la infija (intercambiando
 * aperturas y cierres), se convierte con shunting-yard y se invierte la salida.
 * En ese recorrido intermedio solo se desapila si la cima tiene precedencia
 * estrictamente mayor, para conservar la asociación por la izquierda.
 */
public class ConversorPrefijo {

    public List<Token> convertir(List<Token> infija) {
        if (infija == null || infija.isEmpty()) {
            throw new ExpresionInvalidaException("No hay tokens para convertir.");
        }
        List<Token> invertida = invertirDelimitadores(infija);
        List<Token> postfija = aPostfija(invertida);
        Collections.reverse(postfija);
        return postfija;
    }

    private static List<Token> invertirDelimitadores(List<Token> infija) {
        List<Token> invertida = new ArrayList<>();
        for (int i = infija.size() - 1; i >= 0; i--) {
            Token token = infija.get(i);
            invertida.add(switch (token.getTipo()) {
                case PARENTESIS_APERTURA -> Token.cierre(opuesto(token.getTexto().charAt(0)));
                case PARENTESIS_CIERRE -> Token.apertura(opuesto(token.getTexto().charAt(0)));
                default -> token;
            });
        }
        return invertida;
    }

    private static List<Token> aPostfija(List<Token> infija) {
        PilaEnlazada<Token> operadores = new PilaEnlazada<>();
        List<Token> salida = new ArrayList<>();
        for (Token token : infija) {
            switch (token.getTipo()) {
                case OPERANDO -> salida.add(token);
                case OPERADOR -> {
                    while (!operadores.estaVacia()
                            && operadores.cima().getTipo() == TipoToken.OPERADOR
                            && operadores.cima().getPrecedencia() > token.getPrecedencia()) {
                        salida.add(operadores.desapilar());
                    }
                    operadores.apilar(token);
                }
                case PARENTESIS_APERTURA -> operadores.apilar(token);
                case PARENTESIS_CIERRE -> ConversorPostfijo.vaciarHastaApertura(operadores, salida, token);
            }
        }
        while (!operadores.estaVacia()) {
            Token pendiente = operadores.desapilar();
            if (pendiente.getTipo() == TipoToken.PARENTESIS_APERTURA) {
                throw new ExpresionInvalidaException("Apertura '" + pendiente.getTexto() + "' sin cierre.");
            }
            salida.add(pendiente);
        }
        return salida;
    }

    private static char opuesto(char simbolo) {
        return switch (simbolo) {
            case '(' -> ')';
            case ')' -> '(';
            case '[' -> ']';
            case ']' -> '[';
            case '{' -> '}';
            case '}' -> '{';
            default -> throw new ExpresionInvalidaException("Delimitador no reconocido: " + simbolo);
        };
    }
}
