package com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Parsea texto enriquecido con etiquetas de fuente como [1], [2], etc.
 * para asignar diferentes fuentes (BitmapFont) a caracteres jeroglíficos individuales.
 *
 * Ejemplo de uso:
 *   Input: "[1]A[2]B[1]C"
 *   Resultado: A con fuente 0, B con fuente 1, C con fuente 0
 */
public class HieroglyphicParser {

    /**
     * Parsea una cadena de texto enriquecido, creando una lista de caracteres jeroglíficos con su fuente correspondiente.
     *
     * @param richText Texto que puede incluir etiquetas como [1], [2] para cambiar la fuente activa.
     * @param fonts    Array de fuentes disponibles. [1] corresponderá a fonts[0], [2] a fonts[1], etc.
     * @return Lista de caracteres jeroglíficos con la fuente apropiada asociada.
     */
    public static List<HieroglyphicChar> parse(String richText, BitmapFont... fonts) {
        List<HieroglyphicChar> result = new ArrayList<>();
        int fontIndex = 0; // Fuente activa por defecto

        // Recorre todo el texto carácter por carácter
        for (int i = 0; i < richText.length(); i++) {
            char c = richText.charAt(i);

            // Detecta secuencias como [1], [2], etc.
            if (c == '[' && i + 2 < richText.length() && richText.charAt(i + 2) == ']') {
                // Extrae el número entre corchetes y lo convierte a índice de fuente
                fontIndex = Character.getNumericValue(richText.charAt(i + 1)) - 1;
                i += 2; // Avanza para saltarse la secuencia completa [n]
            } else {
                // Asegura que fontIndex está dentro del rango válido
                fontIndex = Math.max(0, Math.min(fontIndex, fonts.length - 1));

                // Crea el objeto jeroglífico con el carácter actual y la fuente activa
                result.add(new HieroglyphicChar(c, fonts[fontIndex]));
            }
        }

        return result;
    }
}