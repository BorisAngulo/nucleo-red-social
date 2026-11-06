package org.example.redsocial.comentarios;

import org.example.estructuradedatos.ListaDoblementeEnlazada;

/**
 * Gestiona el hilo de comentarios de una publicación utilizando una ListaDoblementeEnlazada.
 * Mantiene un cursor (Posición) sobre el comentario visualizado, permitiendo navegación
 * hacia adelante (next) y hacia atrás (prev), inserción de respuestas intermedias en O(1)
 * y eliminación en O(1) sin mover elementos en memoria continua.
 */
public class HiloComentarios {

    private final String autorPost;
    private final String tituloPost;
    private final String contenidoPost;
    private final ListaDoblementeEnlazada<Comentario> comentarios;
    private final ListaDoblementeEnlazada<Comentario>.Posicion posicion;
    private int proximoId = 1;

    public HiloComentarios(String autorPost, String tituloPost, String contenidoPost) {
        this.autorPost = autorPost;
        this.tituloPost = tituloPost;
        this.contenidoPost = contenidoPost;
        this.comentarios = new ListaDoblementeEnlazada<>();
        this.posicion = comentarios.posicionInicial();
    }

    public String getAutorPost() {
        return autorPost;
    }

    public String getTituloPost() {
        return tituloPost;
    }

    public String getContenidoPost() {
        return contenidoPost;
    }

    public ListaDoblementeEnlazada<Comentario> getComentarios() {
        return comentarios;
    }

    public boolean estaVacio() {
        return comentarios.estaVacia();
    }

    public int getCantidad() {
        return comentarios.tamano();
    }

    /** Posición 1-indexada del comentario en foco. 0 si el hilo está vacío. */
    public int getPosicionActual() {
        return posicion.getPosicion();
    }

    public Comentario getComentarioActual() {
        return posicion.ver();
    }

    /** Navega hacia adelante (next) por el enlace del nodo en O(1). */
    public Comentario avanzar() {
        return posicion.avanzar();
    }

    /** Navega hacia atrás (prev) por el enlace del nodo en O(1). */
    public Comentario retroceder() {
        return posicion.retroceder();
    }

    /** Vuelve al primer comentario del hilo. */
    public void fijarAlInicio() {
        posicion.fijarAlInicio();
    }

    /** Mueve el cursor hasta el último comentario. */
    public void irAlFinal() {
        if (comentarios.estaVacia()) {
            return;
        }
        while (posicion.avanzar() != null) {
            // Avanza hasta el último
        }
    }

    public boolean tieneSiguiente() {
        if (posicion.ver() == null) {
            return false;
        }
        return posicion.getPosicion() < comentarios.tamano();
    }

    public boolean tieneAnterior() {
        if (posicion.ver() == null) {
            return false;
        }
        return posicion.getPosicion() > 1;
    }

    /**
     * Agrega un comentario al final de la lista doblemente enlazada.
     * Si la lista estaba vacía, el cursor se posiciona en el nuevo comentario.
     */
    public Comentario agregarAlFinal(String autor, String texto, String hora) {
        Comentario nuevo = new Comentario(proximoId++, autor, texto, hora);
        boolean estabaVacio = comentarios.estaVacia();
        comentarios.insertarAlFinal(nuevo);
        if (estabaVacio) {
            posicion.fijarAlInicio();
        }
        return nuevo;
    }

    /**
     * Agrega un comentario al inicio de la lista.
     */
    public Comentario agregarAlInicio(String autor, String texto, String hora) {
        Comentario nuevo = new Comentario(proximoId++, autor, texto, hora);
        boolean estabaVacio = comentarios.estaVacia();
        comentarios.insertarAlInicio(nuevo);
        if (estabaVacio) {
            posicion.fijarAlInicio();
        }
        return nuevo;
    }

    /**
     * Inserta una respuesta intermedia justo después del comentario en foco en O(1).
     * Engancha el nuevo nodo entre el actual y su siguiente sin desplazar memoria.
     * Avanza el cursor al comentario recién insertado.
     */
    public Comentario insertarRespuesta(String autor, String texto, String hora) {
        Comentario actual = posicion.ver();
        String destino = (actual != null) ? actual.getAutor() : null;
        Comentario nuevo = new Comentario(proximoId++, autor, texto, hora, destino);

        if (comentarios.estaVacia()) {
            comentarios.insertarAlFinal(nuevo);
            posicion.fijarAlInicio();
        } else {
            posicion.insertarDespues(nuevo);
            posicion.avanzar();
        }
        return nuevo;
    }

    /**
     * Elimina el comentario actualmente enfocado en O(1).
     * Desconecta el nodo mediante sus punteros anterior y siguiente.
     * El cursor pasa al siguiente nodo (o al anterior si era el último).
     */
    public Comentario eliminarActual() {
        if (comentarios.estaVacia()) {
            return null;
        }
        return posicion.eliminar();
    }

    /** Hilo de ejemplo preconfigurado para pruebas interactivas. */
    public static HiloComentarios ejemplo() {
        HiloComentarios hilo = new HiloComentarios(
                "CarlosDev",
                "Actualización del Núcleo de Red Social v2.0",
                "¡Hola comunidad! Desplegamos la arquitectura modular con listas enlazadas dinámicas. Los comentarios ahora usan una lista doblemente enlazada para inserciones O(1).");

        hilo.agregarAlFinal("Ana", "¡Excelente trabajo! La navegación prev/next se siente instantánea.", "10:14");
        hilo.agregarAlFinal("Luis", "¿Por qué lista doble y no un arreglo dinámico como ArrayList?", "10:18");
        hilo.agregarAlFinal("Mia", "Porque insertar respuestas intermedias o borrar comentarios en un arreglo cuesta O(N) por reacomodo de memoria, mientras que en lista doble es O(1).", "10:22");
        hilo.agregarAlFinal("Leo", "¡Totalmente de acuerdo! Además, cada nodo almacena los punteros prev y next para recorrer en ambas direcciones.", "10:25");
        hilo.agregarAlFinal("Sofia", "El modo oscuro con la paleta #121212 quedó impecable.", "10:30");

        hilo.fijarAlInicio();
        return hilo;
    }
}
