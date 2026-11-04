package org.example.interfaz;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.example.redsocial.perfiles.PanelBuscarPerfiles;

/** Ventana independiente, dimensionada para presentar cómodamente la lista de perfiles. */
public final class VentanaBuscarPerfiles extends JFrame {

    public VentanaBuscarPerfiles() {
        super("Núcleo | Búsqueda de Perfiles");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(new PanelBuscarPerfiles(this::dispose));
        setMinimumSize(new Dimension(940, 650));
        setSize(1120, 760);
        setLocationRelativeTo(null);
        setResizable(true);
        if (!GraphicsEnvironment.isHeadless()) {
            setLocationRelativeTo(null);
        }
    }
}
