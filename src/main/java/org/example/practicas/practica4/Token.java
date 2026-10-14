package org.example.practicas.practica4;

/**
 * Pieza de la expresión: tipo, texto y precedencia.
 * Suma y resta valen 1, multiplicación y división 2, potencia 3.
 * Los delimitadores quedan en la precedencia base, 0.
 */
public class Token {

    private final TipoToken tipo;
    private final String texto;
    private final int precedencia;

    private Token(TipoToken tipo, String texto, int precedencia) {
        this.tipo = tipo;
        this.texto = texto;
        this.precedencia = precedencia;
    }

    public static Token operando(String texto) {
        return new Token(TipoToken.OPERANDO, texto, 0);
    }

    public static Token operador(String texto) {
        int nivel = switch (texto) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> throw new ExpresionInvalidaException("Operador no reconocido: " + texto);
        };
        return new Token(TipoToken.OPERADOR, texto, nivel);
    }

    public static Token apertura(char simbolo) {
        return new Token(TipoToken.PARENTESIS_APERTURA, String.valueOf(simbolo), 0);
    }

    public static Token cierre(char simbolo) {
        return new Token(TipoToken.PARENTESIS_CIERRE, String.valueOf(simbolo), 0);
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public String getTexto() {
        return texto;
    }

    public int getPrecedencia() {
        return precedencia;
    }

    public boolean cierraA(Token apertura) {
        if (tipo != TipoToken.PARENTESIS_CIERRE || apertura.tipo != TipoToken.PARENTESIS_APERTURA) {
            return false;
        }
        return switch (apertura.texto) {
            case "(" -> ")".equals(texto);
            case "[" -> "]".equals(texto);
            case "{" -> "}".equals(texto);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return texto;
    }
}
