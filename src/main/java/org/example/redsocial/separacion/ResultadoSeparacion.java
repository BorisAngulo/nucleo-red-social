package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/** El número de saltos y las personas que forman la cadena. */
public class ResultadoSeparacion {

    private int grados = -1;
    private ListaEnlazada<Usuario> camino = new ListaEnlazada<>();

    public boolean encontrado() {
        return grados >= 0;
    }

    public int getGrados() {
        return grados;
    }

    public ListaEnlazada<Usuario> getCamino() {
        return camino;
    }

    void guardarSiMejor(ListaEnlazada<Usuario> candidato) {
        int saltos = candidato.tamano() - 1;
        if (grados < 0 || saltos < grados) {
            grados = saltos;
            camino = candidato;
        }
    }
}
