package org.example.practicas.segundoParcial;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Historial contable en una lista simple.
 * Cada asiento nuevo entra al inicio, así el único visible es el último agregado.
 */
public class RegistroBanco {

    private final ListaEnlazada<Asiento> asientos;

    public RegistroBanco() {
        this.asientos = new ListaEnlazada<>();
    }

    public void anotar(Asiento asiento) {
        if (asiento == null) {
            throw new IllegalArgumentException("El asiento no puede ser nulo.");
        }
        asientos.insertarAlInicio(asiento);
    }

    public void mostrarUltimo() {
        if (asientos.estaVacia()) {
            System.out.println("[ El banco todavía no tiene registros ]");
            return;
        }
        System.out.println("Último registro del banco: " + asientos.obtener(0));
        System.out.println("  Asientos guardados: " + asientos.tamano()
                + ". Los anteriores quedan enlazados y no se listan.");
    }
}
