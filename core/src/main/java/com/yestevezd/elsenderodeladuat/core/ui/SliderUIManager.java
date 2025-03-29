package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class SliderUIManager {

    private final List<SliderOption> sliders = new ArrayList<>();
    private int selectedIndex = 0;

    public void addSlider(SliderOption slider) {
        sliders.add(slider);
    }

    public void navigateUp() {
        selectedIndex = (selectedIndex - 1 + sliders.size()) % sliders.size();
    }

    public void navigateDown() {
        selectedIndex = (selectedIndex + 1) % sliders.size();
    }

    public void increaseValue() {
        sliders.get(selectedIndex).increase();
    }

    public void decreaseValue() {
        sliders.get(selectedIndex).decrease();
    }

    public void render(SpriteBatch batch, BitmapFont font, float startY, float spacing, int selectedIndex, boolean active) {
        float y = startY;
        for (int i = 0; i < sliders.size(); i++) {
            boolean isSelected = (i == selectedIndex) && active;
            sliders.get(i).render(batch, font, y, isSelected);
            y -= spacing;
        }
    }

    public SliderOption getSelectedSlider() {
        return sliders.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getSliderCount() {
        return sliders.size();
    }

    public void resetSelection() {
        selectedIndex = 0;
    }
}