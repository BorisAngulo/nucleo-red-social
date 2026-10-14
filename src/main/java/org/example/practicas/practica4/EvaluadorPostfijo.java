package org.example.practicas.practica4;

import java.util.List;

import org.example.estructuradedatos.PilaEnlazada;

/**
 * Evalúa una expresión postfija. Los números se apilan; cada operador
 * desapila primero el operando derecho y después el izquierdo, de modo que
 * la resta y la división conservan el orden.
 */
public class EvaluadorPostfijo {

    public double evaluar(List<Token> postfija) {
        if (postfija == null || postfija.isEmpty()) {
            throw new ExpresionInvalidaException("No hay expresión postfija para evaluar.");
        }
        PilaEnlazada<Double> operandos = new PilaEnlazada<>();
        for (Token token : postfija) {
            if (token.getTipo() == TipoToken.OPERANDO) {
                operandos.apilar(Double.parseDouble(token.getTexto()));
                continue;
            }
            if (token.getTipo() != TipoToken.OPERADOR) {
                throw new ExpresionInvalidaException("La postfija contiene un delimitador: " + token.getTexto());
            }
            if (operandos.tamano() < 2) {
                throw new ExpresionInvalidaException("Expresión incompleta: falta un operando para '" + token.getTexto() + "'.");
            }
            double derecho = operandos.desapilar();
            double izquierdo = operandos.desapilar();
            operandos.apilar(operar(izquierdo, token.getTexto(), derecho));
        }
        if (operandos.tamano() != 1) {
            throw new ExpresionInvalidaException("Expresión mal formada: sobran operandos.");
        }
        return operandos.desapilar();
    }

    private static double operar(double izquierdo, String operador, double derecho) {
        return switch (operador) {
            case "+" -> izquierdo + derecho;
            case "-" -> izquierdo - derecho;
            case "*" -> izquierdo * derecho;
            case "/" -> {
                if (derecho == 0.0) {
                    throw new ExpresionInvalidaException("División entre cero.");
                }
                yield izquierdo / derecho;
            }
            case "^" -> Math.pow(izquierdo, derecho);
            default -> throw new ExpresionInvalidaException("Operador no reconocido: " + operador);
        };
    }
}
