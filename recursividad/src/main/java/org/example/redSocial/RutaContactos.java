package org.example.redSocial;

public class RutaContactos {

    // Usuarios de la red social
    static String[] usuarios = {
            "Ana", "Pedro", "Carlos", "Maria"
    };

    // Matriz de amistades
    static int[][] amigos = {
            {0, 1, 0, 0},
            {1, 0, 1, 0},
            {0, 1, 0, 1},
            {0, 0, 1, 0}
    };

    // Usuarios visitados
    static boolean[] visitado = new boolean[4];

    // Metodo recursivo para buscar la ruta
    public static boolean buscarRuta(int actual, int destino) {

        // Marcar usuario como visitado
        visitado[actual] = true;

        // Mostrar usuario actual
        System.out.print(usuarios[actual]);

        // Caso base: encontramos al destino
        if (actual == destino) {
            return true;
        }

        System.out.print(" -> ");

        // Buscar amigos del usuario actual
        for (int i = 0; i < 4; i++) {

            if (amigos[actual][i] == 1 && !visitado[i]) {

                if (buscarRuta(i, destino)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {

        System.out.println("BUSQUEDA DE RUTA DE CONTACTOS");

        boolean encontrado = buscarRuta(0, 3);

        if (encontrado) {
            System.out.println("\nRuta encontrada correctamente.");
        } else {
            System.out.println("\nNo existe una ruta de contactos.");
        }
    }
}
