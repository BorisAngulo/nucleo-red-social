package org.example.practicas.practica4;

/** Error de sintaxis o de evaluación que el cliente puede contener. */
public class ExpresionInvalidaException extends RuntimeException {

    public ExpresionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
