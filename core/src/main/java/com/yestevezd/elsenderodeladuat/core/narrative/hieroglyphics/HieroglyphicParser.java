package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Parsea texto enriquecido con códigos como [1], [2] para asignar fuentes distintas a caracteres jeroglíficos.
 */
public class HieroglyphicParser {

    public static List<HieroglyphicChar> parse(String richText, BitmapFont... fonts) {
        List<HieroglyphicChar> result = new ArrayList<>();
        int fontIndex = 0;

        for (int i = 0; i < richText.length(); i++) {
            char c = richText.charAt(i);

            if (c == '[' && i + 2 < richText.length() && richText.charAt(i + 2) == ']') {
                fontIndex = Character.getNumericValue(richText.charAt(i + 1)) - 1;
                i += 2;
            } else {
                fontIndex = Math.max(0, Math.min(fontIndex, fonts.length - 1));
                result.add(new HieroglyphicChar(c, fonts[fontIndex]));
            }
        }

        return result;
    }
}
