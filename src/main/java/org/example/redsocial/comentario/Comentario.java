package org.example.redsocial.comentarios;

/**
 * Representa un comentario dentro de una publicación.
 */
public class Comentario {

    private String autor;
    private String contenido;

    public Comentario(String autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return autor + ": " + contenido;
    }
}
