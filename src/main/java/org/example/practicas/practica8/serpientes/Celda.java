package org.example.practicas.practica8.serpientes;

/** Casilla del tablero. El efecto 0 no mueve, 1 es escalera y 2 es serpiente. */
public class Celda {

    public static final int SIN_EFECTO = 0;
    public static final int ESCALERA = 1;
    public static final int SERPIENTE = 2;

    private final int numero;
    private int efecto;
    private int numeroEfecto;

    public Celda(int numero) {
        if (numero < 1) {
            throw new IllegalArgumentException("El número de celda debe ser mayor que cero.");
        }
        this.numero = numero;
        this.efecto = SIN_EFECTO;
        this.numeroEfecto = 0;
    }

    public void colocarEfecto(int efecto, int numeroEfecto) {
        if (efecto < SIN_EFECTO || efecto > SERPIENTE) {
            throw new IllegalArgumentException("El efecto debe ser 0, 1 o 2.");
        }
        if (efecto == SIN_EFECTO) {
            this.efecto = SIN_EFECTO;
            this.numeroEfecto = 0;
            return;
        }
        if (numeroEfecto < 1 || numeroEfecto == numero) {
            throw new IllegalArgumentException("El destino del efecto no es válido.");
        }
        this.efecto = efecto;
        this.numeroEfecto = numeroEfecto;
    }

    public int getNumero() {
        return numero;
    }

    public int getEfecto() {
        return efecto;
    }

    public int getNumeroEfecto() {
        return numeroEfecto;
    }

    public boolean tieneEfecto() {
        return efecto != SIN_EFECTO;
    }
}
