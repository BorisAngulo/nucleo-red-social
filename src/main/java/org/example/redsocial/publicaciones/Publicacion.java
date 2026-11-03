package org.example.redsocial.publicaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modela una publicación dentro de la red social.
 */
public class Publicacion {

    private Contenido contenido;
    private String titulo;
    private String descripcion;
    private long vistas;
    private int likes;
    private String[] etiquetas;
    private int codigoUsuario;

    public Publicacion(Contenido contenido, String titulo, String descripcion, int codigoUsuario) {
        this(contenido, titulo, descripcion, conversor(descripcion), codigoUsuario);
    }

    public Publicacion(Contenido contenido, String titulo, String descripcion, String[] etiquetas, int codigoUsuario) {
        this.contenido = contenido != null ? contenido : new Contenido(new char[0]);
        this.titulo = titulo != null ? titulo : "";
        this.descripcion = descripcion != null ? descripcion : "";
        this.etiquetas = etiquetas != null ? etiquetas : new String[0];
        this.codigoUsuario = codigoUsuario;
        this.vistas = 0;
        this.likes = 0;
    }

    public static String[] conversor(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            return new String[0];
        }
        List<String> lista = new ArrayList<>();
        Pattern patron = Pattern.compile("#([a-zA-Z0-9_áéíóúÁÉÍÓÚñÑ]+)");
        Matcher matcher = patron.matcher(descripcion);
        while (matcher.find()) {
            lista.add(matcher.group(1));
        }
        return lista.toArray(new String[0]);
    }

    public void incrementarVistas() {
        this.vistas++;
    }

    public void darLike() {
        this.likes++;
    }

    public void quitarLike() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.etiquetas = conversor(descripcion);
    }

    public long getVistas() {
        return vistas;
    }

    public int getLikes() {
        return likes;
    }

    public String[] getEtiquetas() {
        return etiquetas;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public boolean tieneEtiqueta(String etiquetaBuscada) {
        if (etiquetaBuscada == null || etiquetas == null) return false;
        String limpia = etiquetaBuscada.replace("#", "").trim();
        for (String e : etiquetas) {
            if (e != null && e.equalsIgnoreCase(limpia)) {
                return true;
            }
        }
        return false;
    }

    public String mostrarEtiquetas() {
        if (etiquetas == null || etiquetas.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String e : etiquetas) {
            if (e != null && !e.trim().isEmpty()) {
                sb.append("#").append(e.trim()).append(" ");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return String.format("[%d] %s: %s (%d likes, %d vistas)", 
            codigoUsuario, titulo, descripcion, likes, vistas);
    }
}

