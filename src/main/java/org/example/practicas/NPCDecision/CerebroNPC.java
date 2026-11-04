package org.example.practicas.NPCDecision;

import org.example.estructuradedatos.ArbolDecision;

/**
 * Reglas del NPC. La salud abre el árbol: con más de 30 % ataca,
 * con 30 % o menos busca cobertura o huye.
 */
public class CerebroNPC {

    private final ArbolDecision<EstadoNPC, AccionNPC> decisiones;

    public CerebroNPC() {
        ArbolDecision<EstadoNPC, AccionNPC> cuerpoACuerpo = ArbolDecision.hoja(AccionNPC.ATAQUE_CUERPO_A_CUERPO);
        ArbolDecision<EstadoNPC, AccionNPC> disparar = ArbolDecision.hoja(AccionNPC.DISPARAR_ARMA);
        ArbolDecision<EstadoNPC, AccionNPC> granada = ArbolDecision.hoja(AccionNPC.LANZAR_GRANADA);
        ArbolDecision<EstadoNPC, AccionNPC> cubrirse = ArbolDecision.hoja(AccionNPC.CUBRIRSE);
        ArbolDecision<EstadoNPC, AccionNPC> huir = ArbolDecision.hoja(AccionNPC.HUIR);

        ArbolDecision<EstadoNPC, AccionNPC> municion = ArbolDecision.pregunta(
                estado -> estado.isMunicionDisponible(),
                disparar,
                granada);
        ArbolDecision<EstadoNPC, AccionNPC> distancia = ArbolDecision.pregunta(
                estado -> estado.getDistanciaAlJugador() < 5,
                cuerpoACuerpo,
                municion);
        ArbolDecision<EstadoNPC, AccionNPC> cobertura = ArbolDecision.pregunta(
                estado -> estado.isTieneCoberturaCerca(),
                cubrirse,
                huir);
        this.decisiones = ArbolDecision.pregunta(
                estado -> estado.getPorcentajeSalud() > 30,
                distancia,
                cobertura);
    }

    public AccionNPC decidir(EstadoNPC estado) {
        return decisiones.decidir(estado);
    }
}
