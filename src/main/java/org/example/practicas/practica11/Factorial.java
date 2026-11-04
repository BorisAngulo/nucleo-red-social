package org.example.practicas.practica11;

/**
 * Factorial por recursividad.
 * 0! = 1 y n! = n * (n - 1)!
 */
public class Factorial {

    private Factorial() {
    }

    public static long calcular(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El factorial no está definido para negativos.");
        }
        if (n == 0) {
            return 1;
        }
        return n * calcular(n - 1);
    }

    /** Muestra cada llamada y el valor que devuelve al volver. */
    public static long trazar(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El factorial no está definido para negativos.");
        }
        return trazar(n, 0);
    }

    private static long trazar(int n, int nivel) {
        String sangria = "  ".repeat(nivel);
        System.out.println(sangria + "factorial(" + n + ")");
        if (n == 0) {
            System.out.println(sangria + "-> 1");
            return 1;
        }
        long resultado = n * trazar(n - 1, nivel + 1);
        System.out.println(sangria + "factorial(" + n + ") = " + resultado);
        return resultado;
    }
}
