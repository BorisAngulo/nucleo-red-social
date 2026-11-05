package org.example.interfaz;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.example.redsocial.moderacion.ModeracionContenido;
import org.example.redsocial.moderacion.Reporte;

/** Pantalla del botón Moderación de contenido. */
public class PanelModeracion extends JPanel {

    private final ModeracionContenido moderacion = new ModeracionContenido();
    private final JCheckBox spam = FormatoPantalla.casilla("Es spam");
    private final JCheckBox insulto = FormatoPantalla.casilla("Tiene un insulto");
    private final JCheckBox denuncias = FormatoPantalla.casilla("Llegó denunciado varias veces");
    private final JCheckBox confianza = FormatoPantalla.casilla("El autor es confiable");
    private final JTextArea vista = FormatoPantalla.area(8, false);

    public PanelModeracion(Runnable alVolver) {
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Árboles", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Moderación de contenido", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Cada casilla es una pregunta del árbol. El sí baja a la izquierda y el no a la derecha.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(spam);
        FormatoPantalla.espacio(columna, 8);
        columna.add(insulto);
        FormatoPantalla.espacio(columna, 8);
        columna.add(denuncias);
        FormatoPantalla.espacio(columna, 8);
        columna.add(confianza);
        FormatoPantalla.espacio(columna, 16);
        JButton clasificar = new JButton("Clasificar");
        FormatoPantalla.estilo(clasificar);
        clasificar.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        clasificar.addActionListener(evento -> clasificar());
        columna.add(clasificar);
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        vista.setText("Marca lo que aplique y pulsa Clasificar.");
    }

    private void clasificar() {
        Reporte reporte = new Reporte(
                spam.isSelected(),
                insulto.isSelected(),
                denuncias.isSelected(),
                confianza.isSelected());
        vista.setText("Decisión:\n" + moderacion.clasificar(reporte));
    }
}
