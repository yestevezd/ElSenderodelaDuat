package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra un conjunto de sliders (por ejemplo, opciones de volumen) en una UI de configuración.
 * Permite navegar, modificar valores y renderizar la lista de sliders.
 */
public class SliderUIManager {

    private final List<SliderOption> sliders = new ArrayList<>();
    private int selectedIndex = 0;

    /**
     * Añade un nuevo slider al conjunto.
     *
     * @param slider SliderOption que se desea añadir.
     */
    public void addSlider(SliderOption slider) {
        sliders.add(slider);
    }

    /**
     * Mueve la selección hacia arriba.
     */
    public void navigateUp() {
        selectedIndex = (selectedIndex - 1 + sliders.size()) % sliders.size();
    }

    /**
     * Mueve la selección hacia abajo.
     */
    public void navigateDown() {
        selectedIndex = (selectedIndex + 1) % sliders.size();
    }

    /**
     * Aumenta el valor del slider actualmente seleccionado.
     */
    public void increaseValue() {
        sliders.get(selectedIndex).increase();
    }

    /**
     * Disminuye el valor del slider actualmente seleccionado.
     */
    public void decreaseValue() {
        sliders.get(selectedIndex).decrease();
    }

    /**
     * Dibuja todos los sliders en pantalla.
     *
     * @param batch         SpriteBatch usado para dibujar.
     * @param font          Fuente usada para renderizar el texto.
     * @param startY        Coordenada Y inicial desde la cual empezar a dibujar.
     * @param spacing       Espacio vertical entre sliders.
     * @param selectedIndex Índice del slider que se considera seleccionado actualmente (externo).
     * @param active        Indica si este bloque de sliders está activo (usado para resaltado).
     */
    public void render(SpriteBatch batch, BitmapFont font, float startY, float spacing, int selectedIndex, boolean active) {
        float y = startY;
        for (int i = 0; i < sliders.size(); i++) {
            boolean isSelected = (i == selectedIndex) && active;
            sliders.get(i).render(batch, font, y, isSelected);
            y -= spacing;
        }
    }

    /**
     * @return El slider actualmente seleccionado.
     */
    public SliderOption getSelectedSlider() {
        return sliders.get(selectedIndex);
    }

    /**
     * @return El índice del slider actualmente seleccionado.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @return Cantidad total de sliders gestionados.
     */
    public int getSliderCount() {
        return sliders.size();
    }

    /**
     * Resetea la selección al primer slider.
     */
    public void resetSelection() {
        selectedIndex = 0;
    }
}