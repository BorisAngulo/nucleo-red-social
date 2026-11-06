package org.example.practicas.practica17;

import org.example.estructuradedatos.ArbolBinario;

/**
 * Altas y bajas de la empresa. Los empleados viven en un árbol binario
 * ordenado por código: el menor queda a la izquierda y el mayor a la derecha.
 */
public class RegistroEmpleados {

    private final ArbolBinario<Empleado> arbol = new ArbolBinario<>();

    public RegistroEmpleados() {
        alta(new Empleado(40, "Elena Soto", "Directora general"));
        alta(new Empleado(20, "Luis Peña", "Gerente de operaciones"));
        alta(new Empleado(60, "Marta Díaz", "Gerente comercial"));
        alta(new Empleado(10, "Ana Ruiz", "Analista"));
        alta(new Empleado(30, "Pedro Gil", "Supervisor de planta"));
        alta(new Empleado(50, "Nora Paz", "Ejecutiva de cuentas"));
        alta(new Empleado(70, "Iván Cruz", "Contador"));
    }

    public boolean alta(Empleado empleado) {
        return arbol.insertar(empleado);
    }

    /** Quita al empleado con ese código. Devuelve null si no estaba. */
    public Empleado baja(int codigo) {
        Empleado empleado = buscar(codigo);
        if (empleado == null) {
            return null;
        }
        arbol.eliminar(empleado);
        return empleado;
    }

    public Empleado buscar(int codigo) {
        if (codigo <= 0) {
            return null;
        }
        return arbol.buscar(new Empleado(codigo, "consulta", "consulta"));
    }

    public ArbolBinario<Empleado> getArbol() {
        return arbol;
    }

    public int tamano() {
        return arbol.tamano();
    }
}
