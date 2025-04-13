package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.ui.MenuButton;
import com.yestevezd.elsenderodeladuat.core.ui.MenuUIManager;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;

/**
 * Pantalla principal del menú del juego.
 * Muestra un fondo y botones para navegar entre las opciones principales:
 * jugar, configuración y salir del juego.
 */
public class MainMenuScreen extends BaseScreen {

    private Texture fondo;
    private BitmapFont font;
    private MenuUIManager menuUI;

    private float inputCooldown = 0.2f;
    private float tiempoDesdeInput = 0;

    /**
     * Constructor del menú principal.
     *
     * @param game Instancia principal del juego
     */
    public MainMenuScreen(MainGame game) {
        super(game);
    }

    /**
     * Carga los recursos visuales y sonoros del menú.
     */
    @Override
    public void show() {
        AssetLoader.loadMenuAssets();
        AssetLoader.finishLoading();

        fondo = AssetLoader.get("menu/fondo_menu.jpg", Texture.class);
        font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.getData().setScale(1f);

        // Crear los botones del menú con su posición Y
        menuUI = new MenuUIManager();
        menuUI.addButton(new MenuButton("JUGAR", 450f));
        menuUI.addButton(new MenuButton("CONFIGURACION", 350f));
        menuUI.addButton(new MenuButton("SALIR", 250f));
        menuUI.updateSelection(); 

        // Iniciar la música del menú
        AudioManager.playMusic("sounds/musica_menu.mp3", true);
    }

    /**
     * Dibuja la pantalla del menú y gestiona la entrada de usuario.
     *
     * @param delta Tiempo transcurrido desde el último frame
     */
    @Override
    public void render(float delta) {
        tiempoDesdeInput += delta;

        // Limpiar pantalla
        ScreenUtils.clear(0, 0, 0, 1);

        // Dibujar fondo y menú
        batch.begin();
        TextureUtils.drawFullScreen(fondo, batch);
        menuUI.render(batch, font);
        batch.end();

        // Procesar entradas del usuario
        manejarInput();
    }

    /**
     * Lógica de navegación del menú con input del usuario.
     */
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
                case "JUGAR":
                    game.setScreen(new ContextScreen(game)); // Ir a pantalla de contextualización
                    break;
                case "CONFIGURACION":
                    AudioManager.stopMusic(); // Parar música del menú
                    game.setScreen(new ConfigurationScreen(game));
                    break;
                case "SALIR":
                    Gdx.app.exit(); // Cierra el juego
                    break;
            }
        }
    }

    /**
     * Libera los recursos usados por esta pantalla.
     */
    @Override
    public void dispose() {
        font.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadMenuAssets();
    }
}