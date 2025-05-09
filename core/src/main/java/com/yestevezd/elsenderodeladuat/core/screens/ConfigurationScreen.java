package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.ui.ControlsPopup;
import com.yestevezd.elsenderodeladuat.core.ui.SoundPopup;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;

public class ConfigurationScreen extends BaseScreen {

    private Texture fondo;
    private BitmapFont font;
    private ControlsPopup controlsPopup;
    private SoundPopup soundPopup;

    private int selectedIndex = 0;
    private final String[] options = {"CONFIGURAR SONIDO", "CONTROLES", "VOLVER"};

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

        controlsPopup = new ControlsPopup();
        soundPopup = new SoundPopup();

        AudioManager.playMusic("sounds/musica_configuracion.mp3", true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);        
        
        batch.begin();

        TextureUtils.drawFullScreen(fondo, batch);

        font.setColor(Color.RED);
        TextUtils.drawCenteredText(batch, font, "CONFIGURACIÓN", GameConfig.VIRTUAL_HEIGHT * 0.8f);

        float baseY = GameConfig.VIRTUAL_HEIGHT * 0.55f;
        float spacing = 100f;

        for (int i = 0; i < options.length; i++) {
            boolean selected = i == selectedIndex;
            font.setColor(selected ? Color.YELLOW : Color.WHITE);
            String text = selected ? "> " + options[i] + " <" : options[i];
            TextUtils.drawCenteredText(batch, font, text, baseY - i * spacing);
        }

        if (controlsPopup.isVisible()) {
            controlsPopup.render(batch);
        } else if (soundPopup.isVisible()) {
            soundPopup.render(batch);
        }

        batch.end();

        manejarInput();
    }

    private void manejarInput() {
        // Si algún popup está abierto, primero gestionamos su cierre
        if (controlsPopup.isVisible()) {
            if (InputManager.isSelectPressed() || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                controlsPopup.hide();
            }
            return;
        }

        if (soundPopup.isVisible()) {
            soundPopup.handleInput();
            return;
        }

        // Navegación entre opciones
        if (InputManager.isNavigateUpPressed()) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        }

        if (InputManager.isNavigateDownPressed()) {
            selectedIndex = (selectedIndex + 1) % options.length;
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        }

        if (InputManager.isSelectPressed()) {
            AudioManager.playSound("sounds/click_menu.mp3");
            switch (options[selectedIndex]) {
                case "CONFIGURAR SONIDO":
                    soundPopup.show();
                    break;
                case "CONTROLES":
                    controlsPopup.show();
                    break;
                case "VOLVER":
                    game.setScreen(new MainMenuScreen(game));
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        fondo.dispose();
        AssetLoader.unloadConfigAssets();
        font.dispose();
    }
}
