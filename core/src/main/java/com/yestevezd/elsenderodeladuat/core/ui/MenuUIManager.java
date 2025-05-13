package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra una lista de botones de menú, así como la lógica de navegación entre ellos y su renderizado.
 * Es utilizada para representar y controlar menús interactivos (como el menú principal del juego).
 */
public class MenuUIManager {
    
    private final List<MenuButton> buttons = new ArrayList<>();  
    private int selectedIndex = 0;

    /**
     * Agrega un nuevo botón al menú.
     *
     * @param button El botón a agregar.
     */
    public void addButton(MenuButton button) {
        buttons.add(button);
    }

    /**
     * Navega hacia arriba en la lista de botones. Cicla al final si se llega al principio.
     */
    public void navigateUp() {
        selectedIndex = (selectedIndex - 1 + buttons.size()) % buttons.size();
        updateSelection();
    }

    /**
     * Navega hacia abajo en la lista de botones. Cicla al principio si se llega al final.
     */
    public void navigateDown() {
        selectedIndex = (selectedIndex + 1) % buttons.size();
        updateSelection();
    }

    /**
     * Dibuja todos los botones del menú.
     *
     * @param batch SpriteBatch utilizado para renderizar los botones.
     * @param font  Fuente a usar para el texto de los botones.
     */
    public void render(SpriteBatch batch, BitmapFont font) {
        for (MenuButton button : buttons) {
            button.render(batch, font);
        }
    }

    /**
     * Actualiza qué botón está marcado como seleccionado, en función de selectedIndex.
     */
    public void updateSelection() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(i == selectedIndex);
        }
    }

    /**
     * Obtiene el texto del botón actualmente seleccionado.
     *
     * @return Texto del botón seleccionado.
     */
    public String getSelectedOption() {
        return buttons.get(selectedIndex).getText();
    }

    /**
     * Reinicia la selección del menú, posicionándose en el primer botón.
     */
    public void resetSelection() {
        selectedIndex = 0;
        updateSelection();
    }

    public void clearButtons() {
        buttons.clear();
        selectedIndex = 0;
    }
}