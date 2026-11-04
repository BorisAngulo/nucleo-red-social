package org.example.practicas.juegoRPG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Arranque del combate por turnos. */
public class JuegoRpgApp {

    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        new JuegoRpg().jugar(entrada);
    }
}
