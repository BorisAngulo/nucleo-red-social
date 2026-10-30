package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/** Pantalla principal. Cada botón abre el lugar de su función, todavía vacía. */
public class VentanaRedSocial extends JFrame {

    private static final String INICIO = "inicio";
    private static final String DETALLE = "detalle";
    private static final String GRADOS = "grados";

    private final CardLayout cartas = new CardLayout();
    private final JPanel raiz = new JPanel(cartas);
    private final JLabel tituloDetalle = etiqueta("", 28, Paleta.TEXTO);
    private final JLabel grupoDetalle = etiqueta("", 14, Paleta.TEXTO_SECUNDARIO);
    private final JLabel resumenDetalle = etiqueta("", 16, Paleta.TEXTO);

    public VentanaRedSocial() {
        super("Núcleo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 720);
        setLocationRelativeTo(null);
        raiz.setBackground(Paleta.FONDO);
        raiz.add(pantallaInicio(), INICIO);
        raiz.add(pantallaDetalle(), DETALLE);
        raiz.add(new PanelGradosSeparacion(() -> cartas.show(raiz, INICIO)), GRADOS);
        setContentPane(raiz);
    }

    private JScrollPane pantallaInicio() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Paleta.FONDO);
        contenido.setBorder(new EmptyBorder(28, 32, 32, 32));

        JLabel titulo = etiqueta("Núcleo de una red social", 32, Paleta.TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitulo = etiqueta("Estructuras de una red social", 16, Paleta.TEXTO_SECUNDARIO);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(subtitulo);
        contenido.add(Box.createVerticalStrut(24));

        String grupoActual = "";
        JPanel rejilla = null;
        for (FuncionRed funcion : FuncionRed.values()) {
            if (!funcion.getGrupo().equals(grupoActual)) {
                grupoActual = funcion.getGrupo();
                contenido.add(tarjeta(grupoActual, rejilla = nuevaRejilla()));
                contenido.add(Box.createVerticalStrut(16));
            }
            rejilla.add(botonFuncion(funcion));
        }
        for (Component hijo : contenido.getComponents()) {
            if (hijo instanceof JPanel tarjeta) {
                Dimension alto = tarjeta.getPreferredSize();
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, alto.height));
            }
        }

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel nuevaRejilla() {
        JPanel rejilla = new JPanel(new GridLayout(0, 2, 12, 12));
        rejilla.setBackground(Paleta.TARJETA);
        return rejilla;
    }

    private JPanel tarjeta(String titulo, JPanel rejilla) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(16, 16, 16, 16)));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel etiqueta = etiqueta(titulo, 18, Paleta.TEXTO);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(etiqueta);
        tarjeta.add(Box.createVerticalStrut(12));
        rejilla.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(rejilla);
        return tarjeta;
    }

    private JButton botonFuncion(FuncionRed funcion) {
        JButton boton = new JButton("<html><div style='text-align:left'>"
                + funcion.getNombre() + "<br><span style='font-size:11px;color:#A3A3A3'>"
                + funcion.getResumen() + "</span></div></html>");
        estiloBoton(boton);
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.addActionListener(evento -> mostrar(funcion));
        return boton;
    }

    private JPanel pantallaDetalle() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Paleta.FONDO);
        panel.setBorder(new EmptyBorder(28, 32, 32, 32));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(Paleta.TARJETA);
        centro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(28, 28, 28, 28)));

        tituloDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupoDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        resumenDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel aviso = etiqueta("Esta función todavía no está implementada.", 16, Paleta.TEXTO_SECUNDARIO);
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(grupoDetalle);
        centro.add(Box.createVerticalStrut(8));
        centro.add(tituloDetalle);
        centro.add(Box.createVerticalStrut(16));
        centro.add(resumenDetalle);
        centro.add(Box.createVerticalStrut(24));
        centro.add(aviso);

        JButton volver = new JButton("Volver");
        estiloBoton(volver);
        volver.addActionListener(evento -> cartas.show(raiz, INICIO));

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Paleta.FONDO);
        sur.setBorder(new EmptyBorder(16, 0, 0, 0));
        sur.add(volver, BorderLayout.WEST);

        panel.add(centro, BorderLayout.NORTH);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private void mostrar(FuncionRed funcion) {
        if (funcion == FuncionRed.GRADOS_DE_SEPARACION) {
            cartas.show(raiz, GRADOS);
            return;
        }
        grupoDetalle.setText(funcion.getGrupo());
        tituloDetalle.setText(funcion.getNombre());
        resumenDetalle.setText(funcion.getResumen());
        cartas.show(raiz, DETALLE);
    }

    private static void estiloBoton(JButton boton) {
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(12, 14, 12, 14)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evento) {
                boton.setBackground(Paleta.BOTON_ACTIVO);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evento) {
                boton.setBackground(Paleta.BOTON);
            }
        });
    }

    private static JLabel etiqueta(String texto, int tamano, java.awt.Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, tamano));
        return etiqueta;
    }
}
