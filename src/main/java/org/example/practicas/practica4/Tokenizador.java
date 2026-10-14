package org.example.practicas.practica4;

import java.util.ArrayList;
import java.util.List;

/**
 * Parte la expresión infija en tokens. El menos unario solo se acepta
 * pegado a un número, como en {@code -3}; antes de un paréntesis se reescribe
 * como {@code 0 - ( ... )} para que el evaluador siga usando dos operandos.
 */
public class Tokenizador {

    public List<Token> tokenizar(String expresion) {
        if (expresion == null || expresion.isBlank()) {
            throw new ExpresionInvalidaException("La expresión está vacía.");
        }
        List<Token> tokens = new ArrayList<>();
        boolean esperaOperando = true;
        int i = 0;
        while (i < expresion.length()) {
            char simbolo = expresion.charAt(i);
            if (Character.isWhitespace(simbolo)) {
                i++;
                continue;
            }
            if (simbolo == '-' && esperaOperando) {
                i = leerMenosUnario(expresion, i, tokens);
                esperaOperando = false;
                continue;
            }
            if (esOperador(simbolo)) {
                if (esperaOperando) {
                    throw new ExpresionInvalidaException("El operador '" + simbolo + "' no tiene operando a la izquierda.");
                }
                tokens.add(Token.operador(String.valueOf(simbolo)));
                esperaOperando = true;
                i++;
                continue;
            }
            if (esApertura(simbolo)) {
                tokens.add(Token.apertura(simbolo));
                esperaOperando = true;
                i++;
                continue;
            }
            if (esCierre(simbolo)) {
                if (esperaOperando) {
                    throw new ExpresionInvalidaException("Falta un operando antes de '" + simbolo + "'.");
                }
                tokens.add(Token.cierre(simbolo));
                esperaOperando = false;
                i++;
                continue;
            }
            if (Character.isDigit(simbolo) || simbolo == '.') {
                if (!esperaOperando) {
                    throw new ExpresionInvalidaException("Falta un operador antes de '" + simbolo + "'.");
                }
                i = leerNumero(expresion, i, tokens, false);
                esperaOperando = false;
                continue;
            }
            throw new ExpresionInvalidaException("Símbolo no reconocido: '" + simbolo + "'.");
        }
        if (esperaOperando) {
            throw new ExpresionInvalidaException("La expresión está incompleta.");
        }
        return tokens;
    }

    private static int leerMenosUnario(String expresion, int indice, List<Token> tokens) {
        int siguiente = indice + 1;
        while (siguiente < expresion.length() && Character.isWhitespace(expresion.charAt(siguiente))) {
            siguiente++;
        }
        if (siguiente >= expresion.length()) {
            throw new ExpresionInvalidaException("La expresión está incompleta.");
        }
        char simbolo = expresion.charAt(siguiente);
        if (Character.isDigit(simbolo) || simbolo == '.') {
            return leerNumero(expresion, siguiente, tokens, true);
        }
        if (esApertura(simbolo)) {
            tokens.add(Token.operando("0"));
            tokens.add(Token.operador("-"));
            return siguiente;
        }
        throw new ExpresionInvalidaException("El menos unario no está seguido de un número.");
    }

    private static int leerNumero(String expresion, int inicio, List<Token> tokens, boolean negativo) {
        int i = inicio;
        boolean punto = false;
        boolean digito = false;
        while (i < expresion.length()) {
            char simbolo = expresion.charAt(i);
            if (Character.isDigit(simbolo)) {
                digito = true;
                i++;
            } else if (simbolo == '.' && !punto) {
                punto = true;
                i++;
            } else {
                break;
            }
        }
        if (!digito) {
            throw new ExpresionInvalidaException("Número mal formado.");
        }
        String lexema = expresion.substring(inicio, i);
        if (negativo) {
            lexema = "-" + lexema;
        }
        tokens.add(Token.operando(lexema));
        return i;
    }

    static boolean esApertura(char simbolo) {
        return simbolo == '(' || simbolo == '[' || simbolo == '{';
    }

    static boolean esCierre(char simbolo) {
        return simbolo == ')' || simbolo == ']' || simbolo == '}';
    }

    private static boolean esOperador(char simbolo) {
        return simbolo == '+' || simbolo == '-' || simbolo == '*' || simbolo == '/' || simbolo == '^';
    }
}
