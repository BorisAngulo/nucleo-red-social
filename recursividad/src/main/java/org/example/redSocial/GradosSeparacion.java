
package org.example.redSocial;

import org.example.estructuraDeDatos.ListaEnlazada;

public class GradosSeparacion {

    // =====================================
    // RESULTADO DEL ALGORITMO
    // =====================================

    public static class Resultado {

        private final int grados;
        private final ListaEnlazada<UsuarioRed> ruta;

        public Resultado(int grados, ListaEnlazada<UsuarioRed> ruta) {
            this.grados = grados;
            this.ruta = ruta;
        }

        public int getGrados() {
            return grados;
        }

        public ListaEnlazada<UsuarioRed> getRuta() {
            return ruta;
        }

        public boolean existeConexion() {
            return grados != -1;
        }
    }

    // =====================================
    // METODO PRINCIPAL
    // =====================================

    public static Resultado calcular(
            UsuarioRed inicio,
            UsuarioRed destino) {

        if (inicio == null || destino == null) {
            throw new IllegalArgumentException(
                    "Los usuarios no pueden ser nulos."
            );
        }

        ListaEnlazada<UsuarioRed> visitados =
                new ListaEnlazada<>();

        ListaEnlazada<UsuarioRed> rutaActual =
                new ListaEnlazada<>();

        ListaEnlazada<UsuarioRed> mejorRuta =
                new ListaEnlazada<>();

        int[] mejorGrados = {Integer.MAX_VALUE};

        buscarRecursivamente(
                inicio,
                destino,
                visitados,
                rutaActual,
                mejorRuta,
                mejorGrados
        );

        if (mejorGrados[0] == Integer.MAX_VALUE) {
            return new Resultado(-1, new ListaEnlazada<>());
        }

        return new Resultado(mejorGrados[0], mejorRuta);
    }

    // =====================================
    // ALGORITMO RECURSIVO
    // =====================================

    private static void buscarRecursivamente(
            UsuarioRed actual,
            UsuarioRed destino,
            ListaEnlazada<UsuarioRed> visitados,
            ListaEnlazada<UsuarioRed> rutaActual,
            ListaEnlazada<UsuarioRed> mejorRuta,
            int[] mejorGrados) {

        // Marcar usuario como visitado
        visitados.insertarAlFinal(actual);
        rutaActual.insertarAlFinal(actual);

        // Verificar si llegamos al destino
        if (actual.equals(destino)) {

            int gradosActuales = rutaActual.tamano() - 1;

            // Guardar solamente la ruta más corta
            if (gradosActuales < mejorGrados[0]) {

                mejorGrados[0] = gradosActuales;

                // Limpiar la ruta anterior
                while (!mejorRuta.estaVacia()) {
                    mejorRuta.eliminar(mejorRuta.obtener(0));
                }

                // Copiar la nueva ruta
                for (UsuarioRed usuario : rutaActual) {
                    mejorRuta.insertarAlFinal(usuario);
                }
            }

        } else {

            // Evitar explorar rutas que ya no pueden
            // ser más cortas que la mejor encontrada
            if (rutaActual.tamano() - 1 < mejorGrados[0]) {

                // Recorrer los amigos del usuario actual
                for (UsuarioRed amigo : actual.getAmigos()) {

                    // Evitar visitar usuarios repetidos
                    if (!visitados.contiene(amigo)) {

                        buscarRecursivamente(
                                amigo,
                                destino,
                                visitados,
                                rutaActual,
                                mejorRuta,
                                mejorGrados
                        );
                    }
                }
            }
        }

        // Retroceder: eliminar el usuario de la ruta
        rutaActual.eliminar(actual);
        visitados.eliminar(actual);
    }
}
