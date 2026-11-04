package org.example.practicas.practica3;

/**
 * Auditoría del historial: vacío, navegación, Atrás, desbordamiento y subdesbordamiento.
 */
public class AppNavegador {

    public static void main(String[] args) {
        PilaHistorial historial = new PilaHistorial(4);

        seccion("Estado inicial");
        historial.mostrarHistorial();
        System.out.println("  peek: " + historial.peek());

        seccion("Tres navegaciones");
        navegar(historial, "google.com");
        navegar(historial, "github.com");
        navegar(historial, "stackoverflow.com");
        historial.mostrarHistorial();

        seccion("Botón Atrás");
        String retirada = historial.pop();
        System.out.println("  se desapiló: " + retirada);
        historial.mostrarHistorial();

        seccion("Stack Overflow");
        navegar(historial, "oracle.com");
        navegar(historial, "mozilla.org");
        System.out.println("  llena: " + historial.estaLlena());
        navegar(historial, "wikipedia.org");
        historial.mostrarHistorial();

        seccion("Stack Underflow");
        while (!historial.estaVacia()) {
            System.out.println("  se desapiló: " + historial.pop());
        }
        System.out.println("  vacía: " + historial.estaVacia());
        System.out.println("  pop extra: " + historial.pop());
    }

    private static void navegar(PilaHistorial historial, String url) {
        boolean aceptada = historial.push(url);
        System.out.println("  " + url + ": " + (aceptada ? "registrada" : "rechazada"));
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
