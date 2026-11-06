package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.example.redsocial.comentarios.Comentario;
import org.example.redsocial.comentarios.HiloComentarios;

/**
 * Pantalla interactiva para la gestión dinámica del hilo de comentarios.
 * Utiliza una ListaDoblementeEnlazada que permite navegar hacia adelante (next)
 * y hacia atrás (prev), insertar respuestas intermedias o borrar comentarios en O(1)
 * sin reorganizar memoria continua.
 */
public class PanelHiloComentarios extends JPanel {

    private final Runnable alVolver;
    private final HiloComentarios hilo;

    private final JLabel lblPostAutor = texto("", 13, Paleta.TEXTO_SECUNDARIO);
    private final JLabel lblPostTitulo = texto("", 18, Paleta.TEXTO);
    private final JTextArea txtPostContenido = new JTextArea();

    private final JLabel lblPosicion = texto("", 14, Paleta.TEXTO_SECUNDARIO);
    private final JLabel lblComentarioAutor = texto("", 16, Paleta.TEXTO);
    private final JLabel lblComentarioDetalle = texto("", 13, Paleta.TEXTO_SECUNDARIO);
    private final JTextArea txtComentarioCuerpo = new JTextArea();

    private final JButton btnPrimero = boton("|◄ Inicio");
    private final JButton btnAnterior = boton("◄ Anterior (prev)");
    private final JButton btnSiguiente = boton("Siguiente (next) ►");
    private final JButton btnUltimo = boton("Fin ►|");

    private final JTextField campoAutor = new JTextField(12);
    private final JTextField campoTexto = new JTextField(24);

    private final JTextArea visualizacionNodos = new JTextArea();
    private final JTextArea visualizacionLista = new JTextArea();
    private final JLabel lblMetricas = texto("", 13, Paleta.TEXTO_SECUNDARIO);

    public PanelHiloComentarios(Runnable alVolver) {
        this.alVolver = alVolver;
        this.hilo = HiloComentarios.ejemplo();
        configurarComponentes();
        armarInterfaz();
        actualizarPantalla();
    }

    private void configurarComponentes() {
        campoAutor.setText("UsuarioDemo");
        campoTexto.setText("¡Gran implementación con lista doble!");
        estiloCampoTexto(campoAutor);
        estiloCampoTexto(campoTexto);

        configurarAreaTexto(txtPostContenido, 2);
        configurarAreaTexto(txtComentarioCuerpo, 3);
        configurarAreaTexto(visualizacionNodos, 2);
        configurarAreaTexto(visualizacionLista, 7);

        visualizacionNodos.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        visualizacionLista.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        btnPrimero.addActionListener(e -> {
            hilo.fijarAlInicio();
            actualizarPantalla();
        });

        btnAnterior.addActionListener(e -> {
            hilo.retroceder();
            actualizarPantalla();
        });

        btnSiguiente.addActionListener(e -> {
            hilo.avanzar();
            actualizarPantalla();
        });

        btnUltimo.addActionListener(e -> {
            hilo.irAlFinal();
            actualizarPantalla();
        });
    }

    private void armarInterfaz() {
        setLayout(new BorderLayout());
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBackground(Paleta.FONDO);

        columna.add(texto("Listas enlazadas", 14, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(6));
        columna.add(texto("Hilo de comentarios", 28, Paleta.TEXTO));
        columna.add(Box.createVerticalStrut(6));
        columna.add(texto(
                "Navegación bidireccional (prev / next), inserción de respuestas y borrado en O(1) con Lista Doblemente Enlazada.",
                15, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(18));

        columna.add(tarjetaPost());
        columna.add(Box.createVerticalStrut(14));
        columna.add(tarjetaComentarioActual());
        columna.add(Box.createVerticalStrut(14));
        columna.add(tarjetaAcciones());
        columna.add(Box.createVerticalStrut(14));
        columna.add(tarjetaEstructura());

        JScrollPane scroll = new JScrollPane(columna);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        add(scroll, BorderLayout.CENTER);

        JButton volver = boton("Volver");
        volver.addActionListener(e -> alVolver.run());
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Paleta.FONDO);
        pie.setBorder(new EmptyBorder(16, 0, 0, 0));
        pie.add(volver, BorderLayout.WEST);
        add(pie, BorderLayout.SOUTH);
    }

    private JPanel tarjetaPost() {
        JPanel tarjeta = tarjetaContenedor();
        lblPostAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPostTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPostContenido.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblPostAutor);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblPostTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(txtPostContenido);
        return tarjeta;
    }

    private JPanel tarjetaComentarioActual() {
        JPanel tarjeta = tarjetaContenedor();

        JPanel encabezadoFoco = new JPanel(new BorderLayout());
        encabezadoFoco.setBackground(Paleta.TARJETA);
        encabezadoFoco.add(texto("Comentario en foco (Cursor en el nodo actual)", 14, Paleta.TEXTO_SECUNDARIO), BorderLayout.WEST);
        encabezadoFoco.add(lblPosicion, BorderLayout.EAST);
        encabezadoFoco.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(encabezadoFoco);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblComentarioAutor);
        tarjeta.add(Box.createVerticalStrut(2));
        tarjeta.add(lblComentarioDetalle);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(txtComentarioCuerpo);
        tarjeta.add(Box.createVerticalStrut(12));

        JPanel filaNavegacion = new JPanel(new GridLayout(1, 4, 10, 0));
        filaNavegacion.setBackground(Paleta.TARJETA);
        filaNavegacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaNavegacion.add(btnPrimero);
        filaNavegacion.add(btnAnterior);
        filaNavegacion.add(btnSiguiente);
        filaNavegacion.add(btnUltimo);
        tarjeta.add(filaNavegacion);

        return tarjeta;
    }

    private JPanel tarjetaAcciones() {
        JPanel tarjeta = tarjetaContenedor();
        tarjeta.add(texto("Operaciones dinámicas sobre la lista (Inserción y Borrado O(1))", 16, Paleta.TEXTO));
        tarjeta.add(Box.createVerticalStrut(10));

        JPanel filaCampos = new JPanel();
        filaCampos.setLayout(new BoxLayout(filaCampos, BoxLayout.X_AXIS));
        filaCampos.setBackground(Paleta.TARJETA);
        filaCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

        filaCampos.add(texto("Autor: ", 14, Paleta.TEXTO_SECUNDARIO));
        filaCampos.add(Box.createHorizontalStrut(6));
        filaCampos.add(campoAutor);
        filaCampos.add(Box.createHorizontalStrut(14));
        filaCampos.add(texto("Comentario: ", 14, Paleta.TEXTO_SECUNDARIO));
        filaCampos.add(Box.createHorizontalStrut(6));
        filaCampos.add(campoTexto);
        tarjeta.add(filaCampos);
        tarjeta.add(Box.createVerticalStrut(12));

        JPanel filaBotones = new JPanel(new GridLayout(1, 3, 10, 0));
        filaBotones.setBackground(Paleta.TARJETA);
        filaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnResponder = boton("💬 Responder al actual (O(1))");
        btnResponder.setToolTipText("Inserta un nodo intermedio justo después del actual reconectando punteros");
        btnResponder.addActionListener(e -> accionResponder());

        JButton btnComentarFinal = boton("➕ Comentar al final");
        btnComentarFinal.setToolTipText("Agrega un nodo al final de la lista doblemente enlazada");
        btnComentarFinal.addActionListener(e -> accionComentarFinal());

        JButton btnEliminar = boton("🗑️ Borrar actual (O(1))");
        btnEliminar.setToolTipText("Desconecta el nodo actual reacomodando prev y next en un solo paso");
        btnEliminar.addActionListener(e -> accionEliminar());

        filaBotones.add(btnResponder);
        filaBotones.add(btnComentarFinal);
        filaBotones.add(btnEliminar);
        tarjeta.add(filaBotones);

        return tarjeta;
    }

    private JPanel tarjetaEstructura() {
        JPanel tarjeta = tarjetaContenedor();
        tarjeta.add(texto("Inspección de memoria: Nodos y punteros bidireccionales", 16, Paleta.TEXTO));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(visualizacionNodos);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(texto("Lista completa de comentarios del hilo:", 14, Paleta.TEXTO_SECUNDARIO));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(visualizacionLista);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblMetricas);
        return tarjeta;
    }

    private void accionResponder() {
        String autor = campoAutor.getText().trim();
        String contenido = campoTexto.getText().trim();
        if (autor.isEmpty() || contenido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa el autor y el comentario.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        hilo.insertarRespuesta(autor, contenido, "Ahora");
        campoTexto.setText("");
        actualizarPantalla();
    }

    private void accionComentarFinal() {
        String autor = campoAutor.getText().trim();
        String contenido = campoTexto.getText().trim();
        if (autor.isEmpty() || contenido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa el autor y el comentario.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        hilo.agregarAlFinal(autor, contenido, "Ahora");
        campoTexto.setText("");
        actualizarPantalla();
    }

    private void accionEliminar() {
        if (hilo.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El hilo no contiene comentarios para eliminar.", "Lista vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Comentario borrado = hilo.eliminarActual();
        if (borrado != null) {
            actualizarPantalla();
        }
    }

    private void actualizarPantalla() {
        lblPostAutor.setText("Publicado por @" + hilo.getAutorPost() + " • Hilo Oficial");
        lblPostTitulo.setText(hilo.getTituloPost());
        txtPostContenido.setText(hilo.getContenidoPost());

        int total = hilo.getCantidad();
        int pos = hilo.getPosicionActual();
        Comentario actual = hilo.getComentarioActual();

        if (actual == null || total == 0) {
            lblPosicion.setText("[ 0 de 0 ]");
            lblComentarioAutor.setText("No hay comentarios en este hilo");
            lblComentarioDetalle.setText("Escribe el primero en la caja de texto.");
            txtComentarioCuerpo.setText("—");
            btnPrimero.setEnabled(false);
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
        } else {
            lblPosicion.setText("[ " + pos + " de " + total + " ]");
            lblComentarioAutor.setText("#" + actual.getId() + "  @" + actual.getAutor());
            String resp = actual.esRespuesta() ? " (Respuesta a @" + actual.getRespuestaA() + ")" : " (Comentario principal)";
            lblComentarioDetalle.setText("Hora: " + actual.getHora() + resp);
            txtComentarioCuerpo.setText(actual.getTexto());

            btnPrimero.setEnabled(pos > 1);
            btnAnterior.setEnabled(hilo.tieneAnterior());
            btnSiguiente.setEnabled(hilo.tieneSiguiente());
            btnUltimo.setEnabled(pos < total);
        }

        actualizarVisualizacion(total, pos);
    }

    private void actualizarVisualizacion(int total, int pos) {
        StringBuilder esquemaNodos = new StringBuilder();
        esquemaNodos.append("Estructura de Enlaces:  null ◄──► ");
        if (total == 0) {
            esquemaNodos.append("(vacío) ◄──► null");
        } else {
            int i = 1;
            for (Comentario c : hilo.getComentarios()) {
                if (i == pos) {
                    esquemaNodos.append("[* #").append(c.getId()).append(" @").append(c.getAutor()).append(" (EN FOCO) *]");
                } else {
                    esquemaNodos.append("[#").append(c.getId()).append(" @").append(c.getAutor()).append("]");
                }
                esquemaNodos.append(" ◄──► ");
                i++;
            }
            esquemaNodos.append("null");
        }
        visualizacionNodos.setText(esquemaNodos.toString());
        visualizacionNodos.setCaretPosition(0);

        StringBuilder listaTexto = new StringBuilder();
        int indice = 1;
        for (Comentario c : hilo.getComentarios()) {
            boolean esFoco = (indice == pos);
            listaTexto.append(esFoco ? " ► " : "   ");
            listaTexto.append(String.format("[%02d] #%-3d @%-12s", indice, c.getId(), c.getAutor()));
            if (c.esRespuesta()) {
                listaTexto.append(String.format(" ↳ resp a @%-10s", c.getRespuestaA()));
            } else {
                listaTexto.append(String.format("   %-18s", ""));
            }
            listaTexto.append(": \"").append(c.getTexto()).append("\" (").append(c.getHora()).append(")");
            if (esFoco) {
                listaTexto.append("  ◄── [VIENDO]");
            }
            listaTexto.append("\n");
            indice++;
        }
        if (total == 0) {
            listaTexto.append("   (El hilo se encuentra vacío. Puedes agregar comentarios abajo)\n");
        }
        visualizacionLista.setText(listaTexto.toString());
        visualizacionLista.setCaretPosition(0);

        lblMetricas.setText("Nodos en memoria: " + total + " | Inserción intermedia O(1) | Borrado O(1) | Recorrido bidireccional");
    }

    private JPanel tarjetaContenedor() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(16, 18, 16, 18)));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tarjeta;
    }

    private void configurarAreaTexto(JTextArea area, int filas) {
        area.setRows(filas);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Paleta.TARJETA);
        area.setForeground(Paleta.TEXTO);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    private void estiloCampoTexto(JTextField campo) {
        campo.setBackground(Paleta.BOTON);
        campo.setForeground(Paleta.TEXTO);
        campo.setCaretColor(Paleta.TEXTO);
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(6, 8, 6, 8)));
    }

    private JButton boton(String titulo) {
        JButton boton = new JButton(titulo);
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(8, 12, 8, 12)));
        boton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evento) {
                if (boton.isEnabled()) {
                    boton.setBackground(Paleta.BOTON_ACTIVO);
                }
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
}
