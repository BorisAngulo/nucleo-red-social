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
 * Pantalla interactiva del Historial de Navegación.
 * Diseñada de forma visual, limpia y muy fácil de entender.
 */
public class PanelHistorialNavegacion extends JPanel {

    private final Runnable alVolver;
    private final HistorialNavegacionService servicio = new HistorialNavegacionService();

    // Botones principales
    private final JButton btnAtras = PanelPublicaciones.crearBoton("◀ Atrás");
    private final JButton btnAdelante = PanelPublicaciones.crearBoton("▶ Adelante");
    private final JTextField txtBarra = new JTextField();

    // Contenedores visuales
    private final JPanel tarjetaActual = new JPanel(new BorderLayout(14, 0));
    private final JPanel contenedorAtras = new JPanel();
    private final JPanel contenedorAdelante = new JPanel();
    private final JLabel lblTotalAtras = new JLabel();
    private final JLabel lblTotalAdelante = new JLabel();

    public PanelHistorialNavegacion(Runnable alVolver) {
        this.alVolver = alVolver;

        setLayout(new BorderLayout(0, 16));
        setBackground(Paleta.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearCuerpoCentral(), BorderLayout.CENTER);

        actualizarTodo();
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.Y_AXIS));
        barra.setBackground(Paleta.FONDO);

        // Fila 1: Volver + Título
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        fila1.setBackground(Paleta.FONDO);

        JButton btnVolver = PanelPublicaciones.crearBoton("← Volver al Menú");
        btnVolver.addActionListener(e -> {
            if (alVolver != null) alVolver.run();
        });

        JLabel lblTitulo = new JLabel("Historial de Navegación");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Paleta.TEXTO);

        JLabel lblSub = new JLabel("(Ejemplo práctico de Pila: lo último que visitas queda arriba)");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Paleta.TEXTO_SECUNDARIO);

        fila1.add(btnVolver);
        fila1.add(lblTitulo);
        fila1.add(lblSub);

        // Fila 2: Barra tipo navegador
        JPanel fila2 = new JPanel(new BorderLayout(8, 0));
        fila2.setBackground(Paleta.FONDO);
        fila2.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JPanel navBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        navBotones.setBackground(Paleta.FONDO);

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

        navBotones.add(btnAtras);
        navBotones.add(btnAdelante);

        // Input de dirección / búsqueda
        txtBarra.setBackground(Paleta.TARJETA);
        txtBarra.setForeground(Paleta.TEXTO);
        txtBarra.setCaretColor(Paleta.TEXTO);
        txtBarra.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBarra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE, 1),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        txtBarra.addActionListener(e -> navegar("Página"));

        // Botones de acción
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        acciones.setBackground(Paleta.FONDO);

        JButton btnIr = PanelPublicaciones.crearBoton("🔍 Buscar o Ir");
        btnIr.addActionListener(e -> navegar("Búsqueda"));

        JButton btnLimpiar = PanelPublicaciones.crearBoton("🗑 Borrar todo");
        btnLimpiar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Deseas vaciar todo el historial de páginas?",
                    "Borrar Historial", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                servicio.vaciarHistorial();
                actualizarTodo();
            }
        });

        acciones.add(btnIr);
        acciones.add(btnLimpiar);

        fila2.add(navBotones, BorderLayout.WEST);
        fila2.add(txtBarra, BorderLayout.CENTER);
        fila2.add(acciones, BorderLayout.EAST);

        // Fila 3: Atajos fáciles con un solo clic
        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        fila3.setBackground(Paleta.FONDO);

        JLabel lblClicks = new JLabel("Páginas de ejemplo (haz clic para probar):");
        lblClicks.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblClicks.setForeground(Paleta.TEXTO_SECUNDARIO);
        fila3.add(lblClicks);

        String[][] ejemplos = {
            {"🏠 Inicio / Feed", "redsocial.com/feed", "Página"},
            {"👤 Perfil de @boris", "boris_angulo", "Perfil"},
            {"👤 Perfil de @adrian", "adrian_martinez", "Perfil"},
            {"🔥 #tendencias", "#tendencias_java", "Tendencia"},
            {"🔍 Buscar amigos", "Buscar amigos de la universidad", "Búsqueda"}
        };

        for (String[] ej : ejemplos) {
            JButton chip = crearChip(ej[0]);
            chip.addActionListener(e -> {
                servicio.visitar(ej[1], ej[2]);
                actualizarTodo();
            });
            fila3.add(chip);
        }

        barra.add(fila1);
        barra.add(fila2);
        barra.add(fila3);

        return barra;
    }

    private JPanel crearCuerpoCentral() {
        JPanel central = new JPanel(new BorderLayout(0, 14));
        central.setBackground(Paleta.FONDO);

        // 1. Tarjeta superior: ¿Dónde estás parado ahora?
        tarjetaActual.setBackground(Paleta.TARJETA);
        tarjetaActual.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BOTON_ACTIVO, 2),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        central.add(tarjetaActual, BorderLayout.NORTH);

        // 2. Dos columnas fáciles de entender
        JPanel columnas = new JPanel(new GridLayout(1, 2, 18, 0));
        columnas.setBackground(Paleta.FONDO);

        // Columna Izquierda: Historial donde puedes ir con "Atrás"
        JPanel colAtras = new JPanel(new BorderLayout(0, 8));
        colAtras.setBackground(Paleta.FONDO);

        JPanel headAtras = new JPanel(new BorderLayout());
        headAtras.setBackground(Paleta.FONDO);
        JLabel titAtras = new JLabel("⬅️ Historial de páginas visitadas");
        titAtras.setFont(new Font("SansSerif", Font.BOLD, 15));
        titAtras.setForeground(Paleta.TEXTO);
        lblTotalAtras.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTotalAtras.setForeground(Paleta.TEXTO_SECUNDARIO);
        headAtras.add(titAtras, BorderLayout.WEST);
        headAtras.add(lblTotalAtras, BorderLayout.EAST);

        contenedorAtras.setLayout(new BoxLayout(contenedorAtras, BoxLayout.Y_AXIS));
        contenedorAtras.setBackground(Paleta.FONDO);
        JScrollPane scrollAtras = crearScroll(contenedorAtras);

        colAtras.add(headAtras, BorderLayout.NORTH);
        colAtras.add(scrollAtras, BorderLayout.CENTER);

        // Columna Derecha: Páginas a donde puedes ir con "Adelante"
        JPanel colAdelante = new JPanel(new BorderLayout(0, 8));
        colAdelante.setBackground(Paleta.FONDO);

        JPanel headAdelante = new JPanel(new BorderLayout());
        headAdelante.setBackground(Paleta.FONDO);
        JLabel titAdelante = new JLabel("➡️ Páginas a donde puedes volver");
        titAdelante.setFont(new Font("SansSerif", Font.BOLD, 15));
        titAdelante.setForeground(Paleta.TEXTO);
        lblTotalAdelante.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTotalAdelante.setForeground(Paleta.TEXTO_SECUNDARIO);
        headAdelante.add(titAdelante, BorderLayout.WEST);
        headAdelante.add(lblTotalAdelante, BorderLayout.EAST);

        contenedorAdelante.setLayout(new BoxLayout(contenedorAdelante, BoxLayout.Y_AXIS));
        contenedorAdelante.setBackground(Paleta.FONDO);
        JScrollPane scrollAdelante = crearScroll(contenedorAdelante);

        colAdelante.add(headAdelante, BorderLayout.NORTH);
        colAdelante.add(scrollAdelante, BorderLayout.CENTER);

        columnas.add(colAtras);
        columnas.add(colAdelante);

        central.add(columnas, BorderLayout.CENTER);

        return central;
    }

    public void actualizarTodo() {
        EntradaHistorial actual = servicio.paginaActual();

        // 1. Poner dirección en la barra
        txtBarra.setText(actual != null ? actual.getUrlCompleta() : "");

        // 2. Activar / Desactivar botones de navegación
        btnAtras.setEnabled(servicio.puedeRetroceder());
        btnAdelante.setEnabled(servicio.puedeAvanzar());

        // 3. Mostrar la tarjeta de la página actual
        tarjetaActual.removeAll();
        if (actual != null) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
            fila.setBackground(Paleta.TARJETA);

            JLabel icono = new JLabel(actual.getIcono());
            icono.setFont(new Font("SansSerif", Font.PLAIN, 28));

            JPanel textos = new JPanel();
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
            textos.setBackground(Paleta.TARJETA);

            JLabel cartel = new JLabel("PÁGINA QUE ESTÁS VIENDO AHORA MISMO (Cima de la Pila):");
            cartel.setFont(new Font("SansSerif", Font.BOLD, 11));
            cartel.setForeground(Paleta.TEXTO_SECUNDARIO);

            JLabel titulo = new JLabel(actual.getDestino());
            titulo.setFont(new Font("SansSerif", Font.BOLD, 17));
            titulo.setForeground(Paleta.TEXTO);

            JLabel hora = new JLabel("Tipo: " + actual.getTipo() + "  |  Hora de visita: " + actual.getHoraVisita());
            hora.setFont(new Font("SansSerif", Font.PLAIN, 12));
            hora.setForeground(Paleta.TEXTO_SECUNDARIO);

            textos.add(cartel);
            textos.add(Box.createVerticalStrut(2));
            textos.add(titulo);
            textos.add(Box.createVerticalStrut(2));
            textos.add(hora);

            fila.add(icono);
            fila.add(textos);
            tarjetaActual.add(fila, BorderLayout.CENTER);
        } else {
            JLabel vacio = new JLabel("Historial vacío. Elige un ejemplo arriba o escribe algo en la barra.", JLabel.CENTER);
            vacio.setForeground(Paleta.TEXTO_SECUNDARIO);
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            tarjetaActual.add(vacio, BorderLayout.CENTER);
        }

        // 4. Llenar columna izquierda (Atrás)
        contenedorAtras.removeAll();
        List<EntradaHistorial> listaAtras = servicio.obtenerListaAtras();
        lblTotalAtras.setText(listaAtras.size() + " páginas guardadas");

        if (listaAtras.isEmpty()) {
            contenedorAtras.add(crearAvisoVacio("No hay páginas en el historial."));
        } else {
            int orden = 1;
            for (EntradaHistorial e : listaAtras) {
                boolean esActual = (orden == 1);
                contenedorAtras.add(crearFilaHistorial(e, orden, esActual));
                contenedorAtras.add(Box.createVerticalStrut(6));
                orden++;
            }
        }

        // 5. Llenar columna derecha (Adelante)
        contenedorAdelante.removeAll();
        List<EntradaHistorial> listaAdelante = servicio.obtenerListaAdelante();
        lblTotalAdelante.setText(listaAdelante.size() + " disponibles");

        if (listaAdelante.isEmpty()) {
            contenedorAdelante.add(crearAvisoVacio("Está vacía porque estás en la página más reciente.\nSi le das al botón '◀ Atrás', aquí aparecerán las páginas para volver hacia adelante."));
        } else {
            int orden = 1;
            for (EntradaHistorial e : listaAdelante) {
                contenedorAdelante.add(crearFilaHistorial(e, orden, false));
                contenedorAdelante.add(Box.createVerticalStrut(6));
                orden++;
            }
        }

        revalidate();
        repaint();
    }

    private JPanel crearFilaHistorial(EntradaHistorial entrada, int posicion, boolean esActual) {
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setBackground(Paleta.TARJETA);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(esActual ? Paleta.BOTON_ACTIVO : Paleta.BORDE, esActual ? 2 : 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setBackground(Paleta.TARJETA);

        String textoTitulo = (esActual ? "★ ESTÁS AQUÍ: " : (posicion) + ". ") + entrada.getDestino();
        JLabel lblNom = new JLabel(textoTitulo);
        lblNom.setFont(new Font("SansSerif", esActual ? Font.BOLD : Font.PLAIN, 13));
        lblNom.setForeground(Paleta.TEXTO);

        JLabel lblDet = new JLabel(entrada.getIcono() + " " + entrada.getTipo() + " • " + entrada.getHoraVisita());
        lblDet.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDet.setForeground(Paleta.TEXTO_SECUNDARIO);

        panelTexto.add(lblNom);
        panelTexto.add(lblDet);

        fila.add(panelTexto, BorderLayout.CENTER);

        if (!esActual) {
            JButton btnSaltar = PanelPublicaciones.crearBoton("Ir a esta");
            btnSaltar.setFont(new Font("SansSerif", Font.PLAIN, 11));
            btnSaltar.addActionListener(e -> {
                servicio.visitar(entrada.getDestino(), entrada.getTipo());
                actualizarTodo();
            });
            fila.add(btnSaltar, BorderLayout.EAST);
        }

        return fila;
    }

    private JPanel crearAvisoVacio(String mensaje) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(Paleta.FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JLabel lbl = new JLabel("<html><body style='text-align:center;width:260px;'>" + mensaje + "</body></html>");
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lbl.setForeground(Paleta.TEXTO_SECUNDARIO);
        p.add(lbl);
        return p;
    }

    private JScrollPane crearScroll(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private void navegar(String tipo) {
        String texto = txtBarra.getText().trim();
        if (!texto.isEmpty()) {
            servicio.visitar(texto, tipo);
            actualizarTodo();
        }
    }

    private JButton crearChip(String texto) {
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
