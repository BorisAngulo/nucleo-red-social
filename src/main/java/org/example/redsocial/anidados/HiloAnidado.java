package org.example.redsocial.anidados;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Hilo con respuestas dentro de respuestas.
 * Borrar un comentario baja primero por todas sus respuestas.
 */
public class HiloAnidado {

    private final ListaEnlazada<ComentarioAnidado> raices = new ListaEnlazada<>();
    private int siguienteId = 1;

    public HiloAnidado() {
        ComentarioAnidado ana = agregar("Ana", "Qué buena foto");
        ComentarioAnidado luis = responder(ana.getId(), "Luis", "Gracias");
        responder(luis.getId(), "Mia", "Yo también quiero ir");
        responder(ana.getId(), "Bea", "¿Dónde fue?");
        agregar("Sol", "Me encanta el color");
    }

    public ComentarioAnidado agregar(String autor, String texto) {
        ComentarioAnidado comentario = nuevo(autor, texto);
        raices.insertarAlFinal(comentario);
        return comentario;
    }

    public ComentarioAnidado responder(int idPadre, String autor, String texto) {
        ComentarioAnidado padre = buscar(idPadre);
        if (padre == null) {
            return null;
        }
        ComentarioAnidado respuesta = nuevo(autor, texto);
        padre.getRespuestas().insertarAlFinal(respuesta);
        return respuesta;
    }

    /**
     * Quita el comentario y, antes, cada respuesta. Devuelve cuántos se fueron,
     * o 0 si ese número no está.
     */
    public int borrar(int id) {
        return borrarEn(raices, id);
    }

    public String listar() {
        if (raices.estaVacia()) {
            return "No hay comentarios.";
        }
        StringBuilder texto = new StringBuilder();
        for (ComentarioAnidado comentario : raices) {
            describir(comentario, 0, texto);
        }
        return texto.toString();
    }

    private int borrarEn(ListaEnlazada<ComentarioAnidado> lista, int id) {
        for (ComentarioAnidado comentario : lista) {
            if (comentario.getId() == id) {
                int total = 1 + contar(comentario);
                vaciarRespuestas(comentario);
                lista.eliminar(comentario);
                return total;
            }
            int borrados = borrarEn(comentario.getRespuestas(), id);
            if (borrados > 0) {
                return borrados;
            }
        }
        return 0;
    }

    private void vaciarRespuestas(ComentarioAnidado comentario) {
        for (ComentarioAnidado respuesta : comentario.getRespuestas()) {
            vaciarRespuestas(respuesta);
        }
        comentario.getRespuestas().vaciar();
    }

    private int contar(ComentarioAnidado comentario) {
        int total = 0;
        for (ComentarioAnidado respuesta : comentario.getRespuestas()) {
            total += 1 + contar(respuesta);
        }
        return total;
    }

    private ComentarioAnidado buscar(int id) {
        return buscarEn(raices, id);
    }

    private ComentarioAnidado buscarEn(ListaEnlazada<ComentarioAnidado> lista, int id) {
        for (ComentarioAnidado comentario : lista) {
            if (comentario.getId() == id) {
                return comentario;
            }
            ComentarioAnidado hallado = buscarEn(comentario.getRespuestas(), id);
            if (hallado != null) {
                return hallado;
            }
        }
        return null;
    }

    private void describir(ComentarioAnidado comentario, int nivel, StringBuilder texto) {
        for (int i = 0; i < nivel; i++) {
            texto.append("    ");
        }
        texto.append('#').append(comentario.getId()).append(' ')
                .append(comentario.getAutor()).append(": ")
                .append(comentario.getTexto()).append('\n');
        for (ComentarioAnidado respuesta : comentario.getRespuestas()) {
            describir(respuesta, nivel + 1, texto);
        }
    }

    private ComentarioAnidado nuevo(String autor, String texto) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto no puede estar vacío.");
        }
        return new ComentarioAnidado(siguienteId++, autor.trim(), texto.trim());
    }
}
