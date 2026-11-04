package org.example.redsocial.busqueda;

import org.example.estructuradedatos.ListaEnlazada;

/**
 * Utilidad para gestionar el catálogo de usuarios con ID y Nombre,
 * ordenamiento Quicksort, Búsqueda Binaria y Eliminación.
 */
public class CatalogoUsuarios {

    public static class Usuario {
        private final String id;
        private final String nombre;

        public Usuario(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + nombre;
        }
    }

    public static void precargarUsuarios(ListaEnlazada<Usuario> lista) {
        // Lista de 50 nombres con sus IDs originales, pero ordenados de forma mixta (desordenada)
        String[][] datosDesordenados = {
            {"23", "Walter Loayza"}, {"05", "Elena Quispe"}, {"42", "Pamela Galindo"},
            {"12", "Luis Mendoza"}, {"31", "Eduardo Rivero"}, {"01", "Carlos Pérez"},
            {"18", "Raquel Morales"}, {"49", "Zacarías Soliz"}, {"08", "Humberto Soria"},
            {"27", "Alejandro Paz"}, {"14", "Nelson Huanca"}, {"35", "Iván Medrano"},
            {"03", "Beatriz Mamani"}, {"20", "Tatiana Alarcón"}, {"40", "Natalia Quiroga"},
            {"10", "Jorge Condori"}, {"29", "Cristian Nina"}, {"47", "Yara Benavidez"},
            {"07", "Gabriel Vargas"}, {"25", "Yamil Terán"}, {"16", "Pablo Yucra"},
            {"33", "Gonzalo Vargas"}, {"50", "Adriana Prado"}, {"21", "Ulises Mamani"},
            {"38", "Mauricio Camacho"}, {"09", "Isabel Limachi"}, {"11", "Karen Flores"},
            {"45", "Tomás Guardia"}, {"26", "Zulema Rojas"}, {"04", "Daniel Choque"},
            {"30", "Diana Melgar"}, {"15", "Olivia Salazar"}, {"37", "Lorena Justiniano"},
            {"02", "Ana Gómez"}, {"22", "Valeria Cruz"}, {"43", "Renán Zegarra"},
            {"13", "María Belén"}, {"32", "Fabiana Montero"}, {"48", "Adriana Prado*"},
            {"06", "Fernando Rojas"}, {"24", "Ximena Ramos"}, {"41", "Oscar Paredes"},
            {"17", "Quintín Vidal"}, {"36", "Jessica Arze"}, {"44", "Silvia Oporto"},
            {"19", "Samuel Ticona"}, {"28", "Brenda Castro"}, {"34", "Helena Silva"},
            {"39", "Kevin Torrico"}, {"46", "Verónica Rocha"}
        };

        // Se insertan en la lista enlazada exactamente en este orden desordenado
        for (String[] dato : datosDesordenados) {
            lista.insertarAlFinal(new Usuario(dato[0], dato[1]));
        }
    }

    public static boolean eliminarUsuarioPorId(ListaEnlazada<Usuario> lista, String idObjetivo) {
        Usuario encontrado = null;
        for (Usuario u : lista) {
            if (u.getId().equalsIgnoreCase(idObjetivo.trim())) {
                encontrado = u;
                break;
            }
        }
        return encontrado != null && lista.eliminar(encontrado);
    }

    public static boolean eliminarUsuarioPorNombre(ListaEnlazada<Usuario> lista, String nombreObjetivo) {
        Usuario encontrado = null;
        for (Usuario u : lista) {
            if (u.getNombre().equalsIgnoreCase(nombreObjetivo.trim())) {
                encontrado = u;
                break;
            }
        }
        return encontrado != null && lista.eliminar(encontrado);
    }

    public static Usuario[] aArregloUsuarios(ListaEnlazada<Usuario> lista) {
        int tamano = lista.tamano();
        Usuario[] arreglo = new Usuario[tamano];
        int i = 0;
        for (Usuario usuario : lista) {
            arreglo[i++] = usuario;
        }
        return arreglo;
    }

    public static void quicksort(Usuario[] arreglo, int bajo, int alto, String modo) {
        if (bajo < alto) {
            int pi = particion(arreglo, bajo, alto, modo);
            quicksort(arreglo, bajo, pi - 1, modo);
            quicksort(arreglo, pi + 1, alto, modo);
        }
    }

    private static int particion(Usuario[] arreglo, int bajo, int alto, String modo) {
        Usuario pivote = arreglo[alto];
        int i = (bajo - 1);
        for (int j = bajo; j < alto; j++) {
            boolean condicion;
            if (modo.equalsIgnoreCase("ID")) {
                // Comparamos numéricamente si es posible, o alfanuméricamente
                int id1 = Integer.parseInt(arreglo[j].getId());
                int id2 = Integer.parseInt(pivote.getId());
                condicion = id1 <= id2;
            } else {
                condicion = arreglo[j].getNombre().compareToIgnoreCase(pivote.getNombre()) <= 0;
            }

            if (condicion) {
                i++;
                Usuario temp = arreglo[i];
                arreglo[i] = arreglo[j];
                arreglo[j] = temp;
            }
        }
        Usuario temp = arreglo[i + 1];
        arreglo[i + 1] = arreglo[alto];
        arreglo[alto] = temp;
        return i + 1;
    }

    public static int busquedaBinariaPorId(Usuario[] arreglo, String idObjetivo) {
        int izquierda = 0;
        int derecha = arreglo.length - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            int idMedio = Integer.parseInt(arreglo[medio].getId());
            int idBuscado = Integer.parseInt(idObjetivo.trim());

            if (idMedio == idBuscado) return medio;
            if (idMedio < idBuscado) izquierda = medio + 1;
            else derecha = medio - 1;
        }
        return -1;
    }

    public static int busquedaBinariaPorNombre(Usuario[] arreglo, String nombreObjetivo) {
        int izquierda = 0;
        int derecha = arreglo.length - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            int resultado = arreglo[medio].getNombre().compareToIgnoreCase(nombreObjetivo.trim());

            if (resultado == 0) return medio;
            if (resultado < 0) izquierda = medio + 1;
            else derecha = medio - 1;
        }
        return -1;
    }
}