package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/** Red de usuarios con amistades mutuas y relaciones de seguimiento unidireccionales. */
public class RedAmistades {

    private final ListaEnlazada<Usuario> usuarios;

    public RedAmistades() {
        this.usuarios = new ListaEnlazada<>();
    }

    public Usuario agregar(String nombre) {
        return agregar(nombre, 0, "Este usuario todavía no agregó una biografía.");
    }

    public Usuario agregar(String nombre, int edad, String biografia) {
        Usuario usuario = new Usuario(nombre, edad, biografia);
        usuarios.insertarAlFinal(usuario);
        return usuario;
    }

    public void conectar(Usuario a, Usuario b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Los dos usuarios tienen que existir.");
        }
        a.enlazar(b);
        b.enlazar(a);
    }

    /** a comienza a seguir a b; la relación no es necesariamente recíproca. */
    public void seguir(Usuario a, Usuario b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Los dos usuarios tienen que existir.");
        }
        a.seguir(b);
    }

    public ListaEnlazada<Usuario> getUsuarios() { return usuarios; }

    /** Red de demostración con perfiles, amistades y seguimientos de ejemplo. */
    public static RedAmistades ejemplo() {
        RedAmistades red = new RedAmistades();
        Usuario ana = red.agregar("Ana", 21, "Me gusta la programación y aprender cosas nuevas.");
        Usuario luis = red.agregar("Luis", 23, "Fanático de la tecnología y los videojuegos.");
        Usuario mia = red.agregar("Mia", 20, "Disfruto la música, la lectura y la creatividad.");
        Usuario leo = red.agregar("Leo", 22, "Interesado en deportes y desarrollo de software.");
        Usuario sol = red.agregar("Sol", 19, "Compartiendo ideas y buenos momentos.");
        Usuario bea = red.agregar("Bea", 24, "Me encanta conocer personas y viajar.");
        Usuario pia = red.agregar("Pia", 21, "Estudiante y amante de la fotografía.");
        Usuario nora = red.agregar("Nora", 22, "Aprendiendo algo nuevo cada día.");
        Usuario teo = red.agregar("Teo", 25, "Apasionado por la ciencia y la tecnología.");

        red.conectar(ana, luis);
        red.conectar(luis, mia);
        red.conectar(mia, leo);
        red.conectar(leo, sol);
        red.conectar(leo, nora);
        red.conectar(ana, bea);
        red.conectar(bea, sol);
        red.conectar(ana, pia);

        Usuario iria = red.agregar("Iria", 20, "Me gusta el diseño y la creatividad.");
        Usuario joao = red.agregar("Joao", 26, "Interesado en programación.");
        Usuario ken = red.agregar("Ken", 22, "Explorando nuevas tecnologías.");
        Usuario luz = red.agregar("Luz", 19, "La música es parte de mi día.");
        Usuario max = red.agregar("Max", 27, "Deporte, tecnología y amigos.");
        Usuario nia = red.agregar("Nia", 23, "Siempre con ganas de aprender.");
        Usuario omar = red.agregar("Omar", 24, "Me gusta compartir conocimientos.");
        Usuario paz = red.agregar("Paz", 21, "Creatividad y buenas conversaciones.");

        red.conectar(iria, joao);
        red.conectar(joao, ken);
        red.conectar(ken, luz);
        red.conectar(luz, max);
        red.conectar(max, nia);
        red.conectar(nia, omar);
        red.conectar(omar, paz);

        // Seguimientos de demostración: pueden ser unidireccionales.
        red.seguir(ana, mia);
        red.seguir(ana, leo);
        red.seguir(luis, ana);
        red.seguir(mia, ana);
        red.seguir(mia, sol);
        red.seguir(leo, ana);
        red.seguir(leo, mia);
        red.seguir(sol, ana);
        red.seguir(bea, ana);
        red.seguir(pia, ana);
        red.seguir(nora, leo);
        red.seguir(teo, luis);
        red.seguir(iria, joao);
        red.seguir(joao, ken);
        red.seguir(ken, iria);
        red.seguir(luz, max);
        red.seguir(max, nia);
        red.seguir(nia, omar);
        red.seguir(omar, paz);
        return red;
    }
}
