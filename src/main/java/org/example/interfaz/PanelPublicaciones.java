package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.example.redsocial.publicaciones.Contenido;
import org.example.redsocial.publicaciones.Publicacion;
import org.example.redsocial.publicaciones.PublicacionServices;

/**
 * Pantalla Swing interactiva para el Feed de Publicaciones basado en Pila (LIFO).
 */
public class PanelPublicaciones extends JPanel {

    private final Runnable alVolver;
    private final PublicacionServices servicio = new PublicacionServices();
    private final JPanel contenedorPublicaciones = new JPanel();
    private final JLabel lblContador = new JLabel();

    private List<Publicacion> publicacionesFiltradas = null;

    public PanelPublicaciones(Runnable alVolver) {
        this.alVolver = alVolver;

        setLayout(new BorderLayout(0, 16));
        setBackground(Paleta.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        cargarDatosIniciales();

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearCuerpoCentral(), BorderLayout.CENTER);

        refrescarFeed();
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout(16, 10));
        barra.setBackground(Paleta.FONDO);

        // Fila 1: Botón Volver + Título + Contador
        JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filaTitulo.setBackground(Paleta.FONDO);

        JButton btnVolver = crearBoton("← Volver al Menú");
        btnVolver.addActionListener(e -> {
            if (alVolver != null) alVolver.run();
        });

        JLabel lblTitulo = new JLabel("Feed de Publicaciones (Pila LIFO)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Paleta.TEXTO);

        lblContador.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblContador.setForeground(Paleta.TEXTO_SECUNDARIO);

        filaTitulo.add(btnVolver);
        filaTitulo.add(lblTitulo);
        filaTitulo.add(lblContador);

        // Fila 2: Barra de herramientas y acciones
        JPanel filaAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filaAcciones.setBackground(Paleta.FONDO);

        JButton btnNuevo = crearBoton("+ Nueva Publicación");
        btnNuevo.addActionListener(e -> abrirDialogoNuevaPublicacion());

        JButton btnEliminarReciente = crearBoton("🗑 Eliminar publicación reciente");
        btnEliminarReciente.addActionListener(e -> eliminarPublicacionReciente());

        JButton btnFiltrarUsuario = crearBoton("👤 Filtrar por Usuario");
        btnFiltrarUsuario.addActionListener(e -> filtrarPorUsuario());

        JButton btnBuscarTag = crearBoton("# Buscar Hashtag");
        btnBuscarTag.addActionListener(e -> buscarPorHashtag());

        JButton btnRestablecer = crearBoton("↻ Ver Todo el Feed");
        btnRestablecer.addActionListener(e -> {
            publicacionesFiltradas = null;
            refrescarFeed();
        });

        filaAcciones.add(btnNuevo);
        filaAcciones.add(btnEliminarReciente);
        filaAcciones.add(btnFiltrarUsuario);
        filaAcciones.add(btnBuscarTag);
        filaAcciones.add(btnRestablecer);

        barra.add(filaTitulo, BorderLayout.NORTH);
        barra.add(filaAcciones, BorderLayout.SOUTH);

        return barra;
    }

    private JScrollPane crearCuerpoCentral() {
        contenedorPublicaciones.setLayout(new BoxLayout(contenedorPublicaciones, BoxLayout.Y_AXIS));
        contenedorPublicaciones.setBackground(Paleta.FONDO);

        JScrollPane scroll = new JScrollPane(contenedorPublicaciones);
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    public void refrescarFeed() {
        contenedorPublicaciones.removeAll();

        List<Publicacion> lista = (publicacionesFiltradas != null)
                ? publicacionesFiltradas
                : servicio.obtenerTodas();

        lblContador.setText(String.format(" | Total en la Pila: %d publicaciones", servicio.totalPublicaciones()));

        if (lista.isEmpty()) {
            JPanel vacio = new JPanel(new FlowLayout(FlowLayout.CENTER));
            vacio.setBackground(Paleta.FONDO);
            JLabel msg = new JLabel("No hay publicaciones para mostrar.");
            msg.setForeground(Paleta.TEXTO_SECUNDARIO);
            msg.setFont(new Font("SansSerif", Font.ITALIC, 16));
            vacio.add(msg);
            contenedorPublicaciones.add(Box.createVerticalStrut(30));
            contenedorPublicaciones.add(vacio);
        } else {
            int indice = 1;
            for (Publicacion p : lista) {
                p.incrementarVistas();
                contenedorPublicaciones.add(crearTarjetaPublicacion(p, indice == 1 && publicacionesFiltradas == null));
                contenedorPublicaciones.add(Box.createVerticalStrut(14));
                indice++;
            }
        }

        contenedorPublicaciones.revalidate();
        contenedorPublicaciones.repaint();
    }

    private JPanel crearTarjetaPublicacion(Publicacion p, boolean esTope) {
        JPanel tarjeta = new JPanel(new BorderLayout(16, 0));
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(esTope ? Paleta.BOTON_ACTIVO : Paleta.BORDE, esTope ? 2 : 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));

        // Columna izquierda: Imagen o Multimedia compacta y elegante
        JPanel panelIzquierdo = crearPanelMultimediaIzquierdo(p);

        // Columna derecha: Información ordenada y elegante
        JPanel panelDerecho = crearPanelInformacionDerecho(p, esTope);

        tarjeta.add(panelIzquierdo, BorderLayout.WEST);
        tarjeta.add(panelDerecho, BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearPanelMultimediaIzquierdo(Publicacion p) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Paleta.TARJETA);
        contenedor.setPreferredSize(new Dimension(150, 125));
        contenedor.setMinimumSize(new Dimension(150, 125));
        contenedor.setMaximumSize(new Dimension(150, 125));

        Contenido cont = p.getContenido();

        // 1. Si tiene archivo de imagen real asignado
        if (cont != null && cont.tieneRutaImagen()) {
            try {
                ImageIcon icono = new ImageIcon(cont.getRutaImagen());
                Image img = icono.getImage();
                int w = img.getWidth(null);
                int h = img.getHeight(null);
                if (w > 0 && h > 0) {
                    int maxW = 145;
                    int maxH = 115;
                    double escala = Math.min((double) maxW / w, (double) maxH / h);
                    int newW = Math.max(1, (int) (w * escala));
                    int newH = Math.max(1, (int) (h * escala));
                    Image escalada = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    JLabel lblFoto = new JLabel(new ImageIcon(escalada), JLabel.CENTER);
                    lblFoto.setBorder(BorderFactory.createLineBorder(Paleta.BORDE, 1));
                    contenedor.add(lblFoto, BorderLayout.CENTER);
                    return contenedor;
                }
            } catch (Exception ignored) {
            }
        }

        // 2. Si tiene formato de imagen (sin archivo aún)
        if (cont != null && cont.getCantidadImagenes() > 0) {
            JPanel cajaFoto = new JPanel();
            cajaFoto.setLayout(new BoxLayout(cajaFoto, BoxLayout.Y_AXIS));
            cajaFoto.setBackground(Paleta.BOTON);
            cajaFoto.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Paleta.BORDE, 1),
                    BorderFactory.createEmptyBorder(12, 8, 12, 8)
            ));

            JLabel lblIcono = new JLabel("📷", JLabel.CENTER);
            lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 26));
            lblIcono.setForeground(Paleta.TEXTO);
            lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblTexto = new JLabel(cont.getCantidadImagenes() == 1 ? "1 Imagen" : cont.getCantidadImagenes() + " Imágenes");
            lblTexto.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblTexto.setForeground(Paleta.TEXTO);
            lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnSubir = crearBoton("🖼️ Asignar");
            btnSubir.setFont(new Font("SansSerif", Font.PLAIN, 10));
            btnSubir.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnSubir.addActionListener(e -> seleccionarImagenParaPublicacion(p));

            cajaFoto.add(lblIcono);
            cajaFoto.add(Box.createVerticalStrut(4));
            cajaFoto.add(lblTexto);
            cajaFoto.add(Box.createVerticalStrut(6));
            cajaFoto.add(btnSubir);

            contenedor.add(cajaFoto, BorderLayout.CENTER);
            return contenedor;
        }

        // 3. Si tiene formato de video
        if (cont != null && cont.getCantidadVideos() > 0) {
            JPanel cajaVideo = new JPanel();
            cajaVideo.setLayout(new BoxLayout(cajaVideo, BoxLayout.Y_AXIS));
            cajaVideo.setBackground(Paleta.BOTON);
            cajaVideo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Paleta.BORDE, 1),
                    BorderFactory.createEmptyBorder(20, 8, 20, 8)
            ));

            JLabel lblIcono = new JLabel("🎥 ▶", JLabel.CENTER);
            lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 24));
            lblIcono.setForeground(Paleta.TEXTO);
            lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblTexto = new JLabel(cont.getCantidadVideos() == 1 ? "Video" : cont.getCantidadVideos() + " Videos");
            lblTexto.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblTexto.setForeground(Paleta.TEXTO);
            lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

            cajaVideo.add(lblIcono);
            cajaVideo.add(Box.createVerticalStrut(6));
            cajaVideo.add(lblTexto);

            contenedor.add(cajaVideo, BorderLayout.CENTER);
            return contenedor;
        }

        // 4. Si no tiene multimedia (solo texto)
        JPanel cajaTexto = new JPanel();
        cajaTexto.setLayout(new BoxLayout(cajaTexto, BoxLayout.Y_AXIS));
        cajaTexto.setBackground(Paleta.BOTON);
        cajaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(20, 8, 20, 8)
        ));

        JLabel lblIcono = new JLabel("📝", JLabel.CENTER);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblIcono.setForeground(Paleta.TEXTO_SECUNDARIO);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel("Texto");
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblTexto.setForeground(Paleta.TEXTO_SECUNDARIO);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        cajaTexto.add(lblIcono);
        cajaTexto.add(Box.createVerticalStrut(6));
        cajaTexto.add(lblTexto);

        contenedor.add(cajaTexto, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPanelInformacionDerecho(Publicacion p, boolean esTope) {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(Paleta.TARJETA);

        // Fila 1: Cabecera (Tope de pila y Autor ID)
        JPanel filaCabecera = new JPanel(new BorderLayout());
        filaCabecera.setBackground(Paleta.TARJETA);

        String badge = esTope ? "★ [TOPE DE LA PILA - MÁS RECIENTE]" : "• [Publicación]";
        JLabel lblTope = new JLabel(badge);
        lblTope.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTope.setForeground(esTope ? Paleta.TEXTO : Paleta.TEXTO_SECUNDARIO);

        JLabel lblUsuario = new JLabel("👤 Autor ID: " + p.getCodigoUsuario());
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsuario.setForeground(Paleta.TEXTO_SECUNDARIO);

        filaCabecera.add(lblTope, BorderLayout.WEST);
        filaCabecera.add(lblUsuario, BorderLayout.EAST);

        // Fila 2: Título
        JLabel lblTitulo = new JLabel(p.getTitulo());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(Paleta.TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fila 3: Descripción
        JLabel lblDesc = new JLabel("<html><body style='width: 480px;'>" + p.getDescripcion() + "</body></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDesc.setForeground(Paleta.TEXTO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fila 4: Etiquetas
        JLabel lblTags = new JLabel(p.mostrarEtiquetas().isEmpty() ? "" : p.mostrarEtiquetas());
        lblTags.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblTags.setForeground(Paleta.TEXTO_SECUNDARIO);
        lblTags.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fila 5: Pie (Vistas, Likes y botón Like)
        JPanel filaPie = new JPanel(new BorderLayout());
        filaPie.setBackground(Paleta.TARJETA);

        JLabel lblAdjuntos = new JLabel(p.getContenido() != null ? p.getContenido().resumenVisual() : "");
        lblAdjuntos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAdjuntos.setForeground(Paleta.TEXTO_SECUNDARIO);

        JPanel panelLikes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelLikes.setBackground(Paleta.TARJETA);

        JLabel lblStats = new JLabel(String.format("👁️ %d vistas   ❤️ %d likes", p.getVistas(), p.getLikes()));
        lblStats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblStats.setForeground(Paleta.TEXTO);

        JButton btnLike = crearBoton("❤️ Me gusta");
        btnLike.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnLike.addActionListener(e -> {
            p.darLike();
            lblStats.setText(String.format("👁️ %d vistas   ❤️ %d likes", p.getVistas(), p.getLikes()));
        });

        panelLikes.add(lblStats);
        panelLikes.add(btnLike);

        filaPie.add(lblAdjuntos, BorderLayout.WEST);
        filaPie.add(panelLikes, BorderLayout.EAST);

        panelDerecho.add(filaCabecera);
        panelDerecho.add(Box.createVerticalStrut(4));
        panelDerecho.add(lblTitulo);
        panelDerecho.add(Box.createVerticalStrut(3));
        panelDerecho.add(lblDesc);

        if (!p.mostrarEtiquetas().isEmpty()) {
            panelDerecho.add(Box.createVerticalStrut(3));
            panelDerecho.add(lblTags);
        }

        panelDerecho.add(Box.createVerticalStrut(8));
        panelDerecho.add(filaPie);

        return panelDerecho;
    }

    private void abrirDialogoNuevaPublicacion() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Crear Nueva Publicación", JDialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(520, 520);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(Paleta.FONDO);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Paleta.FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JTextField txtUsuario = crearCampoTexto("101");
        JTextField txtTitulo = crearCampoTexto("");
        JTextField txtDesc = crearCampoTexto("");

        // --- Panel desplegable de Multimedia ---
        String[] opcionesMultimedia = {
            "📷 Imagen / Foto",
            "🎥 Video",
            "📷🎥 Imagen y Video",
            "📷📷 Galería de Fotos (2 Imágenes)",
            "📷📷📷 Galería de Fotos (3 Imágenes)",
            "Sin contenido multimedia"
        };
        JComboBox<String> comboMultimedia = new JComboBox<>(opcionesMultimedia);
        configurarCombo(comboMultimedia);

        // Selector opcional de archivo de imagen del equipo
        final String[] rutaImagenSeleccionada = { null };
        JLabel lblArchivo = new JLabel("Ningún archivo de imagen seleccionado (opcional)");
        lblArchivo.setForeground(Paleta.TEXTO_SECUNDARIO);
        lblArchivo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton btnExaminar = crearBoton("📁 Seleccionar imagen del equipo...");
        btnExaminar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccionar Imagen");
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg, *.gif, *.webp)", 
                "png", "jpg", "jpeg", "gif", "webp"));
            int resultado = chooser.showOpenDialog(dialogo);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                rutaImagenSeleccionada[0] = archivo.getAbsolutePath();
                lblArchivo.setText("✓ " + archivo.getName());
                lblArchivo.setForeground(Paleta.TEXTO);
                if (comboMultimedia.getSelectedIndex() == 5) {
                    comboMultimedia.setSelectedIndex(0);
                }
            }
        });

        JPanel panelSelectorArchivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelSelectorArchivo.setBackground(Paleta.FONDO);
        panelSelectorArchivo.add(btnExaminar);
        panelSelectorArchivo.add(lblArchivo);

        // Agregar componentes con GridBagLayout
        int fila = 0;
        gbc.gridy = fila++; panel.add(crearEtiquetaCampo("ID de Usuario (entero):"), gbc);
        gbc.gridy = fila++; panel.add(txtUsuario, gbc);

        gbc.gridy = fila++; panel.add(crearEtiquetaCampo("Título de la publicación:"), gbc);
        gbc.gridy = fila++; panel.add(txtTitulo, gbc);

        gbc.gridy = fila++; panel.add(crearEtiquetaCampo("Descripción (usa #hashtags):"), gbc);
        gbc.gridy = fila++; panel.add(txtDesc, gbc);

        gbc.gridy = fila++; panel.add(crearEtiquetaCampo("Multimedia (Panel Desplegable):"), gbc);
        gbc.gridy = fila++; panel.add(comboMultimedia, gbc);

        gbc.gridy = fila++; panel.add(crearEtiquetaCampo("Opción de poner imagen propia:"), gbc);
        gbc.gridy = fila++; panel.add(panelSelectorArchivo, gbc);

        JButton btnGuardar = crearBoton("✓ Publicar al Tope de la Pila");
        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtUsuario.getText().trim());
                String titulo = txtTitulo.getText().trim();
                String desc = txtDesc.getText().trim();

                // Convertir selección del desplegable a formatos
                int seleccion = comboMultimedia.getSelectedIndex();
                char[] formatos;
                switch (seleccion) {
                    case 0: formatos = new char[]{'f'}; break;
                    case 1: formatos = new char[]{'v'}; break;
                    case 2: formatos = new char[]{'f', 'v'}; break;
                    case 3: formatos = new char[]{'f', 'f'}; break;
                    case 4: formatos = new char[]{'f', 'f', 'f'}; break;
                    default: formatos = new char[0]; break;
                }

                if (rutaImagenSeleccionada[0] != null && formatos.length == 0) {
                    formatos = new char[]{'f'};
                }

                Contenido cont = new Contenido(formatos, rutaImagenSeleccionada[0]);
                Publicacion nueva = new Publicacion(cont, titulo.isEmpty() ? "Sin título" : titulo, desc, id);

                servicio.agregarPublicacion(nueva);
                publicacionesFiltradas = null;
                refrescarFeed();
                dialogo.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El ID de usuario debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = fila++;
        gbc.insets = new Insets(14, 5, 5, 5);
        panel.add(btnGuardar, gbc);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void eliminarPublicacionReciente() {
        if (servicio.estaVacio()) {
            JOptionPane.showMessageDialog(this, 
                "El feed está vacío, no hay ninguna publicación reciente para eliminar.", 
                "Pila Vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Publicacion top = servicio.obtenerMasReciente();
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar la publicación reciente del tope de la pila?\n\n\"" + 
                top.getTitulo() + "\" (Usuario " + top.getCodigoUsuario() + ")",
                "Eliminar publicación reciente", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            servicio.eliminarMasReciente();
            publicacionesFiltradas = null;
            refrescarFeed();
        }
    }

    private void seleccionarImagenParaPublicacion(Publicacion p) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Asignar archivo de imagen a la publicación");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg, *.gif, *.webp)", 
            "png", "jpg", "jpeg", "gif", "webp"));
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            if (p.getContenido() != null) {
                p.getContenido().setRutaImagen(archivo.getAbsolutePath());
            } else {
                p.setContenido(new Contenido(new char[]{'f'}, archivo.getAbsolutePath()));
            }
            refrescarFeed();
        }
    }

    private void filtrarPorUsuario() {
        String input = JOptionPane.showInputDialog(this, "Ingresa el ID del usuario a consultar:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(input.trim());
                publicacionesFiltradas = servicio.filtrarPorUsuario(id);
                refrescarFeed();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPorHashtag() {
        String input = JOptionPane.showInputDialog(this, "Ingresa el hashtag o término a buscar (ej: viaje o #java):");
        if (input != null && !input.trim().isEmpty()) {
            publicacionesFiltradas = servicio.buscarPorEtiqueta(input.trim());
            refrescarFeed();
        }
    }

    private void cargarDatosIniciales() {
        char[] f1 = {'f', 'f', 'f', 'v'};
        Publicacion p1 = new Publicacion(new Contenido(f1), "Paseo por la montaña",
                "Increíble fin de semana al aire libre #viaje #naturaleza #montaña", 101);
        p1.darLike();
        p1.darLike();

        char[] f2 = {'v'};
        Publicacion p2 = new Publicacion(new Contenido(f2), "Implementando Pilas en Java",
                "Aprendiendo estructuras de datos y LIFO #programacion #java #universidad", 102);
        p2.darLike();

        char[] f3 = {'f', 'f'};
        Publicacion p3 = new Publicacion(new Contenido(f3), "Atardecer en la playa",
                "Relajándome junto al mar #viaje #playa #relax", 101);
        p3.darLike();
        p3.darLike();
        p3.darLike();

        servicio.agregarPublicacion(p1);
        servicio.agregarPublicacion(p2);
        servicio.agregarPublicacion(p3);
    }

    public static JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Paleta.BOTON);
        btn.setForeground(Paleta.TEXTO);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Paleta.BOTON_ACTIVO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Paleta.BOTON);
            }
        });
        return btn;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Paleta.TEXTO);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }

    private JTextField crearCampoTexto(String predeterminado) {
        JTextField txt = new JTextField(predeterminado);
        txt.setBackground(Paleta.TARJETA);
        txt.setForeground(Paleta.TEXTO);
        txt.setCaretColor(Paleta.TEXTO);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return txt;
    }

    private void configurarCombo(JComboBox<String> combo) {
        combo.setBackground(Paleta.TARJETA);
        combo.setForeground(Paleta.TEXTO);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? Paleta.BOTON_ACTIVO : Paleta.TARJETA);
                c.setForeground(Paleta.TEXTO);
                return c;
            }
        });
    }
}
