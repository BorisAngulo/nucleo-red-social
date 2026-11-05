package org.example.redsocial.comentario;

import org.example.estructuradedatos.ListaDoblementeEnlazada;

/**
 * Administra el hilo de comentarios de una publicación.
 */
public class HiloComentarios {

    private ListaDoblementeEnlazada<Comentario> comentarios;
    private ListaDoblementeEnlazada<Comentario>.Posicion posicion;

    public HiloComentarios() {
        comentarios = new ListaDoblementeEnlazada<>();
        posicion = comentarios.posicionInicial();
    }

    // Agrega un comentario al final del hilo
    public void agregarComentario(String autor, String contenido) {
        Comentario comentario = new Comentario(autor, contenido);

        comentarios.insertarAlFinal(comentario);

        if (comentarios.tamano() == 1) {
            posicion.fijarAlInicio();
        }
    }

    // Responde al comentario actual
    public void responder(String autor, String contenido) {
        if (posicion.ver() == null) {
            agregarComentario(autor, contenido);
            return;
        }

        Comentario respuesta = new Comentario(autor, contenido);

        posicion.insertarDespues(respuesta);
        posicion.avanzar();
    }

    // Avanza al siguiente comentario
    public boolean siguiente() {
        return posicion.avanzar() != null;
    }

    // Retrocede al comentario anterior
    public boolean anterior() {
        return posicion.retroceder() != null;
    }

    // Obtiene el comentario actual
    public Comentario obtenerActual() {
        return posicion.ver();
    }

    // Obtiene la posición actual
    public int obtenerPosicion() {
        return posicion.getPosicion();
    }

    // Obtiene la cantidad de comentarios
    public int obtenerCantidad() {
        return comentarios.tamano();
    }

    // Verifica si el hilo está vacío
    public boolean estaVacio() {
        return comentarios.estaVacia();
    }

    // Elimina el comentario actual
    public Comentario eliminarActual() {
        return posicion.eliminar();
    }

    // Regresa al primer comentario
    public void irAlInicio() {
        posicion.fijarAlInicio();
    }
}
