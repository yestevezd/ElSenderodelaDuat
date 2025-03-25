# El Sendero de la Duat 🦂🌅

**El Sendero de la Duat** es un videojuego narrativo en 2D, ambientado en el Antiguo Egipto, desarrollado con [libGDX](https://libgdx.com/) para PC.

El jugador encarna a un artesano de Deir el-Medina que, al recibir una misteriosa carta, inicia un viaje espiritual que lo llevará desde Tebas hasta el inframundo egipcio (la Duat), enfrentándose a decisiones morales que afectarán su destino eterno.

---

## 🛠️ Tecnologías y herramientas

- **Java 17**
- **libGDX** (con LWJGL3)
- **Gradle** para gestión de dependencias
- **gdx-liftoff** como generador inicial
- **Modularidad** y arquitectura escalable basada en pantallas y managers

---

## 📁 Estructura del proyecto

```
/core
│── /assets              # Recursos gráficos, sonidos, animaciones...
│── /src
│    ├── /core
│    │   ├── /engine     # Carga de recursos, audio, input
│    │   ├── /entities   # Jugador, NPCs, enemigos
│    │   ├── /game       # MainGame, configuración global, gestores
│    │   ├── /screens    # Pantallas del juego (Intro, Menú, Juego, etc.)
│    │   ├── /systems    # Combate, exploración, puzzles, karma
│    │   ├── /ui         # HUD, diálogos, inventario
│    │   └── /utils      # Funciones comunes (textos, matemáticas, animaciones)
│    └── /docs           # Documentación del juego
/gradle                 # Configuración y scripts de Gradle
/lwjgl3                # Plataforma desktop (PC)
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

---

## ✅ Estado actual

- [x] Arquitectura modular y escalable
- [x] Sistema de carga de assets dividido por bloques funcionales
- [x] Pantalla de introducción animada con video (frames + audio)
- [x] Transición a Menú Principal con texto parpadeante
- [ ] Pantalla de menú principal interactiva
- [ ] Implementación del inicio del juego (casa del artesano)
- [ ] Sistema de decisiones y karma
- [ ] Exploración en Karnak y Valle de los Reyes
- [ ] Juicio de Osiris y finales múltiples

---

## 📌 Pendientes técnicos

- Implementar navegación en menú principal
- Cargar mapas reales con Tiled
- Sistema de combate y puzzles
- Guardado de progreso
- Sistema de diálogos con elecciones

---

## 📚 Créditos

Proyecto de TFG – Ingeniería Informática  
Desarrollado por: Yago Estévez Davila  
Año: 2025

---

## ✨ Licencia

Este proyecto es de carácter académico y no comercial.