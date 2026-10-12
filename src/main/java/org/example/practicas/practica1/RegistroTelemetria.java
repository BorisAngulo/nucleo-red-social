package org.example.practicas.practica1;

/**
 * Registro de temperaturas guardado en un vector de tamaño fijo.
 * Las posiciones 0 .. cantidad-1 están ocupadas. El resto queda libre.
 * No se usan listas: insertar, buscar y eliminar recorren el arreglo.
 */
public class RegistroTelemetria {

    private final LecturaTemperatura[] vector;
    private int cantidad;
    private boolean trazar;

    public RegistroTelemetria(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad del vector debe ser mayor que cero.");
        }
        this.vector = new LecturaTemperatura[capacidad];
        this.cantidad = 0;
        this.trazar = true;
    }

    public void setTrazar(boolean trazar) {
        this.trazar = trazar;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCapacidad() {
        return vector.length;
    }

    public boolean estaVacio() {
        return cantidad == 0;
    }

    public boolean estaLleno() {
        return cantidad == vector.length;
    }

    /**
     * Inserción al final del tramo ocupado. Es O(1): no recorre el vector.
     */
    public boolean insertar(LecturaTemperatura lectura) {
        validarLectura(lectura);
        if (estaLleno()) {
            if (trazar) {
                System.out.println("  rechazo: el vector está lleno (" + cantidad + "/" + vector.length + ")");
            }
            return false;
        }
        vector[cantidad] = lectura;
        if (trazar) {
            System.out.println("  se escribe en la posición " + cantidad + ": " + lectura);
        }
        cantidad++;
        return true;
    }

    /**
     * Inserción en una posición válida (0 .. cantidad).
     * Los elementos de esa posición hacia la derecha se corren un lugar.
     */
    public boolean insertarEn(int posicion, LecturaTemperatura lectura) {
        validarLectura(lectura);
        if (posicion < 0 || posicion > cantidad) {
            if (trazar) {
                System.out.println("  rechazo: la posición " + posicion + " no existe en el tramo ocupado");
            }
            return false;
        }
        if (estaLleno()) {
            if (trazar) {
                System.out.println("  rechazo: no hay casillero libre para abrir un hueco");
            }
            return false;
        }
        for (int i = cantidad; i > posicion; i--) {
            if (trazar) {
                System.out.println("  desplazamiento: posición " + i + " ← posición " + (i - 1)
                        + " (" + vector[i - 1].getIdSensor() + ")");
            }
            vector[i] = vector[i - 1];
        }
        vector[posicion] = lectura;
        cantidad++;
        if (trazar) {
            System.out.println("  se coloca en la posición " + posicion + ": " + lectura);
        }
        return true;
    }

    /**
     * Búsqueda secuencial por identificador de sensor.
     * Compara desde la posición 0 y se detiene en la primera coincidencia.
     * Devuelve el índice, o -1 si no está.
     */
    public int buscarSecuencial(String idSensor) {
        if (idSensor == null || idSensor.isBlank()) {
            return -1;
        }
        String buscado = idSensor.trim();
        if (cantidad == 0 && trazar) {
            System.out.println("  el vector no tiene lecturas: no hay comparaciones");
            return -1;
        }
        for (int i = 0; i < cantidad; i++) {
            boolean coincide = vector[i].getIdSensor().equals(buscado);
            if (trazar) {
                System.out.println("  paso " + (i + 1) + " | posición " + i + " | "
                        + vector[i].getIdSensor() + " | " + (coincide ? "coincide" : "no coincide"));
            }
            if (coincide) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Elimina la primera lectura del sensor. Primero la localiza con
     * búsqueda secuencial y después compacta el vector hacia la izquierda.
     */
    public boolean eliminarPorSensor(String idSensor) {
        if (trazar) {
            System.out.println("  fase 1 — localizar con búsqueda secuencial");
        }
        int posicion = buscarSecuencial(idSensor);
        if (posicion < 0) {
            if (trazar) {
                System.out.println("  fase 2 — no hay compactación: el sensor no está en el vector");
            }
            return false;
        }
        if (trazar) {
            System.out.println("  fase 2 — compactar desde la posición " + posicion);
        }
        return eliminarEn(posicion);
    }

    /**
     * Elimina el elemento de una posición ocupada y cierra el hueco.
     */
    public boolean eliminarEn(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            if (trazar) {
                System.out.println("  rechazo: la posición " + posicion + " no está ocupada");
            }
            return false;
        }
        LecturaTemperatura retirada = vector[posicion];
        for (int i = posicion; i < cantidad - 1; i++) {
            if (trazar) {
                System.out.println("  compactación: posición " + i + " ← posición " + (i + 1)
                        + " (" + vector[i + 1].getIdSensor() + ")");
            }
            vector[i] = vector[i + 1];
        }
        vector[cantidad - 1] = null;
        if (trazar) {
            System.out.println("  se libera la posición " + (cantidad - 1) + " (se quitó " + retirada.getIdSensor() + ")");
        }
        cantidad--;
        return true;
    }

    public LecturaTemperatura obtener(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            throw new IndexOutOfBoundsException("Posición fuera del tramo ocupado: " + posicion);
        }
        return vector[posicion];
    }

    public void mostrar() {
        System.out.println("  ocupados " + cantidad + " / capacidad " + vector.length);
        for (int i = 0; i < vector.length; i++) {
            if (i < cantidad) {
                System.out.println("  [" + i + "] " + vector[i]);
            } else {
                System.out.println("  [" + i + "] —");
            }
        }
    }

    private static void validarLectura(LecturaTemperatura lectura) {
        if (lectura == null) {
            throw new IllegalArgumentException("La lectura no puede ser nula.");
        }
    }
}
