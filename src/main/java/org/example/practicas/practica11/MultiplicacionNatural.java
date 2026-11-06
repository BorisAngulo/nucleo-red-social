package org.example.practicas.practica11;

/**
 * Producto de dos naturales por recursividad, sin usar el operador de multiplicación.
 * a * 0 = 0 y a * b = a + (a * (b - 1))
 */
public class MultiplicacionNatural {

    private MultiplicacionNatural() {
    }

    public static long calcular(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("La multiplicación natural no acepta negativos.");
        }
        if (b == 0) {
            return 0;
        }
        return a + calcular(a, b - 1);
    }

    /** Muestra cada suma que arma el producto. */
    public static long trazar(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("La multiplicación natural no acepta negativos.");
        }
        return trazar(a, b, 0);
    }

    private static long trazar(int a, int b, int nivel) {
        String sangria = "  ".repeat(nivel);
        System.out.println(sangria + "producto(" + a + ", " + b + ")");
        if (b == 0) {
            System.out.println(sangria + "-> 0");
            return 0;
        }
        long resultado = a + trazar(a, b - 1, nivel + 1);
        System.out.println(sangria + "producto(" + a + ", " + b + ") = " + resultado);
        return resultado;
    }
}
