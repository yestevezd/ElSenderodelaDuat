package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Representa un carácter jeroglífico junto con la fuente que se debe usar para renderizarlo.
 *
 * Esta clase se utiliza para mostrar texto jeroglífico donde cada carácter puede estar
 * asociado a una fuente distinta (por ejemplo, diferentes estilos de jeroglíficos).
 */
public class HieroglyphicChar {

    public final char character;
    public final BitmapFont font;

    /**
     * Constructor.
     *
     * @param character Carácter que se quiere representar
     * @param font Fuente (BitmapFont) que se debe usar para dibujar el carácter
     */
    public HieroglyphicChar(char character, BitmapFont font) {
        this.character = character;
        this.font = font;
    }
}