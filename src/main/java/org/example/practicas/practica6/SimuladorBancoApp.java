package org.example.practicas.practica6;

/**
 * Ventanilla con anillo de 4 lugares: reposo, atención, vuelta modular y saturación.
 */
public class SimuladorBancoApp {

    public static void main(String[] args) {
        ColaCircularBanco banco = new ColaCircularBanco(4);

        seccion("Estado inicial");
        System.out.println("  vacía: " + banco.estaVacia() + " | llena: " + banco.estaLlena() + " | size: " + banco.size());
        banco.mostrarCola();
        System.out.println("  peek: " + banco.peek());

        seccion("Llegan cuatro clientes");
        emitir(banco, new Cliente(1, "Ana", "depósito", 1500));
        emitir(banco, new Cliente(2, "Luis", "retiro", 400));
        emitir(banco, new Cliente(3, "Marta", "pago", 250));
        emitir(banco, new Cliente(4, "Pedro", "consulta", 0));
        banco.mostrarCola();
        System.out.println("  peek: " + banco.peek());

        seccion("La ventanilla atiende a los dos primeros");
        banco.desencolar();
        banco.desencolar();
        banco.mostrarCola();

        seccion("El fin da la vuelta y reutiliza el inicio");
        emitir(banco, new Cliente(5, "Sofía", "depósito", 800));
        emitir(banco, new Cliente(6, "Diego", "retiro", 120));
        banco.mostrarCola();

        seccion("Saturación real");
        System.out.println("  llena: " + banco.estaLlena() + " | size: " + banco.size());
        emitir(banco, new Cliente(7, "Elena", "pago", 90));

        seccion("Se atiende hasta vaciar el anillo");
        while (!banco.estaVacia()) {
            banco.desencolar();
        }
        System.out.println("  vacía: " + banco.estaVacia() + " | size: " + banco.size());
        banco.mostrarCola();
        System.out.println("  desencolar extra: " + banco.desencolar());
    }

    private static void emitir(ColaCircularBanco banco, Cliente cliente) {
        boolean aceptado = banco.encolar(cliente);
        System.out.println("  turno " + cliente.getTicket() + " " + cliente.getNombre()
                + ": " + (aceptado ? "emitido" : "rechazado"));
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
