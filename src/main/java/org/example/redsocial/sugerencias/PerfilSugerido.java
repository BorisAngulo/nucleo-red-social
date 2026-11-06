package org.example.redsocial.sugerencias;

/** Perfil sugerido para conectar dentro del carrusel circular. */
public class PerfilSugerido {

    private final String nombre;
    private final String usuario;
    private final String rol;
    private final int amigosEnComun;

    public PerfilSugerido(String nombre, String usuario, String rol, int amigosEnComun) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío.");
        }
        this.nombre = nombre.trim();
        this.usuario = usuario.trim();
        this.rol = (rol != null && !rol.isBlank()) ? rol.trim() : "Miembro";
        this.amigosEnComun = Math.max(0, amigosEnComun);
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    public int getAmigosEnComun() {
        return amigosEnComun;
    }

    @Override
    public String toString() {
        return nombre + " (" + usuario + ") • " + rol + " • " + amigosEnComun + " amigos en común";
    }
}
