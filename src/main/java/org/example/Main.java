package org.example;

import org.example.practicas.LecturaTemperatura;
import org.example.practicas.RegistroTelemetria;

/**
 * Demostración etapa por etapa del registro de telemetría sobre un vector.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("REGISTRO DE TELEMETRÍA DE TEMPERATURAS EN MEMORIA");
        System.out.println("Inserción, eliminación y búsqueda secuencial sobre vectores");
        System.out.println("============================================================");

        etapa(1, "Modelo de una lectura (POO)");
        LecturaTemperatura muestra = new LecturaTemperatura("INV-A", "Invernadero norte", 23.40, "08:00");
        System.out.println("  La clase LecturaTemperatura encapsula sensor, zona, grados e instante.");
        System.out.println("  Lectura creada: " + muestra);
        System.out.println("  El registro no guarda números sueltos: guarda objetos de este tipo.");

        etapa(2, "Vector vacío en memoria");
        RegistroTelemetria registro = new RegistroTelemetria(5);
        System.out.println("  Se reserva un vector de " + registro.getCapacidad() + " casilleros.");
        System.out.println("  La cantidad lógica empieza en 0. Los casilleros libres se muestran como —.");
        registro.mostrar();

        etapa(3, "Inserción al final");
        System.out.println("  Cada alta ocupa la primera posición libre. El orden es el de llegada.");
        registro.insertar(muestra);
        registro.insertar(new LecturaTemperatura("FRI-1", "Cámara fría", 4.10, "08:05"));
        registro.insertar(new LecturaTemperatura("HOR-2", "Horno de secado", 61.80, "08:10"));
        registro.insertar(new LecturaTemperatura("INV-A", "Invernadero norte", 24.10, "08:20"));
        registro.mostrar();
        System.out.println("  INV-A aparece dos veces: el mismo sensor puede tener varias lecturas.");

        etapa(4, "Inserción en una posición (desplazamiento a la derecha)");
        System.out.println("  Entra AGU-3 en la posición 2. Quien estaba ahí, y los siguientes, corren un lugar.");
        registro.insertarEn(2, new LecturaTemperatura("AGU-3", "Tanque de agua", 18.20, "08:07"));
        registro.mostrar();
        System.out.println("  El vector quedó lleno. La siguiente inserción no tiene casillero:");
        boolean cupo = registro.insertar(new LecturaTemperatura("EXT-9", "Exterior", 16.00, "08:25"));
        System.out.println("  resultado de la inserción: " + (cupo ? "aceptada" : "rechazada"));

        etapa(5, "Búsqueda secuencial");
        System.out.println("  Se busca INV-A. Hay dos lecturas; la búsqueda se detiene en la primera.");
        int hallado = registro.buscarSecuencial("INV-A");
        if (hallado >= 0) {
            System.out.println("  resultado: encontrado en la posición " + hallado + " → " + registro.obtener(hallado));
        }
        System.out.println("  Se busca un sensor que no está. Hay que comparar todo el tramo ocupado.");
        int ausente = registro.buscarSecuencial("EXT-9");
        System.out.println("  resultado: " + (ausente >= 0 ? "posición " + ausente : "no encontrado (-1)"));

        etapa(6, "Eliminación y compactación hacia la izquierda");
        System.out.println("  Se elimina FRI-1. Localizarlo también es una búsqueda secuencial.");
        boolean eliminado = registro.eliminarPorSensor("FRI-1");
        System.out.println("  resultado: " + (eliminado ? "eliminado" : "no estaba"));
        registro.mostrar();
        System.out.println("  Se intenta eliminar un sensor ausente:");
        boolean inexistente = registro.eliminarPorSensor("EXT-9");
        System.out.println("  resultado: " + (inexistente ? "eliminado" : "no estaba"));
        registro.mostrar();

        etapa(7, "Vaciar el vector y buscar sobre él");
        while (!registro.estaVacio()) {
            LecturaTemperatura primera = registro.obtener(0);
            System.out.println("  se quita la cabeza del vector: " + primera.getIdSensor());
            registro.eliminarEn(0);
            registro.mostrar();
        }
        System.out.println("  Búsqueda con el registro vacío:");
        int enVacio = registro.buscarSecuencial("INV-A");
        System.out.println("  resultado: " + (enVacio >= 0 ? "posición " + enVacio : "no encontrado (-1)"));
        System.out.println();
        System.out.println("Fin de la demostración. Invariante: ocupados = " + registro.getCantidad()
                + ", capacidad = " + registro.getCapacidad() + ".");
    }

    private static void etapa(int numero, String titulo) {
        System.out.println();
        System.out.println("ETAPA " + numero + " — " + titulo);
    }
}
