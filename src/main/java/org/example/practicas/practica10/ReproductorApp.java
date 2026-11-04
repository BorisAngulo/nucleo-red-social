package org.example.practicas.practica10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Menú del reproductor. Cada tecla mueve el bucle o la cola de pedidos. */
public class ReproductorApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ReproductorMusica reproductor = new ReproductorMusica();
        boolean seguir = true;
        while (seguir) {
            mostrarMenu(reproductor);
            switch (leerLinea(entrada).trim()) {
                case "1" -> agregar(reproductor, entrada);
                case "2" -> pedir(reproductor, entrada);
                case "3" -> siguiente(reproductor);
                case "4" -> anterior(reproductor);
                case "5" -> quitar(reproductor);
                case "6" -> reproductor.mostrar();
                case "0" -> seguir = false;
                default -> System.out.println("  Opción no válida.");
            }
        }
        System.out.println("Reproductor cerrado.");
    }

    private static void mostrarMenu(ReproductorMusica reproductor) {
        System.out.println();
        System.out.println("Sonando: " + texto(reproductor.actual()));
        System.out.println("  Pedidos en cola: " + reproductor.getPedidos());
        System.out.println("  1  Agregar al bucle");
        System.out.println("  2  Pedir canción (entra a la cola)");
        System.out.println("  3  Siguiente");
        System.out.println("  4  Anterior");
        System.out.println("  5  Quitar la que suena");
        System.out.println("  6  Mostrar bucle y cola");
        System.out.println("  0  Salir");
        System.out.print("Opción: ");
        System.out.flush();
    }

    private static void agregar(ReproductorMusica reproductor, BufferedReader entrada) {
        Cancion cancion = leerCancion(entrada);
        if (cancion == null) {
            return;
        }
        reproductor.agregarALista(cancion);
        System.out.println("  Entró al bucle.");
        reproductor.mostrar();
    }

    private static void pedir(ReproductorMusica reproductor, BufferedReader entrada) {
        Cancion cancion = leerCancion(entrada);
        if (cancion == null) {
            return;
        }
        reproductor.pedir(cancion);
        System.out.println("  Pedido encolado. Suena cuando pulses Siguiente.");
        reproductor.mostrar();
    }

    private static void siguiente(ReproductorMusica reproductor) {
        boolean desdeCola = !reproductor.colaVacia();
        Cancion cancion = reproductor.siguiente();
        if (cancion == null) {
            System.out.println("  No hay canciones.");
        } else if (desdeCola) {
            System.out.println("  Suena el pedido: " + texto(cancion));
        } else {
            System.out.println("  Sigue el bucle: " + texto(cancion));
        }
        reproductor.mostrar();
    }

    private static void anterior(ReproductorMusica reproductor) {
        Cancion cancion = reproductor.anterior();
        if (cancion == null) {
            System.out.println("  No hay canciones.");
        } else {
            System.out.println("  Atrás -> " + texto(cancion));
        }
        reproductor.mostrar();
    }

    private static void quitar(ReproductorMusica reproductor) {
        Cancion quitada = reproductor.quitarActual();
        if (quitada == null) {
            System.out.println("  No hay canción sonando.");
        } else {
            System.out.println("  Se quitó: " + texto(quitada));
            System.out.println("  Ahora suena: " + texto(reproductor.actual()));
        }
        reproductor.mostrar();
    }

    private static Cancion leerCancion(BufferedReader entrada) {
        System.out.print("  Título: ");
        System.out.flush();
        String titulo = leerLinea(entrada).trim();
        System.out.print("  Artista: ");
        System.out.flush();
        String artista = leerLinea(entrada).trim();
        System.out.print("  Duración en segundos: ");
        System.out.flush();
        String duracion = leerLinea(entrada).trim();
        if (titulo.isEmpty() || artista.isEmpty()) {
            System.out.println("  El título y el artista no pueden estar vacíos.");
            return null;
        }
        try {
            int segundos = Integer.parseInt(duracion);
            if (segundos <= 0) {
                System.out.println("  La duración debe ser mayor que cero.");
                return null;
            }
            return new Cancion(titulo, artista, segundos);
        } catch (NumberFormatException ignorado) {
            System.out.println("  La duración tiene que ser un número de segundos.");
            return null;
        }
    }

    private static String texto(Cancion cancion) {
        return cancion == null ? "ninguna" : cancion.toString();
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
