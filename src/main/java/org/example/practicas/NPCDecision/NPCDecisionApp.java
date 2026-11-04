package org.example.practicas.NPCDecision;

/** Recorre las hojas del árbol con un estado distinto en cada caso. */
public class NPCDecisionApp {

    public static void main(String[] args) {
        CerebroNPC cerebro = new CerebroNPC();
        probar(cerebro, new EstadoNPC(80, 3, false, true));
        probar(cerebro, new EstadoNPC(80, 5, false, true));
        probar(cerebro, new EstadoNPC(80, 12, true, false));
        probar(cerebro, new EstadoNPC(30, 2, true, true));
        probar(cerebro, new EstadoNPC(10, 20, false, false));
    }

    private static void probar(CerebroNPC cerebro, EstadoNPC estado) {
        System.out.println(estado);
        System.out.println("  -> " + cerebro.decidir(estado));
    }
}
