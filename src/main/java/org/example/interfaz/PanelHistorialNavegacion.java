package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.example.redsocial.historial.EntradaHistorial;
import org.example.redsocial.historial.HistorialNavegacionService;

/**
 * Pantalla Swing interactiva para el Historial de Navegación y Búsquedas Recientes.
 * Implementa dos Pilas (Atrás y Adelante) siguiendo la estructura de datos LIFO.
 */
public class PanelHistorialNavegacion extends JPanel {

    private final Runnable alVolver;
    private final HistorialNavegacionService servicio = new HistorialNavegacionService();

    // Controles de navegación
    private final JButton btnAtras = PanelPublicaciones.crearBoton("◀ Atrás");
    private final JButton btnAdelante = PanelPublicaciones.crearBoton("▶ Adelante");
    private final JTextField txtBarraDireccion = new JTextField();

    // Componentes de visualización
    private final JPanel tarjetaActual = new JPanel(new BorderLayout(14, 0));
    private final JPanel contenedorAtras = new JPanel();
    private final JPanel contenedorAdelante = new JPanel();
    private final JLabel lblContadorAtras = new JLabel();
    private final JLabel lblContadorAdelante = new JLabel();

    public PanelHistorialNavegacion(Runnable alVolver) {
        this.alVolver = alVolver;

        setLayout(new BorderLayout(0, 14));
        setBackground(Paleta.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);

        actualizarTodo();
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.Y_AXIS));
        barra.setBackground(Paleta.FONDO);

        // Fila 1: Botón Volver + Título
        JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filaTitulo.setBackground(Paleta.FONDO);

        JButton btnVolver = PanelPublicaciones.crearBoton("← Volver al Menú");
        btnVolver.addActionListener(e -> {
            if (alVolver != null) alVolver.run();
        });

        JLabel lblTitulo = new JLabel("Historial de Navegación y Búsquedas (Pila LIFO)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Paleta.TEXTO);

        filaTitulo.add(btnVolver);
        filaTitulo.add(lblTitulo);

        // Fila 2: Barra de Dirección estilo Navegador Web (Atrás, Adelante, Input, Botones)
        JPanel filaNavegador = new JPanel(new BorderLayout(8, 0));
        filaNavegador.setBackground(Paleta.FONDO);
        filaNavegador.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));

        JPanel botonesNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        botonesNav.setBackground(Paleta.FONDO);

        btnAtras.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAtras.addActionListener(e -> {
            servicio.retroceder();
            actualizarTodo();
        });

        btnAdelante.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAdelante.addActionListener(e -> {
            servicio.avanzar();
            actualizarTodo();
        });

        botonesNav.add(btnAtras);
        botonesNav.add(btnAdelante);

        // Campo de texto de la barra de dirección
        txtBarraDireccion.setBackground(Paleta.TARJETA);
        txtBarraDireccion.setForeground(Paleta.TEXTO);
        txtBarraDireccion.setCaretColor(Paleta.TEXTO);
        txtBarraDireccion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBarraDireccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        txtBarraDireccion.addActionListener(e -> ejecutarNavegacion("Página"));

        // Acciones de navegación rápida
        JPanel accionesBarra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        accionesBarra.setBackground(Paleta.FONDO);

        JButton btnIr = PanelPublicaciones.crearBoton("🌐 Navegar");
        btnIr.addActionListener(e -> ejecutarNavegacion("Página"));

        JButton btnBuscar = PanelPublicaciones.crearBoton("🔍 Buscar");
        btnBuscar.addActionListener(e -> ejecutarNavegacion("Búsqueda"));

        JButton btnPerfil = PanelPublicaciones.crearBoton("👤 Perfil");
        btnPerfil.addActionListener(e -> ejecutarNavegacion("Perfil"));

        JButton btnLimpiar = PanelPublicaciones.crearBoton("🗑 Limpiar");
        btnLimpiar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Deseas vaciar todo el historial de navegación?",
                    "Limpiar Historial", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                servicio.vaciarHistorial();
                actualizarTodo();
            }
        });

        accionesBarra.add(btnIr);
        accionesBarra.add(btnBuscar);
        accionesBarra.add(btnPerfil);
        accionesBarra.add(btnLimpiar);

        filaNavegador.add(botonesNav, BorderLayout.WEST);
        filaNavegador.add(txtBarraDireccion, BorderLayout.CENTER);
        filaNavegador.add(accionesBarra, BorderLayout.EAST);

        // Fila 3: Accesos directos rápidos
        JPanel filaAtajos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        filaAtajos.setBackground(Paleta.FONDO);

        JLabel lblAtajos = new JLabel("Accesos rápidos:");
        lblAtajos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAtajos.setForeground(Paleta.TEXTO_SECUNDARIO);
        filaAtajos.add(lblAtajos);

        String[][] atajos = {
            {"🏠 Feed", "redsocial.com/feed", "Página"},
            {"👤 @adrian", "adrian_martinez", "Perfil"},
            {"👤 @boris", "boris_angulo", "Perfil"},
            {"🔥 #tendencias", "#tecnologia_java", "Tendencia"},
            {"🔍 Amigos", "Buscar conexiones de amigos", "Búsqueda"}
        };

        for (String[] atajo : atajos) {
            JButton btnChip = crearBotonChip(atajo[0]);
            btnChip.addActionListener(e -> {
                servicio.visitar(atajo[1], atajo[2]);
                actualizarTodo();
            });
            filaAtajos.add(btnChip);
        }

        barra.add(filaTitulo);
        barra.add(filaNavegador);
        barra.add(filaAtajos);

        return barra;
    }

    private JPanel crearPanelCentral() {
        JPanel central = new JPanel(new BorderLayout(0, 12));
        central.setBackground(Paleta.FONDO);

        // 1. Tarjeta destacada de la Página Actual (Tope de la Pila)
        configurarTarjetaActual();
        central.add(tarjetaActual, BorderLayout.NORTH);

        // 2. Columnas de las dos pilas (Historial Atrás y Avance Adelante)
        JPanel columnas = new JPanel(new GridLayout(1, 2, 16, 0));
        columnas.setBackground(Paleta.FONDO);

        // Columna Izquierda: Pila Atrás
        JPanel panelColAtras = new JPanel(new BorderLayout(0, 8));
        panelColAtras.setBackground(Paleta.FONDO);

        JPanel headerAtras = new JPanel(new BorderLayout());
        headerAtras.setBackground(Paleta.FONDO);
        JLabel lblTitAtras = new JLabel("📚 Pila de Historial (Atrás — LIFO)");
        lblTitAtras.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitAtras.setForeground(Paleta.TEXTO);
        lblContadorAtras.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblContadorAtras.setForeground(Paleta.TEXTO_SECUNDARIO);
        headerAtras.add(lblTitAtras, BorderLayout.WEST);
        headerAtras.add(lblContadorAtras, BorderLayout.EAST);

        contenedorAtras.setLayout(new BoxLayout(contenedorAtras, BoxLayout.Y_AXIS));
        contenedorAtras.setBackground(Paleta.FONDO);
        JScrollPane scrollAtras = crearScroll(contenedorAtras);

        panelColAtras.add(headerAtras, BorderLayout.NORTH);
        panelColAtras.add(scrollAtras, BorderLayout.CENTER);

        // Columna Derecha: Pila Adelante
        JPanel panelColAdelante = new JPanel(new BorderLayout(0, 8));
        panelColAdelante.setBackground(Paleta.FONDO);

        JPanel headerAdelante = new JPanel(new BorderLayout());
        headerAdelante.setBackground(Paleta.FONDO);
        JLabel lblTitAdelante = new JLabel("⏩ Pila de Avance (Adelante)");
        lblTitAdelante.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitAdelante.setForeground(Paleta.TEXTO);
        lblContadorAdelante.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblContadorAdelante.setForeground(Paleta.TEXTO_SECUNDARIO);
        headerAdelante.add(lblTitAdelante, BorderLayout.WEST);
        headerAdelante.add(lblContadorAdelante, BorderLayout.EAST);

        contenedorAdelante.setLayout(new BoxLayout(contenedorAdelante, BoxLayout.Y_AXIS));
        contenedorAdelante.setBackground(Paleta.FONDO);
        JScrollPane scrollAdelante = crearScroll(contenedorAdelante);

        panelColAdelante.add(headerAdelante, BorderLayout.NORTH);
        panelColAdelante.add(scrollAdelante, BorderLayout.CENTER);

        columnas.add(panelColAtras);
        columnas.add(panelColAdelante);

        central.add(columnas, BorderLayout.CENTER);

        return central;
    }

    private void configurarTarjetaActual() {
        tarjetaActual.setBackground(Paleta.TARJETA);
        tarjetaActual.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BOTON_ACTIVO, 2),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        tarjetaActual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
    }

    public void actualizarTodo() {
        EntradaHistorial actual = servicio.paginaActual();

        // 1. Actualizar barra de dirección
        if (actual != null) {
            txtBarraDireccion.setText(actual.getUrlCompleta());
        } else {
            txtBarraDireccion.setText("");
        }

        // 2. Habilitar o deshabilitar botones Atrás / Adelante
        btnAtras.setEnabled(servicio.puedeRetroceder());
        btnAdelante.setEnabled(servicio.puedeAvanzar());

        // 3. Renderizar Tarjeta de Página Actual
        tarjetaActual.removeAll();
        if (actual != null) {
            JPanel infoIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            infoIzquierda.setBackground(Paleta.TARJETA);

            JLabel lblIcono = new JLabel(actual.getIcono());
            lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 28));

            JPanel textos = new JPanel();
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
            textos.setBackground(Paleta.TARJETA);

            JLabel lblBadge = new JLabel("★ [PÁGINA ACTUAL — TOPE DE LA PILA]");
            lblBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblBadge.setForeground(Paleta.TEXTO);

            JLabel lblDestino = new JLabel(actual.getDestino());
            lblDestino.setFont(new Font("SansSerif", Font.BOLD, 17));
            lblDestino.setForeground(Paleta.TEXTO);

            JLabel lblDetalles = new JLabel(String.format("Tipo: %s  |  Protocolo: 🔒 %s  |  Hora: %s",
                    actual.getTipo(), actual.getSeguridad().toUpperCase(), actual.getHoraVisita()));
            lblDetalles.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblDetalles.setForeground(Paleta.TEXTO_SECUNDARIO);

            textos.add(lblBadge);
            textos.add(Box.createVerticalStrut(2));
            textos.add(lblDestino);
            textos.add(Box.createVerticalStrut(2));
            textos.add(lblDetalles);

            infoIzquierda.add(lblIcono);
            infoIzquierda.add(textos);

            tarjetaActual.add(infoIzquierda, BorderLayout.CENTER);
        } else {
            JLabel lblVacio = new JLabel("Historial vacío. Navega a una URL o realiza una búsqueda arriba.", JLabel.CENTER);
            lblVacio.setForeground(Paleta.TEXTO_SECUNDARIO);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            tarjetaActual.add(lblVacio, BorderLayout.CENTER);
        }

        // 4. Renderizar Pila Atrás
        contenedorAtras.removeAll();
        List<EntradaHistorial> listaAtras = servicio.obtenerListaAtras();
        lblContadorAtras.setText(listaAtras.size() + " en pila");

        if (listaAtras.isEmpty()) {
            contenedorAtras.add(crearMensajeVacio("Sin páginas en el historial."));
        } else {
            int indice = 0;
            for (EntradaHistorial e : listaAtras) {
                boolean esTope = (indice == 0);
                contenedorAtras.add(crearTarjetaHistorial(e, indice, esTope, true));
                contenedorAtras.add(Box.createVerticalStrut(8));
                indice++;
            }
        }

        // 5. Renderizar Pila Adelante
        contenedorAdelante.removeAll();
        List<EntradaHistorial> listaAdelante = servicio.obtenerListaAdelante();
        lblContadorAdelante.setText(listaAdelante.size() + " disponibles");

        if (listaAdelante.isEmpty()) {
            contenedorAdelante.add(crearMensajeVacio("Pila vacía. Presiona 'Atrás' para habilitar páginas adelante."));
        } else {
            int indice = 0;
            for (EntradaHistorial e : listaAdelante) {
                contenedorAdelante.add(crearTarjetaHistorial(e, indice, false, false));
                contenedorAdelante.add(Box.createVerticalStrut(8));
                indice++;
            }
        }

        revalidate();
        repaint();
    }

    private JPanel crearTarjetaHistorial(EntradaHistorial entrada, int posicion, boolean esTope, boolean esPilaAtras) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 0));
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(esTope ? Paleta.BOTON_ACTIVO : Paleta.BORDE, esTope ? 2 : 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        // Icono + Textos
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelInfo.setBackground(Paleta.TARJETA);

        JLabel lblIcono = new JLabel(entrada.getIcono());
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(Paleta.TARJETA);

        String titulo = (esTope ? "★ [TOPE] " : "[" + posicion + "] ") + entrada.getDestino();
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", esTope ? Font.BOLD : Font.PLAIN, 13));
        lblTitulo.setForeground(Paleta.TEXTO);

        JLabel lblSub = new JLabel(entrada.getTipo() + " • " + entrada.getHoraVisita());
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(Paleta.TEXTO_SECUNDARIO);

        panelTextos.add(lblTitulo);
        panelTextos.add(lblSub);

        panelInfo.add(lblIcono);
        panelInfo.add(panelTextos);

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        // Botón rápido para saltar a esta entrada
        if (!esTope) {
            JButton btnIr = PanelPublicaciones.crearBoton("Ir");
            btnIr.setFont(new Font("SansSerif", Font.PLAIN, 11));
            btnIr.addActionListener(ev -> {
                servicio.visitar(entrada.getDestino(), entrada.getTipo());
                actualizarTodo();
            });
            tarjeta.add(btnIr, BorderLayout.EAST);
        }

        return tarjeta;
    }

    private JPanel crearMensajeVacio(String texto) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(Paleta.FONDO);
        JLabel lbl = new JLabel("<html><body style='text-align:center;width:280px;'>" + texto + "</body></html>");
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lbl.setForeground(Paleta.TEXTO_SECUNDARIO);
        p.add(lbl);
        return p;
    }

    private JScrollPane crearScroll(JPanel contenedor) {
        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private void ejecutarNavegacion(String tipo) {
        String texto = txtBarraDireccion.getText().trim();
        if (texto.isEmpty()) {
            return;
        }
        servicio.visitar(texto, tipo);
        actualizarTodo();
    }

    private JButton crearBotonChip(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Paleta.BOTON);
        btn.setForeground(Paleta.TEXTO_SECUNDARIO);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Paleta.BOTON_ACTIVO);
                btn.setForeground(Paleta.TEXTO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Paleta.BOTON);
                btn.setForeground(Paleta.TEXTO_SECUNDARIO);
            }
        });
        return btn;
    }
}
