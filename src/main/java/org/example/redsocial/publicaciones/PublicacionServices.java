package org.example.redsocial.publicaciones;

import java.util.ArrayList;
import java.util.List;
import org.example.estructuradedatos.PilaEnlazada;

/**
 * Servicio de negocio para gestionar el feed de publicaciones con Pila LIFO.
 */
public class PublicacionServices {

    private final PilaEnlazada<Publicacion> publicacionesRecientes = new PilaEnlazada<>();

    public void agregarPublicacion(Publicacion publicacion) {
        if (publicacion == null) {
            throw new IllegalArgumentException("La publicación no puede ser nula");
        }
        publicacionesRecientes.apilar(publicacion);
    }

    public List<Publicacion> obtenerTodas() {
        List<Publicacion> lista = new ArrayList<>();
        for (Publicacion p : publicacionesRecientes) {
            lista.add(p);
        }
        return lista;
    }

    public List<Publicacion> filtrarPorUsuario(int codigoUsuario) {
        List<Publicacion> lista = new ArrayList<>();
        for (Publicacion p : publicacionesRecientes) {
            if (p.getCodigoUsuario() == codigoUsuario) {
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Publicacion> buscarPorEtiqueta(String etiqueta) {
        List<Publicacion> lista = new ArrayList<>();
        if (etiqueta == null || etiqueta.trim().isEmpty()) {
            return lista;
        }
        String limpia = etiqueta.replace("#", "").trim();
        for (Publicacion p : publicacionesRecientes) {
            if (p.tieneEtiqueta(limpia)) {
                lista.add(p);
            }
        }
        return lista;
    }

    public Publicacion obtenerMasReciente() {
        if (publicacionesRecientes.estaVacia()) {
            return null;
        }
        return publicacionesRecientes.cima();
    }

    public Publicacion eliminarMasReciente() {
        if (publicacionesRecientes.estaVacia()) {
            return null;
        }
        return publicacionesRecientes.desapilar();
    }

    public int totalPublicaciones() {
        return publicacionesRecientes.tamano();
    }

    public boolean estaVacio() {
        return publicacionesRecientes.estaVacia();
    }
}

