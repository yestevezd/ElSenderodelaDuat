package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra una lista de botones de menú, su navegación y selección.
 */
public class MenuUIManager {
    private final List<MenuButton> buttons = new ArrayList<>();
    private int selectedIndex = 0;

    public void addButton(MenuButton button) {
        buttons.add(button);
    }

    public void navigateUp() {
        selectedIndex = (selectedIndex - 1 + buttons.size()) % buttons.size();
        updateSelection();
    }

    public void navigateDown() {
        selectedIndex = (selectedIndex + 1) % buttons.size();
        updateSelection();
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        for (MenuButton button : buttons) {
            button.render(batch, font);
        }
    }

    public void updateSelection() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(i == selectedIndex);
        }
    }

    public String getSelectedOption() {
        return buttons.get(selectedIndex).getText();
    }

    public void resetSelection() {
        selectedIndex = 0;
        updateSelection();
    }
} 
