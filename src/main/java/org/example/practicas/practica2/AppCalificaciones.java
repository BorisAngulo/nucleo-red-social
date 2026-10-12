package org.example.practicas.practica2;

import java.util.Locale;

/**
 * Recorrido del tablero: alta, hipercubo, selección, burbuja y búsqueda binaria.
 */
public class AppCalificaciones {

    public static void main(String[] args) {
        TableroCalificaciones tablero = new TableroCalificaciones(4, 2);

        seccion("Estado inicial");
        System.out.println("  vacío: " + tablero.estaVacio() + " | lleno: " + tablero.estaLleno());
        tablero.mostrarTablero();

        seccion("Registro de cuatro estudiantes");
        registrar(tablero, 101, 45.50, 78.00);
        registrar(tablero, 104, 92.00, 88.50);
        registrar(tablero, 102, 70.00, 65.50);
        registrar(tablero, 103, 55.00, 61.00);
        System.out.println("  vacío: " + tablero.estaVacio() + " | lleno: " + tablero.estaLleno());
        System.out.println("  un quinto alta no cabe y se rechaza: "
                + tablero.registrarEstudiante(105, 80.00, 80.00));

        seccion("Tablero en orden de llegada");
        tablero.mostrarTablero();

        seccion("Hipercubo de notas: sede, estudiante y materia");
        TableroCalificaciones.demostrarHipercuboNotas();

        seccion("Selección descendente por promedio");
        tablero.ordenarPorPromedioSeleccion();
        tablero.mostrarTablero();

        seccion("Búsqueda binaria con los códigos aún desordenados");
        System.out.println("  El 101 está en el tablero, pero los códigos quedaron ordenados por promedio.");
        int busquedaPrematura = tablero.buscarEstudianteBinario(101);
        System.out.println("  buscarEstudianteBinario(101) = " + busquedaPrematura
                + "  (centinela: la mitad descartada contenía al estudiante)");

        seccion("Burbuja ascendente por código");
        tablero.ordenarPorCodigoBurbuja();
        tablero.mostrarTablero();

        seccion("Búsqueda binaria exitosa");
        int indice = tablero.buscarEstudianteBinario(101);
        if (indice == -1) {
            System.out.println("  No se encontró el código 101.");
        } else {
            System.out.printf(Locale.US,
                    "  índice %d | código %d | parciales %.2f y %.2f | promedio %.2f%n",
                    indice,
                    tablero.obtenerCodigo(indice),
                    tablero.obtenerNota(indice, 0),
                    tablero.obtenerNota(indice, 1),
                    tablero.obtenerPromedio(indice));
        }

        seccion("Búsqueda binaria sin resultado");
        int ausente = tablero.buscarEstudianteBinario(999);
        if (ausente == -1) {
            System.out.println("  buscarEstudianteBinario(999) = -1. El código no está; no se lee la matriz.");
        } else {
            System.out.println("  Estudiante inesperado en el índice " + ausente + ".");
        }
    }

    private static void registrar(TableroCalificaciones tablero, int codigo, double nota1, double nota2) {
        boolean aceptado = tablero.registrarEstudiante(codigo, nota1, nota2);
        System.out.printf(Locale.US, "  código %d (%.2f, %.2f): %s%n",
                codigo, nota1, nota2, aceptado ? "aceptado" : "rechazado");
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
