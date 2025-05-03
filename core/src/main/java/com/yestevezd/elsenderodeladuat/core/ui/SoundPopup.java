package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;

public class SoundPopup {

    private boolean visible = false;
    private final SliderUIManager sliderUI;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;

    public SoundPopup() {
        font = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        font.getData().setScale(1.1f);

        sliderUI = new SliderUIManager();
        sliderUI.addSlider(new SliderOption("MÚSICA", GameConfig.MUSIC_VOLUME, 0.1f));
        sliderUI.addSlider(new SliderOption("EFECTOS", GameConfig.SOUND_VOLUME, 0.1f));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void handleInput() {
        if (!visible) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            hide();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            sliderUI.navigateUp();
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            sliderUI.navigateDown();
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            sliderUI.decreaseValue();
            updateGameConfig();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            sliderUI.increaseValue();
            updateGameConfig();
        }
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        batch.end();

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float lineHeight = 80f;
        float padding = 40f;
        int totalLines = sliderUI.getSliderCount() + 2;

        float popupHeight = totalLines * lineHeight + padding;
        float popupWidth = screenWidth * 0.65f;
        float popupX = (screenWidth - popupWidth) / 2f;
        float popupY = (screenHeight - popupHeight) / 2f;

        Color egyptianBrown = new Color(0.29f, 0.2f, 0.13f, 0.95f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(egyptianBrown);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();

        batch.begin();

        float contentY = popupY + popupHeight - 30f;

        font.setColor(Color.GOLD);
        TextUtils.drawCenteredText(batch, font, "Ajustes de Sonido", contentY);
        contentY -= lineHeight;

        sliderUI.render(batch, font, contentY, lineHeight, sliderUI.getSelectedIndex(), true);

        font.setColor(Color.RED);
        TextUtils.drawCenteredText(batch, font, "ESC o ENTER para volver", popupY + padding);
    }

    private void updateGameConfig() {
        SliderOption selected = sliderUI.getSelectedSlider();
        String label = selected.getLabel();

        if (label.equals("MÚSICA")) {
            GameConfig.MUSIC_VOLUME = selected.getValue();
            AudioManager.setVolume(GameConfig.MUSIC_VOLUME);
        }

        if (label.equals("EFECTOS")) {
            GameConfig.SOUND_VOLUME = selected.getValue();
        }

        AudioManager.playSound("sounds/click_menu.mp3");
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
