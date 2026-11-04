package org.example.practicas.juegoRPG;

/** Un objeto del inventario. Cada item aplica un solo efecto. */
public class Item {

    public static final int CURAR = 1;
    public static final int AUMENTAR_ATAQUE = 2;
    public static final int DANO = 3;

    private final String nombre;
    private final int efecto;
    private final int magnitud;

    public Item(String nombre, int efecto, int magnitud) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del item no puede estar vacío.");
        }
        if (efecto != CURAR && efecto != AUMENTAR_ATAQUE && efecto != DANO) {
            throw new IllegalArgumentException("El efecto del item no es válido.");
        }
        if (magnitud <= 0) {
            throw new IllegalArgumentException("La magnitud del item debe ser mayor que cero.");
        }
        this.nombre = nombre.trim();
        this.efecto = efecto;
        this.magnitud = magnitud;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEfecto() {
        return efecto;
    }

    public int getMagnitud() {
        return magnitud;
    }

    public String describir() {
        String accion = switch (efecto) {
            case CURAR -> "cura " + magnitud + " de vida";
            case AUMENTAR_ATAQUE -> "sube el ataque en " + magnitud;
            default -> "hace " + magnitud + " de daño";
        };
        return nombre + " (" + accion + ")";
    }

    @Override
    public String toString() {
        return describir();
    }
}
