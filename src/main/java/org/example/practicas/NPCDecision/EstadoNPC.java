package org.example.practicas.NPCDecision;

/** Lo que el NPC percibe en un frame. */
public class EstadoNPC {

    private final int porcentajeSalud;
    private final double distanciaAlJugador;
    private final boolean tieneCoberturaCerca;
    private final boolean municionDisponible;

    public EstadoNPC(int porcentajeSalud, double distanciaAlJugador,
            boolean tieneCoberturaCerca, boolean municionDisponible) {
        if (porcentajeSalud < 0 || porcentajeSalud > 100) {
            throw new IllegalArgumentException("La salud debe estar entre 0 y 100.");
        }
        if (distanciaAlJugador < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        this.porcentajeSalud = porcentajeSalud;
        this.distanciaAlJugador = distanciaAlJugador;
        this.tieneCoberturaCerca = tieneCoberturaCerca;
        this.municionDisponible = municionDisponible;
    }

    public int getPorcentajeSalud() {
        return porcentajeSalud;
    }

    public double getDistanciaAlJugador() {
        return distanciaAlJugador;
    }

    public boolean isTieneCoberturaCerca() {
        return tieneCoberturaCerca;
    }

    public boolean isMunicionDisponible() {
        return municionDisponible;
    }

    @Override
    public String toString() {
        return "salud " + porcentajeSalud + "% | distancia " + distanciaAlJugador
                + " m | cobertura " + (tieneCoberturaCerca ? "sí" : "no")
                + " | munición " + (municionDisponible ? "sí" : "no");
    }
}
