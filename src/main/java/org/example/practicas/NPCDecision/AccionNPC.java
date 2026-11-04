package org.example.practicas.NPCDecision;

/** Acción que elige el árbol cuando llega a una hoja. */
public enum AccionNPC {

    ATAQUE_CUERPO_A_CUERPO("Ataque cuerpo a cuerpo"),
    DISPARAR_ARMA("Disparar arma"),
    LANZAR_GRANADA("Lanzar granada"),
    CUBRIRSE("Cubrirse y recargar escudos"),
    HUIR("Huir a lo loco");

    private final String texto;

    AccionNPC(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
