package org.example.redsocial.historial;

import java.util.ArrayList;
import java.util.List;
import org.example.estructuradedatos.PilaEnlazada;

/**
 * Servicio encargado de gestionar el historial de navegación y búsquedas
 * utilizando dos pilas (Atrás y Adelante) basadas en {@link PilaEnlazada}.
 */
public class HistorialNavegacionService {

    private final PilaEnlazada<EntradaHistorial> pilaAtras = new PilaEnlazada<>();
    private final PilaEnlazada<EntradaHistorial> pilaAdelante = new PilaEnlazada<>();

    public HistorialNavegacionService() {
        cargarHistorialInicial();
    }

    /**
     * Visita un nuevo destino (URL, perfil o búsqueda).
     * Apila en el historial y vacía la pila de avance (Adelante).
     */
    public void visitar(String destino, String tipo) {
        if (destino == null || destino.trim().isEmpty()) {
            return;
        }
        EntradaHistorial nueva = new EntradaHistorial(destino, tipo);
        pilaAtras.apilar(nueva);
        pilaAdelante.vaciar();
    }

    public void visitar(String destino) {
        visitar(destino, "Página");
    }

    public void buscar(String termino) {
        visitar(termino, "Búsqueda");
    }

    public void visitarPerfil(String usuario) {
        visitar(usuario, "Perfil");
    }

    /**
     * Retrocede a la página anterior en el historial (Botón Atrás).
     * @return La nueva página actual o null si no se puede retroceder.
     */
    public EntradaHistorial retroceder() {
        if (!puedeRetroceder()) {
            return null;
        }
        EntradaHistorial actual = pilaAtras.desapilar();
        pilaAdelante.apilar(actual);
        return pilaAtras.cima();
    }

    /**
     * Avanza a la página siguiente si se había retrocedido previamente (Botón Adelante).
     * @return La página a la que se avanzó o null si no hay avance.
     */
    public EntradaHistorial avanzar() {
        if (!puedeAvanzar()) {
            return null;
        }
        EntradaHistorial siguiente = pilaAdelante.desapilar();
        pilaAtras.apilar(siguiente);
        return siguiente;
    }

    public EntradaHistorial paginaActual() {
        return pilaAtras.estaVacia() ? null : pilaAtras.cima();
    }

    public boolean puedeRetroceder() {
        return pilaAtras.tamano() > 1;
    }

    public boolean puedeAvanzar() {
        return !pilaAdelante.estaVacia();
    }

    public int totalEnHistorial() {
        return pilaAtras.tamano();
    }

    public int totalEnAdelante() {
        return pilaAdelante.tamano();
    }

    public boolean estaVacio() {
        return pilaAtras.estaVacia();
    }

    public void vaciarHistorial() {
        pilaAtras.vaciar();
        pilaAdelante.vaciar();
    }

    /**
     * Retorna los elementos de la pila Atrás desde la cima (más reciente) al fondo.
     */
    public List<EntradaHistorial> obtenerListaAtras() {
        List<EntradaHistorial> lista = new ArrayList<>();
        for (EntradaHistorial e : pilaAtras) {
            lista.add(e);
        }
        return lista;
    }

    /**
     * Retorna los elementos de la pila Adelante desde la cima al fondo.
     */
    public List<EntradaHistorial> obtenerListaAdelante() {
        List<EntradaHistorial> lista = new ArrayList<>();
        for (EntradaHistorial e : pilaAdelante) {
            lista.add(e);
        }
        return lista;
    }

    private void cargarHistorialInicial() {
        pilaAtras.apilar(new EntradaHistorial("redsocial.com/inicio", "Página"));
        pilaAtras.apilar(new EntradaHistorial("#tendencias_tecnologia", "Tendencia"));
        pilaAtras.apilar(new EntradaHistorial("boris_angulo", "Perfil"));
        pilaAtras.apilar(new EntradaHistorial("Estructuras de Datos Pilas", "Búsqueda"));
        pilaAtras.apilar(new EntradaHistorial("adrian_martinez", "Perfil"));
    }
}

