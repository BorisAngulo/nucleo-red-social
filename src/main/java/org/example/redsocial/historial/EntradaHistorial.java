package org.example.redsocial.historial;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Representa una entrada en el historial de navegación (página, perfil o búsqueda).
 */
public class EntradaHistorial {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String destino;
    private final String tipo; // "Página", "Búsqueda", "Perfil", "Tendencia"
    private final String seguridad;
    private final String horaVisita;

    public EntradaHistorial(String destino, String tipo, String seguridad) {
        this.destino = normalizarDestino(destino);
        this.tipo = (tipo != null && !tipo.isBlank()) ? tipo : "Página";
        this.seguridad = (seguridad != null && !seguridad.isBlank()) ? seguridad.toLowerCase() : "https";
        this.horaVisita = LocalDateTime.now().format(FORMATO_HORA);
    }

    public EntradaHistorial(String destino, String tipo) {
        this(destino, tipo, "https");
    }

    public EntradaHistorial(String destino) {
        this(destino, "Página", "https");
    }

    private String normalizarDestino(String raw) {
        if (raw == null) return "";
        String limpia = raw.trim();
        if (limpia.startsWith("https://")) return limpia.substring(8);
        if (limpia.startsWith("http://")) return limpia.substring(7);
        return limpia;
    }

    public String getDestino() {
        return destino;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSeguridad() {
        return seguridad;
    }

    public String getHoraVisita() {
        return horaVisita;
    }

    public String getUrlCompleta() {
        if (tipo.equalsIgnoreCase("Búsqueda")) {
            return "redsocial://buscar?q=" + destino;
        }
        if (tipo.equalsIgnoreCase("Perfil")) {
            return "redsocial://perfiles/" + destino;
        }
        return seguridad + "://" + destino;
    }

    public String getIcono() {
        return switch (tipo.toLowerCase()) {
            case "perfil" -> "👤";
            case "búsqueda", "busqueda" -> "🔍";
            case "tendencia" -> "🔥";
            default -> "🌐";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntradaHistorial that = (EntradaHistorial) o;
        return Objects.equals(destino, that.destino) &&
               Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destino, tipo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s a las %s)", getIcono(), destino, tipo, horaVisita);
    }
}

