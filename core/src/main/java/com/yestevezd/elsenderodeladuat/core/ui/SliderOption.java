package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;

public class SliderOption {
    private final String label;
    private float value;
    private final float step;

    public SliderOption(String label, float initialValue, float step) {
        this.label = label;
        this.value = initialValue;
        this.step = step;
    }

    public void increase() {
        value = Math.min(1f, value + step);
    }

    public void decrease() {
        value = Math.max(0f, value - step);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float newValue) {
        this.value = Math.max(0f, Math.min(1f, newValue));
    }

    public String getLabel() {
        return label;
    }

    private String getVolumeBar() {
        int filled = Math.round(value * 10f);
        int empty = 10 - filled;
        return "▮".repeat(filled) + "▯".repeat(empty);
    }

    public void render(SpriteBatch batch, BitmapFont font, float y, boolean isSelected) {
        String bar = getVolumeBar();
        String display = isSelected ? "> " + label + ": " + bar + " <" : label + ": " + bar;

        font.setColor(isSelected ? com.badlogic.gdx.graphics.Color.YELLOW : com.badlogic.gdx.graphics.Color.WHITE);
        TextUtils.drawCenteredText(batch, font, display, y);
    }
}