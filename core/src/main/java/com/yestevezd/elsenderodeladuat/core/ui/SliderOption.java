package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;

/**
 * Representa una opción de tipo deslizador (slider) que muestra una barra de volumen ajustable.
 * Ideal para usar en menús de configuración (por ejemplo, volumen de música o efectos).
 */
public class SliderOption {
    private final String label; 
    private float value; 
    private final float step; 

    /**
     * Constructor.
     *
     * @param label        Nombre de la opción a mostrar.
     * @param initialValue Valor inicial del deslizador (entre 0.0 y 1.0).
     * @param step         Paso de ajuste del valor (por ejemplo, 0.1f).
     */
    public SliderOption(String label, float initialValue, float step) {
        this.label = label;
        this.value = initialValue;
        this.step = step;
    }

    /**
     * Aumenta el valor del deslizador hasta un máximo de 1.0.
     */
    public void increase() {
        value = Math.min(1f, value + step);
    }

    /**
     * Disminuye el valor del deslizador hasta un mínimo de 0.0.
     */
    public void decrease() {
        value = Math.max(0f, value - step);
    }

    /**
     * @return El valor actual del deslizador.
     */
    public float getValue() {
        return value;
    }

    /**
     * Establece directamente un nuevo valor para el deslizador (clamp entre 0 y 1).
     *
     * @param newValue Nuevo valor a asignar.
     */
    public void setValue(float newValue) {
        this.value = Math.max(0f, Math.min(1f, newValue));
    }

    /**
     * @return El texto descriptivo de la opción.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return Una cadena que representa visualmente la barra de volumen (por ejemplo: ▮▮▮▯▯▯).
     */
    private String getVolumeBar() {
        int filled = Math.round(value * 10f);   // número de bloques llenos
        int empty = 10 - filled;                // número de bloques vacíos
        return "▮".repeat(filled) + "▯".repeat(empty);
    }

    /**
     * Renderiza visualmente la opción en pantalla.
     *
     * @param batch       SpriteBatch usado para dibujar.
     * @param font        Fuente usada para el texto.
     * @param y           Coordenada Y para posicionar el texto.
     * @param isSelected  Si esta opción está actualmente seleccionada en el menú.
     */
    public void render(SpriteBatch batch, BitmapFont font, float y, boolean isSelected) {
        String bar = getVolumeBar();
        String display = isSelected ? "> " + label + ": " + bar + " <" : label + ": " + bar;

        font.setColor(isSelected ? com.badlogic.gdx.graphics.Color.YELLOW : com.badlogic.gdx.graphics.Color.WHITE);
        TextUtils.drawCenteredText(batch, font, display, y);
    }
}