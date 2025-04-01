package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class HieroglyphicRenderer {

    public static void drawCentered(SpriteBatch batch, List<HieroglyphicChar> chars, float y) {
        float totalWidth = 0f;

        GlyphLayout[] layouts = new GlyphLayout[chars.size()];

        // Precalcular layouts y ancho total
        for (int i = 0; i < chars.size(); i++) {
            HieroglyphicChar hc = chars.get(i);
            GlyphLayout layout = new GlyphLayout(hc.font, String.valueOf(hc.character));
            layouts[i] = layout;
            totalWidth += layout.width;
        }

        float x = (Gdx.graphics.getWidth() - totalWidth) / 2f;

        // Dibujar todos los caracteres alineados a la misma línea base
        for (int i = 0; i < chars.size(); i++) {
            HieroglyphicChar hc = chars.get(i);
            GlyphLayout layout = layouts[i];

            float baselineOffset = hc.font.getCapHeight(); // alineación vertical homogénea
            hc.font.draw(batch, layout, x, y + baselineOffset);

            x += layout.width;
        }
    }
}