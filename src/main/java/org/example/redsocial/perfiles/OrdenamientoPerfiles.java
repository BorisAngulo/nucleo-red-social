package org.example.redsocial.perfiles;

/** Algoritmos clásicos de ordenamiento, sin colecciones de Java. */
public final class OrdenamientoPerfiles {
    private OrdenamientoPerfiles() {}

    /** QuickSort ascendente por ID. */
    public static void quickSortPorId(Perfil[] a) {
        if (a != null && a.length > 1) quickSort(a, 0, a.length - 1);
    }
    private static void quickSort(Perfil[] a, int izq, int der) {
        int i = izq, j = der;
        int pivote = a[izq + (der - izq) / 2].getId();
        while (i <= j) {
            while (a[i].getId() < pivote) i++;
            while (a[j].getId() > pivote) j--;
            if (i <= j) {
                Perfil temp = a[i]; a[i] = a[j]; a[j] = temp;
                i++; j--;
            }
        }
        if (izq < j) quickSort(a, izq, j);
        if (i < der) quickSort(a, i, der);
    }

    /** MergeSort ascendente por nombre, ignorando mayúsculas/minúsculas. */
    public static void mergeSortPorNombre(Perfil[] a) {
        if (a == null || a.length < 2) return;
        Perfil[] auxiliar = new Perfil[a.length];
        mergeSort(a, auxiliar, 0, a.length - 1);
    }
    private static void mergeSort(Perfil[] a, Perfil[] aux, int izq, int der) {
        if (izq >= der) return;
        int medio = izq + (der - izq) / 2;
        mergeSort(a, aux, izq, medio);
        mergeSort(a, aux, medio + 1, der);
        int i = izq, j = medio + 1, k = izq;
        while (i <= medio && j <= der) {
            if (a[i].getNombre().compareToIgnoreCase(a[j].getNombre()) <= 0) aux[k++] = a[i++];
            else aux[k++] = a[j++];
        }
        while (i <= medio) aux[k++] = a[i++];
        while (j <= der) aux[k++] = a[j++];
        for (k = izq; k <= der; k++) a[k] = aux[k];
    }
}
