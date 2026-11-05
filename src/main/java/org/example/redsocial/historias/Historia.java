package org.example.redsocial.historias;

/** Una historia del carrusel. Desaparece cuando se le acaban las horas. */
public class Historia {

    private final String autor;
    private final String texto;
    private int horas;

    public Historia(String autor, String texto, int horas) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto no puede estar vacío.");
        }
        if (horas <= 0 || horas > 24) {
            throw new IllegalArgumentException("La historia dura entre 1 y 24 horas.");
        }
        this.autor = autor.trim();
        this.texto = texto.trim();
        this.horas = horas;
    }

    public String getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    public int getHoras() {
        return horas;
    }

    void restarHora() {
        horas--;
    }

    @Override
    public String toString() {
        return autor + " (" + horas + " h): " + texto;
    }
}
