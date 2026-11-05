package org.example.redsocial.moderacion;

/** Lo que se sabe de un reporte antes de clasificarlo. */
public class Reporte {

    private final boolean spam;
    private final boolean insulto;
    private final boolean variasDenuncias;
    private final boolean autorConfiable;

    public Reporte(boolean spam, boolean insulto, boolean variasDenuncias, boolean autorConfiable) {
        this.spam = spam;
        this.insulto = insulto;
        this.variasDenuncias = variasDenuncias;
        this.autorConfiable = autorConfiable;
    }

    public boolean isSpam() {
        return spam;
    }

    public boolean isInsulto() {
        return insulto;
    }

    public boolean isVariasDenuncias() {
        return variasDenuncias;
    }

    public boolean isAutorConfiable() {
        return autorConfiable;
    }
}
