package org.example.practicas.practica2;

import java.util.Locale;

/**
 * Tablero de notas sobre dos arreglos paralelos alojados en el Heap.
 * {@code codigos[i]} y {@code matrizNotas[i]} describen al mismo estudiante.
 * La capacidad física es fija; {@code totalEstudiantes} es el tamaño lógico.
 */
public class TableroCalificaciones {

    /** Con dos parciales, el promedio queda en esta columna. */
    private static final int COLUMNA_PROMEDIO = 2;

    private final double[][] matrizNotas;
    private final int[] codigos;
    private int totalEstudiantes;

    /**
     * Reserva en el Heap la matriz de notas y el vector de códigos.
     * La última columna de cada fila queda apartada para el promedio:
     * la dimensión real es {@code [numEstudiantes][numEvaluaciones + 1]}.
     */
    public TableroCalificaciones(int numEstudiantes, int numEvaluaciones) {
        if (numEstudiantes <= 0) {
            throw new IllegalArgumentException("El número de estudiantes debe ser mayor que cero.");
        }
        if (numEvaluaciones != COLUMNA_PROMEDIO) {
            throw new IllegalArgumentException(
                    "Esta práctica registra dos parciales; el promedio ocupa la columna " + COLUMNA_PROMEDIO + ".");
        }
        this.matrizNotas = new double[numEstudiantes][numEvaluaciones + 1];
        this.codigos = new int[numEstudiantes];
        this.totalEstudiantes = 0;
    }

    public boolean estaVacio() {
        return totalEstudiantes == 0;
    }

    public boolean estaLleno() {
        return totalEstudiantes == codigos.length;
    }

    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }

    /**
     * Alta al final del tramo lógico. O(n) por la revisión de código repetido;
     * la escritura de la fila, una vez validada, es O(1).
     * Devuelve false si no queda cupo o el código ya existe: no hay desbordamiento.
     */
    public boolean registrarEstudiante(int codigo, double nota1, double nota2) {
        if (estaLleno() || codigoExiste(codigo) || !notasValidas(nota1, nota2)) {
            return false;
        }
        int fila = totalEstudiantes;
        codigos[fila] = codigo;
        matrizNotas[fila][0] = nota1;
        matrizNotas[fila][1] = nota2;
        matrizNotas[fila][COLUMNA_PROMEDIO] = (nota1 + nota2) / 2.0;
        totalEstudiantes++;
        return true;
    }

    /** Recorre solo las filas lógicas. O(n). */
    public void mostrarTablero() {
        if (estaVacio()) {
            System.out.println("  El tablero está vacío: no hay filas lógicas que imprimir.");
            return;
        }
        System.out.printf(Locale.US, "  %-10s %12s %12s %12s%n", "Código", "Parcial 1", "Parcial 2", "Promedio");
        for (int i = 0; i < totalEstudiantes; i++) {
            System.out.printf(Locale.US, "  %-10d %12.2f %12.2f %12.2f%n",
                    codigos[i],
                    matrizNotas[i][0],
                    matrizNotas[i][1],
                    matrizNotas[i][COLUMNA_PROMEDIO]);
        }
    }

    public int obtenerCodigo(int indice) {
        validarIndice(indice);
        return codigos[indice];
    }

    public double obtenerNota(int indice, int columna) {
        validarIndice(indice);
        if (columna < 0 || columna >= matrizNotas[indice].length) {
            throw new IndexOutOfBoundsException("Columna fuera de la fila: " + columna);
        }
        return matrizNotas[indice][columna];
    }

    public double obtenerPromedio(int indice) {
        return obtenerNota(indice, COLUMNA_PROMEDIO);
    }

    /**
     * Hipercubo de notas: sedes × estudiantes × materias.
     * {@code new double[2][3][2]} crea en el Heap un arreglo de arreglos de arreglos
     * y rellena cada celda numérica con 0.0. Recorrerlo exige tres bucles anidados, O(s·e·m).
     */
    public static void demostrarHipercuboNotas() {
        double[][][] hipercubo = new double[2][3][2];
        hipercubo[0][0][0] = 70.50;
        hipercubo[0][0][1] = 81.00;
        hipercubo[0][1][0] = 65.00;
        hipercubo[0][1][1] = 90.50;
        hipercubo[0][2][0] = 88.00;
        hipercubo[0][2][1] = 76.50;
        hipercubo[1][0][0] = 59.50;
        hipercubo[1][0][1] = 84.00;
        hipercubo[1][1][0] = 91.00;
        hipercubo[1][1][1] = 73.50;
        hipercubo[1][2][0] = 68.00;
        hipercubo[1][2][1] = 95.00;

        String[] sedes = {"Norte", "Sur"};
        for (int sede = 0; sede < hipercubo.length; sede++) {
            System.out.println("  Sede " + sedes[sede]);
            for (int estudiante = 0; estudiante < hipercubo[sede].length; estudiante++) {
                System.out.print("    Estudiante " + estudiante + ":");
                for (int materia = 0; materia < hipercubo[sede][estudiante].length; materia++) {
                    System.out.printf(Locale.US, "  materia %d = %.2f", materia, hipercubo[sede][estudiante][materia]);
                }
                System.out.println();
            }
        }
    }

    /**
     * Ordenamiento por selección, descendente según el promedio.
     * Siempre hace del orden de n²/2 comparaciones: O(n²), también si ya está ordenado.
     * Cada intercambio mueve la referencia de la fila, no sus casilleros: O(1).
     */
    public void ordenarPorPromedioSeleccion() {
        for (int i = 0; i < totalEstudiantes - 1; i++) {
            int indiceMayor = i;
            for (int j = i + 1; j < totalEstudiantes; j++) {
                if (matrizNotas[j][COLUMNA_PROMEDIO] > matrizNotas[indiceMayor][COLUMNA_PROMEDIO]) {
                    indiceMayor = j;
                }
            }
            if (indiceMayor != i) {
                intercambiar(i, indiceMayor);
            }
        }
    }

    /**
     * Burbuja ascendente por código, con dos cortes.
     * El límite {@code totalEstudiantes - 1 - i} no vuelve a tocar el sufijo ya ordenado.
     * {@code huboIntercambio} corta en O(n) cuando el arreglo ya está ordenado.
     * El peor caso sigue siendo O(n²). Códigos y filas se intercambian juntos.
     */
    public void ordenarPorCodigoBurbuja() {
        for (int i = 0; i < totalEstudiantes - 1; i++) {
            boolean huboIntercambio = false;
            for (int j = 0; j < totalEstudiantes - 1 - i; j++) {
                if (codigos[j] > codigos[j + 1]) {
                    intercambiar(j, j + 1);
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) {
                return;
            }
        }
    }

    /**
     * Búsqueda binaria sobre {@code codigos}. O(log n) comparaciones.
     * Precondición: el tramo {@code 0 .. totalEstudiantes-1} está ordenado ascendente por código.
     * Si no lo está, el descarte de mitades puede saltarse al estudiante y devolver -1.
     * El punto medio {@code inicio + (fin - inicio) / 2} evita el desborde de {@code inicio + fin}.
     */
    public int buscarEstudianteBinario(int codigoBuscado) {
        int inicio = 0;
        int fin = totalEstudiantes - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            if (codigos[medio] == codigoBuscado) {
                return medio;
            }
            if (codigos[medio] < codigoBuscado) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    /**
     * Intercambia dos posiciones de los arreglos paralelos.
     * {@code matrizNotas[i]} es una referencia a la fila: copiarla intercambia
     * las notas completas sin recorrer las columnas.
     */
    private void intercambiar(int i, int j) {
        int codigoTemporal = codigos[i];
        codigos[i] = codigos[j];
        codigos[j] = codigoTemporal;

        double[] tempFila = matrizNotas[i];
        matrizNotas[i] = matrizNotas[j];
        matrizNotas[j] = tempFila;
    }

    private boolean codigoExiste(int codigo) {
        for (int i = 0; i < totalEstudiantes; i++) {
            if (codigos[i] == codigo) {
                return true;
            }
        }
        return false;
    }

    private static boolean notasValidas(double nota1, double nota2) {
        return Double.isFinite(nota1) && Double.isFinite(nota2);
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= totalEstudiantes) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " fuera del tramo lógico (tamaño " + totalEstudiantes + ").");
        }
    }
}
