package org.example.practicas.practica9;

import org.example.estructuradedatos.ListaDoblementeEnlazada;

/**
 * Visor de fotos sobre una lista doblemente enlazada.
 * Se queda en el nodo de la foto en pantalla.
 * Adelante sigue su enlace siguiente y atrás sigue su enlace anterior.
 */
public class VisorGaleria {

    private final ListaDoblementeEnlazada<Foto> fotos;
    private final ListaDoblementeEnlazada<Foto>.Posicion viendo;

    public VisorGaleria() {
        this.fotos = new ListaDoblementeEnlazada<>();
        this.viendo = fotos.posicionInicial();
    }

    public boolean estaVacia() {
        return fotos.estaVacia();
    }

    public int getTamano() {
        return fotos.tamano();
    }

    /** Posición en pantalla, de 1 a tamaño. Cero si no hay fotos. */
    public int getPosicion() {
        return viendo.getPosicion();
    }

    public Foto actual() {
        return viendo.ver();
    }

    /** Agrega la foto al final. Si la galería estaba vacía, esa queda en pantalla. */
    public void agregar(Foto foto) {
        validar(foto);
        boolean estabaVacia = fotos.estaVacia();
        fotos.insertarAlFinal(foto);
        if (estabaVacia) {
            viendo.fijarAlInicio();
        }
    }

    /**
     * Inserta justo después de la foto en pantalla, enganchando ese nodo.
     * Con la galería vacía, la nueva foto queda primera y se muestra.
     */
    public void insertarDespues(Foto foto) {
        validar(foto);
        viendo.insertarDespues(foto);
    }

    /** Pasa a la foto siguiente por el enlace del nodo. En la última no se mueve. */
    public Foto avanzar() {
        return viendo.avanzar();
    }

    /** Vuelve a la foto anterior por el enlace del nodo. En la primera no se mueve. */
    public Foto retroceder() {
        return viendo.retroceder();
    }

    /**
     * Quita la foto en pantalla desenganchando su nodo.
     * El visor se queda en la siguiente, o en la nueva última si se borró la del final.
     */
    public Foto eliminarActual() {
        return viendo.eliminar();
    }

    public void mostrar() {
        if (fotos.estaVacia()) {
            System.out.println("[ Galería vacía: no hay fotos ]");
            return;
        }
        System.out.println("  " + fotos.tamano() + " fotos. Viendo la " + getPosicion() + ".");
        int posicion = 1;
        for (Foto foto : fotos) {
            String marca = posicion == getPosicion() ? "  <-- [VIENDO]" : "";
            System.out.println("  " + posicion + ". " + foto + marca);
            posicion++;
        }
    }

    private static void validar(Foto foto) {
        if (foto == null) {
            throw new IllegalArgumentException("La foto no puede ser nula.");
        }
    }
}
