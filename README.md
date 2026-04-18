# T1 ELO329 — Simulador EloTelTag / FindMy

Simulación en Java de un sistema de localización de dispositivos inspirado en AirTag. Los dispositivos EloTelTag y Tablet se reportan a través de celulares cercanos hacia una nube (ETnube), que registra y provee sus últimas posiciones conocidas.

## Integrantes

| Nombre | GitHub |
|---|---|
| Carlos Ramírez | Carlos-creator |
| Martín | Martinrpr |
| Nicolás Rogel | Nicolás Rogel |
| Yasin Morales | Yasin |

## Compilar y ejecutar

```bash
make        # compila todos los .java
make run    # ejecuta: java SimulatorTest config.txt move.txt
make clean  # elimina los .class generados
```

> En Aragorn `javac` ya está en el PATH. En Windows, asegúrese de que `javac` esté disponible en el PATH antes de invocar `make`.

## Archivos de entrada

| Archivo | Descripción |
|---|---|
| `config.txt` | Define personas, sus EloTelTags, celular y tablet con coordenadas iniciales |
| `move.txt` | Secuencia de comandos: mover equipo (`dx dy`) o consultar ubicaciones (`FindMy`) |

### Formato config.txt
```
<número de personas>
<nombre> <nº tags> <tiene tablet: 0|1>
<celular_x> <celular_y>
(<nombre_tag> <tag_x> <tag_y>)*
[<tablet_x> <tablet_y>]
```

### Formato move.txt
```
<persona>.<equipo> <dx> <dy>
<persona>.<equipo> FindMy
```

## Salida

- **Consola**: salida de cada comando `FindMy` con las posiciones reportadas de los bienes de la persona.
- **output.csv**: posición reportada de cada equipo después de cada paso de simulación. Se puede importar a Excel para graficar trayectorias.

## Etapas de desarrollo

| Etapa | Descripción | Clase principal |
|---|---|---|
| 1 | Lectura de archivos y movimiento de EloTelTags | `T1Stage1` |
| 2 | Celulares reportan posición a ETnube | `T1Stage3` |
| 3 | Visualizador FindMy (clase Viewer) | `T1Stage3` |
| 4 (final) | Incorpora Tablet con FindMy | `SimulatorTest` |

## Diagrama de clases

Ver `doc/documentacion.html` para el diagrama de clases completo y los gráficos de trayectoria.
