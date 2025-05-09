package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

import java.util.List;

/**
 * Utilidad para renderizar texto jeroglífico en pantalla usando múltiples fuentes.
 * 
 * Este renderer permite dibujar una lista de caracteres jeroglíficos (`HieroglyphicChar`)
 * centrados horizontalmente en la pantalla, manteniendo una línea base uniforme.
 */
public class HieroglyphicRenderer {

    /**
     * Dibuja una secuencia de caracteres jeroglíficos centrados horizontalmente en pantalla.
     *
     * @param batch SpriteBatch utilizado para dibujar.
     * @param chars Lista de caracteres jeroglíficos con su fuente asociada.
     * @param y     Coordenada vertical donde se alinea la base del texto.
     */
    public static void drawCentered(SpriteBatch batch, List<HieroglyphicChar> chars, float y) {
        float totalWidth = 0f;

        // Layouts individuales para cada carácter, necesarios para medir y dibujar
        GlyphLayout[] layouts = new GlyphLayout[chars.size()];

        // 1. Precalcular los GlyphLayouts y calcular el ancho total del texto
        for (int i = 0; i < chars.size(); i++) {
            HieroglyphicChar hc = chars.get(i);
            GlyphLayout layout = new GlyphLayout(hc.font, String.valueOf(hc.character));
            layouts[i] = layout;
            totalWidth += layout.width;
        }

        // 2. Calcular la posición inicial en X para centrar el texto horizontalmente
        float x = (GameConfig.VIRTUAL_WIDTH - totalWidth) / 2f;

        // 3. Dibujar cada carácter, uno al lado del otro, alineados por línea base
        for (int i = 0; i < chars.size(); i++) {
            HieroglyphicChar hc = chars.get(i);
            GlyphLayout layout = layouts[i];

            // Alinear verticalmente todos los caracteres con la misma altura de mayúsculas
            float baselineOffset = hc.font.getCapHeight();

            // Dibujar el carácter en su posición
            hc.font.draw(batch, layout, x, y + baselineOffset);

            // Avanzar la posición X para el siguiente carácter
            x += layout.width;
        }
    }
}