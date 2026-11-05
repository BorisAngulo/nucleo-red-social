package org.example.redsocial.hashtags;

import org.example.estructuradedatos.ArbolBinario;
import org.example.estructuradedatos.ListaEnlazada;

/** Árbol binario de búsqueda con las tendencias. El orden es el alfabético. */
public class IndiceHashtags {

    private final ArbolBinario<String> arbol = new ArbolBinario<>();

    public IndiceHashtags() {
        agregar("fotos");
        agregar("java");
        agregar("javascript");
        agregar("juegos");
        agregar("moda");
        agregar("musica");
    }

    public boolean agregar(String texto) {
        String etiqueta = normalizar(texto);
        if (etiqueta == null) {
            return false;
        }
        return arbol.insertar(etiqueta);
    }

    /** Etiquetas en orden alfabético que empiezan por el texto escrito. */
    public ListaEnlazada<String> sugerir(String prefijo) {
        String clave = normalizar(prefijo);
        ListaEnlazada<String> coincidencias = new ListaEnlazada<>();
        if (clave == null || arbol.estaVacio()) {
            return coincidencias;
        }
        for (String etiqueta : arbol.inorden()) {
            if (etiqueta.startsWith(clave)) {
                coincidencias.insertarAlFinal(etiqueta);
            }
        }
        return coincidencias;
    }

    public ListaEnlazada<String> enOrden() {
        return arbol.inorden();
    }

    /** Saca cada etiqueta en postorden y deja el árbol vacío. */
    public ListaEnlazada<String> vaciarEnPostorden() {
        ListaEnlazada<String> orden = arbol.postorden();
        for (String etiqueta : orden) {
            arbol.eliminar(etiqueta);
        }
        return orden;
    }

    public boolean estaVacio() {
        return arbol.estaVacio();
    }

    public int tamano() {
        return arbol.tamano();
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim().toLowerCase();
        if (limpio.startsWith("#")) {
            limpio = limpio.substring(1).trim();
        }
        if (limpio.isEmpty()) {
            return null;
        }
        return limpio;
    }
}
