package com.yestevezd.elsenderodeladuat.core.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextUtils {

    /**
     * Dibuja texto centrado horizontalmente con efecto parpadeo.
     *
     * @param batch SpriteBatch activo
     * @param font Fuente a usar
     * @param message El texto a mostrar
     * @param y Posición vertical (desde abajo)
     * @param stateTime Tiempo acumulado (delta sumado)
     * @param blinkSpeed En segundos: cuánto dura cada ciclo (por ejemplo 1.0f)
     */
    public static void drawBlinkingTextCentered(SpriteBatch batch, BitmapFont font, String message, float y, float stateTime, float blinkSpeed) {
        if ((stateTime % blinkSpeed) < (blinkSpeed / 2f)) {
            GlyphLayout layout = new GlyphLayout(font, message);
            float x = (float) (com.badlogic.gdx.Gdx.graphics.getWidth() - layout.width) / 2f;
            font.draw(batch, layout, x, y);
        }
    }

    /**
     * Dibuja texto centrado horizontalmente.
     *
     * @param batch SpriteBatch activo
     * @param font Fuente a usar
     * @param message El texto a mostrar
     * @param y Posición vertical (desde abajo)
     */
    public static void drawCenteredText(SpriteBatch batch, BitmapFont font, String message, float y) {
        GlyphLayout layout = new GlyphLayout(font, message);
        float x = (float) (com.badlogic.gdx.Gdx.graphics.getWidth() - layout.width) / 2f;
        font.draw(batch, layout, x, y);
    }
}