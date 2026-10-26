package org.example.practicas.segundoParcial;

/** Una casilla del anillo: salida, propiedad, suerte o impuesto. */
public class Casilla {

    public static final int SALIDA = 0;
    public static final int PROPIEDAD = 1;
    public static final int SUERTE = 2;
    public static final int IMPUESTO = 3;

    public static final int COSTO_CASA = 70;
    public static final int RENTA_POR_CASA = 15;
    public static final int MAXIMO_CASAS = 3;

    private final int numero;
    private final String nombre;
    private final int tipo;
    private final int precio;
    private final int rentaBase;
    private final int impuesto;
    private Jugador dueno;
    private int casas;

    private Casilla(int numero, String nombre, int tipo, int precio, int rentaBase, int impuesto) {
        this.numero = numero;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.rentaBase = rentaBase;
        this.impuesto = impuesto;
    }

    public static Casilla salida(int numero) {
        return new Casilla(numero, "Salida", SALIDA, 0, 0, 0);
    }

    public static Casilla propiedad(int numero, String nombre, int precio, int rentaBase) {
        return new Casilla(numero, nombre, PROPIEDAD, precio, rentaBase, 0);
    }

    public static Casilla suerte(int numero) {
        return new Casilla(numero, "Suerte", SUERTE, 0, 0, 0);
    }

    public static Casilla impuesto(int numero, String nombre, int impuesto) {
        return new Casilla(numero, nombre, IMPUESTO, 0, 0, impuesto);
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public int getPrecio() {
        return precio;
    }

    public int getImpuesto() {
        return impuesto;
    }

    public Jugador getDueno() {
        return dueno;
    }

    public int getCasas() {
        return casas;
    }

    public int rentaActual() {
        return rentaBase + casas * RENTA_POR_CASA;
    }

    public boolean tieneDueno() {
        return dueno != null;
    }

    public void asignarDueno(Jugador dueno) {
        this.dueno = dueno;
    }

    public boolean puedeConstruir() {
        return casas < MAXIMO_CASAS;
    }

    public void construirCasa() {
        casas++;
    }

    @Override
    public String toString() {
        if (tipo == PROPIEDAD) {
            String amo = dueno == null ? "en venta $" + precio : dueno.getNickname();
            return numero + " " + nombre + " (" + amo + ", renta $" + rentaActual() + ", casas " + casas + ")";
        }
        if (tipo == IMPUESTO) {
            return numero + " " + nombre + " (multa $" + impuesto + ")";
        }
        return numero + " " + nombre;
    }
}
