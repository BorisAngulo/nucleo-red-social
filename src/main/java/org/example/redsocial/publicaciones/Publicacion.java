package org.example.redsocial.publicaciones;

/** Una publicación del perfil. La más nueva queda en la cima de la pila. */
public class Publicacion {

    private final String autor;
    private final String texto;

    public Publicacion(String autor, String texto) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto no puede estar vacío.");
        }
        this.autor = autor.trim();
        this.texto = texto.trim();
    }

    public String getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String toString() {
        return autor + ": " + texto;
    }
}
