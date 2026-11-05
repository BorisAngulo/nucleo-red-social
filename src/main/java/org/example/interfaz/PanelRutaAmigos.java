package org.example.interfaz;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.separacion.GradosSeparacion;
import org.example.redsocial.separacion.RedAmistades;
import org.example.redsocial.separacion.ResultadoSeparacion;
import org.example.redsocial.separacion.Usuario;

/** Pantalla del botón Ruta de amigos. */
public class PanelRutaAmigos extends JPanel {

    private final RedAmistades red = RedAmistades.ejemplo();
    private final GradosSeparacion grados = new GradosSeparacion();
    private final JComboBox<Usuario> origen;
    private final JComboBox<Usuario> destino;
    private final JTextArea vista = FormatoPantalla.area(12, false);

    public PanelRutaAmigos(Runnable alVolver) {
        Usuario[] personas = aArreglo(red.getUsuarios());
        this.origen = combo(personas);
        this.destino = combo(personas);
        if (personas.length > 1) {
            destino.setSelectedIndex(1);
        }
        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Recursividad", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Ruta de amigos", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "La cadena de contactos entre dos personas, de a un salto.",
                FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 20);
        columna.add(filaEleccion());
        FormatoPantalla.espacio(columna, 16);
        columna.add(FormatoPantalla.caja(vista));
        FormatoPantalla.montar(this, columna, alVolver);
        vista.setText("Elige dos personas y pulsa Ver ruta.");
    }

    private JPanel filaEleccion() {
        JPanel fila = FormatoPantalla.fila();
        fila.add(origen);
        fila.add(Box.createHorizontalStrut(12));
        fila.add(destino);
        fila.add(Box.createHorizontalStrut(12));
        JButton ver = new JButton("Ver ruta");
        FormatoPantalla.estilo(ver);
        ver.addActionListener(evento -> mostrarRuta());
        fila.add(ver);
        return fila;
    }

    private void mostrarRuta() {
        Usuario desde = (Usuario) origen.getSelectedItem();
        Usuario hasta = (Usuario) destino.getSelectedItem();
        ResultadoSeparacion resultado = grados.calcular(desde, hasta);
        if (!resultado.encontrado()) {
            vista.setText("No hay una cadena de " + GradosSeparacion.LIMITE
                    + " amigos o menos entre " + desde.getNombre()
                    + " y " + hasta.getNombre() + ".");
            return;
        }
        StringBuilder texto = new StringBuilder();
        ListaEnlazada<Usuario> camino = resultado.getCamino();
        texto.append(camino.tamano()).append(" personas, ")
                .append(resultado.getGrados()).append(" saltos.\n\n");
        Usuario previo = null;
        int paso = 1;
        for (Usuario persona : camino) {
            if (previo == null) {
                texto.append("Empieza en ").append(persona.getNombre()).append(".\n");
            } else {
                texto.append(paso).append(". ").append(previo.getNombre())
                        .append(" es amigo de ").append(persona.getNombre()).append(".\n");
                paso++;
            }
            previo = persona;
        }
        vista.setText(texto.toString());
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
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FormatoPantalla.ETIQUETA));
        return combo;
    }
}
