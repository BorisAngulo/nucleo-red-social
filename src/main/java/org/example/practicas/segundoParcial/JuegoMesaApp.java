package org.example.practicas.segundoParcial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/** Arranque del juego de mesa. */
public class JuegoMesaApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        new JuegoMesa(new Random()).jugar(entrada);
    }
}
