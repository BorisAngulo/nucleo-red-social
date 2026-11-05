package org.example.interfaz;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.example.redsocial.notificaciones.BandejaAvisos;

/** Pantalla del botón Notificaciones. */
public class PanelNotificaciones extends JPanel {

    private final BandejaAvisos bandeja = new BandejaAvisos();
    private final JTextField aviso = FormatoPantalla.campo();
    private final JTextArea vista = FormatoPantalla.area(10, false);

    public PanelNotificaciones(Runnable alVolver) {
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Colas", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Notificaciones", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Llegan a la cola y se atienden en el mismo orden.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(FormatoPantalla.texto("Nuevo aviso", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(aviso);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton recibir = new JButton("Recibir");
        JButton atender = new JButton("Atender la primera");
        FormatoPantalla.estilo(recibir);
        FormatoPantalla.estilo(atender);
        recibir.addActionListener(evento -> recibir());
        atender.addActionListener(evento -> atender());
        fila.add(recibir);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(atender);
        return fila;
    }

    private void recibir() {
        String texto = aviso.getText().trim();
        if (texto.isEmpty()) {
            vista.setText("Escribe el aviso.");
            return;
        }
        bandeja.recibir(texto);
        aviso.setText("");
        refrescar();
    }

    private void atender() {
        String atendido = bandeja.atender();
        if (atendido == null) {
            vista.setText("No hay notificaciones pendientes.");
            return;
        }
        vista.setText("Atendida:\n" + atendido + "\n\n" + bandeja.listar());
    }

    private void refrescar() {
        vista.setText(bandeja.listar());
    }
}
