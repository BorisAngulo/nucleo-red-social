package org.example.interfaz;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.hashtags.IndiceHashtags;

/** Pantalla del botón Recorrer hashtags. */
public class PanelRecorrerHashtags extends JPanel {

    private final IndiceHashtags indice;
    private final JTextArea vista = FormatoPantalla.area(12, false);

    public PanelRecorrerHashtags(IndiceHashtags indice, Runnable alVolver) {
        this.indice = indice;
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Árboles", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Recorrer hashtags", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Inorden los lista de la A a la Z. Postorden los saca empezando por las hojas.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton listar = new JButton("Listar en inorden");
        JButton vaciar = new JButton("Vaciar en postorden");
        FormatoPantalla.estilo(listar);
        FormatoPantalla.estilo(vaciar);
        listar.addActionListener(evento -> refrescar());
        vaciar.addActionListener(evento -> vaciar());
        fila.add(listar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(vaciar);
        return fila;
    }

    private void vaciar() {
        if (indice.estaVacio()) {
            vista.setText("El árbol ya está vacío.");
            return;
        }
        ListaEnlazada<String> orden = indice.vaciarEnPostorden();
        StringBuilder texto = new StringBuilder();
        texto.append("Se vació en postorden:\n");
        int paso = 1;
        for (String hashtag : orden) {
            texto.append(paso).append(". #").append(hashtag).append('\n');
            paso++;
        }
        texto.append("\nEl árbol quedó vacío. Puedes cargar etiquetas de nuevo en Índice de hashtags.");
        vista.setText(texto.toString());
    }

    public void refrescar() {
        if (indice.estaVacio()) {
            vista.setText("El árbol está vacío.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        texto.append("Inorden:\n");
        for (String hashtag : indice.enOrden()) {
            texto.append("• #").append(hashtag).append('\n');
        }
        vista.setText(texto.toString());
    }
}
