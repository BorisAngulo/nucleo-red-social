
package org.example.redSocial;

import org.example.estructuraDeDatos.ListaEnlazada;

import java.util.Objects;

public class UsuarioRed {

    private final int id;
    private final String nombre;
    private final ListaEnlazada<UsuarioRed> amigos;

    public UsuarioRed(int id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        this.id = id;
        this.nombre = nombre;
        this.amigos = new ListaEnlazada<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaEnlazada<UsuarioRed> getAmigos() {
        return amigos;
    }

    // Agrega una amistad en ambas direcciones.
    public void conectarCon(UsuarioRed usuario) {
        if (usuario == null || this.equals(usuario)) {
            return;
        }

        if (!amigos.contiene(usuario)) {
            amigos.insertarAlFinal(usuario);
        }

        if (!usuario.amigos.contiene(this)) {
            usuario.amigos.insertarAlFinal(this);
        }
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof UsuarioRed)) return false;

        UsuarioRed otro = (UsuarioRed) objeto;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
