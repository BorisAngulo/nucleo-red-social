
package org.example.redsocial.amigos;

import org.example.estructuradedatos.ListaCircular;

public class CarruselAmigos {

    private final ListaCircular<Perfil> perfiles;
    private int posicionActual;

    public CarruselAmigos() {
        perfiles = new ListaCircular<>();
        posicionActual = 0;
        cargarPerfiles();
    }

    // =====================================
    // CARGAR PERFILES
    // =====================================

    private void cargarPerfiles() {

        perfiles.insertarAlFinal(new Perfil(
                "Sofía Martínez", "sofia.m",
                "Amante de la fotografía y los viajes",
                "/imagenes/perfil1.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Daniela Rojas", "daniela.r",
                "Estudiante de informática",
                "/imagenes/perfil2.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Camila Flores", "camila.f",
                "Amante del arte y la música",
                "/imagenes/perfil3.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Andrés Vargas", "andres.v",
                "Deportista y amante de la naturaleza",
                "/imagenes/perfil4.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Lian Cruz", "lian.c",
                "Compartiendo momentos de mi vida",
                "/imagenes/perfil5.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Lucío Fernández", "lucio.f",
                "Me encanta la tecnología y el diseño",
                "/imagenes/perfil6.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Mateo Sánchez", "mateo.s",
                "Amante de los videojuegos",
                "/imagenes/perfil7.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Isabela Torres", "isa.t",
                "Fotografía, música y aventuras",
                "/imagenes/perfil8.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Gabriel Mendoza", "gabriel.m",
                "Desarrollador de software",
                "/imagenes/perfil9.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Valentino Pérez", "valentino.p",
                "Disfrutando cada momento",
                "/imagenes/perfil10.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Jhon Chavez", "jhon.ch",
                "La oscuridad es mi camino",
                "/imagenes/perfil11.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Nanci Quirroga", "nanci.q",
                "Sigamos con todo por delante",
                "/imagenes/perfil12.jpg"
        ));

        perfiles.insertarAlFinal(new Perfil(
                "Camila Suares", "camila.s",
                "Disfrutando de todo",
                "/imagenes/perfil13.jpg"
        ));
    }

    // =====================================
    // OBTENER PERFIL ACTUAL
    // =====================================

    public Perfil obtenerActual() {

        if (perfiles.estaVacia()) {
            return null;
        }

        return perfiles.obtener(0);
    }

    // =====================================
    // OBTENER PERFIL POR POSICIÓN
    // =====================================

    public Perfil obtenerPerfil(int indice) {

        if (indice < 0 || indice >= perfiles.tamano()) {
            return null;
        }

        return perfiles.obtener(indice);
    }

    // =====================================
    // SIGUIENTE PERFIL
    // =====================================

    public Perfil siguiente() {

        if (perfiles.estaVacia()) {
            return null;
        }

        perfiles.rotar();

        posicionActual =
                (posicionActual + 1) % perfiles.tamano();

        return obtenerActual();
    }

    // =====================================
    // PERFIL ANTERIOR
    // =====================================

    public Perfil anterior() {

        if (perfiles.estaVacia()) {
            return null;
        }

        int cantidad = perfiles.tamano();

        for (int i = 0; i < cantidad - 1; i++) {
            perfiles.rotar();
        }

        posicionActual =
                (posicionActual - 1 + cantidad) % cantidad;

        return obtenerActual();
    }

    // =====================================
    // AGREGAR PERFIL
    // =====================================

    public boolean agregarPerfil(Perfil perfil) {

        if (perfil == null || perfiles.contiene(perfil)) {
            return false;
        }

        perfiles.insertarAlFinal(perfil);
        return true;
    }

    // =====================================
    // ELIMINAR PERFIL ACTUAL
    // =====================================

    public boolean eliminarActual() {

        Perfil actual = obtenerActual();

        if (actual == null) {
            return false;
        }

        boolean eliminado = perfiles.eliminar(actual);

        if (eliminado) {
            if (perfiles.estaVacia()) {
                posicionActual = 0;
            } else {
                posicionActual %= perfiles.tamano();
            }
        }

        return eliminado;
    }

    // =====================================
    // ELIMINAR PERFIL ESPECÍFICO
    // =====================================

    public boolean eliminarPerfil(Perfil perfil) {

        if (perfil == null) {
            return false;
        }

        int indice = perfiles.buscar(perfil);

        if (indice == -1) {
            return false;
        }

        perfiles.eliminar(perfil);

        if (perfiles.estaVacia()) {
            posicionActual = 0;
        } else {
            posicionActual %= perfiles.tamano();
        }

        return true;
    }

    // =====================================
    // ELIMINAR POR NOMBRE DE USUARIO
    // =====================================

    public boolean eliminarPerfil(String usuario) {

        Perfil perfil = buscarPerfil(usuario);

        return perfil != null && eliminarPerfil(perfil);
    }

    // =====================================
    // BUSCAR PERFIL
    // =====================================

    public Perfil buscarPerfil(String usuario) {

        if (usuario == null || perfiles.estaVacia()) {
            return null;
        }

        for (int i = 0; i < perfiles.tamano(); i++) {

            Perfil perfil = perfiles.obtener(i);

            if (perfil.getUsuario().equalsIgnoreCase(usuario)) {
                return perfil;
            }
        }

        return null;
    }

    // =====================================
    // ACTUALIZAR SUGERENCIAS
    // =====================================

    public void actualizarSugerencias() {

        if (perfiles.tamano() > 1) {
            perfiles.rotar();

            posicionActual =
                    (posicionActual + 1) % perfiles.tamano();
        }
    }

    // =====================================
    // CANTIDAD DE PERFILES
    // =====================================

    public int cantidadPerfiles() {
        return perfiles.tamano();
    }

    // =====================================
    // COMPROBAR SI ESTÁ VACÍA
    // =====================================

    public boolean estaVacio() {
        return perfiles.estaVacia();
    }

    // =====================================
    // POSICIÓN ACTUAL
    // =====================================

    public int obtenerPosicionActual() {
        return perfiles.estaVacia() ? 0 : posicionActual + 1;
    }
}
