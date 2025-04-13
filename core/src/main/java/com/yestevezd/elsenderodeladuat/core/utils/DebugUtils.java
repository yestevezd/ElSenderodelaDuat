package com.yestevezd.elsenderodeladuat.core.utils;

import com.badlogic.gdx.Gdx;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Utilidad para impresión de logs de desarrollo.
 * Centraliza los mensajes de consola y los filtra según el modo debug definido en {@link GameConfig}.
 * Esto permite evitar ruido en la salida de consola en builds finales.
 */
public class DebugUtils {

    /**
     * Imprime un mensaje normal de log si el modo DEBUG está activo.
     *
     * @param tag     Etiqueta que categoriza el mensaje (ej. "MAP", "DOOR").
     * @param message Mensaje de log que se desea imprimir.
     */
    public static void log(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.log(tag, message);
        }
    }

    /**
     * Imprime un mensaje de tipo debug si el modo DEBUG está activo.
     *
     * @param tag     Etiqueta que categoriza el mensaje.
     * @param message Mensaje de depuración.
     */
    public static void debug(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.debug(tag, message);
        }
    }

    /**
     * Imprime un mensaje de error si el modo DEBUG está activo.
     *
     * @param tag     Etiqueta que categoriza el mensaje.
     * @param message Mensaje de error.
     */
    public static void error(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.error(tag, message);
        }
    }

    /**
     * Imprime un mensaje de error junto con una excepción (stack trace) si el modo DEBUG está activo.
     *
     * @param tag       Etiqueta que categoriza el mensaje.
     * @param message   Mensaje de error.
     * @param throwable Excepción lanzada (por ejemplo, NullPointerException).
     */
    public static void error(String tag, String message, Throwable throwable) {
        if (GameConfig.DEBUG) {
            Gdx.app.error(tag, message, throwable);
        }
    }
}