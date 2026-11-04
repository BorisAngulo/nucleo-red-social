package org.example.practicas.NPCDecision;

import org.example.estructuradedatos.ArbolDecision;
import org.example.estructuradedatos.ArbolDecision.Nodo;

/**
 * Reglas del NPC. La salud abre el árbol: con más de 30 % ataca,
 * con 30 % o menos busca cobertura o huye.
 */
public class CerebroNPC {

    private final ArbolDecision decisiones;

    public CerebroNPC() {
        Nodo cuerpoACuerpo = Nodo.hoja("Ataque cuerpo a cuerpo");
        Nodo disparar = Nodo.hoja("Disparar arma");
        Nodo granada = Nodo.hoja("Lanzar granada");
        Nodo cubrirse = Nodo.hoja("Cubrirse y recargar escudos");
        Nodo huir = Nodo.hoja("Huir a lo loco");

        Nodo municion = Nodo.pregunta("municion", disparar, granada);
        Nodo distancia = Nodo.pregunta("distancia", cuerpoACuerpo, municion);
        Nodo cobertura = Nodo.pregunta("cobertura", cubrirse, huir);
        Nodo salud = Nodo.pregunta("salud", distancia, cobertura);
        this.decisiones = new ArbolDecision(salud);
    }

    public String decidir(EstadoNPC estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        return decidir(decisiones.getRaiz(), estado);
    }

    private String decidir(Nodo nodo, EstadoNPC estado) {
        if (nodo.esHoja()) {
            return nodo.getResultado();
        }
        if (seCumple(nodo.getPregunta(), estado)) {
            return decidir(nodo.getIzquierdo(), estado);
        }
        return decidir(nodo.getDerecho(), estado);
    }

    private boolean seCumple(String pregunta, EstadoNPC estado) {
        if (pregunta.equals("salud")) {
            return estado.getPorcentajeSalud() > 30;
        }
        if (pregunta.equals("distancia")) {
            return estado.getDistanciaAlJugador() < 5;
        }
        if (pregunta.equals("municion")) {
            return estado.isMunicionDisponible();
        }
        if (pregunta.equals("cobertura")) {
            return estado.isTieneCoberturaCerca();
        }
        throw new IllegalArgumentException("Pregunta desconocida: " + pregunta);
    }
}
