# El Sendero de la Duat 🦂🌅

**El Sendero de la Duat** es un videojuego narrativo en 2D, ambientado en el Antiguo Egipto, desarrollado con [libGDX](https://libgdx.com/) para PC.

El jugador encarna a un artesano de Deir el-Medina que, al recibir una misteriosa carta, inicia un viaje espiritual que lo llevará desde Tebas hasta el inframundo egipcio (la Duat), enfrentándose a decisiones morales que afectarán su destino eterno.

---

## 🛠️ Tecnologías y herramientas

- **Java 17**
- **libGDX** (con LWJGL3)
- **Gradle** para gestión de dependencias
- **gdx-liftoff** como generador inicial
- **Modularidad** y arquitectura escalable basada en pantallas, sistemas y managers

---

## 📁 Estructura del proyecto

```
/core
│── /assets                # Recursos gráficos, sonidos, animaciones...
│── /src
│    ├── /core
|    |   |-- /collision    # CollisionSystem
|    |   |-- /combat       # CombatSystem
│    │   ├── /engine       # Carga de recursos, audio, input
│    │   ├── /entities     # Jugador, NPCs, enemigos,items
│    │   ├── /game         # MainGame, configuración global, gestores, flags, sistema de Maat
|    |   |-- /interacction # Sistema de puertas, objetos interactuables
|    |   |-- /inventory    # Inventario
|    |   |-- /maps         # Sistema de carga de mapas
|    |   |-- /narrative    # Sistema de dialogos, sistema de jerooglíficos, sistema de narrativa
|    |   |-- /save         # Sistema de guardado
│    │   ├── /screens      # Pantallas del juego (Intro, Menú, Juego, etc.)
│    │   ├── /ui           # HUD, CombatHUD, botones, sliders, popUps, menu de pausa
│    │   └── /utils        # Funciones comunes (textos, matemáticas, animaciones)
│    └── /docs            # Documentación del juego
/gradle                  # Configuración y scripts de Gradle
/lwjgl3                 # Plataforma desktop (PC)
```

---

## ▶️ Cómo ejecutar el juego

Requisitos:
- Java 17 o superior
- Gradle (ya incluido wrapper)

Comando para ejecutar desde consola:
```bash
./gradlew lwjgl3:run
```
Otra opción es descargarse el .exe y directamente ejecutarlo.

---

## ✅ Estado actual

- [x] Arquitectura modular y escalable
- [x] Sistema de carga de assets dividido por bloques funcionales
- [x] Pantalla de introducción animada con video (frames + audio)
- [x] Transición a Menú Principal con texto parpadeante
- [X] Pantalla de menú principal interactiva
- [x] Sistema de colisiones en todos los mapas
- [X] Casa del artesano: diálogo, comer, dormir (guardado)
- [X] Exploración en Deir el-Medina, Karnak y Valle de los Reyes, Sala Hipóstila y Tumba Kv9
- [X] Sistema de puertas y transiciones
- [X] Diálogos con elecciones y karma (Maat)
- [X] Combate en tiempo real contra NPCs
- [X]  Guardado y carga de partidas
- [X]  Juicio de Osiris con múltiples finales

---

## 📌 Pendientes técnicos

- Futuras mejoras e implementaciones para continuar el videojuego

---

## 📚 Créditos

-Proyecto de TFG - Ingeniería Informática
-Desarrollado por: Yago Estévez Davila
-Tutor: Ruth Sofía Contreras Espinosa
-Departamento: Área de videojuegos, UOC
-Año: 2025

---

## ✨ Licencia

-Este proyecto es de carácter académico y no comercial.
-Licencia: Reconocimiento-NoComercial-SinObraDerivada 3.0 España.