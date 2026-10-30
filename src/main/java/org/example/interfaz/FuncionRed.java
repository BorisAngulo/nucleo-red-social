package org.example.interfaz;

/** Cada botón de la pantalla. La función todavía no está implementada. */
public enum FuncionRed {

    CONEXIONES_DIRECTAS(
            "Conexiones directas",
            "Matriz y arreglo",
            "Matriz de amistades o seguimientos."),
    BUSCAR_PERFILES(
            "Buscar perfiles",
            "Matriz y arreglo",
            "Arreglo de usuarios ordenado, con búsqueda binaria."),
    PUBLICACIONES_DEL_PERFIL(
            "Publicaciones del perfil",
            "Pilas",
            "Pila de publicaciones: lo último publicado se ve primero."),
    HISTORIAL_DE_NAVEGACION(
            "Historial de navegación",
            "Pilas",
            "Búsquedas recientes y el botón Atrás."),
    NOTIFICACIONES(
            "Notificaciones",
            "Colas",
            "Cola de eventos: se atienden en el orden en que llegaron."),
    HISTORIAS_ACTIVAS(
            "Historias activas",
            "Colas",
            "Carrusel circular de historias de 24 horas."),
    HILO_DE_COMENTARIOS(
            "Hilo de comentarios",
            "Listas enlazadas",
            "Lista doble para ir al comentario siguiente o al anterior."),
    AMIGOS_SUGERIDOS(
            "Amigos sugeridos",
            "Listas enlazadas",
            "Lista circular de perfiles que se recorre una y otra vez."),
    GRADOS_DE_SEPARACION(
            "Grados de separación",
            "Recursividad",
            "Cuántos amigos hay entre dos usuarios."),
    RUTA_DE_AMIGOS(
            "Ruta de amigos",
            "Recursividad",
            "Por qué cadena de contactos un usuario llega a otro."),
    BORRAR_COMENTARIOS_ANIDADOS(
            "Borrar comentarios anidados",
            "Recursividad",
            "Quita un comentario y todas sus respuestas."),
    INDICE_DE_HASHTAGS(
            "Índice de hashtags",
            "Árboles",
            "Árbol binario de búsqueda para autocompletar tendencias."),
    RECORRER_HASHTAGS(
            "Recorrer hashtags",
            "Árboles",
            "Inorden para listarlos y postorden para vaciar el árbol."),
    MODERACION_DE_CONTENIDO(
            "Moderación de contenido",
            "Árboles",
            "Árbol de decisión para clasificar reportes y afinidad.");

    private final String nombre;
    private final String grupo;
    private final String resumen;

    FuncionRed(String nombre, String grupo, String resumen) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.resumen = resumen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getResumen() {
        return resumen;
    }
}
