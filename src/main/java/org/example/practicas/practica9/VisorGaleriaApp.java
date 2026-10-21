package org.example.practicas.practica9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Menú del visor. Cada opción mueve la lista y muestra la galería. */
public class VisorGaleriaApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        VisorGaleria visor = new VisorGaleria();
        boolean seguir = true;
        while (seguir) {
            mostrarMenu(visor);
            switch (leerLinea(entrada).trim()) {
                case "1" -> agregarAlFinal(visor, entrada);
                case "2" -> insertarDespues(visor, entrada);
                case "3" -> avanzar(visor);
                case "4" -> retroceder(visor);
                case "5" -> quitar(visor);
                case "6" -> visor.mostrar();
                case "0" -> seguir = false;
                default -> System.out.println("  Opción no válida.");
            }
        }
        System.out.println("Visor cerrado.");
    }

    private static void mostrarMenu(VisorGaleria visor) {
        System.out.println();
        System.out.println("Viendo: " + texto(visor.actual()));
        System.out.println("  1  Agregar al final");
        System.out.println("  2  Insertar después de la foto en pantalla");
        System.out.println("  3  Adelante");
        System.out.println("  4  Atrás");
        System.out.println("  5  Quitar la foto en pantalla");
        System.out.println("  6  Mostrar galería");
        System.out.println("  0  Salir");
        System.out.print("Opción: ");
        System.out.flush();
    }

    private static void agregarAlFinal(VisorGaleria visor, BufferedReader entrada) {
        Foto foto = leerFoto(entrada);
        if (foto == null) {
            return;
        }
        visor.agregar(foto);
        System.out.println("  Agregada al final.");
        visor.mostrar();
    }

    private static void insertarDespues(VisorGaleria visor, BufferedReader entrada) {
        Foto foto = leerFoto(entrada);
        if (foto == null) {
            return;
        }
        visor.insertarDespues(foto);
        System.out.println("  Insertada después de la que se veía. En pantalla sigue: " + texto(visor.actual()));
        visor.mostrar();
    }

    private static void avanzar(VisorGaleria visor) {
        Foto foto = visor.avanzar();
        if (foto == null) {
            System.out.println("  No hay foto siguiente. Se queda en: " + texto(visor.actual()));
        } else {
            System.out.println("  Adelante -> " + texto(foto));
        }
        visor.mostrar();
    }

    private static void retroceder(VisorGaleria visor) {
        Foto foto = visor.retroceder();
        if (foto == null) {
            System.out.println("  No hay foto anterior. Se queda en: " + texto(visor.actual()));
        } else {
            System.out.println("  Atrás -> " + texto(foto));
        }
        visor.mostrar();
    }

    private static void quitar(VisorGaleria visor) {
        Foto quitada = visor.eliminarActual();
        if (quitada == null) {
            System.out.println("  No hay foto en pantalla.");
            visor.mostrar();
            return;
        }
        System.out.println("  Se quitó: " + texto(quitada));
        System.out.println("  Ahora se ve: " + texto(visor.actual()));
        visor.mostrar();
    }

    private static Foto leerFoto(BufferedReader entrada) {
        System.out.print("  Nombre: ");
        System.out.flush();
        String nombre = leerLinea(entrada).trim();
        System.out.print("  Descripción: ");
        System.out.flush();
        String descripcion = leerLinea(entrada).trim();
        if (nombre.isEmpty() || descripcion.isEmpty()) {
            System.out.println("  El nombre y la descripción no pueden estar vacíos.");
            return null;
        }
        return new Foto(nombre, descripcion);
    }

    private static String texto(Foto foto) {
        return foto == null ? "ninguna" : foto.toString();
    }

    /**
     * Lee una línea y descarta el salto extra que algunas consolas agregan al mismo Enter.
     */
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
