package org.example.redsocial.publicaciones;

import java.io.File;

/**
 * Contenido multimedia adjunto a una publicación (formatos y rutas de imagen).
 */
public class Contenido {

    private final char[] formatos;
    private String rutaImagen;

    public Contenido(char[] formatos) {
        this(formatos, null);
    }

    public Contenido(char[] formatos, String rutaImagen) {
        this.formatos = formatos != null ? formatos.clone() : new char[0];
        this.rutaImagen = rutaImagen;
    }

    public char[] getFormatos() {
        return formatos != null ? formatos.clone() : new char[0];
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public boolean tieneRutaImagen() {
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return false;
        }
        File f = new File(rutaImagen);
        return f.exists() && f.isFile();
    }

    public int getCantidad() {
        return formatos != null ? formatos.length : 0;
    }

    public int getCantidadImagenes() {
        int count = 0;
        for (char f : formatos) {
            char c = Character.toLowerCase(f);
            if (c == 'f' || c == 'i') count++;
        }
        return count;
    }

    public int getCantidadVideos() {
        int count = 0;
        for (char f : formatos) {
            if (Character.toLowerCase(f) == 'v') count++;
        }
        return count;
    }

    public String resumenVisual() {
        int img = getCantidadImagenes();
        int vid = getCantidadVideos();
        if (img == 0 && vid == 0 && !tieneRutaImagen()) {
            return "Sin archivos adjuntos";
        }
        StringBuilder sb = new StringBuilder();
        if (tieneRutaImagen()) {
            sb.append("🖼️ Archivo: ").append(new File(rutaImagen).getName()).append(" ");
        }
        if (img > 0) sb.append("📷 ").append(img).append(img == 1 ? " Imagen " : " Imágenes ");
        if (vid > 0) sb.append("🎥 ").append(vid).append(vid == 1 ? " Video" : " Videos");
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return resumenVisual();
    }
}
