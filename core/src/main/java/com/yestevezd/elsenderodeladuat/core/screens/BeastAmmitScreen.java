package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.save.SaveManager;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Pantalla de final si Ammit devora el corazón.
 */
public class BeastAmmitScreen extends BaseScreen {
    private BitmapFont titleFont;
    private BitmapFont promptFont;
    private Texture ammitTexture;
    private float blinkTimer;
    private float baseTitleScale;

    private static final float TITLE_Y = GameConfig.VIRTUAL_HEIGHT * 0.7f;
    private static final float PROMPT_Y = GameConfig.VIRTUAL_HEIGHT * 0.2f;

    public BeastAmmitScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadBeastAmmitAssets();
        AssetLoader.finishLoading();
        ammitTexture = AssetLoader.get("characters/bestia_ammit.png", Texture.class);

        // Ajustar cámara UI a virtual
        game.getUiCamera().setToOrtho(false,
            GameConfig.VIRTUAL_WIDTH,
            GameConfig.VIRTUAL_HEIGHT
        );

        // Fuente título con escala base
        titleFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        baseTitleScale = 2.5f;
        titleFont.getData().setScale(baseTitleScale);
        titleFont.setColor(Color.RED);

        // Fuente prompt
        promptFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        promptFont.getData().setScale(1f);
        promptFont.setColor(Color.WHITE);

        blinkTimer = 0f;
        AudioManager.playSound("sounds/sonido_ammit.mp3");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float uiW = GameConfig.VIRTUAL_WIDTH;
        float uiH = GameConfig.VIRTUAL_HEIGHT;
        float gap = uiH * 0.03f;

        batch.setProjectionMatrix(game.getUiCamera().combined);

        // Ajustar escala del título para que quepa al 90% de ancho
        titleFont.getData().setScale(baseTitleScale);
        String title = "LA BESTIA AMMIT HA DEVORADO EL CORAZÓN DE INHOTEP";
        GlyphLayout titleLayout = new GlyphLayout(titleFont, title);
        float maxWidth = uiW * 0.9f;
        if (titleLayout.width > maxWidth) {
            float newScale = baseTitleScale * (maxWidth / titleLayout.width);
            titleFont.getData().setScale(newScale);
            titleLayout.setText(titleFont, title);
        }

        batch.begin();
        // Dibujar título centrado
        TextUtils.drawCenteredText(batch, titleFont, title, TITLE_Y);

        // Dibujar imagen centrada un poco más abajo
        float texW = ammitTexture.getWidth();
        float texH = ammitTexture.getHeight();
        float scaleX = (uiW * 0.5f) / texW;
        float scaleY = (uiH * 0.3f) / texH;
        float imgScale = Math.min(scaleX, scaleY);
        float drawW = texW * imgScale;
        float drawH = texH * imgScale;
        float imgX = (uiW - drawW) * 0.5f;
        float imgY = TITLE_Y - titleLayout.height - gap - drawH;
        batch.draw(ammitTexture, imgX, imgY, drawW, drawH);

        // Prompt parpadeante
        blinkTimer += delta;
        if ((blinkTimer % 1f) < 0.5f) {
            TextUtils.drawCenteredText(batch, promptFont,
                "Pulsa ENTER para salir", PROMPT_Y);
        }
        batch.end();

        // Navegación
        if (InputManager.isSelectPressed() || InputManager.isBackPressed()) {
            SaveManager.deleteSave();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        promptFont.dispose();
        AssetLoader.unloadBeastAmmitAssets();
    }
}