
package org.example.interfaz;

import org.example.redSocial.EliminarComentarios;
import org.example.redSocial.EliminarComentarios.Comentario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;

public class PanelComentarios extends JPanel {

    private JTree arbolComentarios;
    private DefaultMutableTreeNode raiz;
    private DefaultTreeModel modelo;

    private JTextArea areaResultado;

    private final ArrayList<Comentario> publicaciones =
            new ArrayList<>();

    public PanelComentarios(Runnable alVolver) {

        setLayout(new BorderLayout());
        setBackground(Paleta.FONDO);

        crearDatos();
        crearInterfaz(alVolver);
    }

    // Crear comentarios de ejemplo
    private void crearDatos() {

        Comentario comentario1 =
                EliminarComentarios.crearComentariosEjemplo();

        Comentario comentario2 =
                new Comentario("María: ¡Buenas tardes!");

        Comentario respuesta1 =
                new Comentario("Pedro: Buenas tardes, María.");

        Comentario respuesta2 =
                new Comentario("Carlos: ¿Cómo están?");

        comentario2.agregarRespuesta(respuesta1);
        respuesta1.agregarRespuesta(respuesta2);

        publicaciones.add(comentario1);
        publicaciones.add(comentario2);
    }

    // Construir la interfaz
    private void crearInterfaz(Runnable alVolver) {

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Paleta.FONDO);
        contenido.setBorder(new EmptyBorder(20, 35, 15, 35));

        JLabel titulo = new JLabel("COMENTARIOS ANIDADOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Paleta.TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "Visualiza y administra comentarios y respuestas"
        );
        descripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcion.setForeground(Paleta.TEXTO_SECUNDARIO);
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(8));
        contenido.add(descripcion);
        contenido.add(Box.createVerticalStrut(20));

        // Panel del árbol de comentarios
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(new EmptyBorder(15, 15, 15, 15));
        tarjeta.setMaximumSize(new Dimension(750, 300));

        JLabel tituloArbol = new JLabel("LISTA DE COMENTARIOS");
        tituloArbol.setFont(new Font("Arial", Font.BOLD, 15));
        tituloArbol.setForeground(Paleta.TEXTO);
        tituloArbol.setBorder(new EmptyBorder(0, 0, 12, 0));

        tarjeta.add(tituloArbol, BorderLayout.NORTH);

        crearArbol();

        JScrollPane scrollArbol = new JScrollPane(arbolComentarios);
        scrollArbol.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        scrollArbol.getViewport().setBackground(Paleta.TARJETA);

        tarjeta.add(scrollArbol, BorderLayout.CENTER);

        contenido.add(tarjeta);
        contenido.add(Box.createVerticalStrut(15));

        // Botón para eliminar
        JButton btnEliminar = new JButton(
                "ELIMINAR COMENTARIO Y SUS RESPUESTAS"
        );

        btnEliminar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(150, 45, 45));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(new EmptyBorder(12, 20, 12, 20));
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        contenido.add(btnEliminar);
        contenido.add(Box.createVerticalStrut(15));

        // Área de resultados
        JLabel tituloResultado = new JLabel("REGISTRO DE ELIMINACIÓN");
        tituloResultado.setFont(new Font("Arial", Font.BOLD, 14));
        tituloResultado.setForeground(Paleta.TEXTO);
        tituloResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(tituloResultado);
        contenido.add(Box.createVerticalStrut(8));

        areaResultado = new JTextArea(4, 30);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Arial", Font.PLAIN, 13));
        areaResultado.setForeground(Paleta.TEXTO);
        areaResultado.setBackground(Paleta.TARJETA);
        areaResultado.setBorder(new EmptyBorder(10, 10, 10, 10));
        areaResultado.setText(
                "Selecciona un comentario del árbol para eliminarlo."
        );

        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setMaximumSize(new Dimension(750, 100));
        scrollResultado.setPreferredSize(new Dimension(750, 90));

        contenido.add(scrollResultado);

        add(contenido, BorderLayout.CENTER);

        // Botón volver
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setBackground(Paleta.BOTON);
        btnVolver.setForeground(Paleta.TEXTO);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver.addActionListener(e -> alVolver.run());

        add(btnVolver, BorderLayout.SOUTH);
    }

    // Construir árbol visual de comentarios
    private void crearArbol() {

        raiz = new DefaultMutableTreeNode("Publicaciones");

        for (Comentario comentario : publicaciones) {
            raiz.add(crearNodo(comentario));
        }

        modelo = new DefaultTreeModel(raiz);
        arbolComentarios = new JTree(modelo);

        arbolComentarios.setBackground(Paleta.TARJETA);
        arbolComentarios.setForeground(Paleta.TEXTO);
        arbolComentarios.setFont(new Font("Arial", Font.PLAIN, 14));
        arbolComentarios.setRowHeight(27);
        arbolComentarios.setRootVisible(false);
        arbolComentarios.setShowsRootHandles(true);

        arbolComentarios.setCellRenderer(new javax.swing.tree.DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree,
                    Object value,
                    boolean selected,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus) {

                super.getTreeCellRendererComponent(
                        tree, value, selected, expanded, leaf, row, hasFocus
                );

                setBackgroundNonSelectionColor(Paleta.TARJETA);
                setTextNonSelectionColor(Paleta.TEXTO);
                setBackgroundSelectionColor(Paleta.BOTON_HOVER);
                setTextSelectionColor(Color.WHITE);

                return this;
            }
        });

        arbolComentarios.expandRow(0);
    }

    // Convertir comentario y sus respuestas en nodos
    private DefaultMutableTreeNode crearNodo(Comentario comentario) {

        DefaultMutableTreeNode nodo =
                new DefaultMutableTreeNode(comentario);

        for (Comentario respuesta : comentario.getRespuestas()) {
            nodo.add(crearNodo(respuesta));
        }

        return nodo;
    }

    // Eliminar comentario seleccionado
    private void eliminarSeleccionado() {

        TreePath seleccion = arbolComentarios.getSelectionPath();

        if (seleccion == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero selecciona un comentario.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        DefaultMutableTreeNode nodo =
                (DefaultMutableTreeNode) seleccion.getLastPathComponent();

        if (nodo == raiz) {
            return;
        }

        Comentario comentario =
                (Comentario) nodo.getUserObject();

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este comentario y todas sus respuestas?\n\n"
                        + comentario.getMensaje(),
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        StringBuilder registro = new StringBuilder();

        registrarEliminaciones(comentario, registro);

        // Ejecutar el algoritmo recursivo
        EliminarComentarios.eliminar(comentario);

        // Quitar el comentario del árbol
        DefaultMutableTreeNode padre =
                (DefaultMutableTreeNode) nodo.getParent();

        if (padre == raiz) {
            publicaciones.remove(comentario);
        } else {
            Comentario comentarioPadre =
                    (Comentario) padre.getUserObject();

            comentarioPadre.getRespuestas().remove(comentario);
        }

        modelo.removeNodeFromParent(nodo);

        areaResultado.setText(
                "ELIMINACIÓN COMPLETADA\n\n" + registro
        );

        arbolComentarios.expandRow(0);
    }

    // Registrar todos los comentarios eliminados
    private void registrarEliminaciones(
            Comentario comentario,
            StringBuilder registro) {

        if (comentario == null) {
            return;
        }

        for (Comentario respuesta : comentario.getRespuestas()) {
            registrarEliminaciones(respuesta, registro);
        }

        registro.append("Eliminado: ");
        registro.append(comentario.getMensaje());
        registro.append("\n");
    }
}
