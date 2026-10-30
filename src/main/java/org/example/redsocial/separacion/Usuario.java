package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/** Persona de la red. Sus amigos son una lista enlazada. */
public class Usuario {

    private final String nombre;
    private final ListaEnlazada<Usuario> amigos;

    public Usuario(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
        this.amigos = new ListaEnlazada<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ListaEnlazada<Usuario> getAmigos() {
        return amigos;
    }

    void enlazar(Usuario amigo) {
        if (amigo != null && amigo != this && !amigos.contiene(amigo)) {
            amigos.insertarAlFinal(amigo);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
