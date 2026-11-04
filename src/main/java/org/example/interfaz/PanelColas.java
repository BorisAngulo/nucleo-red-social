package org.example.interfaz;

import org.example.redsocial.colas.CarruselHistorias;
import org.example.redsocial.colas.Historia;
import org.example.redsocial.colas.Notificacion;
import org.example.redsocial.colas.ProcesadorNotificaciones;

import javax.swing.*;
import java.awt.*;

public class PanelColas extends JPanel {

    private final ProcesadorNotificaciones procesador = new ProcesadorNotificaciones();
    private final CarruselHistorias carrusel = new CarruselHistorias();

    private final JTextArea txtNotificaciones = new JTextArea(8, 30);
    private final JLabel lblHistoriaActual = new JLabel("Sin historias", SwingConstants.CENTER);

    public PanelColas(Runnable alVolver) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Paleta.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JButton btnVolver = new JButton("< Volver");
        btnVolver.addActionListener(e -> alVolver.run());
        topPanel.add(btnVolver, BorderLayout.WEST);

        JLabel titulo = new JLabel("Modulo 3: Colas (Notificaciones e Historias)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Paleta.TEXTO);
        topPanel.add(titulo, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 15, 15));
        centro.setOpaque(false);

        JPanel pnlNotif = new JPanel(new BorderLayout(5, 5));
        pnlNotif.setBorder(BorderFactory.createTitledBorder("Cola Simple: Notificaciones"));
        
        txtNotificaciones.setEditable(false);
        pnlNotif.add(new JScrollPane(txtNotificaciones), BorderLayout.CENTER);

        JPanel pnlBotonesNotif = new JPanel(new FlowLayout());
        JTextField txtUsuario = new JTextField(6);
        JTextField txtAccion = new JTextField(10);
        JButton btnAgregarNotif = new JButton("Agregar");
        JButton btnProcesarNotif = new JButton("Procesar Siguiente");

        pnlBotonesNotif.add(new JLabel("Usu:"));
        pnlBotonesNotif.add(txtUsuario);
        pnlBotonesNotif.add(new JLabel("Accion:"));
        pnlBotonesNotif.add(txtAccion);
        pnlBotonesNotif.add(btnAgregarNotif);
        pnlBotonesNotif.add(btnProcesarNotif);

        pnlNotif.add(pnlBotonesNotif, BorderLayout.SOUTH);

        btnAgregarNotif.addActionListener(e -> {
            if (!txtUsuario.getText().isEmpty() && !txtAccion.getText().isEmpty()) {
                procesador.agregar(txtUsuario.getText(), txtAccion.getText());
                actualizarNotificaciones();
                txtUsuario.setText("");
                txtAccion.setText("");
            }
        });

        btnProcesarNotif.addActionListener(e -> {
            Notificacion p = procesador.procesarSiguiente();
            if (p != null) {
                JOptionPane.showMessageDialog(this, "Procesada: " + p);
            } else {
                JOptionPane.showMessageDialog(this, "No hay notificaciones pendientes.");
            }
            actualizarNotificaciones();
        });

        JPanel pnlHistorias = new JPanel(new BorderLayout(5, 5));
        pnlHistorias.setBorder(BorderFactory.createTitledBorder("Cola Circular: Carrusel Historias"));

        lblHistoriaActual.setFont(new Font("SansSerif", Font.BOLD, 14));
        pnlHistorias.add(lblHistoriaActual, BorderLayout.CENTER);

        JPanel pnlBotonesHist = new JPanel(new FlowLayout());
        JTextField txtUsuarioH = new JTextField(6);
        JTextField txtContenidoH = new JTextField(10);
        JButton btnAgregarH = new JButton("Subir Historia");
        JButton btnSiguienteH = new JButton("Ver Siguiente >");

        pnlBotonesHist.add(new JLabel("Usu:"));
        pnlBotonesHist.add(txtUsuarioH);
        pnlBotonesHist.add(new JLabel("Txt:"));
        pnlBotonesHist.add(txtContenidoH);
        pnlBotonesHist.add(btnAgregarH);
        pnlBotonesHist.add(btnSiguienteH);

        pnlHistorias.add(pnlBotonesHist, BorderLayout.SOUTH);

        btnAgregarH.addActionListener(e -> {
            if (!txtUsuarioH.getText().isEmpty() && !txtContenidoH.getText().isEmpty()) {
                carrusel.agregar(txtUsuarioH.getText(), txtContenidoH.getText());
                actualizarHistoriaActual();
                txtUsuarioH.setText("");
                txtContenidoH.setText("");
            }
        });

        btnSiguienteH.addActionListener(e -> {
            Historia h = carrusel.siguienteHistoria();
            if (h != null) {
                lblHistoriaActual.setText("<html><center><b>" + h.getUsuario() + "</b><br>" + h.getContenido() + "</center></html>");
            } else {
                lblHistoriaActual.setText("Sin historias");
            }
        });

        centro.add(pnlNotif);
        centro.add(pnlHistorias);

        add(centro, BorderLayout.CENTER);
    }

    private void actualizarNotificaciones() {
        txtNotificaciones.setText(procesador.listarPendientes());
    }

    private void actualizarHistoriaActual() {
        Historia h = carrusel.obtenerActual();
        if (h != null) {
            lblHistoriaActual.setText("<html><center><b>" + h.getUsuario() + "</b><br>" + h.getContenido() + "</center></html>");
        } else {
            lblHistoriaActual.setText("Sin historias");
        }
    }
}