package org.example.practicas.segundoParcial;

/** Un movimiento del banco: venta, casa o multa. */
public class Asiento {

    public static final String VENTA = "Propiedad vendida";
    public static final String CASA = "Casa construida";
    public static final String MULTA = "Multa cobrada";

    private final String tipo;
    private final String detalle;

    public Asiento(String tipo, String detalle) {
        if (tipo == null || tipo.isBlank() || detalle == null || detalle.isBlank()) {
            throw new IllegalArgumentException("El asiento necesita tipo y detalle.");
        }
        this.tipo = tipo;
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return tipo + " | " + detalle;
    }
}
