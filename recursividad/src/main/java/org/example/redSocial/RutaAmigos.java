
package org.example.redSocial;

import org.example.estructuraDeDatos.ListaEnlazada;

public class RutaAmigos {

    // Busca la ruta entre dos usuarios.
    public ListaEnlazada<UsuarioRed> buscarRuta(
            UsuarioRed origen,
            UsuarioRed destino) {

        ListaEnlazada<UsuarioRed> ruta = new ListaEnlazada<>();
        ListaEnlazada<UsuarioRed> visitados = new ListaEnlazada<>();

        if (origen == null || destino == null) {
            return ruta;
        }

        buscarRecursivo(origen, destino, visitados, ruta);

        return ruta;
    }

    // Algoritmo recursivo DFS.
    private boolean buscarRecursivo(
            UsuarioRed actual,
            UsuarioRed destino,
            ListaEnlazada<UsuarioRed> visitados,
            ListaEnlazada<UsuarioRed> ruta) {

        // Evitar visitar nuevamente al mismo usuario.
        if (visitados.contiene(actual)) {
            return false;
        }

        // Registrar usuario visitado.
        visitados.insertarAlFinal(actual);

        // Agregar usuario a la ruta actual.
        ruta.insertarAlFinal(actual);

        // Caso base: encontramos al destino.
        if (actual.equals(destino)) {
            return true;
        }

        // Explorar recursivamente sus amigos.
        for (UsuarioRed amigo : actual.getAmigos()) {

            if (buscarRecursivo(
                    amigo,
                    destino,
                    visitados,
                    ruta)) {

                return true;
            }
        }

        // Retroceder si no encontramos la ruta.
        ruta.eliminar(actual);

        return false;
    }

    // Calcula los grados de separación según la ruta.
    public int calcularGrados(
            UsuarioRed origen,
            UsuarioRed destino) {

        ListaEnlazada<UsuarioRed> ruta =
                buscarRuta(origen, destino);

        if (ruta.estaVacia()) {
            return -1;
        }

        return ruta.tamano() - 1;
    }

    // Convierte la ruta en texto para mostrarla.
    public String mostrarRuta(
            UsuarioRed origen,
            UsuarioRed destino) {

        ListaEnlazada<UsuarioRed> ruta =
                buscarRuta(origen, destino);

        if (ruta.estaVacia()) {
            return "No existe una ruta de amistad.";
        }

        StringBuilder resultado = new StringBuilder();

        for (UsuarioRed usuario : ruta) {

            if (resultado.length() > 0) {
                resultado.append(" -> ");
            }

            resultado.append(usuario.getNombre());
        }

        return resultado.toString();
    }
}
