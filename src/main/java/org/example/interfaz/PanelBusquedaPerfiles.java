package org.example.interfaz;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.busqueda.CatalogoUsuarios;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelBusquedaPerfiles extends JPanel {

    private final ListaEnlazada<CatalogoUsuarios.Usuario> listaUsuarios;
    private final JTextField campoBusqueda;
    private final JComboBox<String> comboCriterio;
    private final JTextArea areaResultados;
    private final JButton botonPrecargar;
    private final JButton botonOrdenarId;
    private final JButton botonOrdenarAlfabeto;
    private final JButton botonBuscar;
    private final JButton botonEliminar;
    private final JButton botonVolver;

    public PanelBusquedaPerfiles(Runnable alVolver) {
        this.listaUsuarios = new ListaEnlazada<>();

        setLayout(new BorderLayout(15, 15));
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(28, 32, 32, 32));

        // --- Panel Superior ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JLabel etiquetaTitulo = new JLabel("Búsqueda y Gestión de Perfiles (ID y Nombre)");
        etiquetaTitulo.setForeground(Paleta.TEXTO);
        etiquetaTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        panelSuperior.add(etiquetaTitulo, BorderLayout.WEST);

        botonVolver = new JButton("Volver");
        estiloBoton(botonVolver);
        botonVolver.addActionListener(e -> alVolver.run());
        
        JPanel panelBotonSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonSur.setOpaque(false);
        panelBotonSur.add(botonVolver);
        panelSuperior.add(panelBotonSur, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central ---
        JPanel panelCentral = new JPanel(new BorderLayout(12, 12));
        panelCentral.setBackground(Paleta.TARJETA);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(20, 20, 20, 20)));

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControles.setOpaque(false);

        JLabel etiquetaBuscar = new JLabel("Valor:");
        etiquetaBuscar.setForeground(Paleta.TEXTO);
        etiquetaBuscar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        campoBusqueda = new JTextField(12);
        campoBusqueda.setBackground(Paleta.FONDO);
        campoBusqueda.setForeground(Paleta.TEXTO);
        campoBusqueda.setCaretColor(Paleta.TEXTO);
        campoBusqueda.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(6, 8, 6, 8)));

        comboCriterio = new JComboBox<>(new String[]{"Buscar/Eliminar por ID", "Buscar/Eliminar por Nombre"});
        comboCriterio.setBackground(Paleta.FONDO);
        comboCriterio.setForeground(Paleta.TEXTO);
        comboCriterio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

        botonPrecargar = new JButton("Precargar");
        estiloBoton(botonPrecargar);

        botonOrdenarId = new JButton("Ordenar (ID)");
        estiloBoton(botonOrdenarId);

        botonOrdenarAlfabeto = new JButton("Ordenar (Alfabeto)");
        estiloBoton(botonOrdenarAlfabeto);

        botonBuscar = new JButton("Buscar (Binaria)");
        estiloBoton(botonBuscar);

        botonEliminar = new JButton("Eliminar");
        estiloBoton(botonEliminar);

        panelControles.add(etiquetaBuscar);
        panelControles.add(campoBusqueda);
        panelControles.add(comboCriterio);
        panelControles.add(botonPrecargar);
        panelControles.add(botonOrdenarId);
        panelControles.add(botonOrdenarAlfabeto);
        panelControles.add(botonBuscar);
        panelControles.add(botonEliminar);

        panelCentral.add(panelControles, BorderLayout.NORTH);

        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setBackground(Paleta.FONDO);
        areaResultados.setForeground(Paleta.TEXTO);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        areaResultados.setEditable(false);
        areaResultados.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(areaResultados);
        scrollPane.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        scrollPane.getViewport().setBackground(Paleta.FONDO);

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        areaResultados.setText(">> Catálogo vacío. Haga clic en 'Precargar' para cargar los 50 usuarios de forma desordenada.\n");

        // --- ACCIONES ---
        botonPrecargar.addActionListener(e -> {
            if (listaUsuarios.tamano() > 0) {
                areaResultados.setText(">> Los usuarios ya se encuentran precargados.\n");
                mostrarCatalogoActual();
                return;
            }
            CatalogoUsuarios.precargarUsuarios(listaUsuarios);
            areaResultados.setText(">> ¡50 usuarios precargados de forma DESORDENADA!\n");
            mostrarCatalogoActual();
        });

        botonOrdenarId.addActionListener(e -> {
            if (listaUsuarios.tamano() == 0) {
                areaResultados.setText(">> El catálogo está vacío.\n");
                return;
            }
            CatalogoUsuarios.Usuario[] arreglo = CatalogoUsuarios.aArregloUsuarios(listaUsuarios);
            CatalogoUsuarios.quicksort(arreglo, 0, arreglo.length - 1, "ID");
            
            areaResultados.setText(">> Catálogo ordenado por ID mediante Quicksort:\n");
            for (CatalogoUsuarios.Usuario u : arreglo) {
                areaResultados.append("   - " + u.toString() + "\n");
            }
        });

        botonOrdenarAlfabeto.addActionListener(e -> {
            if (listaUsuarios.tamano() == 0) {
                areaResultados.setText(">> El catálogo está vacío.\n");
                return;
            }
            CatalogoUsuarios.Usuario[] arreglo = CatalogoUsuarios.aArregloUsuarios(listaUsuarios);
            CatalogoUsuarios.quicksort(arreglo, 0, arreglo.length - 1, "ALFABETO");
            
            areaResultados.setText(">> Catálogo ordenado alfabéticamente por Nombre mediante Quicksort:\n");
            for (CatalogoUsuarios.Usuario u : arreglo) {
                areaResultados.append("   - " + u.toString() + "\n");
            }
        });

        botonBuscar.addActionListener(e -> {
            String textoBuscado = campoBusqueda.getText().trim();
            if (textoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (listaUsuarios.tamano() == 0) {
                areaResultados.setText(">> El catálogo está vacío.\n");
                return;
            }

            CatalogoUsuarios.Usuario[] arreglo = CatalogoUsuarios.aArregloUsuarios(listaUsuarios);
            int indice = -1;
            String criterioSeleccionado = (String) comboCriterio.getSelectedItem();

            if (criterioSeleccionado.contains("ID")) {
                CatalogoUsuarios.quicksort(arreglo, 0, arreglo.length - 1, "ID");
                indice = CatalogoUsuarios.busquedaBinariaPorId(arreglo, textoBuscado);
            } else {
                CatalogoUsuarios.quicksort(arreglo, 0, arreglo.length - 1, "ALFABETO");
                indice = CatalogoUsuarios.busquedaBinariaPorNombre(arreglo, textoBuscado);
            }

            if (indice != -1) {
                CatalogoUsuarios.Usuario u = arreglo[indice];
                areaResultados.setText(">> ¡Usuario encontrado mediante Búsqueda Binaria!\n");
                areaResultados.append("   - Posición en arreglo ordenado: " + indice + "\n");
                areaResultados.append("   - Datos: " + u.toString() + "\n");
            } else {
                areaResultados.setText(">> No se encontró ningún resultado para '" + textoBuscado + "'.\n");
            }
        });

        botonEliminar.addActionListener(e -> {
            String textoBuscado = campoBusqueda.getText().trim();
            if (textoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el valor que desea eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (listaUsuarios.tamano() == 0) {
                areaResultados.setText(">> El catálogo está vacío.\n");
                return;
            }

            boolean eliminado = false;
            String criterioSeleccionado = (String) comboCriterio.getSelectedItem();

            if (criterioSeleccionado.contains("ID")) {
                eliminado = CatalogoUsuarios.eliminarUsuarioPorId(listaUsuarios, textoBuscado);
            } else {
                eliminado = CatalogoUsuarios.eliminarUsuarioPorNombre(listaUsuarios, textoBuscado);
            }

            if (eliminado) {
                areaResultados.setText(">> Registro '" + textoBuscado + "' eliminado exitosamente.\n");
                campoBusqueda.setText("");
                mostrarCatalogoActual();
            } else {
                areaResultados.setText(">> No se pudo eliminar: No existe el registro '" + textoBuscado + "'.\n");
            }
        });
    }

    private void mostrarCatalogoActual() {
        areaResultados.append(">> Estado actual del catálogo (" + listaUsuarios.tamano() + " usuarios):\n");
        for (CatalogoUsuarios.Usuario u : listaUsuarios) {
            areaResultados.append("   - " + u.toString() + "\n");
        }
    }

    private static void estiloBoton(JButton boton) {
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(8, 12, 8, 12)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
}