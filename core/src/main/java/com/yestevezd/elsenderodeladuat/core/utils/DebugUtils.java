package com.yestevezd.elsenderodeladuat.core.utils;

import com.badlogic.gdx.Gdx;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Utilidad para logs de desarrollo. Permite centralizar y controlar
 * qué mensajes se muestran en consola.
 */
public class DebugUtils {

    public static void log(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.log(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.debug(tag, message);
        }
    }

    public static void error(String tag, String message) {
        if (GameConfig.DEBUG) {
            Gdx.app.error(tag, message);
        }
    }

    public static void error(String tag, String message, Throwable throwable) {
        if (GameConfig.DEBUG) {
            Gdx.app.error(tag, message, throwable);
        }
    }
}