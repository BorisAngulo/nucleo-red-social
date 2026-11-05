
package org.example.interfaz;

import org.example.redSocial.RutaAmigos;
import org.example.redSocial.UsuarioRed;
import org.example.estructuraDeDatos.ListaEnlazada;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRutaAmigos extends JPanel {

    private JComboBox<UsuarioRed> comboOrigen;
    private JComboBox<UsuarioRed> comboDestino;
    private JTextArea areaResultado;

    private final RutaAmigos algoritmo = new RutaAmigos();

    private UsuarioRed ana;
    private UsuarioRed pedro;
    private UsuarioRed carlos;
    private UsuarioRed maria;

    public PanelRutaAmigos(Runnable alVolver) {

        setLayout(new BorderLayout());
        setBackground(Paleta.FONDO);

        crearUsuarios();
        crearInterfaz(alVolver);
    }

    // Crear usuarios y establecer sus amistades
    private void crearUsuarios() {

        ana = new UsuarioRed(1, "Ana");
        pedro = new UsuarioRed(2, "Pedro");
        carlos = new UsuarioRed(3, "Carlos");
        maria = new UsuarioRed(4, "Maria");

        ana.conectarCon(pedro);
        pedro.conectarCon(carlos);
        carlos.conectarCon(maria);
    }

    // Construir interfaz
    private void crearInterfaz(Runnable alVolver) {

        JPanel contenido = new JPanel();
        contenido.setBackground(Paleta.FONDO);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(25, 40, 20, 40));

        JLabel titulo = new JLabel("CADENAS DE CONTACTOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 27));
        titulo.setForeground(Paleta.TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "Encuentra la ruta de amistad entre dos usuarios"
        );
        descripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcion.setForeground(Paleta.TEXTO_SECUNDARIO);
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(Box.createVerticalStrut(15));
        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(8));
        contenido.add(descripcion);
        contenido.add(Box.createVerticalStrut(35));

        // Tarjeta de selección
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Paleta.TARJETA);
        tarjeta.setBorder(new EmptyBorder(25, 30, 25, 30));
        tarjeta.setMaximumSize(new Dimension(650, 230));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel origen = crearEtiqueta("USUARIO DE ORIGEN");
        JLabel destino = crearEtiqueta("USUARIO DE DESTINO");

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();

        agregarUsuarios();

        estilizarCombo(comboOrigen);
        estilizarCombo(comboDestino);

        gbc.gridx = 0;
        gbc.gridy = 0;
        tarjeta.add(origen, gbc);

        gbc.gridx = 1;
        tarjeta.add(destino, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        tarjeta.add(comboOrigen, gbc);

        gbc.gridx = 1;
        tarjeta.add(comboDestino, gbc);

        JButton btnBuscar = new JButton("BUSCAR CADENA");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(Paleta.BOTON);
        btnBuscar.setForeground(Paleta.TEXTO);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(new EmptyBorder(12, 25, 12, 25));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBuscar.addActionListener(e -> buscarCadena());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        tarjeta.add(btnBuscar, gbc);

        contenido.add(tarjeta);
        contenido.add(Box.createVerticalStrut(25));

        // Título del resultado
        JLabel tituloResultado = new JLabel("RESULTADO DE LA BÚSQUEDA");
        tituloResultado.setFont(new Font("Arial", Font.BOLD, 17));
        tituloResultado.setForeground(Paleta.TEXTO);
        tituloResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(tituloResultado);
        contenido.add(Box.createVerticalStrut(12));

        // Área donde se muestra la ruta
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Arial", Font.PLAIN, 16));
        areaResultado.setForeground(Paleta.TEXTO);
        areaResultado.setBackground(Paleta.TARJETA);
        areaResultado.setBorder(new EmptyBorder(20, 20, 20, 20));
        areaResultado.setText(
                "Selecciona dos usuarios y presiona BUSCAR CADENA."
        );

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        scroll.setMaximumSize(new Dimension(650, 170));
        scroll.setPreferredSize(new Dimension(650, 150));

        contenido.add(scroll);

        add(contenido, BorderLayout.CENTER);

        // Botón volver
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setBackground(Paleta.BOTON);
        btnVolver.setForeground(Paleta.TEXTO);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver.addActionListener(e -> alVolver.run());

        add(btnVolver, BorderLayout.SOUTH);
    }

    // Agregar usuarios a los desplegables
    private void agregarUsuarios() {

        comboOrigen.addItem(ana);
        comboOrigen.addItem(pedro);
        comboOrigen.addItem(carlos);
        comboOrigen.addItem(maria);

        comboDestino.addItem(ana);
        comboDestino.addItem(pedro);
        comboDestino.addItem(carlos);
        comboDestino.addItem(maria);
    }

    // Estilo de los desplegables
    private void estilizarCombo(JComboBox<UsuarioRed> combo) {

        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBackground(Paleta.BOTON);
        combo.setForeground(Paleta.TEXTO);
        combo.setPreferredSize(new Dimension(230, 35));
    }

    // Estilo de etiquetas
    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        etiqueta.setForeground(Paleta.TEXTO_SECUNDARIO);

        return etiqueta;
    }

    // Ejecutar búsqueda recursiva
    private void buscarCadena() {

        UsuarioRed origen =
                (UsuarioRed) comboOrigen.getSelectedItem();

        UsuarioRed destino =
                (UsuarioRed) comboDestino.getSelectedItem();

        if (origen == null || destino == null) {
            areaResultado.setText("Selecciona los dos usuarios.");
            return;
        }

        ListaEnlazada<UsuarioRed> ruta =
                algoritmo.buscarRuta(origen, destino);

        int grados = algoritmo.calcularGrados(origen, destino);

        if (grados == -1) {

            areaResultado.setText(
                    "NO SE ENCONTRÓ UNA CADENA DE CONTACTOS.\n\n" +
                            "Los usuarios no tienen una conexión entre sí."
            );

        } else {

            StringBuilder resultado = new StringBuilder();

            resultado.append("CADENA ENCONTRADA\n\n");

            resultado.append("Ruta de contactos:\n\n");

            resultado.append(algoritmo.mostrarRuta(origen, destino));

            resultado.append("\n\nGrados de separación: ");
            resultado.append(grados);

            resultado.append("\n\nCantidad de usuarios en la ruta: ");
            resultado.append(ruta.tamano());

            areaResultado.setText(resultado.toString());
        }
    }
}
