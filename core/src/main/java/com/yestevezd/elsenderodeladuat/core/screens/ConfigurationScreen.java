package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.ui.SliderOption;
import com.yestevezd.elsenderodeladuat.core.ui.SliderUIManager;
import com.yestevezd.elsenderodeladuat.core.utils.DebugUtils;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;

public class ConfigurationScreen extends BaseScreen {

    private Texture fondo;
    private SliderUIManager sliderUI;
    private BitmapFont font;
    private boolean onBackOption = false;

    public ConfigurationScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadConfigAssets();
        AssetLoader.finishLoading();
        fondo = AssetLoader.get("others/fondo_configuracion.jpg", Texture.class);

        font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.getData().setScale(2f);

        sliderUI = new SliderUIManager();
        sliderUI.addSlider(new SliderOption("MÚSICA", GameConfig.MUSIC_VOLUME, 0.1f));
        sliderUI.addSlider(new SliderOption("EFECTOS", GameConfig.SOUND_VOLUME, 0.1f));

        AudioManager.playMusic("sounds/musica_configuracion.mp3", true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        TextureUtils.drawFullScreen(fondo, batch);

        String configurationTitle = "CONFIGURAR SONIDO";
        font.setColor(com.badlogic.gdx.graphics.Color.RED);
        TextUtils.drawCenteredText(batch, font, configurationTitle, 650f);

        sliderUI.render(batch, font, 500f, 100f, sliderUI.getSelectedIndex(), !onBackOption);

        String volverText = onBackOption ? "> VOLVER <" : "VOLVER";
        font.setColor(onBackOption ? com.badlogic.gdx.graphics.Color.YELLOW : com.badlogic.gdx.graphics.Color.WHITE);
        TextUtils.drawCenteredText(batch, font, volverText, 250f);

        batch.end();

        manejarInput();
    }

    private void manejarInput() {
        if (InputManager.isNavigateUpPressed()) {
            if (onBackOption) {
                onBackOption = false;
                sliderUI.resetSelection();
            } else {
                sliderUI.navigateUp();
            }
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        }

        if (InputManager.isNavigateDownPressed()) {
            if (sliderUI.getSelectedIndex() == sliderUI.getSliderCount() - 1 && !onBackOption) {
                onBackOption = true;
            } else if (!onBackOption) {
                sliderUI.navigateDown();
            }
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        }

        if (InputManager.isSelectPressed()) {
            if (onBackOption) {
                AudioManager.playSound("sounds/click_menu.mp3");
                game.setScreen(new MainMenuScreen(game));
            }
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT) && !onBackOption) {
            sliderUI.decreaseValue();
            updateGameConfig();
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT) && !onBackOption) {
            sliderUI.increaseValue();
            updateGameConfig();
        }
    }

    private void updateGameConfig() {
        SliderOption selected = sliderUI.getSelectedSlider();
        String label = selected.getLabel();

       if (label.equals("MÚSICA")) {
        GameConfig.MUSIC_VOLUME = selected.getValue();
        AudioManager.setVolume(GameConfig.MUSIC_VOLUME);
        DebugUtils.log("Configuración", "Volumen MÚSICA: " + GameConfig.MUSIC_VOLUME);
        AudioManager.playSound("sounds/click_menu.mp3");
        }

        if (label.equals("EFECTOS")) {
            GameConfig.SOUND_VOLUME = selected.getValue();
            DebugUtils.log("Configuración", "Volumen EFECTOS: " + GameConfig.SOUND_VOLUME);
            AudioManager.playSound("sounds/click_menu.mp3");
        }
    }

    @Override
    public void dispose() {
        fondo.dispose();
        AssetLoader.unloadConfigAssets();
        font.dispose();
    }
}