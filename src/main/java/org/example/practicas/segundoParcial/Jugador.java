package org.example.practicas.segundoParcial;

import org.example.estructuradedatos.ListaDobleCircular;

/** Participante. Su ficha es una marca sobre el anillo de casillas. */
public class Jugador {

    public static final int DINERO_INICIAL = 400;

    private final String nickname;
    private int dinero;
    private ListaDobleCircular<Casilla>.Posicion ficha;

    public Jugador(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("El nickname no puede estar vacío.");
        }
        this.nickname = nickname.trim();
        this.dinero = DINERO_INICIAL;
    }

    public String getNickname() {
        return nickname;
    }

    public int getDinero() {
        return dinero;
    }

    public boolean tieneDinero() {
        return dinero > 0;
    }

    public void fijarFicha(ListaDobleCircular<Casilla>.Posicion ficha) {
        if (ficha == null) {
            throw new IllegalArgumentException("La ficha no puede ser nula.");
        }
        this.ficha = ficha;
    }

    public Casilla getCasilla() {
        return ficha.ver();
    }

    public Casilla avanzar() {
        return ficha.avanzar();
    }

    public Casilla retroceder() {
        return ficha.retroceder();
    }

    public void cobrar(int cantidad) {
        dinero += cantidad;
    }

    /**
     * Entrega hasta {@code cantidad}. Si no alcanza, suelta todo y queda en cero.
     * Devuelve lo que realmente pagó.
     */
    public int pagar(int cantidad) {
        int pago = Math.min(dinero, cantidad);
        dinero -= pago;
        return pago;
    }

    public boolean puedePagar(int cantidad) {
        return dinero > cantidad;
    }

    @Override
    public String toString() {
        Casilla casilla = ficha == null ? null : ficha.ver();
        String lugar = casilla == null ? "sin ficha" : casilla.getNumero() + " " + casilla.getNombre();
        return nickname + " | $" + dinero + " | " + lugar;
    }
}
