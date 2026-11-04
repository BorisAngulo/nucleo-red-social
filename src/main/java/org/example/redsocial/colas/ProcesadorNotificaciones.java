package org.example.redsocial.colas;

import org.example.estructuradedatos.ColaEnlazada;

public class ProcesadorNotificaciones {

    private final ColaEnlazada<Notificacion> cola;

    public ProcesadorNotificaciones() {
        cola = new ColaEnlazada<>();
    }

    public void agregar(Notificacion notificacion) {
        cola.encolar(notificacion);
    }

    public void agregar(String usuario, String mensaje) {
        cola.encolar(new Notificacion(usuario, mensaje));
    }

    public Notificacion procesarSiguiente() {
        if (cola.estaVacia()) {
            return null;
        }

        return cola.desencolar();
    }

    public Notificacion verSiguiente() {
        if (cola.estaVacia()) {
            return null;
        }

        return cola.frente();
    }

    public boolean estaVacia() {
        return cola.estaVacia();
    }

    public int cantidadPendiente() {
        return cola.tamano();
    }

    public void limpiar() {
        cola.vaciar();
    }

    public String listarPendientes() {
        if (cola.estaVacia()) {
            return "No hay notificaciones pendientes.";
        }

        StringBuilder texto = new StringBuilder();
        int numero = 1;

        for (Notificacion notificacion : cola) {
            texto.append(numero)
                 .append(". ")
                 .append(notificacion)
                 .append("\n");
            numero++;
        }

        return texto.toString();
    }
}