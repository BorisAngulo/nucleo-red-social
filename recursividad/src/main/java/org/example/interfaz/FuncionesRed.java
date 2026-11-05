
package org.example.interfaz;

public enum FuncionesRed {

    GRADOS_DE_SEPARACION(
            "Grados de separación",
            "Recursividad",
            "Calcula la distancia entre dos usuarios."
    ),

    RUTA_DE_AMIGOS(
            "Cadenas de contactos",
            "Recursividad",
            "Encuentra la ruta de amigos entre dos usuarios."
    ),

    ELIMINAR_COMENTARIOS(
            "Comentarios anidados",
            "Recursividad",
            "Elimina un comentario y todas sus respuestas."
    );

    private final String nombre;
    private final String grupo;
    private final String descripcion;

    FuncionesRed(String nombre, String grupo, String descripcion) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

