package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.example.redsocial.publicaciones.MuroPublicaciones;
import org.example.redsocial.publicaciones.Publicacion;

/** Pantalla del botón Publicaciones del perfil. */
public class PanelPublicaciones extends JPanel {

    private static final int ANCHO_TARJETA = 520;

    private final MuroPublicaciones muro = new MuroPublicaciones();
    private final JTextField autor = FormatoPantalla.campo();
    private final JTextArea texto = FormatoPantalla.area(3, true);
    private final JLabel aviso = FormatoPantalla.texto(" ", FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO);
    private final JPanel pilaVisual = new JPanel();

    public PanelPublicaciones(Runnable alVolver) {
        autor.setText("Ana");
        pilaVisual.setLayout(new BoxLayout(pilaVisual, BoxLayout.Y_AXIS));
        pilaVisual.setBackground(Paleta.FONDO);
        pilaVisual.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Pilas", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Publicaciones del perfil", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "La de arriba es la cima: lo último que se publicó.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(FormatoPantalla.texto("Autor", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(autor);
        FormatoPantalla.espacio(columna, 12);
        columna.add(FormatoPantalla.texto("Texto", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(FormatoPantalla.caja(texto));
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(aviso);
        FormatoPantalla.espacio(columna, 12);
        columna.add(pilaVisual);
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton publicar = new JButton("Publicar");
        JButton ver = new JButton("Ver la última");
        JButton retirar = new JButton("Retirar la última");
        FormatoPantalla.estilo(publicar);
        FormatoPantalla.estilo(ver);
        FormatoPantalla.estilo(retirar);
        publicar.addActionListener(evento -> publicar());
        ver.addActionListener(evento -> verUltima());
        retirar.addActionListener(evento -> retirar());
        fila.add(publicar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(ver);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(retirar);
        return fila;
    }

    private void publicar() {
        String nombre = autor.getText().trim();
        String contenido = texto.getText().trim();
        if (nombre.isEmpty() || contenido.isEmpty()) {
            aviso.setText("Escribe el autor y el texto.");
            return;
        }
        muro.publicar(nombre, contenido);
        texto.setText("");
        aviso.setText("Entró a la cima.");
        refrescar();
    }

    private void verUltima() {
        Publicacion ultima = muro.verUltima();
        if (ultima == null) {
            aviso.setText("Todavía no hay publicaciones.");
            return;
        }
        aviso.setText("Cima: " + ultima.getAutor() + " — " + ultima.getTexto());
    }

    private void retirar() {
        Publicacion retirada = muro.retirarUltima();
        if (retirada == null) {
            aviso.setText("No hay nada que retirar.");
            refrescar();
            return;
        }
        aviso.setText("Se retiró: " + retirada.getAutor() + " — " + retirada.getTexto());
        refrescar();
    }

    private void refrescar() {
        pilaVisual.removeAll();
        if (muro.tamano() == 0) {
            pilaVisual.add(FormatoPantalla.texto(
                    "La pila está vacía.", FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        } else {
            boolean cima = true;
            for (Publicacion publicacion : muro.desdeLaCima()) {
                pilaVisual.add(tarjeta(publicacion, cima));
                pilaVisual.add(Box.createVerticalStrut(12));
                cima = false;
            }
        }
        pilaVisual.revalidate();
        pilaVisual.repaint();
    }

    private JPanel tarjeta(Publicacion publicacion, boolean cima) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
        tarjeta.setBackground(cima ? Paleta.BOTON : Paleta.TARJETA);
        tarjeta.setBorder(bordeTarjeta(cima));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel marca = new JLabel(cima ? "Cima" : "");
        marca.setForeground(Paleta.TEXTO_SECUNDARIO);
        marca.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        JLabel nombre = new JLabel(escapar(publicacion.getAutor()));
        nombre.setForeground(Paleta.TEXTO);
        nombre.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JLabel cuerpo = new JLabel("<html><body style='width:460px'>"
                + escapar(publicacion.getTexto()) + "</body></html>");
        cuerpo.setForeground(Paleta.TEXTO);
        cuerpo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        JPanel cabeza = new JPanel(new BorderLayout());
        cabeza.setOpaque(false);
        cabeza.add(nombre, BorderLayout.WEST);
        cabeza.add(marca, BorderLayout.EAST);

        tarjeta.add(cabeza, BorderLayout.NORTH);
        tarjeta.add(cuerpo, BorderLayout.CENTER);

        Dimension alto = tarjeta.getPreferredSize();
        tarjeta.setMaximumSize(new Dimension(ANCHO_TARJETA, alto.height));
        tarjeta.setPreferredSize(new Dimension(ANCHO_TARJETA, alto.height));
        return tarjeta;
    }

    private Border bordeTarjeta(boolean cima) {
        return javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(cima ? Paleta.TEXTO_SECUNDARIO : Paleta.BORDE, cima ? 2 : 1),
                new javax.swing.border.EmptyBorder(16, 18, 16, 18));
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
