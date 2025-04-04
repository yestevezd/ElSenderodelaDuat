package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Gestor centralizado del input para facilitar personalización y reutilización.
 */
public class InputManager {

    // === Menús ===

    public static boolean isNavigateUpPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    public static boolean isNavigateDownPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
    }

    public static boolean isSelectPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }

    public static boolean isBackPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    // === Movimiento continuo (mantener tecla) ===

    public static boolean isMoveUpPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
    }

    public static boolean isMoveDownPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
    }

    public static boolean isMoveLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public static boolean isMoveRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    // === Interacción ===

    public static boolean isInteractPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.E);
    }
}