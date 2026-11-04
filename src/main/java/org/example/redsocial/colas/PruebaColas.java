package org.example.redsocial.colas;

public class PruebaColas {

    public static void main(String[] args) {

        System.out.println("=== COLA SIMPLE ===");

        ProcesadorNotificaciones notificaciones =
                new ProcesadorNotificaciones();

        notificaciones.agregar("Ana", "le dio like a tu publicacion");
        notificaciones.agregar("Luis", "te envio una solicitud");
        notificaciones.agregar("Maria", "comento tu foto");

        System.out.println("Notificaciones pendientes:");
        System.out.println(notificaciones.listarPendientes());

        System.out.println("Procesando:");
        System.out.println(notificaciones.procesarSiguiente());

        System.out.println("Quedan: "
                + notificaciones.cantidadPendiente());

        System.out.println("\n=== COLA CIRCULAR ===");

        CarruselHistorias historias = new CarruselHistorias();

        historias.agregar("Ana", "Hola a todos");
        historias.agregar("Luis", "De paseo");
        historias.agregar("Maria", "Nuevo dia");

        System.out.println("Historia actual:");
        System.out.println(historias.verActual());

        System.out.println("Siguiente:");
        System.out.println(historias.siguiente());

        System.out.println("Siguiente:");
        System.out.println(historias.siguiente());

        System.out.println("Siguiente, vuelve al inicio:");
        System.out.println(historias.siguiente());

        System.out.println("Anterior:");
        System.out.println(historias.anterior());

        System.out.println("Total de historias: "
                + historias.cantidadHistorias());
    }
}