package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/** Persona de la red. Sus amistades y cuentas seguidas se almacenan en listas enlazadas. */
public class Usuario {

    private final String nombre;
    private final int edad;
    private final String biografia;
    private final ListaEnlazada<Usuario> amigos;
    private final ListaEnlazada<Usuario> seguidos;

    public Usuario(String nombre) {
        this(nombre, 0, "Este usuario todavía no agregó una biografía.");
    }

    public Usuario(String nombre, int edad, String biografia) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (edad < 0 || edad > 120) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 120.");
        }
        this.nombre = nombre.trim();
        this.edad = edad;
        this.biografia = biografia == null || biografia.isBlank()
                ? "Este usuario todavía no agregó una biografía." : biografia.trim();
        this.amigos = new ListaEnlazada<>();
        this.seguidos = new ListaEnlazada<>();
    }

    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getBiografia() { return biografia; }
    public ListaEnlazada<Usuario> getAmigos() { return amigos; }
    public ListaEnlazada<Usuario> getSeguidos() { return seguidos; }

    /** Registra una relación de seguimiento unidireccional. */
    public void seguir(Usuario usuario) {
        if (usuario != null && usuario != this && !seguidos.contiene(usuario)) {
            seguidos.insertarAlFinal(usuario);
        }
    }

    void enlazar(Usuario amigo) {
        if (amigo != null && amigo != this && !amigos.contiene(amigo)) {
            amigos.insertarAlFinal(amigo);
        }
    }

    @Override
    public String toString() { return nombre; }
}
