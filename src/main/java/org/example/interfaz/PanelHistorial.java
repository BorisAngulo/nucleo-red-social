package org.example.interfaz;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.example.redsocial.historial.HistorialNavegacion;

/** Pantalla del botón Historial de navegación. */
public class PanelHistorial extends JPanel {

    private final HistorialNavegacion historial = new HistorialNavegacion();
    private final JTextField pagina = FormatoPantalla.campo();
    private final JTextArea vista = FormatoPantalla.area(10, false);

    public PanelHistorial(Runnable alVolver) {
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Pilas", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Historial de navegación", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Cada visita se apila. Atrás saca la última y vuelve a la anterior.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(FormatoPantalla.texto("Página o búsqueda", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(pagina);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton abrir = new JButton("Abrir");
        JButton atras = new JButton("Atrás");
        FormatoPantalla.estilo(abrir);
        FormatoPantalla.estilo(atras);
        abrir.addActionListener(evento -> abrir());
        atras.addActionListener(evento -> atras());
        fila.add(abrir);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(atras);
        return fila;
    }

    private void abrir() {
        String destino = pagina.getText().trim();
        if (destino.isEmpty()) {
            vista.setText("Escribe una página o una búsqueda.");
            return;
        }
        historial.visitar(destino);
        pagina.setText("");
        refrescar();
    }

    private void atras() {
        String ahora = historial.atras();
        if (ahora == null && historial.tamano() == 0) {
            vista.setText("El historial está vacío.");
            return;
        }
        refrescar();
    }

    private void refrescar() {
        vista.setText(historial.listar());
    }
}
