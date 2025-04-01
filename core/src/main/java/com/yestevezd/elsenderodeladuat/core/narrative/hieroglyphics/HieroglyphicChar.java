package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Representa un carácter jeroglífico asociado a una fuente específica.
 */
public class HieroglyphicChar {
    public final char character;
    public final BitmapFont font;

    public HieroglyphicChar(char character, BitmapFont font) {
        this.character = character;
        this.font = font;
    }
}