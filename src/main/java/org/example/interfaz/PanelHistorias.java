package org.example.interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.historias.CarruselHistorias;
import org.example.redsocial.historias.Historia;

/** Pantalla del botón Historias activas. */
public class PanelHistorias extends JPanel {

    private final CarruselHistorias carrusel = new CarruselHistorias();
    private final JTextField autor = FormatoPantalla.campo();
    private final JTextField texto = FormatoPantalla.campo();
    private final JTextField horas = FormatoPantalla.campo();
    private final JLabel aviso = FormatoPantalla.texto(
            "La grande es la que está en pantalla.", FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO);
    private final JPanel tira = new JPanel(null);
    private final ListaEnlazada<Marco> marcos = new ListaEnlazada<>();

    private Timer animacion;
    private boolean animando;

    public PanelHistorias(Runnable alVolver) {
        autor.setText("Ana");
        horas.setText("24");
        tira.setBackground(Paleta.FONDO);
        tira.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel columna = FormatoPantalla.columna();
        columna.add(FormatoPantalla.texto("Colas", FormatoPantalla.GRUPO, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto("Historias activas", FormatoPantalla.TITULO, Paleta.TEXTO));
        FormatoPantalla.espacio(columna, 8);
        columna.add(FormatoPantalla.texto(
                "Lista circular: al pasar la última, el cuadro grande vuelve a ser el primero.",
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
        columna.add(FormatoPantalla.texto("Horas (1 a 24)", FormatoPantalla.ETIQUETA, Paleta.TEXTO_SECUNDARIO));
        FormatoPantalla.espacio(columna, 6);
        columna.add(horas);
        FormatoPantalla.espacio(columna, 12);
        columna.add(filaAcciones());
        FormatoPantalla.espacio(columna, 16);
        columna.add(aviso);
        FormatoPantalla.espacio(columna, 12);
        columna.add(tira);
        FormatoPantalla.montar(this, columna, alVolver);
        refrescar();
    }

    private JPanel filaAcciones() {
        JPanel fila = FormatoPantalla.fila();
        JButton publicar = new JButton("Publicar");
        JButton siguiente = new JButton("Siguiente");
        JButton quitar = new JButton("Quitar");
        JButton hora = new JButton("Pasar una hora");
        FormatoPantalla.estilo(publicar);
        FormatoPantalla.estilo(siguiente);
        FormatoPantalla.estilo(quitar);
        FormatoPantalla.estilo(hora);
        publicar.addActionListener(evento -> publicar());
        siguiente.addActionListener(evento -> siguiente());
        quitar.addActionListener(evento -> quitar());
        hora.addActionListener(evento -> pasarHora());
        fila.add(publicar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(siguiente);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(quitar);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(hora);
        return fila;
    }

    private void publicar() {
        if (animando) {
            return;
        }
        try {
            int duracion = Integer.parseInt(horas.getText().trim());
            carrusel.publicar(autor.getText(), texto.getText(), duracion);
            texto.setText("");
            aviso.setText("Entró al final de la vuelta.");
            refrescar();
            crecerUltima();
        } catch (NumberFormatException error) {
            aviso.setText("Las horas tienen que ser un número entero.");
        } catch (IllegalArgumentException error) {
            aviso.setText(error.getMessage());
        }
    }

    private void siguiente() {
        if (animando) {
            return;
        }
        int cantidad = carrusel.tamano();
        if (cantidad == 0) {
            aviso.setText("No hay historias activas.");
            return;
        }
        if (cantidad == 1) {
            aviso.setText("Solo hay una historia en la vuelta.");
            return;
        }
        int indice = 0;
        for (Marco marco : marcos) {
            marco.inicio = marco.panel.getBounds();
            int destino = indice == 0 ? cantidad - 1 : indice - 1;
            marco.fin = ranura(destino);
            indice++;
        }
        animar(() -> {
            carrusel.siguiente();
            aviso.setText("Ahora se ve la historia de " + carrusel.actual().getAutor() + ".");
            refrescar();
        });
    }

    private void quitar() {
        if (animando) {
            return;
        }
        if (carrusel.tamano() == 0) {
            aviso.setText("No hay historias activas.");
            return;
        }
        int indice = 0;
        for (Marco marco : marcos) {
            marco.inicio = marco.panel.getBounds();
            if (indice == 0) {
                marco.fin = new Rectangle(marco.inicio.x, -260, marco.inicio.width, marco.inicio.height);
            } else {
                marco.fin = ranura(indice - 1);
            }
            indice++;
        }
        animar(() -> {
            Historia quitada = carrusel.quitarActual();
            aviso.setText("Se quitó la historia de " + quitada.getAutor() + ".");
            refrescar();
        });
    }

    private void pasarHora() {
        if (animando) {
            return;
        }
        if (carrusel.tamano() == 0) {
            aviso.setText("No hay historias activas.");
            return;
        }
        boolean venceAlguna = false;
        for (Marco marco : marcos) {
            marco.inicio = marco.panel.getBounds();
            if (marco.historia.getHoras() <= 1) {
                marco.fin = new Rectangle(marco.inicio.x, 300, marco.inicio.width, marco.inicio.height);
                venceAlguna = true;
            } else {
                marco.fin = marco.inicio;
            }
        }
        if (!venceAlguna) {
            carrusel.pasarHora();
            aviso.setText("Pasó una hora. Todas siguen activas.");
            refrescar();
            return;
        }
        animar(() -> {
            ListaEnlazada<Historia> vencidas = carrusel.pasarHora();
            StringBuilder mensaje = new StringBuilder("Vencieron: ");
            boolean primera = true;
            for (Historia historia : vencidas) {
                if (!primera) {
                    mensaje.append(", ");
                }
                mensaje.append(historia.getAutor());
                primera = false;
            }
            aviso.setText(mensaje.toString());
            refrescar();
        });
    }

    private void crecerUltima() {
        if (marcos.estaVacia()) {
            return;
        }
        Marco ultima = null;
        for (Marco marco : marcos) {
            ultima = marco;
        }
        Rectangle fin = ultima.panel.getBounds();
        ultima.inicio = new Rectangle(
                fin.x + fin.width / 2, fin.y + fin.height / 2, 1, 1);
        ultima.fin = fin;
        ultima.panel.setBounds(ultima.inicio);
        animar(this::refrescar);
    }

    private void refrescar() {
        tira.removeAll();
        marcos.vaciar();
        ListaEnlazada<Historia> orden = carrusel.enOrden();
        if (orden.estaVacia()) {
            ajustarTira(0);
            JLabel vacio = FormatoPantalla.texto(
                    "No hay historias activas.", FormatoPantalla.CUERPO, Paleta.TEXTO_SECUNDARIO);
            vacio.setBounds(12, 24, 400, 32);
            tira.add(vacio);
        } else {
            ajustarTira(orden.tamano());
            int indice = 0;
            for (Historia historia : orden) {
                Rectangle lugar = ranura(indice);
                JPanel cuadro = cuadro(historia, indice == 0, lugar.width);
                cuadro.setBounds(lugar);
                tira.add(cuadro);
                Marco marco = new Marco();
                marco.historia = historia;
                marco.panel = cuadro;
                marco.inicio = lugar;
                marco.fin = lugar;
                marcos.insertarAlFinal(marco);
                indice++;
            }
        }
        tira.revalidate();
        tira.repaint();
    }

    private void ajustarTira(int cantidad) {
        int ancho = cantidad == 0 ? 640 : 276 + Math.max(0, cantidad - 1) * 164 + 24;
        int alto = cantidad == 0 ? 72 : 280;
        Dimension tamano = new Dimension(ancho, alto);
        tira.setPreferredSize(tamano);
        tira.setMinimumSize(new Dimension(480, alto));
        tira.setMaximumSize(new Dimension(Math.max(ancho, 2400), alto));
    }

    private Rectangle ranura(int indiceVisual) {
        if (indiceVisual == 0) {
            return new Rectangle(12, 16, 240, 240);
        }
        return new Rectangle(276 + (indiceVisual - 1) * 164, 61, 150, 150);
    }

    private JPanel cuadro(Historia historia, boolean alFrente, int lado) {
        JPanel cuadro = new JPanel(new BorderLayout(0, 8));
        cuadro.setBackground(alFrente ? Paleta.BOTON : Paleta.TARJETA);
        cuadro.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        alFrente ? Paleta.TEXTO_SECUNDARIO : Paleta.BORDE, alFrente ? 2 : 1),
                new javax.swing.border.EmptyBorder(12, 12, 12, 12)));

        JLabel inicial = new JLabel(historia.getAutor().substring(0, 1).toUpperCase());
        inicial.setForeground(Paleta.TEXTO);
        inicial.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, alFrente ? 42 : 28));

        JLabel nombre = new JLabel(escapar(historia.getAutor()));
        nombre.setForeground(Paleta.TEXTO);
        nombre.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, alFrente ? 18 : 14));

        JLabel tiempo = new JLabel(historia.getHoras() + " h");
        tiempo.setForeground(Paleta.TEXTO_SECUNDARIO);
        tiempo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, alFrente ? 16 : 13));

        JPanel cabeza = new JPanel(new BorderLayout());
        cabeza.setOpaque(false);
        cabeza.add(inicial, BorderLayout.WEST);
        cabeza.add(tiempo, BorderLayout.EAST);

        int anchoTexto = Math.max(80, lado - 48);
        JLabel cuerpo = new JLabel("<html><body style='width:" + anchoTexto + "px'>"
                + escapar(historia.getTexto()) + "</body></html>");
        cuerpo.setForeground(Paleta.TEXTO);
        cuerpo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, alFrente ? 16 : 13));

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.setOpaque(false);
        centro.add(nombre, BorderLayout.NORTH);
        centro.add(cuerpo, BorderLayout.CENTER);

        cuadro.add(cabeza, BorderLayout.NORTH);
        cuadro.add(centro, BorderLayout.CENTER);
        cuadro.add(barra(historia.getHoras()), BorderLayout.SOUTH);
        return cuadro;
    }

    private JPanel barra(int horasRestantes) {
        JPanel barra = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics graficos) {
                super.paintComponent(graficos);
                graficos.setColor(Paleta.BORDE);
                graficos.fillRect(0, 0, getWidth(), getHeight());
                int lleno = getWidth() * horasRestantes / 24;
                graficos.setColor(Paleta.TEXTO);
                graficos.fillRect(0, 0, lleno, getHeight());
            }
        };
        barra.setPreferredSize(new Dimension(10, 8));
        barra.setOpaque(false);
        return barra;
    }

    private void animar(Runnable alTerminar) {
        if (animacion != null) {
            animacion.stop();
        }
        animando = true;
        final int[] paso = {0};
        final int pasos = 16;
        animacion = new Timer(16, evento -> {
            paso[0]++;
            double avance = Math.min(1, paso[0] / (double) pasos);
            avance = 1 - (1 - avance) * (1 - avance);
            for (Marco marco : marcos) {
                marco.panel.setBounds(interpolar(marco.inicio, marco.fin, avance));
                marco.panel.revalidate();
            }
            tira.repaint();
            if (paso[0] >= pasos) {
                animacion.stop();
                animando = false;
                alTerminar.run();
            }
        });
        animacion.start();
    }

    private Rectangle interpolar(Rectangle inicio, Rectangle fin, double avance) {
        return new Rectangle(
                (int) (inicio.x + (fin.x - inicio.x) * avance),
                (int) (inicio.y + (fin.y - inicio.y) * avance),
                Math.max(1, (int) (inicio.width + (fin.width - inicio.width) * avance)),
                Math.max(1, (int) (inicio.height + (fin.height - inicio.height) * avance)));
    }

    private String escapar(String valor) {
        return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static final class Marco {
        private Historia historia;
        private JPanel panel;
        private Rectangle inicio;
        private Rectangle fin;
    }
}
