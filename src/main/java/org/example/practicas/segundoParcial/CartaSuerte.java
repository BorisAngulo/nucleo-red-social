package org.example.practicas.segundoParcial;

/** Carta del mazo. Después de leerla vuelve al fondo. */
public class CartaSuerte {

    public static final int RETROCEDER = 1;
    public static final int AVANZAR = 2;
    public static final int MULTA = 3;
    public static final int COBRAR = 4;

    private final String texto;
    private final int efecto;
    private final int magnitud;

    public CartaSuerte(String texto, int efecto, int magnitud) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("La carta necesita un texto.");
        }
        if (efecto < RETROCEDER || efecto > COBRAR) {
            throw new IllegalArgumentException("El efecto de la carta no es válido.");
        }
        if (magnitud <= 0) {
            throw new IllegalArgumentException("La magnitud de la carta debe ser mayor que cero.");
        }
        this.texto = texto;
        this.efecto = efecto;
        this.magnitud = magnitud;
    }

    public String getTexto() {
        return texto;
    }

    public int getEfecto() {
        return efecto;
    }

    public int getMagnitud() {
        return magnitud;
    }
}
