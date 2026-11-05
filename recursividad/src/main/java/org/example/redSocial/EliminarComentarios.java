
package org.example.redSocial;

import java.util.ArrayList;

public class EliminarComentarios {

    // Clase que representa un comentario
    public static class Comentario {

        private String mensaje;
        private ArrayList<Comentario> respuestas;

        public Comentario(String mensaje) {
            this.mensaje = mensaje;
            this.respuestas = new ArrayList<>();
        }

        public String getMensaje() {
            return mensaje;
        }

        public ArrayList<Comentario> getRespuestas() {
            return respuestas;
        }

        // Agregar una respuesta
        public void agregarRespuesta(Comentario respuesta) {
            if (respuesta != null) {
                respuestas.add(respuesta);
            }
        }

        @Override
        public String toString() {
            return mensaje;
        }
    }

    // Método recursivo para eliminar comentarios y respuestas
    public static void eliminar(Comentario comentario) {

        // Caso base
        if (comentario == null) {
            return;
        }

        // Eliminar recursivamente todas las respuestas
        for (Comentario respuesta : comentario.respuestas) {
            eliminar(respuesta);
        }

        // Vaciar las respuestas
        comentario.respuestas.clear();

        System.out.println("Eliminado: " + comentario.mensaje);
    }

    // Crear comentarios de ejemplo
    public static Comentario crearComentariosEjemplo() {

        Comentario principal = new Comentario("¡Hola a todos!");

        Comentario respuesta1 = new Comentario("¡Hola Ana!");
        Comentario respuesta2 = new Comentario("Saludos a todos.");

        Comentario respuesta3 = new Comentario("¿Cómo estás?");
        Comentario respuesta4 = new Comentario("Muy bien, gracias.");

        principal.agregarRespuesta(respuesta1);
        principal.agregarRespuesta(respuesta2);

        respuesta1.agregarRespuesta(respuesta3);
        respuesta3.agregarRespuesta(respuesta4);

        return principal;
    }
}
