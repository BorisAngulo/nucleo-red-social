package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Grados de separación por recursividad.
 * Cada llamada prueba un amigo que todavía no está en el camino
 * y se queda con la cadena más corta que no pase de {@link #LIMITE} saltos.
 */
public class GradosSeparacion {

    public static final int LIMITE = 6;

    public ResultadoSeparacion calcular(Usuario origen, Usuario destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Hay que elegir a las dos personas.");
        }
        ResultadoSeparacion mejor = new ResultadoSeparacion();
        ListaEnlazada<Usuario> camino = new ListaEnlazada<>();
        camino.insertarAlFinal(origen);
        if (origen == destino) {
            mejor.guardarSiMejor(camino);
            return mejor;
        }
        explorar(origen, destino, camino, mejor);
        return mejor;
    }

    private void explorar(Usuario actual, Usuario destino, ListaEnlazada<Usuario> camino, ResultadoSeparacion mejor) {
        if (mejor.encontrado() && camino.tamano() - 1 >= mejor.getGrados()) {
            return;
        }
        for (Usuario amigo : actual.getAmigos()) {
            if (camino.contiene(amigo)) {
                continue;
            }
            camino.insertarAlFinal(amigo);
            if (amigo == destino) {
                mejor.guardarSiMejor(copiar(camino));
            } else if (camino.tamano() - 1 < LIMITE) {
                explorar(amigo, destino, camino, mejor);
            }
            camino.eliminarAlFinal();
        }
    }

    private ListaEnlazada<Usuario> copiar(ListaEnlazada<Usuario> origen) {
        ListaEnlazada<Usuario> copia = new ListaEnlazada<>();
        for (Usuario usuario : origen) {
            copia.insertarAlFinal(usuario);
        }
        return copia;
    }
}
