package org.example.practicas.practica10;

/** Un tema de la lista: título, artista y duración en segundos. */
public class Cancion {

    private final String titulo;
    private final String artista;
    private final int duracionSegundos;

    public Cancion(String titulo, String artista, int duracionSegundos) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("El artista no puede estar vacío.");
        }
        if (duracionSegundos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracionSegundos = duracionSegundos;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    @Override
    public String toString() {
        int minutos = duracionSegundos / 60;
        int segundos = duracionSegundos % 60;
        String reloj = minutos + ":" + (segundos < 10 ? "0" : "") + segundos;
        return titulo + " | " + artista + " | " + reloj;
    }
}
