package org.example.redsocial.historias;

import org.example.estructuradedatos.ListaCircular;
import org.example.estructuradedatos.ListaEnlazada;

/** Lista circular de historias. Siguiente vuelve a la primera al terminar la vuelta. */
public class CarruselHistorias {

    private final ListaCircular<Historia> historias = new ListaCircular<>();

    public CarruselHistorias() {
        publicar("Ana", "En la playa", 5);
        publicar("Luis", "Café de la esquina", 2);
        publicar("Mia", "Entrando al concierto", 24);
    }

    public void publicar(String autor, String texto, int horas) {
        historias.insertarAlFinal(new Historia(autor, texto, horas));
    }

    public Historia actual() {
        if (historias.estaVacia()) {
            return null;
        }
        return historias.obtener(0);
    }

    public Historia siguiente() {
        if (historias.estaVacia()) {
            return null;
        }
        historias.rotar();
        return actual();
    }

    public Historia quitarActual() {
        if (historias.estaVacia()) {
            return null;
        }
        return historias.eliminarAlInicio();
    }

    /** Resta una hora a cada historia y saca las que llegan a cero. */
    public ListaEnlazada<Historia> pasarHora() {
        int cantidad = historias.tamano();
        for (int i = 0; i < cantidad; i++) {
            historias.obtener(0).restarHora();
            historias.rotar();
        }
        ListaEnlazada<Historia> vencidas = new ListaEnlazada<>();
        for (Historia historia : historias) {
            if (historia.getHoras() <= 0) {
                vencidas.insertarAlFinal(historia);
            }
        }
        for (Historia historia : vencidas) {
            historias.eliminar(historia);
        }
        return vencidas;
    }

    public int tamano() {
        return historias.tamano();
    }

    /** La que está en pantalla primero, y después el resto de la vuelta. */
    public ListaEnlazada<Historia> enOrden() {
        ListaEnlazada<Historia> copia = new ListaEnlazada<>();
        for (Historia historia : historias) {
            copia.insertarAlFinal(historia);
        }
        return copia;
    }

    public String listar() {
        if (historias.estaVacia()) {
            return "No hay historias activas.";
        }
        StringBuilder texto = new StringBuilder();
        texto.append("En pantalla: ").append(actual()).append("\n\n");
        texto.append("Vuelta del carrusel:\n");
        for (Historia historia : historias) {
            texto.append("• ").append(historia).append('\n');
        }
        return texto.toString();
    }
}
