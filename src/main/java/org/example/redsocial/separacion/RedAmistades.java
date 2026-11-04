package org.example.redsocial.separacion;

import org.example.estructuradedatos.ListaEnlazada;

/** Amistades en ambos sentidos. Si Ana conoce a Luis, Luis conoce a Ana. */
public class RedAmistades {

    private final ListaEnlazada<Usuario> usuarios;

    public RedAmistades() {
        this.usuarios = new ListaEnlazada<>();
    }

    public Usuario agregar(String nombre) {
        Usuario usuario = new Usuario(nombre);
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

    public ListaEnlazada<Usuario> getUsuarios() {
        return usuarios;
    }

    /** Red de ejemplo: un grupo cercano y una cadena más larga que 6 saltos. */
    public static RedAmistades ejemplo() {
        RedAmistades red = new RedAmistades();
        Usuario ana = red.agregar("Ana");
        Usuario luis = red.agregar("Luis");
        Usuario mia = red.agregar("Mia");
        Usuario leo = red.agregar("Leo");
        Usuario sol = red.agregar("Sol");
        Usuario bea = red.agregar("Bea");
        Usuario pia = red.agregar("Pia");
        Usuario nora = red.agregar("Nora");
        red.agregar("Teo");

        red.conectar(ana, luis);
        red.conectar(luis, mia);
        red.conectar(mia, leo);
        red.conectar(leo, sol);
        red.conectar(leo, nora);
        red.conectar(ana, bea);
        red.conectar(bea, sol);
        red.conectar(ana, pia);

        Usuario iria = red.agregar("Iria");
        Usuario joao = red.agregar("Joao");
        Usuario ken = red.agregar("Ken");
        Usuario luz = red.agregar("Luz");
        Usuario max = red.agregar("Max");
        Usuario nia = red.agregar("Nia");
        Usuario omar = red.agregar("Omar");
        Usuario paz = red.agregar("Paz");
        red.conectar(iria, joao);
        red.conectar(joao, ken);
        red.conectar(ken, luz);
        red.conectar(luz, max);
        red.conectar(max, nia);
        red.conectar(nia, omar);
        red.conectar(omar, paz);
        return red;
    }
}
