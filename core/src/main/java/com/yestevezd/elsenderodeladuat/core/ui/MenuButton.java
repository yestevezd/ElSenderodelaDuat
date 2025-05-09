package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.badlogic.gdx.graphics.Color;

/**
 * Representa un botón simple de menú basado en texto, utilizado en pantallas como el menú principal.
 * El botón puede ser marcado como seleccionado, lo que afecta su color y estilo visual.
 */
public class MenuButton {

    private final String text; 
    private final float y; 
    private boolean selected = false; 

    /**
     * Constructor del botón.
     *
     * @param text Texto que se mostrará en el botón.
     * @param y    Posición vertical en pantalla donde se debe renderizar el botón.
     */
    public MenuButton(String text, float y) {
        this.text = text;
        this.y = y;
    }

    /**
     * Cambia el estado de selección del botón.
     *
     * @param selected true si el botón está seleccionado; false si no lo está.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Dibuja el botón en pantalla utilizando el batch y la fuente proporcionada.
     * Si el botón está seleccionado, se resalta en color amarillo y se rodea con signos de mayor/menor.
     *
     * @param batch SpriteBatch usado para dibujar el texto.
     * @param font  Fuente usada para renderizar el texto.
     */
    public void render(SpriteBatch batch, BitmapFont font) {
        font.setColor(selected ? Color.YELLOW : Color.WHITE);
        String displayText = selected ? "> " + text + " <" : text;

        GlyphLayout layout = new GlyphLayout(font, displayText);
        float x = (float) (GameConfig.VIRTUAL_WIDTH - layout.width) / 2f;

        font.draw(batch, layout, x, y);
    }

    /**
     * Devuelve el texto sin formato del botón (sin símbolos de selección).
     *
     * @return El texto original del botón.
     */
    public String getText() {
        return text;
    }
}