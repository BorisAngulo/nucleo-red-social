package org.example.practicas.practica4;

import java.util.List;
import java.util.Locale;

/**
 * Flujo de cada expresión: validar delimitadores, convertir a postfija y a prefija, evaluar.
 */
public class CalculadoraApp {

    public static void main(String[] args) {
        ValidadorSintactico validador = new ValidadorSintactico();
        Tokenizador tokenizador = new Tokenizador();
        ConversorPostfijo aPostfija = new ConversorPostfijo();
        ConversorPrefijo aPrefija = new ConversorPrefijo();
        EvaluadorPostfijo evaluador = new EvaluadorPostfijo();

        System.out.println("Expresiones válidas");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "3 + 4 * 2");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "( 3 + 4 ) * 2");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "10 - 3 - 2");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "8 / 2 / 2");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "2 ^ 3 ^ 2");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "{ [ ( 1 + 2 ) * 3 ] - 4 }");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "-3 + 8 * 2");

        System.out.println();
        System.out.println("Expresiones rechazadas");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "( 3 + 4");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "[ ( ] )");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "4 / 0");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "3 +");
        probar(validador, tokenizador, aPostfija, aPrefija, evaluador, "3 + + 4");
    }

    private static void probar(ValidadorSintactico validador, Tokenizador tokenizador,
                               ConversorPostfijo aPostfija, ConversorPrefijo aPrefija,
                               EvaluadorPostfijo evaluador, String expresion) {
        System.out.println();
        System.out.println("  " + expresion);
        try {
            validador.validar(expresion);
            List<Token> infija = tokenizador.tokenizar(expresion);
            List<Token> postfija = aPostfija.convertir(infija);
            List<Token> prefija = aPrefija.convertir(infija);
            double resultado = evaluador.evaluar(postfija);
            System.out.println("    postfija: " + unir(postfija));
            System.out.println("    prefija:  " + unir(prefija));
            System.out.println("    resultado: " + formatear(resultado));
        } catch (ExpresionInvalidaException error) {
            System.out.println("    rechazada: " + error.getMessage());
        }
    }

    private static String unir(List<Token> tokens) {
        StringBuilder texto = new StringBuilder();
        for (Token token : tokens) {
            if (!texto.isEmpty()) {
                texto.append(' ');
            }
            texto.append(token.getTexto());
        }
        return texto.toString();
    }

    private static String formatear(double valor) {
        if (Double.isFinite(valor) && valor == Math.rint(valor) && Math.abs(valor) < 1e15) {
            return Long.toString(Math.round(valor));
        }
        String texto = String.format(Locale.US, "%.4f", valor);
        return texto.replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
