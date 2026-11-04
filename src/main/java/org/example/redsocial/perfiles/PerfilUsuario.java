package org.example.redsocial.perfiles;

/**
 * Representa el perfil de un usuario en la red social.
 * Mantiene la información pública del usuario y permite ordenamiento lexicográfico por su nombre de usuario.
 */
public class PerfilUsuario implements Comparable<PerfilUsuario> {

    private final String nombreUsuario;
    private final String nombreCompleto;
    private final String biografia;
    private final int seguidores;
    private final int seguidos;

    public PerfilUsuario(String nombreUsuario, String nombreCompleto, String biografia, int seguidores, int seguidos) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío.");
        }
        this.nombreUsuario = limpiarNombreUsuario(nombreUsuario);
        this.nombreCompleto = nombreCompleto.trim();
        this.biografia = (biografia != null && !biografia.isBlank()) ? biografia.trim() : "Sin biografía.";
        this.seguidores = Math.max(0, seguidores);
        this.seguidos = Math.max(0, seguidos);
    }

    private static String limpiarNombreUsuario(String usuario) {
        String limpio = usuario.trim();
        if (limpio.startsWith("@")) {
            limpio = limpio.substring(1);
        }
        if (limpio.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        return limpio;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getBiografia() {
        return biografia;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public int getSeguidos() {
        return seguidos;
    }

    @Override
    public int compareTo(PerfilUsuario otro) {
        if (otro == null) {
            return 1;
        }
        return this.nombreUsuario.compareToIgnoreCase(otro.nombreUsuario);
    }

    @Override
    public String toString() {
        return "@" + nombreUsuario + " (" + nombreCompleto + ")";
    }
}
