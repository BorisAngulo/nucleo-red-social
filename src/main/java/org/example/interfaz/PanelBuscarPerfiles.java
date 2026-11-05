package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.example.redsocial.perfiles.ArregloPerfiles;
import org.example.redsocial.perfiles.PerfilUsuario;
import org.example.redsocial.perfiles.ResultadoBusquedaBinaria;

/**
 * Pantalla para la funcionalidad "Buscar perfiles":
 * Administra un arreglo ordenado de usuarios en memoria y ejecuta búsqueda binaria en O(log n).
 */
public class PanelBuscarPerfiles extends JPanel {

    private final Runnable alVolver;
    private final ArregloPerfiles catalogo = ArregloPerfiles.ejemplo();

    private final JTextField campoBusqueda = new JTextField(12);
    private final JComboBox<PerfilUsuario> comboRapido = new JComboBox<>();

    private final JTextField campoNuevoUser = new JTextField(8);
    private final JTextField campoNuevoNombre = new JTextField(12);
    private final JTextField campoNuevoBio = new JTextField(14);

    private final JTextArea visualizacionArreglo = new JTextArea();
    private final JTextArea visualizacionResultado = new JTextArea();

    public PanelBuscarPerfiles(Runnable alVolver) {
        this.alVolver = alVolver;
        prepararTextAreas();
        armar();
        actualizar();
        campoBusqueda.setText("carlos");
        ejecutarBusqueda();
    }

    private void prepararTextAreas() {
        configurarTextArea(visualizacionArreglo);
        configurarTextArea(visualizacionResultado);
    }

    private void configurarTextArea(JTextArea area) {
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
        area.setBackground(Paleta.TARJETA);
        area.setForeground(Paleta.TEXTO);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        area.setLineWrap(false);
    }

    private void armar() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Paleta.FONDO);

        contenido.add(texto("Matriz y arreglo", 18, Paleta.TEXTO_SECUNDARIO));
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(texto("Buscar perfiles", 34, Paleta.TEXTO));
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(texto("Arreglo de usuarios ordenado, con búsqueda binaria O(log n).",
                20, Paleta.TEXTO_SECUNDARIO));
        contenido.add(Box.createVerticalStrut(18));

        contenido.add(filaBusqueda());
        contenido.add(Box.createVerticalStrut(12));
        contenido.add(filaNuevoPerfil());
        contenido.add(Box.createVerticalStrut(16));

        JPanel panelesCentrales = new JPanel(new GridLayout(1, 2, 14, 0));
        panelesCentrales.setBackground(Paleta.FONDO);
        panelesCentrales.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelesCentrales.add(seccionTarjeta("Arreglo ordenado en memoria", visualizacionArreglo));
        panelesCentrales.add(seccionTarjeta("Resultado y traza de búsqueda binaria", visualizacionResultado));

        contenido.add(panelesCentrales);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        add(scroll, BorderLayout.CENTER);

        JButton volver = boton("Volver");
        volver.addActionListener(evento -> alVolver.run());
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Paleta.FONDO);
        pie.add(volver, BorderLayout.WEST);
        add(pie, BorderLayout.SOUTH);
    }

    private JPanel filaBusqueda() {
        JPanel fila = fila();
        estiloCampoTexto(campoBusqueda);
        campoBusqueda.addActionListener(evento -> ejecutarBusqueda());

        JButton botonBuscar = boton("Buscar con Búsqueda Binaria");
        botonBuscar.addActionListener(evento -> ejecutarBusqueda());

        estiloCombo(comboRapido);
        comboRapido.addActionListener(evento -> {
            PerfilUsuario seleccionado = (PerfilUsuario) comboRapido.getSelectedItem();
            if (seleccionado != null) {
                campoBusqueda.setText(seleccionado.getNombreUsuario());
            }
        });

        JButton botonCargarRapido = boton("Seleccionar");
        botonCargarRapido.addActionListener(evento -> {
            PerfilUsuario seleccionado = (PerfilUsuario) comboRapido.getSelectedItem();
            if (seleccionado != null) {
                campoBusqueda.setText(seleccionado.getNombreUsuario());
                ejecutarBusqueda();
            }
        });

        fila.add(texto("Buscar usuario:", 18, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(8));
        fila.add(campoBusqueda);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(botonBuscar);
        fila.add(Box.createHorizontalStrut(16));
        fila.add(texto("Prueba rápida:", 18, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(6));
        fila.add(comboRapido);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(botonCargarRapido);
        return fila;
    }

    private JPanel filaNuevoPerfil() {
        JPanel fila = fila();
        estiloCampoTexto(campoNuevoUser);
        estiloCampoTexto(campoNuevoNombre);
        estiloCampoTexto(campoNuevoBio);

        JButton botonAgregar = boton("Insertar perfil ordenado");
        botonAgregar.addActionListener(evento -> agregarPerfil());

        JButton botonEliminar = boton("Eliminar usuario buscado");
        botonEliminar.addActionListener(evento -> eliminarPerfil());

        fila.add(texto("Nuevo @usuario:", 17, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(6));
        fila.add(campoNuevoUser);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(texto("Nombre:", 17, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(6));
        fila.add(campoNuevoNombre);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(texto("Bio:", 17, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(6));
        fila.add(campoNuevoBio);
        fila.add(Box.createHorizontalStrut(10));
        fila.add(botonAgregar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(botonEliminar);
        return fila;
    }

    private JPanel seccionTarjeta(String titulo, JTextArea areaTexto) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel etiquetaTitulo = texto(titulo, 20, Paleta.TEXTO);
        tarjeta.add(etiquetaTitulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        scroll.getViewport().setBackground(Paleta.TARJETA);
        scroll.setPreferredSize(new Dimension(420, 360));
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private void ejecutarBusqueda() {
        String objetivo = campoBusqueda.getText();
        if (objetivo == null || objetivo.isBlank()) {
            mostrarAviso("Escribe un nombre de usuario para buscar.");
            return;
        }

        ResultadoBusquedaBinaria resultado = catalogo.buscarBinario(objetivo);
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        if (resultado.encontrado()) {
            PerfilUsuario perfil = resultado.getPerfil();
            sb.append("✓ ¡PERFIL ENCONTRADO EN EL ÍNDICE [")
                    .append(resultado.getPosicion()).append("]!\n");
            sb.append("\n\n");
            sb.append("INFORMACIÓN DEL PERFIL:\n");
            sb.append("  • Usuario:      @").append(perfil.getNombreUsuario()).append("\n");
            sb.append("  • Nombre:       ").append(perfil.getNombreCompleto()).append("\n");
            sb.append("  • Biografía:    ").append(perfil.getBiografia()).append("\n");
            sb.append("  • Estadísticas: ").append(perfil.getSeguidores()).append(" seguidores  |  ")
                    .append(perfil.getSeguidos()).append(" seguidos\n\n");
        } else {
            sb.append("✗ PERFIL NO ENCONTRADO (@").append(objetivo.trim()).append(")\n");
            sb.append("\n\n");
            sb.append("El usuario no existe en la red social.\n");
            sb.append("Punto de inserción ordenado: índice [")
                    .append(resultado.getPuntoInsercion()).append("].\n\n");
        }

        sb.append("\n");
        sb.append("ANÁLISIS ALGORÍTMICO (BÚSQUEDA BINARIA):\n");
        sb.append("  • Elementos en el arreglo (n):  ").append(catalogo.getCantidad()).append("\n");
        sb.append("  • Comparaciones realizadas:     ").append(resultado.getComparaciones()).append("\n");
        sb.append("  • Complejidad temporal:         O(log₂ n)\n");
        int maxComparaciones = catalogo.estaVacio() ? 0 : (int) Math.ceil(Math.log(catalogo.getCantidad()) / Math.log(2)) + 1;
        sb.append("  • Peor caso teórico (log₂ n):   <= ").append(maxComparaciones).append(" comparaciones\n");
        sb.append("  • Ahorro vs Búsqueda lineal:    ")
                .append(catalogo.getCantidad() - resultado.getComparaciones())
                .append(" comparaciones menos\n");
        sb.append("\n\n");

        sb.append("TRAZA PASO A PASO:\n");
        for (String paso : resultado.getTraza()) {
            sb.append("  ").append(paso).append("\n\n");
        }

        visualizacionResultado.setText(sb.toString());
        visualizacionResultado.setCaretPosition(0);
    }

    private void agregarPerfil() {
        String usuario = campoNuevoUser.getText();
        String nombre = campoNuevoNombre.getText();
        String bio = campoNuevoBio.getText();

        if (usuario == null || usuario.isBlank() || nombre == null || nombre.isBlank()) {
            mostrarAviso("Debes ingresar al menos el usuario y el nombre completo.");
            return;
        }

        try {
            PerfilUsuario nuevo = new PerfilUsuario(usuario, nombre, bio, 0, 0);
            catalogo.insertar(nuevo);
            campoNuevoUser.setText("");
            campoNuevoNombre.setText("");
            campoNuevoBio.setText("");
            actualizar();
            campoBusqueda.setText(nuevo.getNombreUsuario());
            ejecutarBusqueda();
        } catch (IllegalArgumentException ex) {
            mostrarAviso(ex.getMessage());
        }
    }

    private void eliminarPerfil() {
        String objetivo = campoBusqueda.getText();
        if (objetivo == null || objetivo.isBlank()) {
            mostrarAviso("Escribe el nombre de usuario a eliminar.");
            return;
        }

        boolean eliminado = catalogo.eliminar(objetivo);
        if (eliminado) {
            actualizar();
            visualizacionResultado.setText("El perfil @" + objetivo.trim() + " fue eliminado del arreglo ordenado.");
        } else {
            mostrarAviso("No se encontró el perfil @" + objetivo.trim() + " para eliminar.");
        }
    }

    private void actualizar() {
        PerfilUsuario[] perfiles = catalogo.getPerfiles();
        comboRapido.setModel(new DefaultComboBoxModel<>(perfiles));

        StringBuilder sb = new StringBuilder();
        sb.append("ESTRUCTURA DE DATOS: ARREGLO CONTIGUO ORDENADO\n");
        sb.append("Cantidad de usuarios (n): ").append(catalogo.getCantidad())
                .append("  |  Capacidad física: ").append(catalogo.getCapacidad()).append("\n");
        sb.append("Invariante: Orden alfabético ascendente por @nombreUsuario\n");
        sb.append("\n");
        sb.append(String.format("%-8s %-14s %-20s %-10s%n", "ÍNDICE", "USUARIO", "NOMBRE COMPLETO", "SEGUIDORES"));
        sb.append("\n");

        for (int i = 0; i < perfiles.length; i++) {
            PerfilUsuario p = perfiles[i];
            sb.append(String.format("[%02d]     @%-12.12s %-20.20s %-10d%n",
                    i, p.getNombreUsuario(), p.getNombreCompleto(), p.getSeguidores()));
        }

        if (perfiles.length == 0) {
            sb.append("(El arreglo está actualmente vacío)\n");
        }

        visualizacionArreglo.setText(sb.toString());
        visualizacionArreglo.setCaretPosition(0);
    }

    private JPanel fila() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Paleta.FONDO);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void estiloCampoTexto(JTextField campo) {
        campo.setBackground(Paleta.BOTON);
        campo.setForeground(Paleta.TEXTO);
        campo.setCaretColor(Paleta.TEXTO);
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), new EmptyBorder(5, 8, 5, 8)));
    }

    private void estiloCombo(JComboBox<?> combo) {
        combo.setBackground(Paleta.BOTON);
        combo.setForeground(Paleta.TEXTO);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    }

    private JButton boton(String titulo) {
        JButton boton = new JButton(titulo);
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), new EmptyBorder(8, 12, 8, 12)));
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
        return boton;
    }

    private JLabel texto(String contenido, int tamano, java.awt.Color color) {
        JLabel etiqueta = new JLabel(contenido);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, tamano));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Buscar perfiles", JOptionPane.INFORMATION_MESSAGE);
    }
}
