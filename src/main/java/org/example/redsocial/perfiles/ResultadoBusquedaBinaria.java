package org.example.redsocial.perfiles;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Encapsula el resultado y la traza de ejecucion paso a paso de la busqueda binaria.
 */
public class ResultadoBusquedaBinaria {

    private final boolean encontrado;
    private final int posicion;
    private final int puntoInsercion;
    private final PerfilUsuario perfil;
    private final int comparaciones;
    private final ListaEnlazada<String> traza;

    public ResultadoBusquedaBinaria(boolean encontrado, int posicion, int puntoInsercion,
                                    PerfilUsuario perfil, int comparaciones,
                                    ListaEnlazada<String> traza) {
        this.encontrado = encontrado;
        this.posicion = posicion;
        this.puntoInsercion = puntoInsercion;
        this.perfil = perfil;
        this.comparaciones = comparaciones;
        this.traza = traza;
    }

    public boolean encontrado() {
        return encontrado;
    }

    public int getPosicion() {
        return posicion;
    }

    public int getPuntoInsercion() {
        return puntoInsercion;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public int getComparaciones() {
        return comparaciones;
    }

    public ListaEnlazada<String> getTraza() {
        return traza;
    }
}
