package org.example.redsocial.moderacion;

import org.example.estructuradedatos.ArbolDecision;
import org.example.estructuradedatos.ArbolDecision.Nodo;

/**
 * Árbol de decisión de un reporte. Si la pregunta se cumple baja a la
 * izquierda; si no, a la derecha.
 */
public class ModeracionContenido {

    private final ArbolDecision decisiones;

    public ModeracionContenido() {
        Nodo ocultar = Nodo.hoja("Ocultar la publicación y avisar al autor");
        Nodo eliminar = Nodo.hoja("Eliminar la publicación");
        Nodo revision = Nodo.hoja("Enviar a revisión humana");
        Nodo mantener = Nodo.hoja("Mantener visible");
        Nodo reducir = Nodo.hoja("Reducir el alcance");

        Nodo denuncias = Nodo.pregunta("denuncias", eliminar, revision);
        Nodo confianza = Nodo.pregunta("confianza", mantener, reducir);
        Nodo insulto = Nodo.pregunta("insulto", denuncias, confianza);
        Nodo spam = Nodo.pregunta("spam", ocultar, insulto);
        this.decisiones = new ArbolDecision(spam);
    }

    public String clasificar(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo.");
        }
        return decidir(decisiones.getRaiz(), reporte);
    }

    private String decidir(Nodo nodo, Reporte reporte) {
        if (nodo.esHoja()) {
            return nodo.getResultado();
        }
        if (seCumple(nodo.getPregunta(), reporte)) {
            return decidir(nodo.getIzquierdo(), reporte);
        }
        return decidir(nodo.getDerecho(), reporte);
    }

    private boolean seCumple(String pregunta, Reporte reporte) {
        if (pregunta.equals("spam")) {
            return reporte.isSpam();
        }
        if (pregunta.equals("insulto")) {
            return reporte.isInsulto();
        }
        if (pregunta.equals("denuncias")) {
            return reporte.isVariasDenuncias();
        }
        if (pregunta.equals("confianza")) {
            return reporte.isAutorConfiable();
        }
        throw new IllegalArgumentException("Pregunta desconocida: " + pregunta);
    }
}
