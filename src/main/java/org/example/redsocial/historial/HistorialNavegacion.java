package org.example.redsocial.historial;

import org.example.estructuradedatos.Pila;
import org.example.estructuradedatos.PilaEnlazada;

/** Pila de páginas visitadas. Atrás desapila la última. */
public class HistorialNavegacion {

    private final Pila<String> paginas = new PilaEnlazada<>();

    public HistorialNavegacion() {
        visitar("Inicio");
        visitar("Perfil de Ana");
        visitar("Buscar: música");
    }

    public void visitar(String pagina) {
        if (pagina == null || pagina.isBlank()) {
            throw new IllegalArgumentException("La página no puede estar vacía.");
        }
        paginas.apilar(pagina.trim());
    }

    public String actual() {
        if (paginas.estaVacia()) {
            return null;
        }
        return paginas.cima();
    }

    /** Quita la página actual y deja a la vista la anterior. */
    public String atras() {
        if (paginas.estaVacia()) {
            return null;
        }
        paginas.desapilar();
        return actual();
    }

    public int tamano() {
        return paginas.tamano();
    }

    public String listar() {
        if (paginas.estaVacia()) {
            return "El historial está vacío.";
        }
        StringBuilder texto = new StringBuilder();
        texto.append("Ahora: ").append(actual()).append("\n\n");
        texto.append("De la más reciente a la más antigua:\n");
        for (String pagina : paginas) {
            texto.append("• ").append(pagina).append('\n');
        }
        return texto.toString();
    }
}
