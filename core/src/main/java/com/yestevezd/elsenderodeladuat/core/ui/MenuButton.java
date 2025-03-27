package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

/**
 * Representa un botón simple de menú basado en texto.
 */
public class MenuButton {
    private final String text;
    private final float y;
    private boolean selected = false;

    public MenuButton(String text, float y) {
        this.text = text;
        this.y = y;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        font.setColor(selected ? Color.YELLOW : Color.WHITE);
        String displayText = selected ? "> " + text + " <" : text;

        GlyphLayout layout = new GlyphLayout(font, displayText);
        float x = (float) (com.badlogic.gdx.Gdx.graphics.getWidth() - layout.width) / 2f;

        font.draw(batch, layout, x, y);
    }

    public String getText() {
        return text;
    }
}