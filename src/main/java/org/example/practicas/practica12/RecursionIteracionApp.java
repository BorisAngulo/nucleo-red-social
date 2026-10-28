package org.example.practicas.practica12;

/** Recorrido etapa por etapa: palíndromos, potencias y el costo de cada versión. */
public class RecursionIteracionApp {

    private static final int REPETICIONES_PALINDROMO = 20_000;
    private static final int REPETICIONES_POTENCIA = 200_000;
    private static final int LONGITUD_PALINDROMO = 400;
    private static final long BASE = 2;
    private static final int EXPONENTE = 20;

    public static void main(String[] args) {
        etapa1DefinicionPalindromo();
        etapa2CasosPalindromo();
        etapa3PalindromoIterativo();
        etapa4RendimientoPalindromo();
        etapa5PotenciaRecursiva();
        etapa6PotenciaIterativa();
        etapa7RendimientoPotencia();
    }

    private static void etapa1DefinicionPalindromo() {
        seccion("Etapa 1. Palíndromo recursivo");
        System.out.println("  Un texto es palíndromo cuando sus extremos coinciden");
        System.out.println("  y el texto que queda en el centro también lo es.");
        System.out.println("  Caso base: vacío o una sola letra.");
        System.out.println("Traza de \"reconocer\"");
        VerificadorPalindromo.trazar("reconocer");
    }

    private static void etapa2CasosPalindromo() {
        seccion("Etapa 2. Casos del verificador recursivo");
        probarPalindromo("reconocer");
        probarPalindromo("Anita lava la tina");
        probarPalindromo("");
        probarPalindromo("a");
        probarPalindromo("casa");
        probarPalindromo("No es palindromo");
    }

    private static void etapa3PalindromoIterativo() {
        seccion("Etapa 3. La misma verificación con un ciclo");
        System.out.println("  Dos índices, uno al inicio y otro al final, se acercan.");
        System.out.println("  Si alguna pareja difiere, ya no es palíndromo.");
        String[] textos = {"anilina", "La ruta nos aporto otro paso natural", "java"};
        for (String texto : textos) {
            boolean recursivo = VerificadorPalindromo.recursivo(texto);
            boolean iterativo = VerificadorPalindromo.iterativo(texto);
            System.out.println("  \"" + texto + "\"");
            System.out.println("    recursivo: " + texto(recursivo) + " | iterativo: " + texto(iterativo));
        }
    }

    private static void etapa4RendimientoPalindromo() {
        seccion("Etapa 4. Rendimiento del palíndromo");
        String texto = palindromoLargo(LONGITUD_PALINDROMO);
        System.out.println("  Peor caso: palíndromo de " + LONGITUD_PALINDROMO
                + " letras, " + REPETICIONES_PALINDROMO + " repeticiones.");
        System.out.println("  El texto ya está limpio: se mide solo la verificación, no el filtrado de letras.");
        System.out.println("  Las dos comparan todas las parejas; la recursiva además apila una llamada por pareja.");
        long recursivo = medir(REPETICIONES_PALINDROMO, () -> VerificadorPalindromo.recursivoSobreLetras(texto));
        long iterativo = medir(REPETICIONES_PALINDROMO, () -> VerificadorPalindromo.iterativoSobreLetras(texto));
        informar(recursivo, iterativo);
    }

    private static void etapa5PotenciaRecursiva() {
        seccion("Etapa 5. Potencia recursiva");
        System.out.println("  base^0 = 1");
        System.out.println("  base^n = base * base^(n - 1)");
        System.out.println("Traza de 2^5");
        EvaluadorPotencia.trazar(2, 5);
        System.out.println("  2^10 = " + EvaluadorPotencia.recursivo(2, 10));
        System.out.println("  5^0 = " + EvaluadorPotencia.recursivo(5, 0));
        System.out.println("  (-3)^4 = " + EvaluadorPotencia.recursivo(-3, 4));
    }

    private static void etapa6PotenciaIterativa() {
        seccion("Etapa 6. Potencia con un ciclo");
        System.out.println("  Empieza en 1 y multiplica por la base, una vez por cada unidad del exponente.");
        long[][] casos = {{2, 10}, {5, 0}, {-3, 4}, {3, 5}};
        for (long[] caso : casos) {
            long base = caso[0];
            int exponente = (int) caso[1];
            long recursivo = EvaluadorPotencia.recursivo(base, exponente);
            long iterativo = EvaluadorPotencia.iterativo(base, exponente);
            System.out.println("  (" + base + ")^" + exponente + " recursivo = " + recursivo
                    + " | iterativo = " + iterativo);
        }
    }

    private static void etapa7RendimientoPotencia() {
        seccion("Etapa 7. Rendimiento de la potencia");
        System.out.println("  " + BASE + "^" + EXPONENTE + ", " + REPETICIONES_POTENCIA + " repeticiones.");
        System.out.println("  Las dos hacen " + EXPONENTE + " multiplicaciones. La recursiva suma " + EXPONENTE + " llamadas.");
        long recursivo = medir(REPETICIONES_POTENCIA, () -> EvaluadorPotencia.recursivo(BASE, EXPONENTE));
        long iterativo = medir(REPETICIONES_POTENCIA, () -> EvaluadorPotencia.iterativo(BASE, EXPONENTE));
        informar(recursivo, iterativo);
    }

    private static void probarPalindromo(String texto) {
        String visible = texto.isEmpty() ? "(vacío)" : texto;
        System.out.println("  \"" + visible + "\" -> " + texto(VerificadorPalindromo.recursivo(texto)));
    }

    private static String palindromoLargo(int longitud) {
        StringBuilder texto = new StringBuilder(longitud);
        int mitad = longitud / 2;
        for (int i = 0; i < mitad; i++) {
            texto.append((char) ('a' + (i % 26)));
        }
        if (longitud % 2 == 1) {
            texto.append('z');
        }
        for (int i = mitad - 1; i >= 0; i--) {
            texto.append((char) ('a' + (i % 26)));
        }
        return texto.toString();
    }

    private static long medir(int repeticiones, Runnable trabajo) {
        int calentamiento = Math.max(1, repeticiones / 20);
        for (int i = 0; i < calentamiento; i++) {
            trabajo.run();
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            trabajo.run();
        }
        return System.nanoTime() - inicio;
    }

    private static void informar(long recursivoNanos, long iterativoNanos) {
        System.out.println("  recursivo: " + millis(recursivoNanos));
        System.out.println("  iterativo: " + millis(iterativoNanos));
        if (iterativoNanos == 0) {
            return;
        }
        double veces = (double) recursivoNanos / iterativoNanos;
        System.out.printf("  la recursión tardó %.2f veces el ciclo%n", veces);
    }

    private static String millis(long nanos) {
        return String.format("%.3f ms", nanos / 1_000_000.0);
    }

    private static String texto(boolean esPalindromo) {
        return esPalindromo ? "palíndromo" : "no es palíndromo";
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
