package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.separacion.GradosSeparacion;
import org.example.redsocial.separacion.RedAmistades;
import org.example.redsocial.separacion.ResultadoSeparacion;
import org.example.redsocial.separacion.Usuario;

/** Pantalla del botón Grados de separación. */
public class PanelGradosSeparacion extends JPanel {

    private final Runnable alVolver;
    private final RedAmistades red;
    private final GradosSeparacion grados;
    private final JComboBox<Usuario> origen;
    private final JComboBox<Usuario> destino;
    private final JTextArea resultado;

    public PanelGradosSeparacion(Runnable alVolver) {
        this.alVolver = alVolver;
        this.red = RedAmistades.ejemplo();
        this.grados = new GradosSeparacion();
        Usuario[] personas = aArreglo(red.getUsuarios());
        this.origen = combo(personas);
        this.destino = combo(personas);
        if (personas.length > 1) {
            destino.setSelectedIndex(1);
        }
        this.resultado = new JTextArea(8, 30);
        resultado.setEditable(false);
        resultado.setLineWrap(true);
        resultado.setWrapStyleWord(true);
        resultado.setBackground(Paleta.TARJETA);
        resultado.setForeground(Paleta.TEXTO);
        resultado.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        resultado.setBorder(new EmptyBorder(0, 0, 0, 0));
        armar();
    }

    private void armar() {
        setLayout(new BorderLayout());
        setBackground(Paleta.FONDO);
        setBorder(new EmptyBorder(28, 32, 32, 32));

        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBackground(Paleta.FONDO);

        columna.add(texto("Recursividad", 14, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(texto("Grados de separación", 28, Paleta.TEXTO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(texto("La cadena más corta entre dos personas, sin pasar de "
                + GradosSeparacion.LIMITE + " amigos.", 16, Paleta.TEXTO_SECUNDARIO));
        columna.add(Box.createVerticalStrut(20));
        columna.add(filaEleccion());
        columna.add(Box.createVerticalStrut(16));
        columna.add(cajaResultado());
        columna.add(Box.createVerticalStrut(16));
        columna.add(cajaRed());

        JButton volver = new JButton("Volver");
        estilo(volver);
        volver.setAlignmentX(Component.LEFT_ALIGNMENT);
        volver.addActionListener(evento -> alVolver.run());

        JScrollPane scroll = new JScrollPane(columna);
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scroll.setBackground(Paleta.FONDO);
        scroll.getViewport().setBackground(Paleta.FONDO);
        add(scroll, BorderLayout.CENTER);
        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Paleta.FONDO);
        sur.setBorder(new EmptyBorder(16, 0, 0, 0));
        sur.add(volver, BorderLayout.WEST);
        add(sur, BorderLayout.SOUTH);
    }

    private JPanel filaEleccion() {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(Paleta.FONDO);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(bloque("Desde", origen));
        fila.add(Box.createHorizontalStrut(16));
        fila.add(bloque("Hasta", destino));
        fila.add(Box.createHorizontalStrut(16));
        JButton calcular = new JButton("Calcular");
        estilo(calcular);
        calcular.addActionListener(evento -> calcular());
        fila.add(calcular);
        return fila;
    }

    private JPanel bloque(String titulo, JComboBox<Usuario> combo) {
        JPanel bloque = new JPanel();
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        bloque.setBackground(Paleta.FONDO);
        JLabel etiqueta = texto(titulo, 14, Paleta.TEXTO_SECUNDARIO);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloque.add(etiqueta);
        bloque.add(Box.createVerticalStrut(6));
        bloque.add(combo);
        return bloque;
    }

    private JPanel cajaResultado() {
        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(Paleta.TARJETA);
        caja.setBorder(bordeCaja());
        caja.setAlignmentX(Component.LEFT_ALIGNMENT);
        caja.add(resultado, BorderLayout.CENTER);
        return caja;
    }

    private JPanel cajaRed() {
        JTextArea redTexto = new JTextArea(describirRed());
        redTexto.setEditable(false);
        redTexto.setBackground(Paleta.TARJETA);
        redTexto.setForeground(Paleta.TEXTO_SECUNDARIO);
        redTexto.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        redTexto.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(Paleta.TARJETA);
        caja.setBorder(bordeCaja());
        caja.setAlignmentX(Component.LEFT_ALIGNMENT);
        caja.add(redTexto, BorderLayout.CENTER);
        return caja;
    }

    private void calcular() {
        Usuario desde = (Usuario) origen.getSelectedItem();
        Usuario hasta = (Usuario) destino.getSelectedItem();
        ResultadoSeparacion resultadoBusqueda = grados.calcular(desde, hasta);
        if (!resultadoBusqueda.encontrado()) {
            resultado.setText("No hay una cadena de " + GradosSeparacion.LIMITE
                    + " amigos o menos entre " + desde.getNombre() + " y " + hasta.getNombre() + ".");
            return;
        }
        int saltos = resultadoBusqueda.getGrados();
        StringBuilder texto = new StringBuilder();
        texto.append("Grados de separación: ").append(saltos).append(".\n");
        texto.append(cadena(resultadoBusqueda.getCamino())).append(".\n");
        if (saltos == 0) {
            texto.append("Es la misma persona.");
        } else if (saltos == 1) {
            texto.append("Son amigos directos.");
        } else {
            texto.append("Cabe en la teoría de los seis grados.");
        }
        resultado.setText(texto.toString());
    }

    private String describirRed() {
        StringBuilder texto = new StringBuilder("Amistades de la red de ejemplo\n");
        for (Usuario usuario : red.getUsuarios()) {
            texto.append(usuario.getNombre()).append(": ");
            if (usuario.getAmigos().estaVacia()) {
                texto.append("sin amigos");
            } else {
                boolean primero = true;
                for (Usuario amigo : usuario.getAmigos()) {
                    if (!primero) {
                        texto.append(", ");
                    }
                    texto.append(amigo.getNombre());
                    primero = false;
                }
            }
            texto.append('\n');
        }
        return texto.toString();
    }

    private String cadena(ListaEnlazada<Usuario> camino) {
        StringBuilder texto = new StringBuilder();
        boolean primero = true;
        for (Usuario usuario : camino) {
            if (!primero) {
                texto.append(" → ");
            }
            texto.append(usuario.getNombre());
            primero = false;
        }
        return texto.toString();
    }

    private Usuario[] aArreglo(ListaEnlazada<Usuario> usuarios) {
        Usuario[] personas = new Usuario[usuarios.tamano()];
        int indice = 0;
        for (Usuario usuario : usuarios) {
            personas[indice] = usuario;
            indice++;
        }
        return personas;
    }

    private JComboBox<Usuario> combo(Usuario[] personas) {
        JComboBox<Usuario> combo = new JComboBox<>(personas);
        combo.setBackground(Paleta.BOTON);
        combo.setForeground(Paleta.TEXTO);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        return combo;
    }

    private javax.swing.border.Border bordeCaja() {
        return javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(Paleta.BORDE),
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
        boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(12, 14, 12, 14)));
        boton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    }
}
