
package org.example.interfaz;

import org.example.redsocial.amigos.CarruselAmigos;
import org.example.redsocial.amigos.Perfil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class PanelCarruselAmigos extends JPanel {

    private final CarruselAmigos carrusel;
    private final Runnable volver;

    private JPanel filaPerfiles;
    private JScrollPane scroll;

    private JButton btnSiguiente;
    private JButton btnAnterior;
    private JButton btnActualizar;

    private JTextField campoBuscar;
    private JLabel lblCantidad;

    private boolean animando = false;

    private static final int ANCHO_TARJETA = 245;
    private static final int ESPACIO_TARJETAS = 20;

    private static final int DESPLAZAMIENTO =
            ANCHO_TARJETA + ESPACIO_TARJETAS;

    // =====================================
    // COLORES
    // =====================================

    private static final Color FONDO = Paleta.FONDO;
    private static final Color TARJETA = Paleta.TARJETA;
    private static final Color AZUL = new Color(100, 180, 255);
    private static final Color ROSADO = new Color(255, 105, 180);
    private static final Color TEXTO = Paleta.TEXTO;
    private static final Color SECUNDARIO = Paleta.TEXTO_SECUNDARIO;
    private static final Color ROJO = new Color(170, 65, 75);

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public PanelCarruselAmigos(Runnable volver) {

        this.volver = volver;
        this.carrusel = new CarruselAmigos();

        setLayout(new BorderLayout(15, 20));
        setBackground(FONDO);

        setBorder(new EmptyBorder(25, 30, 25, 30));

        crearEncabezado();
        crearCarrusel();
        crearNavegacion();

        cargarPerfiles();
    }

    // =====================================
    // ENCABEZADO
    // =====================================

    private void crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout(15, 10));
        encabezado.setBackground(FONDO);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(FONDO);

        JLabel titulo = new JLabel("AMIGOS SUGERIDOS");

        titulo.setFont(new Font("Arial", Font.BOLD, 27));
        titulo.setForeground(TEXTO);

        JLabel subtitulo = new JLabel(
                "Descubre personas y conecta con nuevos amigos"
        );

        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(SECUNDARIO);

        lblCantidad = new JLabel("Perfiles disponibles: 0");
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblCantidad.setForeground(AZUL);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(7));
        textos.add(subtitulo);
        textos.add(Box.createVerticalStrut(7));
        textos.add(lblCantidad);

        // PANEL DE ACCIONES

        JPanel acciones = new JPanel();
        acciones.setLayout(new BoxLayout(acciones, BoxLayout.Y_AXIS));
        acciones.setBackground(FONDO);

        campoBuscar = new JTextField(17);
        campoBuscar.setFont(new Font("Arial", Font.PLAIN, 13));
        campoBuscar.setForeground(TEXTO);
        campoBuscar.setBackground(TARJETA);
        campoBuscar.setCaretColor(TEXTO);
        campoBuscar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Paleta.BORDE),
                        new EmptyBorder(8, 10, 8, 10)
                )
        );

        campoBuscar.setMaximumSize(new Dimension(230, 38));

        campoBuscar.setToolTipText("Buscar por nombre o usuario");

        campoBuscar.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        cargarPerfiles();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        cargarPerfiles();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        cargarPerfiles();
                    }
                });

        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 5, 8)
        );

        botones.setBackground(FONDO);

        btnActualizar = crearBoton(" Actualizar");

        btnActualizar.addActionListener(e -> {

            if (animando) {
                return;
            }

            campoBuscar.setText("");

            carrusel.actualizarSugerencias();

            cargarPerfiles();
        });

        JButton btnVolver = crearBoton("Volver");

        btnVolver.addActionListener(e -> {

            if (this.volver != null) {
                this.volver.run();
            }
        });

        botones.add(btnActualizar);
        botones.add(btnVolver);

        acciones.add(campoBuscar);
        acciones.add(botones);

        encabezado.add(textos, BorderLayout.WEST);
        encabezado.add(acciones, BorderLayout.EAST);

        add(encabezado, BorderLayout.NORTH);
    }

    // =====================================
    // CREAR CARRUSEL
    // =====================================

    private void crearCarrusel() {

        filaPerfiles = new JPanel();

        filaPerfiles.setLayout(
                new BoxLayout(filaPerfiles, BoxLayout.X_AXIS)
        );

        filaPerfiles.setBackground(FONDO);

        filaPerfiles.setBorder(
                new EmptyBorder(20, 10, 20, 10)
        );

        scroll = new JScrollPane(
                filaPerfiles,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.setBackground(FONDO);
        scroll.getViewport().setBackground(FONDO);

        add(scroll, BorderLayout.CENTER);
    }

    // =====================================
    // CARGAR PERFILES
    // =====================================

    private void cargarPerfiles() {

        filaPerfiles.removeAll();

        String filtro = campoBuscar.getText()
                .trim()
                .toLowerCase();

        int cantidad = carrusel.cantidadPerfiles();
        int encontrados = 0;

        for (int i = 0; i < cantidad; i++) {

            Perfil perfil = carrusel.obtenerPerfil(i);

            if (perfil == null) {
                continue;
            }

            boolean coincide =
                    perfil.getNombre().toLowerCase().contains(filtro)
                            || perfil.getUsuario().toLowerCase().contains(filtro);

            if (coincide) {

                filaPerfiles.add(crearTarjeta(perfil));

                filaPerfiles.add(
                        Box.createHorizontalStrut(ESPACIO_TARJETAS)
                );

                encontrados++;
            }
        }

        lblCantidad.setText(
                "Perfiles disponibles: " + cantidad
                        + " | Mostrados: " + encontrados
        );

        if (encontrados == 0) {

            JLabel mensaje = new JLabel(
                    cantidad == 0
                            ? "No hay amigos sugeridos disponibles."
                            : "No se encontraron perfiles."
            );

            mensaje.setForeground(TEXTO);
            mensaje.setFont(new Font("Arial", Font.BOLD, 20));

            filaPerfiles.add(Box.createHorizontalStrut(30));
            filaPerfiles.add(mensaje);
        }

        filaPerfiles.revalidate();
        filaPerfiles.repaint();

        actualizarBotones();

        SwingUtilities.invokeLater(() -> {

            scroll.getViewport().setViewPosition(new Point(0, 0));
        });
    }

    // =====================================
    // ACTUALIZAR ESTADO DE BOTONES
    // =====================================

    private void actualizarBotones() {

        boolean hayPerfiles = carrusel.cantidadPerfiles() > 0;
        boolean hayVarios = carrusel.cantidadPerfiles() > 1;

        btnSiguiente.setEnabled(hayVarios && !animando);
        btnAnterior.setEnabled(hayVarios && !animando);
        btnActualizar.setEnabled(hayVarios && !animando);
    }

    // =====================================
    // CREAR TARJETA
    // =====================================

    private JPanel crearTarjeta(Perfil perfil) {

        JPanel tarjeta = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(TARJETA);

                g2.fill(
                        new RoundRectangle2D.Double(
                                0, 0,
                                getWidth() - 1,
                                getHeight() - 1,
                                30, 30
                        )
                );

                GradientPaint borde = new GradientPaint(
                        0, 0, AZUL,
                        getWidth(), getHeight(), ROSADO
                );

                g2.setPaint(borde);
                g2.setStroke(new BasicStroke(2));

                g2.draw(
                        new RoundRectangle2D.Double(
                                1, 1,
                                getWidth() - 3,
                                getHeight() - 3,
                                30, 30
                        )
                );

                g2.dispose();
            }
        };

        tarjeta.setOpaque(false);

        tarjeta.setLayout(
                new BoxLayout(tarjeta, BoxLayout.Y_AXIS)
        );

        tarjeta.setBorder(
                new EmptyBorder(22, 18, 22, 18)
        );

        Dimension dimension = new Dimension(ANCHO_TARJETA, 410);

        tarjeta.setPreferredSize(dimension);
        tarjeta.setMinimumSize(dimension);
        tarjeta.setMaximumSize(dimension);

        // FOTO DE PERFIL

        Avatar avatar = new Avatar();

        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        String ruta = perfil.getRutaFoto();

        URL recurso = ruta == null
                ? null
                : getClass().getResource(ruta);

        if (recurso != null) {

            avatar.setImagen(
                    new ImageIcon(recurso).getImage()
            );

        } else {

            String nombre = perfil.getNombre();

            if (nombre != null && !nombre.isEmpty()) {
                avatar.setInicial(
                        nombre.substring(0, 1).toUpperCase()
                );
            }
        }

        // NOMBRE

        JLabel nombre = etiqueta(
                perfil.getNombre(), 19, true
        );

        // USUARIO

        JLabel usuario = etiqueta(
                "@" + perfil.getUsuario(), 14, false
        );

        // DESCRIPCIÓN

        JLabel descripcion = etiqueta(
                perfil.getDescripcion(), 13, false
        );

        // BOTÓN SEGUIR

        JButton seguir = crearBoton(
                perfil.isSiguiendo()
                        ? "Siguiendo"
                        : "Seguir"
        );

        seguir.setAlignmentX(Component.CENTER_ALIGNMENT);

        actualizarColorSeguir(seguir, perfil);

        seguir.addActionListener(e -> {

            perfil.cambiarSeguimiento();

            seguir.setText(
                    perfil.isSiguiendo()
                            ? "Siguiendo"
                            : "Seguir"
            );

            actualizarColorSeguir(seguir, perfil);
        });

        // BOTÓN ELIMINAR

        JButton eliminar = crearBoton("Eliminar sugerencia");

        eliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
        eliminar.setBackground(ROJO);
        eliminar.setForeground(Color.WHITE);

        eliminar.addActionListener(e -> {

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas eliminar a "
                            + perfil.getNombre()
                            + " de tus sugerencias?",
                    "Eliminar sugerencia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {

                carrusel.eliminarPerfil(perfil);

                cargarPerfiles();
            }
        });

        // AGREGAR ELEMENTOS

        tarjeta.add(avatar);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(nombre);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(usuario);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(descripcion);
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(seguir);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(eliminar);

        return tarjeta;
    }

    // =====================================
    // ETIQUETAS
    // =====================================

    private JLabel etiqueta(
            String texto,
            int tamanio,
            boolean negrita
    ) {

        JLabel etiqueta = new JLabel(
                texto == null ? "" : texto,
                SwingConstants.CENTER
        );

        etiqueta.setFont(
                new Font(
                        "Arial",
                        negrita ? Font.BOLD : Font.PLAIN,
                        tamanio
                )
        );

        etiqueta.setForeground(
                negrita ? TEXTO : SECUNDARIO
        );

        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        etiqueta.setMaximumSize(new Dimension(210, 55));

        return etiqueta;
    }

    // =====================================
    // COLOR DEL BOTÓN SEGUIR
    // =====================================

    private void actualizarColorSeguir(
            JButton boton,
            Perfil perfil
    ) {

        if (perfil.isSiguiendo()) {

            boton.setBackground(new Color(100, 190, 140));

        } else {

            boton.setBackground(new Color(100, 170, 245));
        }
    }

    // =====================================
    // NAVEGACIÓN
    // =====================================

    private void crearNavegacion() {

        JPanel navegacion = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10)
        );

        navegacion.setBackground(FONDO);

        btnAnterior = crearBoton("ANTERIOR");

        btnSiguiente = crearBoton("SIGUIENTE ");

        btnAnterior.addActionListener(e -> {

            if (animando || carrusel.cantidadPerfiles() <= 1) {
                return;
            }

            carrusel.anterior();

            cargarPerfiles();
        });

        btnSiguiente.addActionListener(e -> {

            if (animando || carrusel.cantidadPerfiles() <= 1) {
                return;
            }

            animarCarrusel();
        });

        navegacion.add(btnAnterior);
        navegacion.add(btnSiguiente);

        add(navegacion, BorderLayout.SOUTH);
    }

    // =====================================
    // ANIMAR CARRUSEL
    // =====================================

    private void animarCarrusel() {

        animando = true;
        actualizarBotones();

        JScrollBar barra = scroll.getHorizontalScrollBar();

        int inicio = barra.getValue();

        int maximo = barra.getMaximum()
                - barra.getVisibleAmount();

        int destino = Math.min(
                inicio + DESPLAZAMIENTO,
                Math.max(0, maximo)
        );

        if (destino > inicio) {

            Timer timer = new Timer(10, null);

            timer.addActionListener(e -> {

                int actual = barra.getValue();

                int diferencia = destino - actual;

                if (diferencia > 4) {

                    barra.setValue(actual + 4);

                } else {

                    barra.setValue(destino);

                    timer.stop();

                    finalizarMovimiento();
                }
            });

            timer.start();

        } else {

            finalizarMovimiento();
        }
    }

    // =====================================
    // FINALIZAR MOVIMIENTO
    // =====================================

    private void finalizarMovimiento() {

        carrusel.siguiente();

        animando = false;

        cargarPerfiles();
    }

    // =====================================
    // CREAR BOTONES
    // =====================================

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        boton.setForeground(Color.BLACK);

        boton.setBackground(
                new Color(235, 240, 250)
        );

        boton.setFocusPainted(false);
        boton.setOpaque(true);

        boton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(190, 200, 220),
                                1
                        ),
                        new EmptyBorder(10, 15, 10, 15)
                )
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    // =====================================
    // AVATAR CIRCULAR
    // =====================================

    private static class Avatar extends JLabel {

        private Image imagen;
        private String inicial = "?";

        public Avatar() {

            Dimension dimension = new Dimension(125, 125);

            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);

            setOpaque(false);
        }

        public void setImagen(Image imagen) {

            this.imagen = imagen;
            repaint();
        }

        public void setInicial(String inicial) {

            this.inicial = inicial;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int diametro = 115;

            int x = (getWidth() - diametro) / 2;
            int y = (getHeight() - diametro) / 2;

            g2.setColor(new Color(55, 70, 95));

            g2.fillOval(x, y, diametro, diametro);

            Graphics2D imagenG = (Graphics2D) g2.create();

            imagenG.setClip(
                    new Ellipse2D.Double(
                            x, y, diametro, diametro
                    )
            );

            if (imagen != null) {

                int ancho = imagen.getWidth(this);
                int alto = imagen.getHeight(this);

                if (ancho > 0 && alto > 0) {

                    double escala = Math.max(
                            (double) diametro / ancho,
                            (double) diametro / alto
                    );

                    int nuevoAncho = (int) (ancho * escala);
                    int nuevoAlto = (int) (alto * escala);

                    int imagenX =
                            x + (diametro - nuevoAncho) / 2;

                    int imagenY =
                            y + (diametro - nuevoAlto) / 2;

                    imagenG.drawImage(
                            imagen,
                            imagenX,
                            imagenY,
                            nuevoAncho,
                            nuevoAlto,
                            this
                    );
                }

            } else {

                imagenG.setColor(new Color(55, 85, 125));

                imagenG.fillOval(x, y, diametro, diametro);

                imagenG.setColor(Color.WHITE);

                imagenG.setFont(
                        new Font("Arial", Font.BOLD, 55)
                );

                FontMetrics fm = imagenG.getFontMetrics();

                int textoX =
                        x + (diametro - fm.stringWidth(inicial)) / 2;

                int textoY =
                        y + (diametro - fm.getHeight()) / 2
                                + fm.getAscent();

                imagenG.drawString(inicial, textoX, textoY);
            }

            imagenG.dispose();

            GradientPaint degradado = new GradientPaint(
                    x, y, AZUL,
                    x + diametro, y + diametro, ROSADO
            );

            g2.setPaint(degradado);
            g2.setStroke(new BasicStroke(5));

            g2.drawOval(x, y, diametro, diametro);

            g2.dispose();
        }
    }
}
