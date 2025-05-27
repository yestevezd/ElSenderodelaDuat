package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Pantalla de Game Over, adaptada a la resolución virtual
 * y con imagen de derrota.
 */
public class GameOverScreen extends BaseScreen {
    private BitmapFont titleFont;
    private BitmapFont promptFont;
    private Texture defeatTexture;
    private float blinkTimer;

    // Y offsets en unidades virtuales
    private static final float TITLE_Y = GameConfig.VIRTUAL_HEIGHT * 0.6f;
    private static final float IMAGE_Y = GameConfig.VIRTUAL_HEIGHT * 0.3f;
    private static final float PROMPT_Y = GameConfig.VIRTUAL_HEIGHT * 0.1f;

    public GameOverScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadGameOverAssets();
        AssetLoader.finishLoading();

        // Ajusta cámara UI a virtual
        game.getUiCamera().setToOrtho(false,
            GameConfig.VIRTUAL_WIDTH,
            GameConfig.VIRTUAL_HEIGHT
        );

        titleFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.RED);

        promptFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        promptFont.getData().setScale(1f);
        promptFont.setColor(Color.WHITE);

        defeatTexture = AssetLoader.get("characters/Inhotep_pierde.png", Texture.class);

        blinkTimer = 0f;
        AudioManager.playSound("sounds/sonido_gameover.mp3");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(game.getUiCamera().combined);

        batch.begin();
        // Título centrado
        TextUtils.drawCenteredText(batch, titleFont,
            "GAME OVER", TITLE_Y);

        // Imagen de derrota centrada
        float uiW = GameConfig.VIRTUAL_WIDTH;
        float uiH = GameConfig.VIRTUAL_HEIGHT;
        float texW = defeatTexture.getWidth();
        float texH = defeatTexture.getHeight();
        float scaleX = (uiW * 0.5f) / texW;
        float scaleY = (uiH * 0.3f) / texH;
        float scale = Math.min(scaleX, scaleY);
        float drawW = texW * scale;
        float drawH = texH * scale;
        float imgX = (uiW - drawW) * 0.5f;
        float imgY = IMAGE_Y - drawH * 0.5f;
        batch.draw(defeatTexture, imgX, imgY, drawW, drawH);

        // Prompt
        blinkTimer += delta;
        if ((blinkTimer % 1f) < 0.5f) {
            TextUtils.drawCenteredText(batch, promptFont,
                "Pulsa ENTER para salir", PROMPT_Y);
        }
        batch.end();

        if (InputManager.isSelectPressed()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        promptFont.dispose();
        AssetLoader.unloadGameOverAssets();
    }
}