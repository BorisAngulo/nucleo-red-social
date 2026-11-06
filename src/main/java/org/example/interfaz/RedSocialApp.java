package org.example.interfaz;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Abre la pantalla de funciones de la red social. */
public class RedSocialApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception error) {
            throw new IllegalStateException("No se pudo preparar la interfaz.", error);
        }
        SwingUtilities.invokeLater(() -> new VentanaRedSocial().setVisible(true));
    }
}
