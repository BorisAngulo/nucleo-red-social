package org.example.redsocial.anidados;

import org.example.estructuradedatos.ListaEnlazada;

/** Comentario con sus respuestas en una lista. */
public class ComentarioAnidado {

    private final int id;
    private final String autor;
    private final String texto;
    private final ListaEnlazada<ComentarioAnidado> respuestas = new ListaEnlazada<>();

    ComentarioAnidado(int id, String autor, String texto) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    public ListaEnlazada<ComentarioAnidado> getRespuestas() {
        return respuestas;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof ComentarioAnidado comentario)) {
            return false;
        }
        return id == comentario.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
