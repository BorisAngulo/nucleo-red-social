package org.example.practicas.practica12;

/**
 * Verifica palíndromos.
 * Recursivo: los extremos coinciden y el centro también lo es.
 * Iterativo: dos índices se acercan hasta cruzarse.
 * Espacios y mayúsculas no cuentan: solo se comparan las letras.
 */
public class VerificadorPalindromo {

    private VerificadorPalindromo() {
    }

    public static boolean recursivo(String texto) {
        return recursivoSobreLetras(letras(texto));
    }

    public static boolean iterativo(String texto) {
        return iterativoSobreLetras(letras(texto));
    }

    /** Compara extremos sobre un texto que ya está en letras minúsculas. */
    static boolean recursivoSobreLetras(String letras) {
        return recursivo(letras, 0, letras.length() - 1);
    }

    static boolean iterativoSobreLetras(String letras) {
        int izquierda = 0;
        int derecha = letras.length() - 1;
        while (izquierda < derecha) {
            if (letras.charAt(izquierda) != letras.charAt(derecha)) {
                return false;
            }
            izquierda++;
            derecha--;
        }
        return true;
    }

    /** Muestra cada pareja de extremos y el caso base. */
    public static boolean trazar(String texto) {
        String letras = letras(texto);
        System.out.println("  letras: \"" + letras + "\"");
        return trazar(letras, 0, letras.length() - 1, 1);
    }

    private static boolean recursivo(String letras, int izquierda, int derecha) {
        if (izquierda >= derecha) {
            return true;
        }
        if (letras.charAt(izquierda) != letras.charAt(derecha)) {
            return false;
        }
        return recursivo(letras, izquierda + 1, derecha - 1);
    }

    private static boolean trazar(String letras, int izquierda, int derecha, int nivel) {
        String sangria = "  ".repeat(nivel);
        if (izquierda >= derecha) {
            String motivo = letras.isEmpty() ? "cadena vacía" : "centro \"" + letras.charAt(izquierda) + "\"";
            System.out.println(sangria + motivo + " -> sí");
            return true;
        }
        char a = letras.charAt(izquierda);
        char b = letras.charAt(derecha);
        System.out.println(sangria + "'" + a + "' con '" + b + "'");
        if (a != b) {
            System.out.println(sangria + "-> no");
            return false;
        }
        boolean centro = trazar(letras, izquierda + 1, derecha - 1, nivel + 1);
        System.out.println(sangria + "-> " + (centro ? "sí" : "no"));
        return centro;
    }

    static String letras(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("El texto no puede ser nulo.");
        }
        StringBuilder limpias = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (Character.isLetter(caracter)) {
                limpias.append(Character.toLowerCase(caracter));
            }
        }
        return limpias.toString();
    }
}
