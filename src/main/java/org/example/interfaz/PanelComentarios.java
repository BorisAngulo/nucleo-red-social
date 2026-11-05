package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.example.redsocial.comentario.Comentario;
import org.example.redsocial.comentario.HiloComentarios;

/**
 * Pantalla para gestionar el hilo de comentarios.
 */
public class PanelComentarios extends JPanel {

    private final Runnable alVolver;
    private final HiloComentarios hilo;

    private final JLabel posicionTexto;
    private final JTextArea comentarioActual;
    private final JTextField autor;
    private final JTextArea contenido;

    private final JButton anterior;
    private final JButton siguiente;
    private final JButton eliminar;
    private final JButton responder;

    public PanelComentarios(Runnable alVolver) {
        this.alVolver = alVolver;
        this.hilo = new HiloComentarios();

        this.posicionTexto = texto("Sin comentarios", 14, Paleta.TEXTO_SECUNDARIO);

        this.comentarioActual = new JTextArea(8, 30);
        comentarioActual.setEditable(false);
        comentarioActual.setLineWrap(true);
        comentarioActual.setWrapStyleWord(true);
        comentarioActual.setBackground(Paleta.TARJETA);
        comentarioActual.setForeground(Paleta.TEXTO);
        comentarioActual.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        this.autor = new JTextField("Usuario");
        this.contenido = new JTextArea(3, 30);
        contenido.setLineWrap(true);
        contenido.setWrapStyleWord(true);
        contenido.setBackground(Paleta.TARJETA);
        contenido.setForeground(Paleta.TEXTO);
        contenido.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        this.anterior = new JButton("Anterior");
        this.siguiente = new JButton("Siguiente");
        this.eliminar = new JButton("Eliminar");
        this.responder = new JButton("Responder");

        armar();
        actualizar();
    }

    private void armar() {
        setLayout(new BorderLayout());
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(28, 32, 32, 32));

        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBackground(Paleta.FONDO);

        columna.add(texto("Comentarios", 14, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(texto("Hilo de comentarios", 28, Paleta.TEXTO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(texto(
                "Agrega comentarios, responde y navega por el hilo.",
                16,
                Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(20));

        columna.add(posicionTexto);
        columna.add(Box.createVerticalStrut(10));
        columna.add(cajaComentario());
        columna.add(Box.createVerticalStrut(12));
        columna.add(filaNavegacion());
        columna.add(Box.createVerticalStrut(24));

        columna.add(texto("Autor", 14, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(6));
        autor.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 36));
        columna.add(autor);

        columna.add(Box.createVerticalStrut(12));
        columna.add(texto("Contenido del comentario", 14, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(6));
        columna.add(cajaContenido());
        columna.add(Box.createVerticalStrut(12));
        columna.add(filaAcciones());

        JButton volver = new JButton("Volver");
        estilo(volver);
        volver.setAlignmentX(Component.LEFT_ALIGNMENT);
        volver.addActionListener(evento -> alVolver.run());

        JScrollPane scroll = new JScrollPane(columna);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);

        add(scroll, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Paleta.FONDO);
        sur.setBorder(new EmptyBorder(16, 0, 0, 0));
        sur.add(volver, BorderLayout.WEST);

        add(sur, BorderLayout.SOUTH);
    }

    private JPanel cajaComentario() {
        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(Paleta.TARJETA);
        caja.setBorder(bordeCaja());
        caja.add(comentarioActual, BorderLayout.CENTER);
        return caja;
    }

    private JPanel cajaContenido() {
        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(Paleta.TARJETA);
        caja.setBorder(bordeCaja());
        caja.add(new JScrollPane(contenido), BorderLayout.CENTER);
        return caja;
    }

    private JPanel filaNavegacion() {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(Paleta.FONDO);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        estilo(anterior);
        estilo(siguiente);
        estilo(eliminar);

        anterior.addActionListener(evento -> {
            hilo.anterior();
            actualizar();
        });

        siguiente.addActionListener(evento -> {
            hilo.siguiente();
            actualizar();
        });

        eliminar.addActionListener(evento -> {
            hilo.eliminarActual();
            actualizar();
        });

        fila.add(anterior);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(siguiente);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(eliminar);

        return fila;
    }

    private JPanel filaAcciones() {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(Paleta.FONDO);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton agregar = new JButton("Agregar comentario");
        estilo(agregar);
        estilo(responder);

        agregar.addActionListener(evento -> agregarComentario());

        responder.addActionListener(evento -> responderComentario());

        fila.add(agregar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(responder);

        return fila;
    }

    private void agregarComentario() {
        String nombre = autor.getText().trim();
        String texto = contenido.getText().trim();

        if (nombre.isEmpty() || texto.isEmpty()) {
            return;
        }

        hilo.agregarComentario(nombre, texto);

        contenido.setText("");
        actualizar();
    }

    private void responderComentario() {
        String nombre = autor.getText().trim();
        String texto = contenido.getText().trim();

        if (nombre.isEmpty() || texto.isEmpty()) {
            return;
        }

        hilo.responder(nombre, texto);

        contenido.setText("");
        actualizar();
    }

    private void actualizar() {
        Comentario actual = hilo.obtenerActual();

        if (actual == null) {
            posicionTexto.setText("Sin comentarios");
            comentarioActual.setText("Todavía no hay comentarios.");
        } else {
            posicionTexto.setText(
                    "Comentario " + hilo.obtenerPosicion()
                    + " de " + hilo.obtenerCantidad());

            comentarioActual.setText(
                    actual.getAutor() + ":\n\n"
                    + actual.getContenido());
        }

        anterior.setEnabled(!hilo.estaVacio());
        siguiente.setEnabled(!hilo.estaVacio());
        eliminar.setEnabled(!hilo.estaVacio());
        responder.setEnabled(!hilo.estaVacio());
    }

    private javax.swing.border.Border bordeCaja() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(16, 16, 16, 16));
    }

    private JLabel texto(String contenido, int tamano, java.awt.Color color) {
        JLabel etiqueta = new JLabel(contenido);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, tamano));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private void estilo(JButton boton) {
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(12, 14, 12, 14)));
        boton.setCursor(
                java.awt.Cursor.getPredefinedCursor(
                        java.awt.Cursor.HAND_CURSOR));
    }
}
