package com.yestevezd.elsenderodeladuat.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Utilidades gráficas para dibujar texturas centradas o a pantalla completa.
 */
public class TextureUtils {

    /**
     * Dibuja una textura centrada y escalada para encajar dentro de la pantalla,
     * manteniendo la proporción de la imagen.
     *
     * @param texture La textura a dibujar.
     * @param batch El SpriteBatch activo sobre el que se dibuja.
     */
    public static void drawCenteredFit(Texture texture, SpriteBatch batch) {
        float imageWidth = texture.getWidth();
        float imageHeight = texture.getHeight();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float scale = Math.min(screenWidth / imageWidth, screenHeight / imageHeight);

        float drawWidth = imageWidth * scale;
        float drawHeight = imageHeight * scale;

        float x = (screenWidth - drawWidth) / 2f;
        float y = (screenHeight - drawHeight) / 2f;

        batch.draw(texture, x, y, drawWidth, drawHeight);
    }

    /**
     * Dibuja una textura estirada para ocupar toda la pantalla,
     * sin mantener la proporción (puede deformarse).
     *
     * @param texture La textura a dibujar.
     * @param batch El SpriteBatch activo sobre el que se dibuja.
     */
    public static void drawFullScreen(Texture texture, SpriteBatch batch) {
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}