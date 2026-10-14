package org.example.practicas.practica4;

import java.util.ArrayList;
import java.util.List;

import org.example.estructuradedatos.PilaEnlazada;

/**
 * Shunting-yard de infija a postfija.
 * Los operandos pasan directo a la salida. Un operador espera en la pila
 * y se desapila mientras la cima tenga precedencia mayor o igual: así
 * suma, resta, multiplicación, división y potencia asocian por la izquierda.
 */
public class ConversorPostfijo {

    public List<Token> convertir(List<Token> infija) {
        if (infija == null || infija.isEmpty()) {
            throw new ExpresionInvalidaException("No hay tokens para convertir.");
        }
        PilaEnlazada<Token> operadores = new PilaEnlazada<>();
        List<Token> salida = new ArrayList<>();
        for (Token token : infija) {
            switch (token.getTipo()) {
                case OPERANDO -> salida.add(token);
                case OPERADOR -> {
                    while (!operadores.estaVacia()
                            && operadores.cima().getTipo() == TipoToken.OPERADOR
                            && operadores.cima().getPrecedencia() >= token.getPrecedencia()) {
                        salida.add(operadores.desapilar());
                    }
                    operadores.apilar(token);
                }
                case PARENTESIS_APERTURA -> operadores.apilar(token);
                case PARENTESIS_CIERRE -> vaciarHastaApertura(operadores, salida, token);
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

    static void vaciarHastaApertura(PilaEnlazada<Token> operadores, List<Token> salida, Token cierre) {
        while (!operadores.estaVacia() && operadores.cima().getTipo() != TipoToken.PARENTESIS_APERTURA) {
            salida.add(operadores.desapilar());
        }
        if (operadores.estaVacia() || !cierre.cierraA(operadores.cima())) {
            throw new ExpresionInvalidaException("Cierre '" + cierre.getTexto() + "' sin su apertura.");
        }
        operadores.desapilar();
    }
}
