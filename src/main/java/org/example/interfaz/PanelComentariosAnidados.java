package org.example.interfaz;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.example.redsocial.anidados.ComentarioAnidado;
import org.example.redsocial.anidados.HiloAnidado;

/** Pantalla del botón Borrar comentarios anidados. */
public class PanelComentariosAnidados extends JPanel {

    private final HiloAnidado hilo = new HiloAnidado();
    private final JTextField autor = FormatoPantalla.campo();
    private final JTextField texto = FormatoPantalla.campo();
    private final JTextField idPadre = FormatoPantalla.campo();
    private final JTextField idBorrar = FormatoPantalla.campo();
    private final JTextArea vista = FormatoPantalla.area(12, false);

    public PanelComentariosAnidados(Runnable alVolver) {
        autor.setText("Ana");
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Recursividad", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Borrar comentarios anidados", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Al borrar uno, primero se quitan todas sus respuestas.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(FormatoPantalla.texto("Autor", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(autor);
        FormatoPantalla.espacio(columna, 12);
        columna.add(FormatoPantalla.texto("Texto", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(texto);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAlta());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.texto("Número del comentario a borrar", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(idBorrar);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaBorrar());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar("");
    }

    private JPanel filaAlta() {
        JPanel fila = FormatoPantalla.fila();
        idPadre.setMaximumSize(new java.awt.Dimension(120, 44));
        JButton agregar = new JButton("Agregar");
        JButton responder = new JButton("Responder al número");
        FormatoPantalla.estilo(agregar);
        FormatoPantalla.estilo(responder);
        agregar.addActionListener(evento -> agregar());
        responder.addActionListener(evento -> responder());
        fila.add(agregar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(idPadre);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(responder);
        return fila;
    }

    private JPanel filaBorrar() {
        JPanel fila = FormatoPantalla.fila();
        JButton borrar = new JButton("Borrar con sus respuestas");
        FormatoPantalla.estilo(borrar);
        borrar.addActionListener(evento -> borrar());
        fila.add(borrar);
        return fila;
    }

    private void agregar() {
        try {
            ComentarioAnidado comentario = hilo.agregar(autor.getText(), texto.getText());
            texto.setText("");
            refrescar("Entró el comentario #" + comentario.getId() + ".\n\n");
        } catch (IllegalArgumentException error) {
            vista.setText(error.getMessage());
        }
    }

    private void responder() {
        try {
            int id = Integer.parseInt(idPadre.getText().trim());
            ComentarioAnidado respuesta = hilo.responder(id, autor.getText(), texto.getText());
            if (respuesta == null) {
                vista.setText("No hay un comentario con el número " + id + ".");
                return;
            }
            texto.setText("");
            refrescar("La respuesta #" + respuesta.getId() + " quedó dentro de #" + id + ".\n\n");
        } catch (NumberFormatException error) {
            vista.setText("El número del comentario tiene que ser un entero.");
        } catch (IllegalArgumentException error) {
            vista.setText(error.getMessage());
        }
    }

    private void borrar() {
        try {
            int id = Integer.parseInt(idBorrar.getText().trim());
            int borrados = hilo.borrar(id);
            if (borrados == 0) {
                vista.setText("No hay un comentario con el número " + id + ".");
                return;
            }
            String mensaje = borrados == 1
                    ? "Se borró el comentario #" + id + ".\n\n"
                    : "Se borró el comentario #" + id + " y " + (borrados - 1) + " respuestas.\n\n";
            refrescar(mensaje);
        } catch (NumberFormatException error) {
            vista.setText("El número del comentario tiene que ser un entero.");
        }
    }

    private void refrescar(String aviso) {
        vista.setText(aviso + hilo.listar());
    }
}
