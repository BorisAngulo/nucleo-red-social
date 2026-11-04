package org.example.estructuradedatos;

final class Texto {

    private Texto() {
    }

    static <T> String de(Iterable<T> elementos) {
        StringBuilder texto = new StringBuilder("[");
        boolean primero = true;
        for (T dato : elementos) {
            if (!primero) {
                texto.append(", ");
            }
            texto.append(dato);
            primero = false;
        }
        texto.append(']');
        return texto.toString();
    }
}
