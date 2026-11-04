package org.example.practicas.practica17;

import org.example.estructuradedatos.ArbolBinario;
import org.example.estructuradedatos.Cola;
import org.example.estructuradedatos.ColaEnlazada;
import org.example.estructuradedatos.ListaEnlazada;

/**
 * Muestra el árbol nivel por nivel.
 * La cola auxiliar guarda a quién falta visitar: se vacía un nivel entero
 * antes de imprimir el siguiente.
 */
public class VisualizadorOrganigrama {

    public void mostrar(ArbolBinario<Empleado> arbol) {
        if (arbol.estaVacio()) {
            System.out.println("  El organigrama está vacío.");
            return;
        }
        Cola<Empleado> cola = new ColaEnlazada<>();
        cola.encolar(arbol.datoRaiz());
        int nivel = 0;
        while (!cola.estaVacia()) {
            int enEsteNivel = cola.tamano();
            System.out.println("  Nivel " + nivel);
            for (int i = 0; i < enEsteNivel; i++) {
                Empleado actual = cola.desencolar();
                System.out.println("    " + actual);
                for (Empleado hijo : arbol.hijos(actual)) {
                    cola.encolar(hijo);
                }
            }
            nivel++;
        }
    }
}
