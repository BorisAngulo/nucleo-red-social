package org.example.practicas.practica7;

/**
 * Recorrido del gestor: altas al inicio y al final, inserción intermedia y bajas por id.
 */
public class GestorTareasApp {

    public static void main(String[] args) {
        ListaTareas lista = new ListaTareas();

        seccion("Estado inicial");
        System.out.println("  vacía: " + lista.estaVacia() + " | tamaño: " + lista.getTamano());
        lista.mostrarLista();

        seccion("Altas al inicio y al final");
        lista.agregarAlInicio(new Tarea(2, "Revisar correo", "media"));
        lista.agregarAlInicio(new Tarea(1, "Preparar informe", "alta"));
        lista.agregarAlFinal(new Tarea(4, "Enviar acta", "baja"));
        lista.mostrarLista();

        seccion("Inserción en la posición 3");
        boolean insertada = lista.insertarEnPosicion(3, new Tarea(3, "Llamar al cliente", "alta"));
        System.out.println("  insertar en 3: " + (insertada ? "aceptada" : "rechazada"));
        lista.mostrarLista();

        seccion("Eliminar la tarea intermedia 2 y la cabecera 1");
        System.out.println("  eliminar id 2: " + (lista.eliminarPorId(2) ? "eliminada" : "no estaba"));
        lista.mostrarLista();
        System.out.println("  eliminar id 1: " + (lista.eliminarPorId(1) ? "eliminada" : "no estaba"));
        lista.mostrarLista();

        seccion("Posición fuera de rango e id inexistente");
        System.out.println("  insertar en 9: " + (lista.insertarEnPosicion(9, new Tarea(9, "Archivar", "baja"))
                ? "aceptada" : "rechazada"));
        System.out.println("  eliminar id 99: " + (lista.eliminarPorId(99) ? "eliminada" : "no estaba"));
        System.out.println("  tamaño: " + lista.getTamano());
        lista.mostrarLista();
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
