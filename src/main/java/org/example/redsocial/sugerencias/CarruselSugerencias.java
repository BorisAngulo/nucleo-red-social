package org.example.redsocial.sugerencias;

import org.example.estructuradedatos.ListaCircular;

/**
 * Gestiona los perfiles sugeridos de amistad utilizando una ListaCircular.
 * Al llegar al último nodo, el siguiente vuelve automáticamente a la cabeza,
 * permitiendo recorrer el carrusel una y otra vez de forma infinita.
 */
public class CarruselSugerencias {

    private final ListaCircular<PerfilSugerido> perfiles;
    private int indiceActual = 0;

    public CarruselSugerencias() {
        this.perfiles = new ListaCircular<>();
    }

    public boolean estaVacio() {
        return perfiles.estaVacia();
    }

    public int getCantidad() {
        return perfiles.tamano();
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    /** Obtiene el perfil actualmente enfocado en el carrusel. */
    public PerfilSugerido actual() {
        if (perfiles.estaVacia()) {
            return null;
        }
        return perfiles.obtener(indiceActual);
    }

    /**
     * Avanza al siguiente perfil de forma circular.
     * Si supera el último elemento, regresa al primero (índice 0).
     */
    public PerfilSugerido siguiente() {
        if (perfiles.estaVacia()) {
            return null;
        }
        indiceActual = (indiceActual + 1) % perfiles.tamano();
        return actual();
    }

    /**
     * Retrocede al perfil anterior de forma circular.
     * Si está en el primero, salta al último elemento.
     */
    public PerfilSugerido anterior() {
        if (perfiles.estaVacia()) {
            return null;
        }
        indiceActual = (indiceActual - 1 + perfiles.tamano()) % perfiles.tamano();
        return actual();
    }

    /**
     * Agrega un nuevo perfil al final de la lista circular.
     */
    public void agregar(PerfilSugerido perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil no puede ser nulo.");
        }
        perfiles.insertarAlFinal(perfil);
    }

    /**
     * Descarta o conecta al perfil en pantalla, retirándolo de la lista circular.
     * El carrusel se mantiene en la posición actual o vuelve al inicio si la lista cambió.
     */
    public PerfilSugerido eliminarActual() {
        if (perfiles.estaVacia()) {
            return null;
        }
        PerfilSugerido eliminado = perfiles.eliminarEn(indiceActual);
        if (perfiles.estaVacia()) {
            indiceActual = 0;
        } else if (indiceActual >= perfiles.tamano()) {
            indiceActual = 0;
        }
        return eliminado;
    }

    public ListaCircular<PerfilSugerido> getPerfiles() {
        return perfiles;
    }

    /** Ejemplo de carrusel cargado con perfiles sugeridos. */
    public static CarruselSugerencias ejemplo() {
        CarruselSugerencias carrusel = new CarruselSugerencias();
        carrusel.agregar(new PerfilSugerido("Elena Ruiz", "@elena_r", "Desarrolladora Backend", 5));
        carrusel.agregar(new PerfilSugerido("Marcos Paz", "@marcos_paz", "Diseñador UI/UX", 3));
        carrusel.agregar(new PerfilSugerido("Lucía Torres", "@lucia_t", "Ingeniera de Datos", 7));
        carrusel.agregar(new PerfilSugerido("Andrés Mendoza", "@andres_m", "Desarrollador Mobile", 2));
        carrusel.agregar(new PerfilSugerido("Camila Vega", "@cami_vega", "Especialista en QA", 4));
        carrusel.agregar(new PerfilSugerido("Rodrigo Silva", "@rodrigo_s", "Arquitecto Cloud", 6));
        return carrusel;
    }
}
