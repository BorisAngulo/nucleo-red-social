
package org.example;

import org.example.interfaz.VentanaRedSocial;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaRedSocial ventana = new VentanaRedSocial();

            ventana.setVisible(true);

        });
    }
}
