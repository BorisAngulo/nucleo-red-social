package org.example.redsocial.comentarios;

/**
 * Representa un comentario en el hilo de una publicación.
 */
public class Comentario {

    private final int id;
    private final String autor;
    private final String texto;
    private final String hora;
    private final String respuestaA;

    public Comentario(int id, String autor, String texto, String hora, String respuestaA) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto del comentario no puede estar vacío.");
        }
        this.id = id;
        this.autor = autor.trim();
        this.texto = texto.trim();
        this.hora = (hora != null && !hora.isBlank()) ? hora.trim() : "Ahora";
        this.respuestaA = (respuestaA != null && !respuestaA.isBlank()) ? respuestaA.trim() : null;
    }

    public Comentario(int id, String autor, String texto, String hora) {
        this(id, autor, texto, hora, null);
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

    public String getHora() {
        return hora;
    }

    public String getRespuestaA() {
        return respuestaA;
    }

    public boolean esRespuesta() {
        return respuestaA != null;
    }

    @Override
    public String toString() {
        String resp = esRespuesta() ? " [resp a @" + respuestaA + "]" : "";
        return "#" + id + " @" + autor + resp + ": \"" + texto + "\" (" + hora + ")";
    }
}
