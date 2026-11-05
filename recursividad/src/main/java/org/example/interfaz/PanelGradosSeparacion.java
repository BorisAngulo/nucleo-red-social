
package org.example.interfaz;

import org.example.redSocial.GradosSeparacion;
import org.example.redSocial.UsuarioRed;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelGradosSeparacion extends JPanel {

    private JComboBox<UsuarioRed> usuarioInicial;
    private JComboBox<UsuarioRed> usuarioDestino;
    private JTextArea areaResultado;

    // Usuarios de ejemplo
    private UsuarioRed ana;
    private UsuarioRed pedro;
    private UsuarioRed carlos;
    private UsuarioRed maria;

    public PanelGradosSeparacion(Runnable alVolver) {

        setLayout(new BorderLayout(20, 20));
        setBackground(Paleta.FONDO);

        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Crear usuarios y amistades
        crearRedSocial();

        // Crear las secciones
        add(crearEncabezado(alVolver), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    // =====================================
    // CREAR USUARIOS Y AMISTADES
    // =====================================

    private void crearRedSocial() {

        ana = new UsuarioRed(1, "Ana");
        pedro = new UsuarioRed(2, "Pedro");
        carlos = new UsuarioRed(3, "Carlos");
        maria = new UsuarioRed(4, "Maria");

        // Establecer conexiones
        ana.conectarCon(pedro);
        pedro.conectarCon(carlos);
        carlos.conectarCon(maria);
    }

    // =====================================
    // ENCABEZADO
    // =====================================

    private JPanel crearEncabezado(Runnable alVolver) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Paleta.FONDO);

        JLabel titulo = new JLabel("GRADOS DE SEPARACIÓN");

        titulo.setForeground(Paleta.TEXTO);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        JButton volver = crearBoton("VOLVER");

        volver.addActionListener(e -> alVolver.run());

        panel.add(titulo, BorderLayout.CENTER);
        panel.add(volver, BorderLayout.EAST);

        return panel;
    }

    // =====================================
    // CONTENIDO PRINCIPAL
    // =====================================

    private JPanel crearContenido() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Paleta.FONDO);

        // TARJETA DE SELECCION
        JPanel tarjeta = crearTarjeta();

        JLabel descripcion = new JLabel(
                "Selecciona dos usuarios para conocer su conexión."
        );

        descripcion.setForeground(Paleta.TEXTO_SECUNDARIO);
        descripcion.setFont(new Font("Arial", Font.PLAIN, 13));

        usuarioInicial = new JComboBox<>(
                new UsuarioRed[]{ana, pedro, carlos, maria}
        );

        usuarioDestino = new JComboBox<>(
                new UsuarioRed[]{ana, pedro, carlos, maria}
        );

        JButton calcular = crearBoton("CALCULAR GRADOS");

        calcular.setBackground(Paleta.ACENTO);
        calcular.setForeground(Color.WHITE);

        tarjeta.add(descripcion);
        tarjeta.add(Box.createVerticalStrut(20));

        tarjeta.add(crearEtiqueta("USUARIO INICIAL"));
        tarjeta.add(usuarioInicial);

        tarjeta.add(Box.createVerticalStrut(15));

        tarjeta.add(crearEtiqueta("USUARIO DESTINO"));
        tarjeta.add(usuarioDestino);

        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(calcular);

        // TARJETA DE RESULTADOS
        JPanel tarjetaResultado = crearTarjeta();

        JLabel tituloResultado = new JLabel("RESULTADO DEL ANÁLISIS");

        tituloResultado.setForeground(Paleta.TEXTO);
        tituloResultado.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        areaResultado = new JTextArea(
                "Selecciona los usuarios y presiona CALCULAR GRADOS."
        );

        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        areaResultado.setBackground(Paleta.TARJETA);
        areaResultado.setForeground(Paleta.TEXTO);
        areaResultado.setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );

        JScrollPane scroll = new JScrollPane(areaResultado);

        scroll.setBorder(
                BorderFactory.createLineBorder(Paleta.BORDE)
        );

        calcular.addActionListener(e -> calcularGrados());

        tarjetaResultado.add(tituloResultado);
        tarjetaResultado.add(Box.createVerticalStrut(12));
        tarjetaResultado.add(scroll);

        panel.add(tarjeta);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tarjetaResultado);

        return panel;
    }

    // =====================================
    // CALCULAR GRADOS
    // =====================================

    private void calcularGrados() {

        UsuarioRed inicio =
                (UsuarioRed) usuarioInicial.getSelectedItem();

        UsuarioRed destino =
                (UsuarioRed) usuarioDestino.getSelectedItem();

        if (inicio == null || destino == null) {
            areaResultado.setText("Selecciona ambos usuarios.");
            return;
        }

        // Ejecutar algoritmo recursivo
        GradosSeparacion.Resultado resultado =
                GradosSeparacion.calcular(inicio, destino);

        if (!resultado.existeConexion()) {

            areaResultado.setText(
                    "No existe una conexión entre los usuarios."
            );

            return;
        }

        StringBuilder ruta = new StringBuilder();

        for (UsuarioRed usuario : resultado.getRuta()) {

            ruta.append(usuario.getNombre());
            ruta.append("  →  ");
        }

        // Eliminar la última flecha
        if (ruta.length() >= 5) {
            ruta.setLength(ruta.length() - 5);
        }

        areaResultado.setText(
                "CONEXIÓN ENCONTRADA\n\n" +
                        "Usuario inicial: " + inicio.getNombre() + "\n" +
                        "Usuario destino: " + destino.getNombre() + "\n\n" +
                        "GRADOS DE SEPARACIÓN: " +
                        resultado.getGrados() + "\n\n" +
                        "RUTA DE AMISTAD:\n" +
                        ruta
        );
    }

    // =====================================
    // CREAR TARJETAS
    // =====================================

    private JPanel crearTarjeta() {

        JPanel tarjeta = new JPanel();

        tarjeta.setLayout(new BoxLayout(
                tarjeta, BoxLayout.Y_AXIS
        ));

        tarjeta.setBackground(Paleta.TARJETA);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Paleta.BORDE),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        return tarjeta;
    }

    // =====================================
    // ETIQUETAS
    // =====================================

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setForeground(Paleta.TEXTO_SECUNDARIO);
        etiqueta.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        return etiqueta;
    }

    // =====================================
    // BOTONES
    // =====================================

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setBackground(Paleta.BOTON);
        boton.setForeground(Paleta.TEXTO);
        boton.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    // =====================================
    // PIE DE PANTALLA
    // =====================================

    private JPanel crearPie() {

        JPanel panel = new JPanel();

        panel.setBackground(Paleta.FONDO);

        JLabel texto = new JLabel(
                "RED SOCIAL | ALGORITMO RECURSIVO"
        );

        texto.setForeground(Paleta.TEXTO_SECUNDARIO);
        texto.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );

        panel.add(texto);

        return panel;
    }
}

