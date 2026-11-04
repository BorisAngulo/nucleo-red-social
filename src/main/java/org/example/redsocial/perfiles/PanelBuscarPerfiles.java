package org.example.redsocial.perfiles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.example.interfaz.Paleta;
import org.example.redsocial.separacion.RedAmistades;

/** Interfaz de presentación para ordenar y buscar perfiles mediante arreglo fijo. */
public final class PanelBuscarPerfiles extends JPanel {

    private final Runnable alCerrar;
    private final Perfil[] perfiles;
    private final JComboBox<String> criterioBusqueda = new JComboBox<>(new String[]{"ID", "Nombre"});
    private final JComboBox<String> criterioOrden = new JComboBox<>(new String[]{"ID (menor a mayor)", "Nombre (A–Z)"});
    private final JTextField entrada = new JTextField();
    private final JLabel etiquetaTiempo = new JLabel("—");
    private final JLabel etiquetaEstado = new JLabel("Perfiles cargados: 0");
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre del perfil", "Tiempo de búsqueda (s)"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelBuscarPerfiles(Runnable alCerrar) {
        this.alCerrar = alCerrar == null ? () -> { } : alCerrar;
        this.perfiles = new ArregloPerfiles(RedAmistades.ejemplo().getUsuarios()).copiarOcupados();
        configurarEstilo();
        construirInterfaz();
        mostrarOrdenado();
    }

    private void configurarEstilo() {
        setBackground(Paleta.FONDO);
        tabla.setBackground(Paleta.TARJETA);
        tabla.setForeground(Paleta.TEXTO);
        tabla.setGridColor(Paleta.BORDE);
        tabla.setSelectionBackground(Paleta.BOTON_ACTIVO);
        tabla.setSelectionForeground(Paleta.TEXTO);
        tabla.setRowHeight(34);
        tabla.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(Paleta.BOTON);
        encabezado.setForeground(Paleta.TEXTO);
        encabezado.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        encabezado.setPreferredSize(new Dimension(0, 38));
        encabezado.setReorderingAllowed(false);

        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                    boolean focus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                if (selected) {
                    c.setBackground(Paleta.BOTON_ACTIVO);
                    c.setForeground(Paleta.TEXTO);
                } else {
                    c.setBackground(row % 2 == 0 ? Paleta.TARJETA : Paleta.FONDO);
                    c.setForeground(Paleta.TEXTO);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        renderizador.setHorizontalAlignment(SwingConstants.LEFT);
        tabla.setDefaultRenderer(Object.class, renderizador);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(0).setMaxWidth(150);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(360);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(210);
        tabla.getColumnModel().getColumn(2).setMinWidth(180);

        estiloCampo(entrada);
        estiloCombo(criterioBusqueda);
        estiloCombo(criterioOrden);
        etiquetaTiempo.setForeground(Paleta.TEXTO);
        etiquetaTiempo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        etiquetaEstado.setForeground(Paleta.TEXTO_SECUNDARIO);
        etiquetaEstado.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(0, 18));
        setBorder(BorderFactory.createEmptyBorder(26, 30, 24, 30));

        JPanel zonaSuperior = new JPanel();
        zonaSuperior.setOpaque(false);
        zonaSuperior.setLayout(new BoxLayout(zonaSuperior, BoxLayout.Y_AXIS));

        JPanel tituloPanel = new JPanel(new BorderLayout(12, 0));
        tituloPanel.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = etiqueta("Búsqueda de Perfiles", 28, Paleta.TEXTO, Font.BOLD);
        JLabel subtitulo = etiqueta("Administra la visualización, el orden y la búsqueda de usuarios", 14,
                Paleta.TEXTO_SECUNDARIO, Font.PLAIN);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(subtitulo);
        tituloPanel.add(textos, BorderLayout.CENTER);
        JLabel etiquetaModulo = new JLabel("MÓDULO DE PERFILES");
        etiquetaModulo.setForeground(Paleta.TEXTO_SECUNDARIO);
        etiquetaModulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        etiquetaModulo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), BorderFactory.createEmptyBorder(8, 11, 8, 11)));
        tituloPanel.add(etiquetaModulo, BorderLayout.EAST);
        zonaSuperior.add(tituloPanel);
        zonaSuperior.add(Box.createVerticalStrut(20));

        JPanel controles = new JPanel(new GridLayout(1, 2, 14, 0));
        controles.setOpaque(false);
        controles.add(crearTarjetaBusqueda());
        controles.add(crearTarjetaOrdenamiento());
        zonaSuperior.add(controles);
        add(zonaSuperior, BorderLayout.NORTH);

        JPanel panelTabla = crearTarjetaTabla();
        add(panelTabla, BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaBusqueda() {
        JPanel tarjeta = crearTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.add(tituloSeccion("BUSCAR PERFIL", "Encuentra un usuario por identificador o nombre"), BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 8);
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0;
        campos.add(criterioBusqueda, c);
        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 8);
        campos.add(entrada, c);
        c.gridx = 2;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        JButton botonBuscar = crearBotonPrimario("Buscar");
        campos.add(botonBuscar, c);
        tarjeta.add(campos, BorderLayout.CENTER);
        botonBuscar.addActionListener(e -> buscarPerfil());
        entrada.addActionListener(e -> buscarPerfil());
        return tarjeta;
    }

    private JPanel crearTarjetaOrdenamiento() {
        JPanel tarjeta = crearTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.add(tituloSeccion("ORDENAR LISTA", "Selecciona cómo deseas organizar los perfiles"), BorderLayout.NORTH);
        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.setOpaque(false);
        controles.add(criterioOrden, BorderLayout.CENTER);
        JButton botonMostrar = crearBoton("Mostrar ordenado");
        controles.add(botonMostrar, BorderLayout.EAST);
        tarjeta.add(controles, BorderLayout.CENTER);
        botonMostrar.addActionListener(e -> mostrarOrdenado());
        return tarjeta;
    }

    private JPanel crearTarjetaTabla() {
        JPanel tarjeta = crearTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 12));
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(tituloSeccion("LISTA DE PERFILES", "Los resultados se muestran en una tabla unificada de ID y nombre"), BorderLayout.WEST);
        JLabel contador = etiqueta("VISTA GENERAL", 11, Paleta.TEXTO_SECUNDARIO, Font.BOLD);
        encabezado.add(contador, BorderLayout.EAST);
        tarjeta.add(encabezado, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        scroll.getViewport().setBackground(Paleta.TARJETA);
        scroll.setBackground(Paleta.TARJETA);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new BorderLayout(14, 0));
        pie.setOpaque(false);
        JPanel estadoCard = crearIndicador("ESTADO", etiquetaEstado);
        JPanel tiempoCard = crearIndicador("TIEMPO DE BÚSQUEDA", etiquetaTiempo);
        JPanel indicadores = new JPanel(new GridLayout(1, 2, 12, 0));
        indicadores.setOpaque(false);
        indicadores.add(estadoCard);
        indicadores.add(tiempoCard);
        pie.add(indicadores, BorderLayout.CENTER);
        JButton cerrar = crearBoton("Cerrar ventana");
        cerrar.addActionListener(e -> alCerrar.run());
        pie.add(cerrar, BorderLayout.EAST);
        return pie;
    }

    private JPanel crearIndicador(String titulo, JLabel valor) {
        JPanel panel = crearTarjeta();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel etiqueta = etiqueta(titulo, 10, Paleta.TEXTO_SECUNDARIO, Font.BOLD);
        etiqueta.setAlignmentX(LEFT_ALIGNMENT);
        valor.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(etiqueta);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valor);
        return panel;
    }

    private JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(Paleta.TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                BorderFactory.createEmptyBorder(15, 16, 15, 16)));
        return panel;
    }

    private JPanel tituloSeccion(String titulo, String descripcion) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel principal = etiqueta(titulo, 13, Paleta.TEXTO, Font.BOLD);
        JLabel secundaria = etiqueta(descripcion, 12, Paleta.TEXTO_SECUNDARIO, Font.PLAIN);
        principal.setAlignmentX(LEFT_ALIGNMENT);
        secundaria.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(principal);
        panel.add(Box.createVerticalStrut(4));
        panel.add(secundaria);
        return panel;
    }

    private void mostrarOrdenado() {
        Perfil[] ordenados = perfiles.clone();
        if (criterioOrden.getSelectedIndex() == 0) {
            OrdenamientoPerfiles.quickSortPorId(ordenados);
        } else {
            OrdenamientoPerfiles.mergeSortPorNombre(ordenados);
        }
        long[] tiempos = medirTiempoIndividual(ordenados, criterioBusqueda.getSelectedIndex());
        cargarTabla(ordenados, tiempos);
        etiquetaEstado.setText("Perfiles mostrados: " + ordenados.length + " | Orden: " + criterioOrden.getSelectedItem());
        actualizarTiempoTotal(tiempos);
    }

    private void buscarPerfil() {
        String valor = entrada.getText().trim();
        if (valor.isEmpty()) {
            etiquetaEstado.setText("Escribe un ID o nombre para buscar.");
            etiquetaTiempo.setText("Total de la lista: 0.000000000 s");
            entrada.requestFocusInWindow();
            return;
        }

        Perfil[] ordenados = perfiles.clone();
        Perfil encontrado;
        long inicio;
        long fin;
        if (criterioBusqueda.getSelectedIndex() == 0) {
            final int id;
            try {
                id = Integer.parseInt(valor);
            } catch (NumberFormatException ex) {
                etiquetaEstado.setText("El ID debe ser un número entero.");
                etiquetaTiempo.setText("Total de la lista: 0.000000000 s");
                entrada.selectAll();
                entrada.requestFocusInWindow();
                return;
            }
            OrdenamientoPerfiles.quickSortPorId(ordenados);
            inicio = System.nanoTime();
            encontrado = BusquedaBinaria.porId(ordenados, id);
            fin = System.nanoTime();
        } else {
            OrdenamientoPerfiles.mergeSortPorNombre(ordenados);
            inicio = System.nanoTime();
            encontrado = BusquedaBinaria.porNombre(ordenados, valor);
            fin = System.nanoTime();
        }

        long nanosegundos = fin - inicio;
        double milisegundos = nanosegundos / 1_000_000.0;
        long[] tiempoResultado = new long[]{nanosegundos};
        actualizarTiempoTotal(tiempoResultado);

        if (encontrado == null) {
            etiquetaEstado.setText("No se encontró el perfil solicitado.");
            cargarTabla(new Perfil[0]);
            etiquetaTiempo.setText("Total de la lista: 0.000000000 s");
            return;
        }
        cargarTabla(new Perfil[]{encontrado}, new long[]{nanosegundos});
        etiquetaEstado.setText("Perfil encontrado: " + encontrado.getNombre() + " (ID " + encontrado.getId() + ")");
    }

    /** Suma los tiempos individuales mostrados en la tabla y presenta el total en segundos. */
    private void actualizarTiempoTotal(long[] tiempos) {
        long totalNanosegundos = 0L;
        for (long tiempo : tiempos) {
            totalNanosegundos += tiempo;
        }
        double totalSegundos = totalNanosegundos / 1_000_000_000.0;
        etiquetaTiempo.setText(String.format("Total de la lista: %.9f s", totalSegundos));
    }

    /**
     * Mide por separado la búsqueda binaria de cada perfil visible. El ordenamiento
     * se realiza antes de iniciar el cronómetro, para medir solamente la búsqueda.
     */
    private long[] medirTiempoIndividual(Perfil[] perfilesVisibles, int criterio) {
        Perfil[] arregloBusqueda = perfiles.clone();
        if (criterio == 0) {
            OrdenamientoPerfiles.quickSortPorId(arregloBusqueda);
        } else {
            OrdenamientoPerfiles.mergeSortPorNombre(arregloBusqueda);
        }

        long[] tiempos = new long[perfilesVisibles.length];
        for (int i = 0; i < perfilesVisibles.length; i++) {
            Perfil perfil = perfilesVisibles[i];
            long inicio = System.nanoTime();
            if (criterio == 0) {
                BusquedaBinaria.porId(arregloBusqueda, perfil.getId());
            } else {
                BusquedaBinaria.porNombre(arregloBusqueda, perfil.getNombre());
            }
            tiempos[i] = System.nanoTime() - inicio;
        }
        return tiempos;
    }

    private void cargarTabla(Perfil[] datos) {
        cargarTabla(datos, new long[datos.length]);
    }

    private void cargarTabla(Perfil[] datos, long[] tiempos) {
        modelo.setRowCount(0);
        for (int i = 0; i < datos.length; i++) {
            Perfil perfil = datos[i];
            modelo.addRow(new Object[]{perfil.getId(), perfil.getNombre(),
                    i < tiempos.length ? String.format("%.9f", tiempos[i] / 1_000_000_000.0) : "—"});
        }
    }

    private void estiloCampo(JTextField campo) {
        campo.setBackground(Paleta.FONDO);
        campo.setForeground(Paleta.TEXTO);
        campo.setCaretColor(Paleta.TEXTO);
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        campo.setToolTipText("Escribe el valor que deseas buscar");
    }

    private void estiloCombo(JComboBox<String> combo) {
        combo.setBackground(Paleta.BOTON);
        combo.setForeground(Paleta.TEXTO);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        combo.setPreferredSize(new Dimension(150, 38));
    }

    private JButton crearBoton(String texto) {
        JButton boton = crearBotonBase(texto);
        boton.setBackground(Paleta.BOTON);
        return boton;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton boton = crearBotonBase(texto);
        boton.setBackground(Paleta.BOTON_ACTIVO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        return boton;
    }

    private JButton crearBotonBase(String texto) {
        JButton boton = new JButton(texto);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { boton.setBackground(Paleta.BOTON_ACTIVO); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(texto.equals("Buscar") ? Paleta.BOTON_ACTIVO : Paleta.BOTON);
            }
        });
        return boton;
    }

    private JLabel etiqueta(String texto, int tamano, Color color, int estilo) {
        JLabel label = new JLabel(texto);
        label.setForeground(color);
        label.setFont(new Font(Font.SANS_SERIF, estilo, tamano));
        return label;
    }
}
