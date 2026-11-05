package org.example.redsocial.publicaciones;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.estructuradedatos.Pila;
import org.example.estructuradedatos.PilaEnlazada;

/** Pila de publicaciones: lo último publicado es lo primero que se ve. */
public class MuroPublicaciones {

    private final Pila<Publicacion> pila = new PilaEnlazada<>();

    public MuroPublicaciones() {
        publicar("Ana", "Primera foto del día.");
        publicar("Ana", "El café de la tarde.");
    }

    public void publicar(String autor, String texto) {
        pila.apilar(new Publicacion(autor, texto));
    }

    public Publicacion verUltima() {
        if (pila.estaVacia()) {
            return null;
        }
        return pila.cima();
    }

    public Publicacion retirarUltima() {
        if (pila.estaVacia()) {
            return null;
        }
        return pila.desapilar();
    }

    public int tamano() {
        return pila.tamano();
    }

    /** De la cima hacia el fondo, sin sacar ninguna publicación. */
    public ListaEnlazada<Publicacion> desdeLaCima() {
        ListaEnlazada<Publicacion> copia = new ListaEnlazada<>();
        for (Publicacion publicacion : pila) {
            copia.insertarAlFinal(publicacion);
        }
        return copia;
    }

    public String listar() {
        if (pila.estaVacia()) {
            return "Todavía no hay publicaciones.";
        }
        StringBuilder texto = new StringBuilder();
        texto.append("Lo último publicado está arriba.\n");
        for (Publicacion publicacion : pila) {
            texto.append("• ").append(publicacion).append('\n');
        }
        return texto.toString();
    }
}
