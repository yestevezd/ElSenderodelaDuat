package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MaatSystem;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.save.SaveManager;
import com.yestevezd.elsenderodeladuat.core.ui.MenuButton;
import com.yestevezd.elsenderodeladuat.core.ui.MenuUIManager;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;

/**
 * Pantalla principal del menú del juego.
 */
public class MainMenuScreen extends BaseScreen {

    private Texture fondo;
    private BitmapFont font;
    private MenuUIManager menuUI;

    private float inputCooldown = 0.2f;
    private float tiempoDesdeInput = 0;

    public MainMenuScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadMenuAssets();
        AssetLoader.finishLoading();

        fondo = AssetLoader.get("menu/fondo_menu.jpg", Texture.class);
        font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.getData().setScale(1f);

        menuUI = new MenuUIManager();
        menuUI.clearButtons();
        if (SaveManager.hasSave()) {
            menuUI.addButton(new MenuButton("CONTINUAR PARTIDA", 700f));
        }
        menuUI.addButton(new MenuButton("NUEVA PARTIDA", 600f));
        menuUI.addButton(new MenuButton("CONFIGURACION", 500f));
        menuUI.addButton(new MenuButton("SALIR", 400f));
        menuUI.updateSelection();

        AudioManager.playMusic("sounds/musica_menu.mp3", true);
    }

    @Override
    public void render(float delta) {
        tiempoDesdeInput += delta;
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        TextureUtils.drawFullScreen(fondo, batch);
        menuUI.render(batch, font);
        batch.end();

        manejarInput();
    }

    private void manejarInput() {
        if (tiempoDesdeInput < inputCooldown) return;

        if (InputManager.isNavigateUpPressed()) {
            menuUI.navigateUp();
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
            tiempoDesdeInput = 0;
        }

        if (InputManager.isNavigateDownPressed()) {
            menuUI.navigateDown();
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
            tiempoDesdeInput = 0;
        }

        if (InputManager.isSelectPressed()) {
            AudioManager.playSound("sounds/click_menu.mp3");

            switch (menuUI.getSelectedOption()) {
                case "CONTINUAR PARTIDA":
                    SaveManager.loadGame(game);
                    break;
                case "NUEVA PARTIDA":
                    SaveManager.deleteSave();
                    EventFlags.resetAll();
                    MaatSystem.get().reset();
                    game.getInventory().clear();
                    game.getPlayer().restoreFullHealth();
                    game.setScreen(new ContextScreen(game));
                    break;
                case "CONFIGURACION":
                    AudioManager.stopMusic();
                    game.setScreen(new ConfigurationScreen(game));
                    break;
                case "SALIR":
                    Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadMenuAssets();
    }
}
