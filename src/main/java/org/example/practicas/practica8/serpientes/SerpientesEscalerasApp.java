package org.example.practicas.practica8.serpientes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/** Arranque de serpientes y escaleras. Pide los jugadores y reparte los turnos. */
public class SerpientesEscalerasApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        JuegoSerpientesEscaleras juego = new JuegoSerpientesEscaleras(new Random());
        juego.jugar(entrada);
    }
}
