package com.yestevezd.elsenderodeladuat.core.game;

/**
 * Configuración global del juego.
 * Centraliza parámetros como resolución, volumen, y opciones de depuración.
 */
public class GameConfig {

    // Resolución de pantalla
    public static final int SCREEN_WIDTH = 1920;   // Ancho virtual del juego
    public static final int SCREEN_HEIGHT = 1080;  // Alto virtual del juego

    // Configuraciones gráficas
    public static final boolean FULLSCREEN = true;  // Modo pantalla completa
    public static final boolean VSYNC = true;       // Sincronización vertical

    // Título de la ventana del juego
    public static final String GAME_TITLE = "El Sendero de la Duat";

    // Volumen global (valores entre 0.0 y 1.0)
    public static float MUSIC_VOLUME = 0.2f;  // Volumen de la música
    public static float SOUND_VOLUME = 0.2f;  // Volumen de efectos de sonido

    // Habilitar o deshabilitar herramientas de depuración visual
    public static final boolean DEBUG = true;
}