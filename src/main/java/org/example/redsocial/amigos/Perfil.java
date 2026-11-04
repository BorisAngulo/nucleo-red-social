
package org.example.redsocial.amigos;

import java.util.Objects;

public class Perfil {

    private String nombre;
    private String usuario;
    private String descripcion;
    private String rutaFoto;
    private boolean siguiendo;

    public Perfil(String nombre, String usuario,
                  String descripcion, String rutaFoto) {

        this.nombre = nombre;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.rutaFoto = rutaFoto;
        this.siguiendo = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public boolean isSiguiendo() {
        return siguiendo;
    }

    public void cambiarSeguimiento() {
        siguiendo = !siguiendo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Perfil)) {
            return false;
        }

        Perfil otro = (Perfil) objeto;

        return Objects.equals(usuario, otro.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario);
    }

    @Override
    public String toString() {
        return nombre + " (@" + usuario + ")";
    }
}
