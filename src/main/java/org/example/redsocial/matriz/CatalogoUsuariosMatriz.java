package org.example.redsocial.matriz;

import org.example.estructuradedatos.ListaEnlazada;

/** Administra usuarios y sus conexiones directas mediante una matriz NxN. */
public class CatalogoUsuariosMatriz {

    private final ListaEnlazada<UsuarioMatriz> usuarios = new ListaEnlazada<>();
    private boolean[][] matriz = new boolean[0][0];

    public UsuarioMatriz agregarUsuario(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        String nombreLimpio = nombre.trim();
        for (UsuarioMatriz usuario : usuarios) {
            if (usuario.getNombre().equalsIgnoreCase(nombreLimpio)) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre.");
            }
        }

        UsuarioMatriz nuevo = new UsuarioMatriz(nombreLimpio);
        int nuevoTamano = usuarios.tamano() + 1;
        boolean[][] ampliada = new boolean[nuevoTamano][nuevoTamano];
        copiarMatriz(matriz, ampliada, -1, -1);
        usuarios.insertarAlFinal(nuevo);
        matriz = ampliada;
        return nuevo;
    }

    public boolean quitarUsuario(UsuarioMatriz usuario) {
        int indice = indiceDe(usuario);
        if (indice < 0) {
            return false;
        }

        int nuevoTamano = usuarios.tamano() - 1;
        boolean[][] reducida = new boolean[nuevoTamano][nuevoTamano];
        copiarMatriz(matriz, reducida, indice, indice);
        usuarios.eliminarEn(indice);
        matriz = reducida;
        return true;
    }

    /** Registra una conexión dirigida: el usuario origen sigue al destino. */
    public void seguir(UsuarioMatriz origen, UsuarioMatriz destino) {
        validarExtremos(origen, destino);
        matriz[indiceValidado(origen)][indiceValidado(destino)] = true;
    }

    public boolean dejarDeSeguir(UsuarioMatriz origen, UsuarioMatriz destino) {
        validarExtremos(origen, destino);
        int fila = indiceValidado(origen);
        int columna = indiceValidado(destino);
        boolean existia = matriz[fila][columna];
        matriz[fila][columna] = false;
        return existia;
    }

    /** Registra una amistad en ambos sentidos. */
    public void agregarAmistad(UsuarioMatriz primero, UsuarioMatriz segundo) {
        validarExtremos(primero, segundo);
        int fila = indiceValidado(primero);
        int columna = indiceValidado(segundo);
        matriz[fila][columna] = true;
        matriz[columna][fila] = true;
    }

    public boolean quitarAmistad(UsuarioMatriz primero, UsuarioMatriz segundo) {
        validarExtremos(primero, segundo);
        int fila = indiceValidado(primero);
        int columna = indiceValidado(segundo);
        boolean existia = matriz[fila][columna] || matriz[columna][fila];
        matriz[fila][columna] = false;
        matriz[columna][fila] = false;
        return existia;
    }

    public boolean estaConectado(UsuarioMatriz origen, UsuarioMatriz destino) {
        return matriz[indiceValidado(origen)][indiceValidado(destino)];
    }

    /** Vecinos directos: usuarios con conexión entrante o saliente. */
    public ListaEnlazada<UsuarioMatriz> obtenerVecinos(UsuarioMatriz usuario) {
        int indice = indiceValidado(usuario);
        ListaEnlazada<UsuarioMatriz> vecinos = new ListaEnlazada<>();
        for (int otro = 0; otro < usuarios.tamano(); otro++) {
            if (otro != indice && (matriz[indice][otro] || matriz[otro][indice])) {
                vecinos.insertarAlFinal(usuarios.obtener(otro));
            }
        }
        return vecinos;
    }

    /** Amigos directos: conexiones presentes en ambos sentidos. */
    public ListaEnlazada<UsuarioMatriz> obtenerAmigos(UsuarioMatriz usuario) {
        int indice = indiceValidado(usuario);
        ListaEnlazada<UsuarioMatriz> amigos = new ListaEnlazada<>();
        for (int otro = 0; otro < usuarios.tamano(); otro++) {
            if (otro != indice && matriz[indice][otro] && matriz[otro][indice]) {
                amigos.insertarAlFinal(usuarios.obtener(otro));
            }
        }
        return amigos;
    }

    public ListaEnlazada<UsuarioMatriz> obtenerSeguidos(UsuarioMatriz usuario) {
        int indice = indiceValidado(usuario);
        ListaEnlazada<UsuarioMatriz> seguidos = new ListaEnlazada<>();
        for (int otro = 0; otro < usuarios.tamano(); otro++) {
            if (matriz[indice][otro]) {
                seguidos.insertarAlFinal(usuarios.obtener(otro));
            }
        }
        return seguidos;
    }

    public ListaEnlazada<UsuarioMatriz> obtenerSeguidores(UsuarioMatriz usuario) {
        int indice = indiceValidado(usuario);
        ListaEnlazada<UsuarioMatriz> seguidores = new ListaEnlazada<>();
        for (int otro = 0; otro < usuarios.tamano(); otro++) {
            if (matriz[otro][indice]) {
                seguidores.insertarAlFinal(usuarios.obtener(otro));
            }
        }
        return seguidores;
    }

    public ListaEnlazada<UsuarioMatriz> getUsuarios() {
        ListaEnlazada<UsuarioMatriz> copia = new ListaEnlazada<>();
        for (UsuarioMatriz usuario : usuarios) {
            copia.insertarAlFinal(usuario);
        }
        return copia;
    }

    public UsuarioMatriz obtenerUsuario(int indice) {
        return usuarios.obtener(indice);
    }

    public int getCantidadUsuarios() {
        return usuarios.tamano();
    }

    /** Devuelve una copia para impedir modificaciones externas de la matriz. */
    public boolean[][] getMatrizAdyacencia() {
        boolean[][] copia = new boolean[matriz.length][matriz.length];
        copiarMatriz(matriz, copia, -1, -1);
        return copia;
    }

    private int indiceValidado(UsuarioMatriz usuario) {
        int indice = indiceDe(usuario);
        if (indice < 0) {
            throw new IllegalArgumentException("El usuario no pertenece a este catálogo.");
        }
        return indice;
    }

    private void validarExtremos(UsuarioMatriz origen, UsuarioMatriz destino) {
        int indiceOrigen = indiceValidado(origen);
        int indiceDestino = indiceValidado(destino);
        if (indiceOrigen == indiceDestino) {
            throw new IllegalArgumentException("Un usuario no puede conectarse consigo mismo.");
        }
    }

    private int indiceDe(UsuarioMatriz usuario) {
        if (usuario == null) {
            return -1;
        }
        for (int indice = 0; indice < usuarios.tamano(); indice++) {
            if (usuarios.obtener(indice) == usuario) {
                return indice;
            }
        }
        return -1;
    }

    private void copiarMatriz(boolean[][] origen, boolean[][] destino, int omitirFila, int omitirColumna) {
        int filaDestino = 0;
        for (int fila = 0; fila < origen.length; fila++) {
            if (fila == omitirFila) {
                continue;
            }
            int columnaDestino = 0;
            for (int columna = 0; columna < origen.length; columna++) {
                if (columna == omitirColumna) {
                    continue;
                }
                destino[filaDestino][columnaDestino] = origen[fila][columna];
                columnaDestino++;
            }
            filaDestino++;
        }
    }
}
