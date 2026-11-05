package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/** Piezas comunes de las pantallas: mismos colores, bordes y letras grandes. */
final class FormatoPantalla {

    static final int GRUPO = 18;
    static final int TITULO = 34;
    static final int CUERPO = 20;
    static final int ETIQUETA = 18;
    static final int BOTON = 19;

    private FormatoPantalla() {
    }

    static JPanel columna() {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBackground(Paleta.FONDO);
        return columna;
    }

    static void montar(JPanel panel, JComponent columna, Runnable alVolver) {
        panel.setLayout(new BorderLayout());
        panel.setBackground(Paleta.FONDO);
        panel.setBorder(new EmptyBorder(28, 32, 32, 32));

        JButton volver = new JButton("Volver");
        estilo(volver);
        volver.setAlignmentX(Component.LEFT_ALIGNMENT);
        volver.addActionListener(evento -> alVolver.run());

        JScrollPane scroll = new JScrollPane(columna);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Paleta.FONDO);
        sur.setBorder(new EmptyBorder(16, 0, 0, 0));
        sur.add(volver, BorderLayout.WEST);
        panel.add(sur, BorderLayout.SOUTH);
    }

    static JLabel texto(String contenido, int tamano, java.awt.Color color) {
        JLabel etiqueta = new JLabel(contenido);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, tamano));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    static void estilo(JButton boton) {
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, BOTON));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(12, 14, 12, 14)));
        boton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        boton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    }

    static JTextField campo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, ETIQUETA));
        campo.setBackground(Paleta.TARJETA);
        campo.setForeground(Paleta.TEXTO);
        campo.setCaretColor(Paleta.TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(8, 10, 8, 10)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return campo;
    }

    static JTextArea area(int filas, boolean editable) {
        JTextArea area = new JTextArea(filas, 28);
        area.setEditable(editable);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Paleta.TARJETA);
        area.setForeground(editable ? Paleta.TEXTO : Paleta.TEXTO);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, CUERPO));
        area.setBorder(new EmptyBorder(0, 0, 0, 0));
        return area;
    }

    static JCheckBox casilla(String contenido) {
        JCheckBox casilla = new JCheckBox(contenido);
        casilla.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, ETIQUETA));
        casilla.setForeground(Paleta.TEXTO);
        casilla.setBackground(Paleta.FONDO);
        casilla.setFocusPainted(false);
        casilla.setAlignmentX(Component.LEFT_ALIGNMENT);
        return casilla;
    }

    static JPanel caja(JComponent contenido) {
        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(Paleta.TARJETA);
        caja.setBorder(borde());
        caja.setAlignmentX(Component.LEFT_ALIGNMENT);
        caja.add(contenido, BorderLayout.CENTER);
        return caja;
    }

    static JPanel fila() {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(Paleta.FONDO);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        return fila;
    }

    static Border borde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(16, 16, 16, 16));
    }

    static void espacio(JPanel columna, int alto) {
        columna.add(Box.createVerticalStrut(alto));
    }
}
