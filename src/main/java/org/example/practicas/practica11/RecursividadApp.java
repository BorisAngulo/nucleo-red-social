package org.example.practicas.practica11;

/** Demostración de factorial, producto de naturales y Fibonacci. */
public class RecursividadApp {

    public static void main(String[] args) {
        seccion("5.1.1 Factorial");
        mostrarFactorial(0);
        mostrarFactorial(1);
        mostrarFactorial(5);
        mostrarFactorial(7);
        System.out.println("Traza de 4!");
        Factorial.trazar(4);

        seccion("5.1.2 Multiplicación de números naturales");
        mostrarProducto(4, 3);
        mostrarProducto(7, 1);
        mostrarProducto(6, 0);
        mostrarProducto(0, 5);
        System.out.println("Traza de 3 * 4");
        MultiplicacionNatural.trazar(3, 4);

        seccion("5.1.3 La secuencia de Fibonacci");
        Fibonacci.mostrarSecuencia(10);
        System.out.println("Traza de F(4)");
        Fibonacci.trazar(4);
    }

    private static void mostrarFactorial(int n) {
        System.out.println("  " + n + "! = " + Factorial.calcular(n));
    }

    private static void mostrarProducto(int a, int b) {
        System.out.println("  " + a + " * " + b + " = " + MultiplicacionNatural.calcular(a, b));
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
