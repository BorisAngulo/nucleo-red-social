package org.example.redsocial.matriz;

/** Usuario registrado en el catálogo de la matriz de adyacencia. */
public final class UsuarioMatriz {

    private final String nombre;

    UsuarioMatriz(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
