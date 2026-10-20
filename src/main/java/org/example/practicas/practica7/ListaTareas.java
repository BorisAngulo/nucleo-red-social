package org.example.practicas.practica7;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Gestor de tareas sobre una lista simplemente enlazada.
 * Cada alta crea un nodo en el Heap y lo engancha por el campo siguiente.
 * Las posiciones de esta clase empiezan en 1.
 */
public class ListaTareas {

    private final ListaEnlazada<Tarea> tareas;

    public ListaTareas() {
        this.tareas = new ListaEnlazada<>();
    }

    public boolean estaVacia() {
        return tareas.estaVacia();
    }

    public int getTamano() {
        return tareas.tamano();
    }

    public void mostrarLista() {
        if (estaVacia()) {
            System.out.println("[ Lista de tareas vacía: no hay pendientes ]");
            return;
        }
        int posicion = 1;
        for (Tarea tarea : tareas) {
            System.out.println("  " + posicion + ". " + tarea);
            posicion++;
        }
    }

    public void agregarAlInicio(Tarea nuevaTarea) {
        validar(nuevaTarea);
        tareas.insertarAlInicio(nuevaTarea);
    }

    public void agregarAlFinal(Tarea nuevaTarea) {
        validar(nuevaTarea);
        tareas.insertarAlFinal(nuevaTarea);
    }

    /**
     * Inserta en una posición de 1 a tamaño + 1.
     * La 1 reutiliza el alta al inicio y la última, el alta al final.
     * Fuera de ese rango devuelve false y la lista no cambia.
     */
    public boolean insertarEnPosicion(int posicion, Tarea nuevaTarea) {
        validar(nuevaTarea);
        if (posicion < 1 || posicion > tareas.tamano() + 1) {
            return false;
        }
        tareas.insertarEn(posicion - 1, nuevaTarea);
        return true;
    }

    /** Quita la primera tarea con ese id. Si no está, devuelve false. */
    public boolean eliminarPorId(int id) {
        int indice = 0;
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tareas.eliminarEn(indice);
                return true;
            }
            indice++;
        }
        return false;
    }

    private static void validar(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
    }
}
