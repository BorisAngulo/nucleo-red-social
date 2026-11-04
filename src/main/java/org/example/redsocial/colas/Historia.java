package org.example.redsocial.colas;

public class Historia {

    private final String usuario;
    private final String contenido;

    public Historia(String usuario, String contenido) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacio.");
        }

        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacio.");
        }

        this.usuario = usuario.trim();
        this.contenido = contenido.trim();
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String toString() {
        return usuario + ": " + contenido;
    }
}