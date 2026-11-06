package org.example.practicas.juegoRPG;

/** Combatiente. Cada uno guarda su vida, su ataque y su inventario. */
public class Jugador {

    static final int VIDA_INICIAL = 30;
    static final int VIDA_MAXIMA = 40;
    static final int ATAQUE_INICIAL = 6;

    private final String nickname;
    private int puntosVida;
    private int ataque;
    private final Inventario inventario;

    public Jugador(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("El nickname no puede estar vacío.");
        }
        this.nickname = nickname.trim();
        this.puntosVida = VIDA_INICIAL;
        this.ataque = ATAQUE_INICIAL;
        this.inventario = new Inventario();
    }

    public String getNickname() {
        return nickname;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getAtaque() {
        return ataque;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public boolean sigueEnPie() {
        return puntosVida > 0;
    }

    public void recibirDano(int cantidad) {
        puntosVida -= cantidad;
        if (puntosVida < 0) {
            puntosVida = 0;
        }
    }

    public void curar(int cantidad) {
        puntosVida += cantidad;
        if (puntosVida > VIDA_MAXIMA) {
            puntosVida = VIDA_MAXIMA;
        }
    }

    public void aumentarAtaque(int cantidad) {
        ataque += cantidad;
    }

    @Override
    public String toString() {
        return nickname + " | vida " + puntosVida + " | ataque " + ataque + " | items " + inventario.tamano();
    }
}
