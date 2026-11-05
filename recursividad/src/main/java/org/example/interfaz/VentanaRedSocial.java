
package org.example.interfaz;

import javax.swing.*;
import java.awt.*;

public class VentanaRedSocial extends JFrame {

    // Administrador de pantallas
    private final CardLayout cartas;
    private final JPanel raiz;

    // Identificadores de las pantallas
    private static final String INICIO = "inicio";
    private static final String GRADOS = "grados";
    private static final String RUTAS = "rutas";
    private static final String COMENTARIOS = "comentarios";

    public VentanaRedSocial() {

        setTitle("RED SOCIAL - ALGORITMOS RECURSIVOS");
        setSize(850, 650);
        setMinimumSize(new Dimension(750, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cartas = new CardLayout();
        raiz = new JPanel(cartas);
        raiz.setBackground(Paleta.FONDO);

        // Crear las pantallas
        raiz.add(crearInicio(), INICIO);

        raiz.add(
                new PanelGradosSeparacion(
                        () -> cartas.show(raiz, INICIO)
                ),
                GRADOS
        );

        raiz.add(
                new PanelRutaAmigos(
                        () -> cartas.show(raiz, INICIO)
                ),
                RUTAS
        );

        raiz.add(
                new PanelComentarios(
                        () -> cartas.show(raiz, INICIO)
                ),
                COMENTARIOS
        );
        add(raiz);

        cartas.show(raiz, INICIO);
    }

    // =====================================
    // PANTALLA PRINCIPAL
    // =====================================

    private JPanel crearInicio() {

        JPanel panel = new JPanel(new BorderLayout(20, 25));
        panel.setBackground(Paleta.FONDO);

        panel.setBorder(
                BorderFactory.createEmptyBorder(35, 45, 30, 45)
        );

        // ENCABEZADO
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(Paleta.FONDO);

        JLabel titulo = new JLabel("RED SOCIAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Paleta.TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "CENTRO DE ALGORITMOS RECURSIVOS"
        );

        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(Paleta.TEXTO_SECUNDARIO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(subtitulo);

        // TARJETAS DE FUNCIONES
        JPanel tarjetas = new JPanel(new GridLayout(3, 1, 15, 15));
        tarjetas.setBackground(Paleta.FONDO);

        for (FuncionesRed funcion : FuncionesRed.values()) {

            tarjetas.add(crearTarjeta(funcion));
        }

        // PIE DE PAGINA
        JLabel pie = new JLabel(
                "PROYECTO DE ESTRUCTURA DE DATOS",
                JLabel.CENTER
        );

        pie.setForeground(Paleta.TEXTO_SECUNDARIO);
        pie.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(tarjetas, BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);

        return panel;
    }

    // =====================================
    // CREAR TARJETAS
    // =====================================

    private JPanel crearTarjeta(FuncionesRed funcion) {

        JPanel tarjeta = new JPanel(new BorderLayout(15, 8));

        tarjeta.setBackground(Paleta.TARJETA);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Paleta.BORDE),
                        BorderFactory.createEmptyBorder(18, 20, 18, 20)
                )
        );

        // NOMBRE
        JLabel nombre = new JLabel(funcion.getNombre());

        nombre.setForeground(Paleta.TEXTO);
        nombre.setFont(new Font("Arial", Font.BOLD, 19));

        // DESCRIPCION
        JLabel descripcion = new JLabel(funcion.getDescripcion());

        descripcion.setForeground(Paleta.TEXTO_SECUNDARIO);
        descripcion.setFont(new Font("Arial", Font.PLAIN, 13));

        // GRUPO
        JLabel grupo = new JLabel(funcion.getGrupo());

        grupo.setForeground(Paleta.ACENTO);
        grupo.setFont(new Font("Arial", Font.BOLD, 11));

        JPanel informacion = new JPanel();
        informacion.setLayout(new BoxLayout(informacion, BoxLayout.Y_AXIS));
        informacion.setBackground(Paleta.TARJETA);

        informacion.add(nombre);
        informacion.add(Box.createVerticalStrut(6));
        informacion.add(descripcion);
        informacion.add(Box.createVerticalStrut(8));
        informacion.add(grupo);

        // BOTON
        JButton boton = new JButton("ABRIR  >");

        boton.setForeground(Paleta.TEXTO);
        boton.setBackground(Paleta.BOTON);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(Paleta.BOTON_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(Paleta.BOTON);
            }
        });

        boton.addActionListener(e -> mostrar(funcion));

        tarjeta.add(informacion, BorderLayout.CENTER);
        tarjeta.add(boton, BorderLayout.EAST);

        return tarjeta;
    }

    // =====================================
    // NAVEGACION ENTRE PANTALLAS
    // =====================================

    public void mostrar(FuncionesRed funcion) {

        if (funcion == FuncionesRed.GRADOS_DE_SEPARACION) {

            cartas.show(raiz, GRADOS);

        } else if (funcion == FuncionesRed.RUTA_DE_AMIGOS) {

            cartas.show(raiz, RUTAS);

        } else if (funcion == FuncionesRed.ELIMINAR_COMENTARIOS) {

            cartas.show(raiz, COMENTARIOS);
        }
    }
}
