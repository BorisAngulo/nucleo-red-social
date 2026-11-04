package org.example.redsocial.perfiles;

import org.example.estructuradedatos.ListaEnlazada;



public class ArregloPerfiles {

    private static final int CAPACIDAD_INICIAL_DEFECTO = 16;

    private PerfilUsuario[] perfiles;
    private int cantidad;

    public ArregloPerfiles() {
        this(CAPACIDAD_INICIAL_DEFECTO);
    }

    public ArregloPerfiles(int capacidadInicial) {
        if (capacidadInicial <= 0) {
            throw new IllegalArgumentException("La capacidad inicial debe ser mayor que cero.");
        }
        this.perfiles = new PerfilUsuario[capacidadInicial];
        this.cantidad = 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCapacidad() {
        return perfiles.length;
    }

    public boolean estaVacio() {
        return cantidad == 0;
    }

    public boolean estaLleno() {
        return cantidad == perfiles.length;
    }

    public PerfilUsuario obtener(int indice) {
        validarIndice(indice);
        return perfiles[indice];
    }

    /**
     * Retorna una copia exacta del tramo logico ocupado en el arreglo.
     */
    public PerfilUsuario[] getPerfiles() {
        PerfilUsuario[] copia = new PerfilUsuario[cantidad];
        System.arraycopy(perfiles, 0, copia, 0, cantidad);
        return copia;
    }

    /**
     * Realiza una busqueda binaria sobre el arreglo ordenado de perfiles.
     * Complejidad temporal: O(log n).
     *
     * @param nombreUsuarioBuscado Nombre o handle a localizar.
     * @return Objeto con el resultado booleano, posicion, perfil y traza detallada de comparaciones.
     */
    public ResultadoBusquedaBinaria buscarBinario(String nombreUsuarioBuscado) {
        if (nombreUsuarioBuscado == null || nombreUsuarioBuscado.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario a buscar no puede estar vacío.");
        }
        String objetivo = normalizarUsuario(nombreUsuarioBuscado);
        ListaEnlazada<String> traza = new ListaEnlazada<>();

        int inicio = 0;
        int fin = cantidad - 1;
        int comparaciones = 0;
        int paso = 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            PerfilUsuario actual = perfiles[medio];
            comparaciones++;

            int cmp = objetivo.compareToIgnoreCase(actual.getNombreUsuario());

            if (cmp == 0) {
                traza.insertarAlFinal("Paso " + paso + ": Rango [" + inicio + " .. " + fin + "] | "
                        + "Índice medio = " + medio + " (@" + actual.getNombreUsuario() + "). "
                        + "Comparación: '" + objetivo + "' == '" + actual.getNombreUsuario()
                        + "' -> ¡Coincidencia exacta encontrada!");
                return new ResultadoBusquedaBinaria(true, medio, medio, actual, comparaciones, traza);
            }

            if (cmp < 0) {
                traza.insertarAlFinal("Paso " + paso + ": Rango [" + inicio + " .. " + fin + "] | "
                        + "Índice medio = " + medio + " (@" + actual.getNombreUsuario() + "). "
                        + "Comparación: '" + objetivo + "' < '" + actual.getNombreUsuario()
                        + "' -> Descartar mitad derecha [" + medio + " .. " + fin + "]; "
                        + "continuar en subarreglo izquierdo [" + inicio + " .. " + (medio - 1) + "].");
                fin = medio - 1;
            } else {
                traza.insertarAlFinal("Paso " + paso + ": Rango [" + inicio + " .. " + fin + "] | "
                        + "Índice medio = " + medio + " (@" + actual.getNombreUsuario() + "). "
                        + "Comparación: '" + objetivo + "' > '" + actual.getNombreUsuario()
                        + "' -> Descartar mitad izquierda [" + inicio + " .. " + medio + "]; "
                        + "continuar en subarreglo derecho [" + (medio + 1) + " .. " + fin + "].");
                inicio = medio + 1;
            }
            paso++;
        }

        traza.insertarAlFinal("Fin de la búsqueda: Rango agotado (inicio=" + inicio + " > fin=" + fin + "). "
                + "El perfil @" + objetivo + " no existe en el arreglo. "
                + "Punto de inserción ordenado: índice [" + inicio + "].");

        return new ResultadoBusquedaBinaria(false, -1, inicio, null, comparaciones, traza);
    }

    /**
     * Inserta un nuevo perfil manteniendo el arreglo estrictamente ordenado en todo momento.
     * Utiliza busqueda binaria para localizar la posicion de insercion en O(log n) y desplaza
     * los elementos a la derecha en O(n).
     */
    public boolean insertar(PerfilUsuario nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El perfil no puede ser nulo.");
        }
        if (cantidad > 0) {
            ResultadoBusquedaBinaria busqueda = buscarBinario(nuevo.getNombreUsuario());
            if (busqueda.encontrado()) {
                throw new IllegalArgumentException("Ya existe un perfil con el nombre de usuario @"
                        + nuevo.getNombreUsuario() + ".");
            }
            asegurarCapacidad();
            int puntoInsercion = busqueda.getPuntoInsercion();
            for (int i = cantidad; i > puntoInsercion; i--) {
                perfiles[i] = perfiles[i - 1];
            }
            perfiles[puntoInsercion] = nuevo;
        } else {
            asegurarCapacidad();
            perfiles[0] = nuevo;
        }
        cantidad++;
        return true;
    }

    /**
     * Elimina un perfil del arreglo identificandolo mediante busqueda binaria
     * y compactando los elementos hacia la izquierda.
     */
    public boolean eliminar(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank() || cantidad == 0) {
            return false;
        }
        ResultadoBusquedaBinaria busqueda = buscarBinario(nombreUsuario);
        if (!busqueda.encontrado()) {
            return false;
        }
        int indice = busqueda.getPosicion();
        for (int i = indice; i < cantidad - 1; i++) {
            perfiles[i] = perfiles[i + 1];
        }
        perfiles[cantidad - 1] = null;
        cantidad--;
        return true;
    }

    /**
     * Carga un conjunto inicial de perfiles de ejemplo ordenados alfabeticamente.
     */
    public static ArregloPerfiles ejemplo() {
        ArregloPerfiles catalogo = new ArregloPerfiles(12);
        catalogo.insertar(new PerfilUsuario("alicia", "Alicia Gómez", "Diseñadora UX/UI y amante del café", 145, 98));
        catalogo.insertar(new PerfilUsuario("beto", "Roberto Silva", "Ingeniero de datos y apasionado del ciclismo", 88, 120));
        catalogo.insertar(new PerfilUsuario("carlos", "Carlos Ruiz", "Desarrollador Java backend y gamer", 312, 190));
        catalogo.insertar(new PerfilUsuario("diana", "Diana Morales", "Fotógrafa urbana y creadora de contenido", 520, 240));
        catalogo.insertar(new PerfilUsuario("elena", "Elena Torres", "Estudiante de Ciencia de la Computación", 210, 185));
        catalogo.insertar(new PerfilUsuario("fernando", "Fernando Vega", "Músico independiente y productor de audio", 95, 75));
        catalogo.insertar(new PerfilUsuario("gabriela", "Gabriela Soto", "Especialista en ciberseguridad y redes", 410, 310));
        catalogo.insertar(new PerfilUsuario("hugo", "Hugo Ramos", "Arquitecto de software y entusiasta open source", 630, 420));
        return catalogo;
    }

    private void asegurarCapacidad() {
        if (cantidad >= perfiles.length) {
            int nuevaCapacidad = perfiles.length * 2;
            PerfilUsuario[] nuevoArreglo = new PerfilUsuario[nuevaCapacidad];
            System.arraycopy(perfiles, 0, nuevoArreglo, 0, cantidad);
            perfiles = nuevoArreglo;
        }
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice fuera del rango lógico: " + indice + " (tamaño " + cantidad + ")");
        }
    }

    private static String normalizarUsuario(String usuario) {
        String limpio = usuario.trim();
        if (limpio.startsWith("@")) {
            limpio = limpio.substring(1);
        }
        return limpio;
    }
}
