package org.example.redsocial.colas;

import org.example.estructuradedatos.ColaArreglo;

public class CarruselHistorias {

    private static final int CAPACIDAD = 50;

    private final ColaArreglo<Historia> historias;

    public CarruselHistorias() {
        historias = new ColaArreglo<>(CAPACIDAD);
    }

    public void agregar(Historia historia) {
        if (historia == null) {
            throw new IllegalArgumentException("La historia no puede ser nula.");
        }

        historias.encolar(historia);
    }

    public void agregar(String usuario, String contenido) {
        agregar(new Historia(usuario, contenido));
    }

    public Historia verActual() {
        if (historias.estaVacia()) {
            return null;
        }

        return historias.frente();
    }

    public Historia siguiente() {
        if (historias.estaVacia()) {
            return null;
        }

        Historia actual = historias.desencolar();
        historias.encolar(actual);

        return historias.frente();
    }

    public Historia siguienteHistoria() {
        return siguiente();
    }

    public Historia obtenerActual() {
        return verActual();
    }

    public Historia anterior() {
        if (historias.estaVacia()) {
            return null;
        }

        int cantidad = historias.tamano();

        for (int i = 0; i < cantidad - 1; i++) {
            historias.encolar(historias.desencolar());
        }

        return historias.frente();
    }

    public int cantidadHistorias() {
        return historias.tamano();
    }

    public boolean estaVacio() {
        return historias.estaVacia();
    }

    public void limpiar() {
        historias.vaciar();
    }
}