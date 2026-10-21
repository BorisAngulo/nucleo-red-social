package org.example.practicas.practica8.serpientes;

/** Participante. estado es true mientras sigue en la partida. */
public class Jugador {

    private final String nickname;
    private int celdaPosicion;
    private boolean estado;

    public Jugador(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("El nickname no puede estar vacío.");
        }
        this.nickname = nickname.trim();
        this.celdaPosicion = 0;
        this.estado = true;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCeldaPosicion() {
        return celdaPosicion;
    }

    public void setCeldaPosicion(int celdaPosicion) {
        this.celdaPosicion = celdaPosicion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String situacion = estado ? "en juego" : "terminó";
        return nickname + " | celda " + celdaPosicion + " | " + situacion;
    }
}
