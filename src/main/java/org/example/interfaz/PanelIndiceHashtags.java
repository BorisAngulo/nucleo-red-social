package org.example.interfaz;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.hashtags.IndiceHashtags;

/** Pantalla del botón Índice de hashtags. */
public class PanelIndiceHashtags extends JPanel {

    private final IndiceHashtags indice;
    private final JTextField etiqueta = FormatoPantalla.campo();
    private final JTextArea vista = FormatoPantalla.area(10, false);

    public PanelIndiceHashtags(IndiceHashtags indice, Runnable alVolver) {
        this.indice = indice;
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Árboles", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Índice de hashtags", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "El árbol guarda las tendencias en orden. Escribe el inicio y aparecen las que coinciden.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(FormatoPantalla.texto("Hashtag o inicio", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(etiqueta);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton agregar = new JButton("Agregar");
        JButton sugerir = new JButton("Autocompletar");
        FormatoPantalla.estilo(agregar);
        FormatoPantalla.estilo(sugerir);
        agregar.addActionListener(evento -> agregar());
        sugerir.addActionListener(evento -> sugerir());
        fila.add(agregar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(sugerir);
        return fila;
    }

    private void agregar() {
        String texto = etiqueta.getText().trim();
        if (texto.isEmpty()) {
            vista.setText("Escribe un hashtag.");
            return;
        }
        if (!indice.agregar(texto)) {
            vista.setText("Ese hashtag ya está en el árbol, o quedó vacío.\n\n" + listado());
            return;
        }
        etiqueta.setText("");
        refrescar();
    }

    private void sugerir() {
        String prefijo = etiqueta.getText().trim();
        if (prefijo.isEmpty()) {
            vista.setText("Escribe el inicio del hashtag.\n\n" + listado());
            return;
        }
        ListaEnlazada<String> coincidencias = indice.sugerir(prefijo);
        if (coincidencias.estaVacia()) {
            vista.setText("Nada empieza por \"" + prefijo + "\".\n\n" + listado());
            return;
        }
        StringBuilder texto = new StringBuilder();
        texto.append("Sugerencias para \"").append(prefijo).append("\":\n");
        for (String hashtag : coincidencias) {
            texto.append("• #").append(hashtag).append('\n');
        }
        texto.append('\n').append(listado());
        vista.setText(texto.toString());
    }

    public void refrescar() {
        vista.setText(listado());
    }

    private String listado() {
        if (indice.estaVacio()) {
            return "El árbol está vacío.";
        }
        StringBuilder texto = new StringBuilder();
        texto.append("En orden alfabético (").append(indice.tamano()).append("):\n");
        for (String hashtag : indice.enOrden()) {
            texto.append("• #").append(hashtag).append('\n');
        }
        return texto.toString();
    }
}
