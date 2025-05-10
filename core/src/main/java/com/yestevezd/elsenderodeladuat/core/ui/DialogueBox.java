package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeOption;

import java.util.List;

public class DialogueBox extends TextBox {

    private List<NarrativeOption> options;
    private int selectedOption = 0;
    private boolean optionsVisible = false;

    private float fadeInAlpha = 0f;
    private boolean fadeInActive = false;

    @Override
    public void show(String text) {
        super.show(text);
        optionsVisible = false;
        options = null;
    }

    public void showWithOptions(String text, List<NarrativeOption> options) {
        this.options = options;
        this.selectedOption = 0;
        this.optionsVisible = true;
        super.showWithOptions(text, options.size());
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (!isVisible() || options == null || !optionsVisible) return;

        BitmapFont font = getFont();
        int boxX = getBoxX();
        int boxY = getBoxY();
        int boxW = getBoxWidth();
        float boxH = getBoxHeight();
        int pad = getPadding();
        float textHeight = getTextHeight();

        float baseY = boxY + boxH - pad - textHeight - 30;
        float optionX = boxX + boxW / 2f;

        if (fadeInActive) {
            fadeInAlpha += Gdx.graphics.getDeltaTime() * 2f;
            if (fadeInAlpha >= 1f) {
                fadeInAlpha = 1f;
                fadeInActive = false;
            }
        } else {
            fadeInAlpha = 1f;
        }

        for (int i = 0; i < options.size(); i++) {
            boolean isSelected = (i == selectedOption);
            String prefix = isSelected ? "> " : "  ";
            String fullText = prefix + options.get(i).getText();

            Color baseColor = isSelected ? Color.ROYAL : new Color(0.2f, 0.1f, 0f, 1f);
            font.setColor(baseColor.r, baseColor.g, baseColor.b, fadeInAlpha);

            GlyphLayout layout = new GlyphLayout(font, fullText);
            float centeredX = optionX - layout.width / 2f;
            float y = baseY - i * 35;

            font.draw(batch, layout, centeredX, y);
        }
    }

    public void update() {
        if (!isVisible() || !optionsVisible) return;

        if (InputManager.isNavigateUpPressed()) {
            selectedOption = (selectedOption - 1 + options.size()) % options.size();
        }

        if (InputManager.isNavigateDownPressed()) {
            selectedOption = (selectedOption + 1) % options.size();
        }

    }

    public boolean isOptionsVisible() {
        return optionsVisible;
    }

    public int getSelectedOptionIndex() {
        return selectedOption;
    }

    public NarrativeOption getSelectedOption() {
        return options != null && selectedOption < options.size() ? options.get(selectedOption) : null;
    }

    public void setFadeInActive(boolean active) {
        this.fadeInActive = active;
        this.fadeInAlpha = 0f;
    }
}
