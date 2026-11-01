# Núcleo

Pantalla de las funciones de la red social. Cada botón del inicio corresponde a un valor de `FuncionRed`. La lógica de cada función vive en su propio paquete, fuera de la interfaz.

## Colores

Definidos en `src/main/java/org/example/interfaz/Paleta.java`.

| Uso | Hex |
|---|---|
| Fondo de la pantalla | `#121212` |
| Texto principal | `#E0E0E0` |
| Texto secundario | `#A3A3A3` |
| Tarjetas | `#1E1E1E` |
| Botones | `#2A2A2A` |
| Botón al pasar el mouse | `#363636` |
| Bordes | `#333333` |

## Dónde va cada cosa

| Qué | Dónde |
|---|---|
| Nombre del botón, grupo y resumen | `org.example.interfaz.FuncionRed` |
| Pantalla Swing del botón | `org.example.interfaz`, una clase `Panel...` |
| Algoritmo y datos | un paquete nuevo bajo `org.example.redsocial` |
| Listas, pilas, colas y árboles | `org.example.estructuradedatos`, sin crear otra estructura |

La referencia ya armada es **Grados de separación**:

- algoritmo en `org.example.redsocial.separacion`
- pantalla en `org.example.interfaz.PanelGradosSeparacion`
- el botón `FuncionRed.GRADOS_DE_SEPARACION` abre esa pantalla desde `VentanaRedSocial.mostrar`

## Cómo conectar un botón que ya existe

1. Crea el paquete `org.example.redsocial.<nombre>` con las clases de la función. Ahí va el algoritmo. Las colecciones salen de `org.example.estructuradedatos`.
2. Crea `org.example.interfaz.Panel<Nombre>`. El constructor recibe un `Runnable alVolver`. Usa `Paleta` para fondo, texto, tarjetas y botones. El botón Volver ejecuta `alVolver`.
3. En `VentanaRedSocial`, declara una constante de carta y agrégala en el constructor:

```java
private static final String RUTA = "ruta";

raiz.add(new PanelRutaAmigos(() -> cartas.show(raiz, INICIO)), RUTA);
```

4. En `mostrar`, abre esa carta antes del panel genérico:

```java
if (funcion == FuncionRed.RUTA_DE_AMIGOS) {
    cartas.show(raiz, RUTA);
    return;
}
```

El panel genérico sigue sirviendo para los botones que todavía no tienen función.

## Cómo agregar un botón nuevo

Añade un valor al final del grupo que le corresponda en `FuncionRed`:

```java
NUEVA_FUNCION(
        "Nombre visible",
        "Recursividad",
        "Una línea que se lee bajo el botón."),
```

El inicio arma las tarjetas solo con ese enum. El segundo texto tiene que coincidir con un grupo ya usado (`Matriz y arreglo`, `Pilas`, `Colas`, `Listas enlazadas`, `Recursividad`, `Árboles`) para que el botón caiga en esa tarjeta. Después conéctalo con los pasos de la sección anterior.

## Abrir la pantalla

```bash
javac -encoding UTF-8 -d out \
  src/main/java/org/example/estructuradedatos/Texto.java \
  src/main/java/org/example/estructuradedatos/ListaEnlazada.java \
  src/main/java/org/example/redsocial/separacion/*.java \
  src/main/java/org/example/interfaz/*.java
java -cp out org.example.interfaz.RedSocialApp
```

Si la función nueva usa otra estructura, agrégala al `javac` junto a `ListaEnlazada.java`.
