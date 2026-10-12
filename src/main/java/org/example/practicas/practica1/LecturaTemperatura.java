package org.example.practicas.practica1;

import java.util.Locale;

/**
 * Una lectura de telemetría. El objeto guarda sus datos y el registro
 * solo decide dónde vive dentro del vector.
 */
public class LecturaTemperatura {

    private final String idSensor;
    private final String zona;
    private final double celsius;
    private final String instante;

    public LecturaTemperatura(String idSensor, String zona, double celsius, String instante) {
        if (idSensor == null || idSensor.isBlank()) {
            throw new IllegalArgumentException("El sensor debe tener un identificador.");
        }
        if (zona == null || zona.isBlank()) {
            throw new IllegalArgumentException("La zona no puede estar vacía.");
        }
        if (instante == null || instante.isBlank()) {
            throw new IllegalArgumentException("El instante no puede estar vacío.");
        }
        this.idSensor = idSensor.trim();
        this.zona = zona.trim();
        this.celsius = celsius;
        this.instante = instante.trim();
    }

    public String getIdSensor() {
        return idSensor;
    }

    public String getZona() {
        return zona;
    }

    public double getCelsius() {
        return celsius;
    }

    public String getInstante() {
        return instante;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s | %s | %.2f °C | %s", idSensor, zona, celsius, instante);
    }
}
