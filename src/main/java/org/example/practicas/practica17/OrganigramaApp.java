package org.example.practicas.practica17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Menú de altas, bajas y organigrama por niveles. */
public class OrganigramaApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        RegistroEmpleados registro = new RegistroEmpleados();
        VisualizadorOrganigrama visualizador = new VisualizadorOrganigrama();
        boolean seguir = true;
        while (seguir) {
            mostrarMenu(registro);
            switch (leerLinea(entrada).trim()) {
                case "1" -> alta(registro, entrada);
                case "2" -> baja(registro, visualizador, entrada);
                case "3" -> visualizador.mostrar(registro.getArbol());
                case "4" -> buscar(registro, entrada);
                case "0" -> seguir = false;
                default -> System.out.println("  Opción no válida.");
            }
        }
        System.out.println("Registro cerrado.");
    }

    private static void mostrarMenu(RegistroEmpleados registro) {
        System.out.println();
        System.out.println("Empresa (" + registro.tamano() + " empleados, ordenados por código)");
        System.out.println("  1  Alta");
        System.out.println("  2  Baja");
        System.out.println("  3  Organigrama por niveles");
        System.out.println("  4  Buscar por código");
        System.out.println("  0  Salir");
        System.out.print("Opción: ");
        System.out.flush();
    }

    private static void alta(RegistroEmpleados registro, BufferedReader entrada) {
        System.out.print("  Código: ");
        System.out.flush();
        String codigoTexto = leerLinea(entrada).trim();
        System.out.print("  Nombre: ");
        System.out.flush();
        String nombre = leerLinea(entrada).trim();
        System.out.print("  Cargo: ");
        System.out.flush();
        String cargo = leerLinea(entrada).trim();
        try {
            int codigo = Integer.parseInt(codigoTexto);
            Empleado empleado = new Empleado(codigo, nombre, cargo);
            if (registro.alta(empleado)) {
                System.out.println("  Alta registrada: " + empleado);
            } else {
                System.out.println("  Ese código ya está en el árbol.");
            }
        } catch (NumberFormatException error) {
            System.out.println("  El código tiene que ser un número entero.");
        } catch (IllegalArgumentException error) {
            System.out.println("  " + error.getMessage());
        }
    }

    private static void baja(RegistroEmpleados registro, VisualizadorOrganigrama visualizador,
            BufferedReader entrada) {
        System.out.print("  Código a dar de baja: ");
        System.out.flush();
        try {
            int codigo = Integer.parseInt(leerLinea(entrada).trim());
            Empleado empleado = registro.baja(codigo);
            if (empleado == null) {
                System.out.println("  No hay un empleado con ese código.");
            } else {
                System.out.println("  Baja registrada: " + empleado);
                visualizador.mostrar(registro.getArbol());
            }
        } catch (NumberFormatException error) {
            System.out.println("  El código tiene que ser un número entero.");
        }
    }

    private static void buscar(RegistroEmpleados registro, BufferedReader entrada) {
        System.out.print("  Código: ");
        System.out.flush();
        try {
            int codigo = Integer.parseInt(leerLinea(entrada).trim());
            Empleado empleado = registro.buscar(codigo);
            if (empleado == null) {
                System.out.println("  No está en el registro.");
            } else {
                System.out.println("  " + empleado);
            }
        } catch (NumberFormatException error) {
            System.out.println("  El código tiene que ser un número entero.");
        }
    }

    private static String leerLinea(BufferedReader entrada) {
        try {
            String linea = entrada.readLine();
            if (linea == null) {
                throw new IllegalStateException("Se cerró la entrada.");
            }
            descartarEnterDuplicado(entrada);
            return linea;
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo leer la consola.", error);
        }
    }

    private static void descartarEnterDuplicado(BufferedReader entrada) throws IOException {
        try {
            Thread.sleep(50);
        } catch (InterruptedException interrumpido) {
            Thread.currentThread().interrupt();
        }
        if (!entrada.ready()) {
            return;
        }
        entrada.mark(2);
        int extra = entrada.read();
        if (extra != '\n' && extra != '\r') {
            entrada.reset();
            return;
        }
        if (!entrada.ready()) {
            return;
        }
        entrada.mark(1);
        int par = entrada.read();
        boolean esPar = (extra == '\r' && par == '\n') || (extra == '\n' && par == '\r');
        if (!esPar) {
            entrada.reset();
        }
    }
}
