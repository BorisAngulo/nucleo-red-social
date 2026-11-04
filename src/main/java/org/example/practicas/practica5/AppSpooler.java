package org.example.practicas.practica5;

/**
 * Auditoría del spooler: vacío, FIFO, overflow lineal y vaciado total.
 */
public class AppSpooler {

    public static void main(String[] args) {
        ColaImpresion cola = new ColaImpresion(4);

        seccion("Estado inicial");
        System.out.println("  vacía: " + cola.estaVacia() + " | llena: " + cola.estaLlena() + " | size: " + cola.size());
        cola.mostrarCola();
        System.out.println("  peek: " + cola.peek());

        seccion("Encolado de documentos");
        encolar(cola, "Informe.pdf");
        encolar(cola, "Tarea.docx");
        encolar(cola, "Foto.png");
        cola.mostrarCola();
        System.out.println("  peek: " + cola.peek());

        seccion("Impresión FIFO");
        cola.remove();
        cola.mostrarCola();

        seccion("Overflow aunque el frente ya avanzó");
        encolar(cola, "Contrato.pdf");
        System.out.println("  llena: " + cola.estaLlena() + " | size: " + cola.size());
        encolar(cola, "Notas.txt");
        cola.mostrarCola();

        seccion("Vaciado total");
        while (!cola.estaVacia()) {
            cola.remove();
        }
        System.out.println("  vacía: " + cola.estaVacia() + " | size: " + cola.size());
        cola.mostrarCola();
        System.out.println("  remove extra: " + cola.remove());
    }

    private static void encolar(ColaImpresion cola, String documento) {
        boolean aceptado = cola.insert(documento);
        System.out.println("  " + documento + ": " + (aceptado ? "en cola" : "rechazado"));
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
