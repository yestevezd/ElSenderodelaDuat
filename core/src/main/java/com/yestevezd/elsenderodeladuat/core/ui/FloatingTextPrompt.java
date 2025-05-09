package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class FloatingTextPrompt {

    private String text;
    private Vector2 worldPosition;
    private boolean visible = true;

    public FloatingTextPrompt(String text, Vector2 worldPosition) {
        this.text = text;
        this.worldPosition = worldPosition;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(Vector2 worldPosition) {
        this.worldPosition = worldPosition;
    }

    public void render(SpriteBatch batch, BitmapFont font, OrthographicCamera camera) {
        if (!visible) return;

        font.setColor(Color.WHITE);

        // Centrar texto midiendo su ancho real
        GlyphLayout layout = new GlyphLayout(font, text);
        float drawX = worldPosition.x - layout.width / 2f;
        float drawY = worldPosition.y + 25;

        font.draw(batch, layout, drawX, drawY);
    }
}
