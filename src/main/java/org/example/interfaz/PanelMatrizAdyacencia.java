package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.matriz.CatalogoUsuariosMatriz;
import org.example.redsocial.matriz.UsuarioMatriz;

/** Pantalla para administrar el catálogo y explorar la matriz NxN. */
public class PanelMatrizAdyacencia extends JPanel {

    private final Runnable alVolver;
    private final CatalogoUsuariosMatriz catalogo = new CatalogoUsuariosMatriz();
    private final JTextField nombreUsuario = new JTextField(5); // Mantén tu tamaño compacto preferido
    private final JComboBox<UsuarioMatriz> origen = new JComboBox<>();
    private final JComboBox<UsuarioMatriz> destino = new JComboBox<>();
    private final JComboBox<String> tipoConexion = new JComboBox<>(new String[]{"Seguir", "Amistad"});
    private final JTextArea visualizacion = new JTextArea();

    public PanelMatrizAdyacencia(Runnable alVolver) {
        this.alVolver = alVolver;
        cargarEjemplo();
        prepararVisualizacion();
        armar();
        actualizar();
    }

    private void cargarEjemplo() {
        UsuarioMatriz ana = catalogo.agregarUsuario("Ana");
        UsuarioMatriz luis = catalogo.agregarUsuario("Luis");
        UsuarioMatriz mia = catalogo.agregarUsuario("Mia");
        UsuarioMatriz leo = catalogo.agregarUsuario("Leo");
        catalogo.agregarAmistad(ana, luis);
        catalogo.seguir(mia, ana);
        catalogo.seguir(ana, leo);
    }

    private void prepararVisualizacion() {
        visualizacion.setEditable(false);
        visualizacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        visualizacion.setBackground(Paleta.TARJETA);
        visualizacion.setForeground(Paleta.TEXTO);
        visualizacion.setBorder(new EmptyBorder(12, 12, 12, 12));
        visualizacion.setLineWrap(false);
    }

    private void armar() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Paleta.FONDO);
        contenido.add(texto("Matriz y arreglo", 14, Paleta.TEXTO_SECUNDARIO));
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(texto("Matriz de Adyacencia", 28, Paleta.TEXTO));
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(texto("Gestiona usuarios, amistades bidireccionales y seguimientos.",
                15, Paleta.TEXTO_SECUNDARIO));
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(filaAgregarUsuario());
        contenido.add(Box.createVerticalStrut(12));
        contenido.add(filaConexion());
        contenido.add(Box.createVerticalStrut(12));
        contenido.add(filaConsulta());
        contenido.add(Box.createVerticalStrut(12));

        JScrollPane matrizScroll = new JScrollPane(visualizacion);
        matrizScroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        matrizScroll.getViewport().setBackground(Paleta.TARJETA);
        contenido.add(matrizScroll);

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

    private JPanel filaAgregarUsuario() {
        JPanel fila = fila();
        nombreUsuario.setBackground(Paleta.BOTON);
        nombreUsuario.setForeground(Paleta.TEXTO);
        nombreUsuario.setCaretColor(Paleta.TEXTO);
        nombreUsuario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        nombreUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), new EmptyBorder(4, 8, 4, 8)));

        // Limitamos el tamaño para que BoxLayout no lo estire verticalmente
        nombreUsuario.setPreferredSize(new java.awt.Dimension(180, 35));
        nombreUsuario.setMaximumSize(new java.awt.Dimension(200, 35));

        JButton agregar = boton("Agregar usuario");
        agregar.addActionListener(evento -> agregarUsuario());

        fila.add(nombreUsuario);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(agregar);
        return fila;
    }

    private JPanel filaConexion() {
        JPanel fila = fila();
        estiloCombo(origen);
        estiloCombo(destino);
        estiloCombo(tipoConexion);
        JButton conectar = boton("Conectar");
        conectar.addActionListener(evento -> cambiarConexion(true));
        JButton desconectar = boton("Quitar conexión");
        desconectar.addActionListener(evento -> cambiarConexion(false));
        JButton eliminar = boton("Eliminar usuario origen");
        eliminar.addActionListener(evento -> eliminarUsuario());
        fila.add(origen);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(tipoConexion);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(destino);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(conectar);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(desconectar);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(eliminar);
        return fila;
    }

    private JPanel filaConsulta() {
        JPanel fila = fila();
        fila.add(texto("Usuario:", 14, Paleta.TEXTO_SECUNDARIO));
        fila.add(Box.createHorizontalStrut(8));
        JButton seguidos = boton("Ver seguidos");
        seguidos.addActionListener(evento -> mostrarRelaciones(true));
        fila.add(seguidos);
        fila.add(Box.createHorizontalStrut(6));
        JButton seguidores = boton("Ver seguidores");
        seguidores.addActionListener(evento -> mostrarRelaciones(false));
        fila.add(seguidores);
        return fila;
    }

    private void agregarUsuario() {
        try {
            catalogo.agregarUsuario(nombreUsuario.getText());
            nombreUsuario.setText("");
            actualizar();
        } catch (IllegalArgumentException error) {
            mostrarError(error.getMessage());
        }
    }

    private void cambiarConexion(boolean agregar) {
        UsuarioMatriz seleccionadoOrigen = (UsuarioMatriz) origen.getSelectedItem();
        UsuarioMatriz seleccionadoDestino = (UsuarioMatriz) destino.getSelectedItem();
        if (seleccionadoOrigen == null || seleccionadoDestino == null) {
            mostrarError("Agrega al menos dos usuarios para gestionar conexiones.");
            return;
        }
        if (seleccionadoOrigen == seleccionadoDestino) {
            mostrarError("Selecciona dos usuarios distintos para gestionar una conexión.");
            return;
        }
        boolean amistad = "Amistad".equals(tipoConexion.getSelectedItem());
        if (agregar) {
            if (amistad) {
                catalogo.agregarAmistad(seleccionadoOrigen, seleccionadoDestino);
            } else {
                catalogo.seguir(seleccionadoOrigen, seleccionadoDestino);
            }
        } else {
            boolean existia = amistad
                    ? catalogo.quitarAmistad(seleccionadoOrigen, seleccionadoDestino)
                    : catalogo.dejarDeSeguir(seleccionadoOrigen, seleccionadoDestino);
            if (!existia) {
                mostrarError("No existe esa conexión.");
            }
        }
        actualizar();
    }

    private void eliminarUsuario() {
        UsuarioMatriz seleccionado = (UsuarioMatriz) origen.getSelectedItem();
        if (seleccionado != null && catalogo.quitarUsuario(seleccionado)) {
            actualizar();
        }
    }

    private void mostrarRelaciones(boolean mostrarSeguidos) {
        UsuarioMatriz seleccionado = (UsuarioMatriz) origen.getSelectedItem();
        if (seleccionado == null) {
            mostrarError("No hay usuarios en el catálogo.");
            return;
        }
        String relacion = mostrarSeguidos ? ", sigues a: " : ", te siguen: ";
        StringBuilder texto = new StringBuilder(seleccionado.getNombre()).append(relacion);
        ListaEnlazada<UsuarioMatriz> usuariosRelacionados = mostrarSeguidos
                ? catalogo.obtenerSeguidos(seleccionado)
                : catalogo.obtenerSeguidores(seleccionado);
        if (usuariosRelacionados.estaVacia()) {
            texto.append("ninguno");
        } else {
            boolean primero = true;
            for (UsuarioMatriz usuario : usuariosRelacionados) {
                if (!primero) {
                    texto.append(", ");
                }
                texto.append(usuario.getNombre());
                primero = false;
            }
        }
        JOptionPane.showMessageDialog(this, texto.toString(),
                mostrarSeguidos ? "Seguidos" : "Seguidores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizar() {
        UsuarioMatriz seleccionOrigen = (UsuarioMatriz) origen.getSelectedItem();
        UsuarioMatriz seleccionDestino = (UsuarioMatriz) destino.getSelectedItem();
        UsuarioMatriz[] personas = arreglo(catalogo.getUsuarios());
        origen.setModel(new javax.swing.DefaultComboBoxModel<>(personas));
        destino.setModel(new javax.swing.DefaultComboBoxModel<>(personas));
        restaurarSeleccion(origen, personas, seleccionOrigen);
        restaurarSeleccion(destino, personas, seleccionDestino);

        StringBuilder tabla = new StringBuilder("MATRIZ NxN: fila -> origen; columna -> destino\n");
        tabla.append(String.format("%-12s", ""));
        for (UsuarioMatriz persona : personas) {
            tabla.append(String.format("%12.12s", persona.getNombre()));
        }
        boolean[][] matriz = catalogo.getMatrizAdyacencia();
        for (int fila = 0; fila < personas.length; fila++) {
            tabla.append('\n').append(String.format("%-12.12s", personas[fila].getNombre()));
            for (int columna = 0; columna < personas.length; columna++) {
                tabla.append(String.format("%12s", matriz[fila][columna] ? "1" : "0"));
            }
        }
        visualizacion.setText(tabla.toString());
        visualizacion.setCaretPosition(0);
    }

    private UsuarioMatriz[] arreglo(ListaEnlazada<UsuarioMatriz> usuarios) {
        UsuarioMatriz[] personas = new UsuarioMatriz[usuarios.tamano()];
        int indice = 0;
        for (UsuarioMatriz usuario : usuarios) {
            personas[indice++] = usuario;
        }
        return personas;
    }

    private void restaurarSeleccion(JComboBox<UsuarioMatriz> combo, UsuarioMatriz[] personas,
                                    UsuarioMatriz seleccion) {
        for (int indice = 0; indice < personas.length; indice++) {
            if (personas[indice] == seleccion) {
                combo.setSelectedIndex(indice);
                return;
            }
        }
    }

    private JPanel fila() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Paleta.FONDO);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void estiloCombo(JComboBox<?> combo) {
        combo.setBackground(Paleta.BOTON);
        combo.setForeground(Paleta.TEXTO);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        // Limitamos la altura y el ancho para que no se estiren verticalmente
        combo.setPreferredSize(new java.awt.Dimension(140, 35));
        combo.setMaximumSize(new java.awt.Dimension(160, 35));

        // Estilo opcional para el borde si deseas que combine con los botones
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), new EmptyBorder(4, 6, 4, 6)));
    }

    private JButton boton(String titulo) {
        JButton boton = new JButton(titulo);
        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE), new EmptyBorder(8, 10, 8, 10)));
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

    private javax.swing.JLabel texto(String contenido, int tamano, java.awt.Color color) {
        javax.swing.JLabel etiqueta = new javax.swing.JLabel(contenido);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, tamano));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Matriz de Adyacencia", JOptionPane.WARNING_MESSAGE);
    }
}
