package org.example.redsocial.notificaciones;

import org.example.estructuradedatos.Cola;
import org.example.estructuradedatos.ColaEnlazada;

/** Cola de avisos: el primero que llegó es el primero que se atiende. */
public class BandejaAvisos {

    private final Cola<String> avisos = new ColaEnlazada<>();

    public BandejaAvisos() {
        recibir("Ana te empezó a seguir");
        recibir("Luis comentó tu foto");
        recibir("Mia reaccionó a tu historia");
    }

    public void recibir(String aviso) {
        if (aviso == null || aviso.isBlank()) {
            throw new IllegalArgumentException("El aviso no puede estar vacío.");
        }
        avisos.encolar(aviso.trim());
    }

    public String siguiente() {
        if (avisos.estaVacia()) {
            return null;
        }
        return avisos.frente();
    }

    public String atender() {
        if (avisos.estaVacia()) {
            return null;
        }
        return avisos.desencolar();
    }

    public int tamano() {
        return avisos.tamano();
    }

    public String listar() {
        if (avisos.estaVacia()) {
            return "No hay notificaciones pendientes.";
        }
        StringBuilder texto = new StringBuilder();
        texto.append("La primera de la lista es la que se atiende ahora.\n");
        for (String aviso : avisos) {
            texto.append("• ").append(aviso).append('\n');
        }
        return texto.toString();
    }
}
