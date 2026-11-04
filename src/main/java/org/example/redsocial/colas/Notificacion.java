package org.example.redsocial.colas;

public class Notificacion {

    private final String usuario;
    private final String mensaje;

    public Notificacion(String usuario, String mensaje) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacio.");
        }

        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacio.");
        }

        this.usuario = usuario.trim();
        this.mensaje = mensaje.trim();
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return usuario + " " + mensaje;
    }
}