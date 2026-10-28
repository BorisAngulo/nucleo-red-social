package org.example.practicas.practica12;

/**
 * Potencia de exponente entero no negativo.
 * Recursiva: base^0 = 1 y base^n = base * base^(n - 1).
 * Iterativa: el mismo producto, acumulado en un ciclo.
 */
public class EvaluadorPotencia {

    private EvaluadorPotencia() {
    }

    public static long recursivo(long base, int exponente) {
        validar(exponente);
        if (exponente == 0) {
            return 1;
        }
        return base * recursivo(base, exponente - 1);
    }

    public static long iterativo(long base, int exponente) {
        validar(exponente);
        long resultado = 1;
        for (int i = 0; i < exponente; i++) {
            resultado *= base;
        }
        return resultado;
    }

    /** Muestra cada llamada hasta el exponente 0 y la vuelta con el producto. */
    public static long trazar(long base, int exponente) {
        validar(exponente);
        return trazar(base, exponente, 0);
    }

    private static long trazar(long base, int exponente, int nivel) {
        String sangria = "  ".repeat(nivel);
        System.out.println(sangria + base + "^" + exponente);
        if (exponente == 0) {
            System.out.println(sangria + "-> 1");
            return 1;
        }
        long resultado = base * trazar(base, exponente - 1, nivel + 1);
        System.out.println(sangria + base + "^" + exponente + " = " + resultado);
        return resultado;
    }

    private static void validar(int exponente) {
        if (exponente < 0) {
            throw new IllegalArgumentException("El exponente no puede ser negativo.");
        }
    }
}
