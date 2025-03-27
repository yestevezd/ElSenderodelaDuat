package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Gestor centralizado del input para facilitar personalización y reutilización.
 */
public class InputManager {

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

}
