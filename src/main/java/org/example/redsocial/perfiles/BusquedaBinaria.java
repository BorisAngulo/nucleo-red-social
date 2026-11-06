package org.example.redsocial.perfiles;

//Metodo para buscar
public final class BusquedaBinaria {
    private BusquedaBinaria() {}
//Busqueda por ID
    public static Perfil porId(Perfil[] a, int id) {
        if (a == null) return null;
        int izq = 0, der = a.length - 1;
        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            int valor = a[medio].getId();
            if (valor == id) return a[medio];
            if (valor < id) izq = medio + 1;
            else der = medio - 1;
        }
        return null;
    }
//Busqueda por Nombre
    public static Perfil porNombre(Perfil[] a, String nombre) {
        if (a == null || nombre == null) return null;
        int izq = 0, der = a.length - 1;
        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            int comparacion = a[medio].getNombre().compareToIgnoreCase(nombre.trim());
            if (comparacion == 0) return a[medio];
            if (comparacion < 0) izq = medio + 1;
            else der = medio - 1;
        }
        return null;
    }
}
