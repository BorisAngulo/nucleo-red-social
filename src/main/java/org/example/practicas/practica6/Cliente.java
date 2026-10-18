package org.example.practicas.practica6;

import java.util.Locale;

/** Un turno del banco: ticket, cliente, transacción y monto. */
public class Cliente {

    private final int ticket;
    private final String nombre;
    private final String tipoTransaccion;
    private final double monto;

    public Cliente(int ticket, String nombre, String tipoTransaccion, double monto) {
        if (ticket <= 0) {
            throw new IllegalArgumentException("El ticket debe ser mayor que cero.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (tipoTransaccion == null || tipoTransaccion.isBlank()) {
            throw new IllegalArgumentException("La transacción no puede estar vacía.");
        }
        if (!Double.isFinite(monto) || monto < 0) {
            throw new IllegalArgumentException("El monto debe ser un número mayor o igual que cero.");
        }
        this.ticket = ticket;
        this.nombre = nombre.trim();
        this.tipoTransaccion = tipoTransaccion.trim();
        this.monto = monto;
    }

    public int getTicket() {
        return ticket;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Turno %d | %s | %s | $%.2f", ticket, nombre, tipoTransaccion, monto);
    }
}
