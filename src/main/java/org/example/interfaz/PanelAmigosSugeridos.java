package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
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

import org.example.redsocial.sugerencias.CarruselSugerencias;
import org.example.redsocial.sugerencias.PerfilSugerido;

/**
 * Pantalla para explorar amigos sugeridos con un carrusel basado en ListaCircular.
 * El último perfil se conecta de nuevo con el primero, permitiendo recorrer
 * las recomendaciones de forma cíclica e indefinida.
 */
public class PanelAmigosSugeridos extends JPanel {

    private final Runnable alVolver;
    private final CarruselSugerencias carrusel;

    private final JLabel lblIndice = texto("", 14, Paleta.TEXTO_SECUNDARIO);
    private final JLabel lblNombre = texto("", 20, Paleta.TEXTO);
    private final JLabel lblUsuario = texto("", 15, Paleta.TEXTO_SECUNDARIO);
    private final JLabel lblRol = texto("", 14, Paleta.TEXTO);
    private final JLabel lblAmigosComun = texto("", 13, Paleta.TEXTO_SECUNDARIO);

    private final JButton btnAnterior = boton("◄ Anterior (Circular)");
    private final JButton btnSiguiente = boton("Siguiente sugerencia (Ciclo) ►");

    private final JTextField campoNombre = new JTextField(12);
    private final JTextField campoUsuario = new JTextField(12);
    private final JTextField campoRol = new JTextField(12);
    private final JTextField campoComun = new JTextField(4);

    private final JTextArea visualizacionCiclo = new JTextArea();
    private final JTextArea visualizacionLista = new JTextArea();

    public PanelAmigosSugeridos(Runnable alVolver) {
        this.alVolver = alVolver;
        this.carrusel = CarruselSugerencias.ejemplo();
        configurarComponentes();
        armarInterfaz();
        actualizarPantalla();
    }

    private void configurarComponentes() {
        campoNombre.setText("Daniel Rocha");
        campoUsuario.setText("@daniel_r");
        campoRol.setText("DevOps Specialist");
        campoComun.setText("3");

        estiloCampo(campoNombre);
        estiloCampo(campoUsuario);
        estiloCampo(campoRol);
        estiloCampo(campoComun);

        configurarAreaTexto(visualizacionCiclo, 2);
        configurarAreaTexto(visualizacionLista, 7);

        visualizacionCiclo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        visualizacionLista.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        btnAnterior.addActionListener(e -> {
            carrusel.anterior();
            actualizarPantalla();
        });

        btnSiguiente.addActionListener(e -> {
            carrusel.siguiente();
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
        columna.add(texto("Amigos sugeridos", 28, Paleta.TEXTO));
        columna.add(Box.createVerticalStrut(6));
        columna.add(texto(
                "Lista circular de perfiles recomendados que se recorre una y otra vez sin fin de memoria.",
                15, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(18));

        columna.add(tarjetaPerfilActual());
        columna.add(Box.createVerticalStrut(14));
        columna.add(tarjetaNuevoPerfil());
        columna.add(Box.createVerticalStrut(14));
        columna.add(tarjetaEstructuraCircular());

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

    private JPanel tarjetaPerfilActual() {
        JPanel tarjeta = tarjetaContenedor();

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Paleta.TARJETA);
        encabezado.add(texto("Tarjeta de recomendación activa", 14, Paleta.TEXTO_SECUNDARIO), BorderLayout.WEST);
        encabezado.add(lblIndice, BorderLayout.EAST);
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(encabezado);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(2));
        tarjeta.add(lblUsuario);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblRol);
        tarjeta.add(Box.createVerticalStrut(2));
        tarjeta.add(lblAmigosComun);
        tarjeta.add(Box.createVerticalStrut(14));

        JPanel filaNavegacion = new JPanel(new GridLayout(1, 2, 12, 0));
        filaNavegacion.setBackground(Paleta.TARJETA);
        filaNavegacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaNavegacion.add(btnAnterior);
        filaNavegacion.add(btnSiguiente);
        tarjeta.add(filaNavegacion);
        tarjeta.add(Box.createVerticalStrut(10));

        JPanel filaAcciones = new JPanel(new GridLayout(1, 2, 12, 0));
        filaAcciones.setBackground(Paleta.TARJETA);
        filaAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnConectar = boton("🤝 Conectar (Agregar amigo)");
        btnConectar.addActionListener(e -> accionConectar());

        JButton btnDescartar = boton("✖ Descartar sugerencia");
        btnDescartar.addActionListener(e -> accionDescartar());

        filaAcciones.add(btnConectar);
        filaAcciones.add(btnDescartar);
        tarjeta.add(filaAcciones);

        return tarjeta;
    }

    private JPanel tarjetaNuevoPerfil() {
        JPanel tarjeta = tarjetaContenedor();
        tarjeta.add(texto("Añadir nuevo perfil al carrusel circular", 16, Paleta.TEXTO));
        tarjeta.add(Box.createVerticalStrut(10));

        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(Paleta.TARJETA);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        fila.add(texto("Nombre: ", 13, Paleta.TEXTO_SECUNDARIO));
        fila.add(campoNombre);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(texto("Usuario: ", 13, Paleta.TEXTO_SECUNDARIO));
        fila.add(campoUsuario);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(texto("Rol: ", 13, Paleta.TEXTO_SECUNDARIO));
        fila.add(campoRol);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(texto("Amigos: ", 13, Paleta.TEXTO_SECUNDARIO));
        fila.add(campoComun);
        tarjeta.add(fila);
        tarjeta.add(Box.createVerticalStrut(10));

        JButton btnAgregar = boton("➕ Insertar en lista circular");
        btnAgregar.addActionListener(e -> accionAgregar());
        tarjeta.add(btnAgregar);

        return tarjeta;
    }

    private JPanel tarjetaEstructuraCircular() {
        JPanel tarjeta = tarjetaContenedor();
        tarjeta.add(texto("Estructura en memoria: Ciclo continuo (el último apunta al primero)", 16, Paleta.TEXTO));
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(visualizacionCiclo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(texto("Perfiles activos en el anillo:", 14, Paleta.TEXTO_SECUNDARIO));
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(visualizacionLista);
        return tarjeta;
    }

    private void accionConectar() {
        PerfilSugerido actual = carrusel.actual();
        if (actual == null) {
            JOptionPane.showMessageDialog(this, "No hay más sugerencias en el carrusel.", "Lista vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        carrusel.eliminarActual();
        JOptionPane.showMessageDialog(this, "¡Solicitud de amistad enviada a " + actual.getNombre() + " (" + actual.getUsuario() + ")!", "Conectado", JOptionPane.INFORMATION_MESSAGE);
        actualizarPantalla();
    }

    private void accionDescartar() {
        if (carrusel.estaVacio()) {
            return;
        }
        carrusel.eliminarActual();
        actualizarPantalla();
    }

    private void accionAgregar() {
        String nombre = campoNombre.getText().trim();
        String usuario = campoUsuario.getText().trim();
        String rol = campoRol.getText().trim();
        int comun = 1;
        try {
            comun = Integer.parseInt(campoComun.getText().trim());
        } catch (NumberFormatException ignored) {
        }

        if (nombre.isEmpty() || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa nombre y usuario.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!usuario.startsWith("@")) {
            usuario = "@" + usuario;
        }

        carrusel.agregar(new PerfilSugerido(nombre, usuario, rol, comun));
        campoNombre.setText("");
        campoUsuario.setText("");
        actualizarPantalla();
    }

    private void actualizarPantalla() {
        int total = carrusel.getCantidad();
        PerfilSugerido actual = carrusel.actual();

        if (actual == null || total == 0) {
            lblIndice.setText("[ Carrusel vacío ]");
            lblNombre.setText("No hay más sugerencias");
            lblUsuario.setText("Has visto o conectado con todos los perfiles.");
            lblRol.setText("—");
            lblAmigosComun.setText("—");
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
        } else {
            lblIndice.setText("Sugerencia " + (carrusel.getIndiceActual() + 1) + " de " + total + " (Ciclo continuo)");
            lblNombre.setText(actual.getNombre());
            lblUsuario.setText(actual.getUsuario());
            lblRol.setText("Área / Ocupación: " + actual.getRol());
            lblAmigosComun.setText("👥 " + actual.getAmigosEnComun() + " amigos en común con tu red");
            btnAnterior.setEnabled(total > 1);
            btnSiguiente.setEnabled(total > 1);
        }

        actualizarVisualizacion(total);
    }

    private void actualizarVisualizacion(int total) {
        StringBuilder esquema = new StringBuilder();
        esquema.append("Anillo circular: ");
        if (total == 0) {
            esquema.append("(carrusel vacío)");
        } else {
            int i = 0;
            String primerHandle = null;
            for (PerfilSugerido p : carrusel.getPerfiles()) {
                if (i == 0) {
                    primerHandle = p.getUsuario();
                }
                if (i == carrusel.getIndiceActual()) {
                    esquema.append("[* ").append(p.getUsuario()).append(" (ACTUAL) *]");
                } else {
                    esquema.append("(").append(p.getUsuario()).append(")");
                }
                esquema.append(" ──► ");
                i++;
            }
            esquema.append("(vuelve a ").append(primerHandle).append(")");
        }
        visualizacionCiclo.setText(esquema.toString());
        visualizacionCiclo.setCaretPosition(0);

        StringBuilder lista = new StringBuilder();
        int idx = 0;
        for (PerfilSugerido p : carrusel.getPerfiles()) {
            boolean esActual = (idx == carrusel.getIndiceActual());
            lista.append(esActual ? " ► " : "   ");
            lista.append(String.format("[%02d] %-18s %-14s • %-24s • %d amigos en común",
                    idx + 1, p.getNombre(), p.getUsuario(), p.getRol(), p.getAmigosEnComun()));
            if (esActual) {
                lista.append("  ◄── [EN PANTALLA]");
            }
            lista.append("\n");
            idx++;
        }
        visualizacionLista.setText(lista.toString());
        visualizacionLista.setCaretPosition(0);
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

    private void estiloCampo(JTextField campo) {
        campo.setBackground(Paleta.BOTON);
        campo.setForeground(Paleta.TEXTO);
        campo.setCaretColor(Paleta.TEXTO);
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(5, 7, 5, 7)));
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
