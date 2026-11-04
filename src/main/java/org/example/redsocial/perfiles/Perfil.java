package org.example.redsocial.perfiles;

import org.example.redsocial.separacion.Usuario;

/** Vista indexable de un usuario existente en la red de amistades. */
public final class Perfil {
    private final int id;
    private final Usuario usuario;

    public Perfil(int id, Usuario usuario) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser positivo.");
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");
        this.id = id;
        this.usuario = usuario;
    }

    public int getId() { return id; }
    public String getNombre() { return usuario.getNombre(); }
    public Usuario getUsuario() { return usuario; }

    @Override public String toString() { return id + " - " + getNombre(); }
}
