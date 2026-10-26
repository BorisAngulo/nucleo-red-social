package org.example.practicas.practica11;

/**
 * Sucesión de Fibonacci por recursividad.
 * F(0) = 0, F(1) = 1 y F(n) = F(n - 1) + F(n - 2)
 */
public class Fibonacci {

    private Fibonacci() {
    }

    public static long calcular(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci no está definido para negativos.");
        }
        if (n == 0 || n == 1) {
            return n;
        }
        return calcular(n - 1) + calcular(n - 2);
    }

    /** Imprime F(0) hasta F(hasta). Cada término se calcula con la recurrencia. */
    public static void mostrarSecuencia(int hasta) {
        if (hasta < 0) {
            throw new IllegalArgumentException("El último término no puede ser negativo.");
        }
        for (int i = 0; i <= hasta; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print("F(" + i + ")=" + calcular(i));
        }
        System.out.println();
    }

    /** Muestra las dos llamadas que abre cada término. */
    public static long trazar(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci no está definido para negativos.");
        }
        return trazar(n, 0);
    }

    private static long trazar(int n, int nivel) {
        String sangria = "  ".repeat(nivel);
        System.out.println(sangria + "fibonacci(" + n + ")");
        if (n == 0 || n == 1) {
            System.out.println(sangria + "-> " + n);
            return n;
        }
        long resultado = trazar(n - 1, nivel + 1) + trazar(n - 2, nivel + 1);
        System.out.println(sangria + "fibonacci(" + n + ") = " + resultado);
        return resultado;
    }
}
