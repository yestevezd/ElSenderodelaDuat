package com.yestevezd.elsenderodeladuat.core.screens;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
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
 * Pantalla de victoria cuando Inhotep accede a los Campos de Aaru.
 * Incluye rolling credits detrás de la UI y prompt para salir.
 */
public class VictoryScreen extends BaseScreen {
    private BitmapFont titleFont;
    private BitmapFont promptFont;
    private BitmapFont creditsFont;
    private Texture victoryTexture;
    private float blinkTimer;

    // Rolling credits
    private List<String> credits;
    private float creditsY;
    private static final float CREDITS_SPEED = 30f; // Unidades por segundo

    // Y offsets en unidades virtuales
    private static final float TITLE_Y = GameConfig.VIRTUAL_HEIGHT * 0.7f;
    private static final float IMAGE_Y = GameConfig.VIRTUAL_HEIGHT * 0.5f;
    private static final float PROMPT_Y = GameConfig.VIRTUAL_HEIGHT * 0.2f;

    public VictoryScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Carga assets de victoria
        AssetLoader.loadVictoryAssets();
        AssetLoader.finishLoading();
        victoryTexture = AssetLoader.get("characters/Inhotep_gana.png", Texture.class);

        // Fuente título
        titleFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        titleFont.getData().setScale(2.5f);
        titleFont.setColor(Color.GOLD);

        // Fuente prompt
        promptFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        promptFont.getData().setScale(1f);
        promptFont.setColor(Color.WHITE);

        // Fuente credits
        creditsFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        creditsFont.getData().setScale(0.8f);
        creditsFont.setColor(Color.WHITE);

        // Lista de créditos (orden lista, luego invertimos para scroll ascendente)
        credits = Arrays.asList(
            "Autor: Yago Estévez Davila",
            "Tutor: Ruth Sofía Contreras Espinosa",
            "Departamento: Área de videojuegos, UOC",
            "Diseño de juego: Yago Estévez Davila",
            "Programación: Yago Estévez Davila",
            "Música: Pixabay (CC0)",
            "Efectos de sonido: Pixabay (CC0)",
            "Motor de juego: LibGDX",
            "Lenguaje: Java 17",
            "Programas: VSCode, Photoshop, Tiled, Hiero V5, GDX-Liftoff, GitHub Desktop",
            "Control de versiones: GitHub",
            "Testers: Inmaculada D. González, Noelia E. Davila, Sara, Diego A. Davila",
            "Agradecimientos especiales: Inmaculada Delage González, Noelia Estévez Davila",
            "Licencia del proyecto: Reconocimiento-NoComercial-SinObraDerivada 3.0 España",  
            "Copyright © 2025 Yago Estévez Davila"
        );
        // Invertir para que el primer elemento salga primero
        Collections.reverse(credits);

        // Iniciar scroll por debajo de la pantalla
        creditsY = -credits.size() * (creditsFont.getLineHeight() + 8);

        blinkTimer = 0f;

        // Ajustar cámara UI a virtual
        game.getUiCamera().setToOrtho(false,
            GameConfig.VIRTUAL_WIDTH,
            GameConfig.VIRTUAL_HEIGHT
        );

        AudioManager.playMusic("sounds/musica_final.mp3", true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float uiW = GameConfig.VIRTUAL_WIDTH;
        float uiH = GameConfig.VIRTUAL_HEIGHT;

        // Actualizar posición de los créditos (scroll ascendente)
        creditsY += delta * CREDITS_SPEED;

        batch.setProjectionMatrix(game.getUiCamera().combined);
        batch.begin();

        // Dibujar créditos detrás de la UI
        for (int i = 0; i < credits.size(); i++) {
            String line = credits.get(i);
            GlyphLayout layout = new GlyphLayout(creditsFont, line);
            float x = (uiW - layout.width) * 0.5f;
            float y = creditsY + i * (creditsFont.getLineHeight() + 8);
            creditsFont.draw(batch, layout, x, y);
        }

        // Título centrado
        String title = "INHOTEP ACCEDE A LOS CAMPOS DE AARU";
        GlyphLayout titleLayout = new GlyphLayout(titleFont, title);
        float tx = (uiW - titleLayout.width) * 0.5f;
        titleFont.draw(batch, titleLayout, tx, TITLE_Y);

        // Imagen centrada
        float texW = victoryTexture.getWidth();
        float texH = victoryTexture.getHeight();
        float scaleX = (uiW * 0.5f) / texW;
        float scaleY = (uiH * 0.3f) / texH;
        float scale = Math.min(scaleX, scaleY);
        float drawW = texW * scale;
        float drawH = texH * scale;
        float imgX = (uiW - drawW) * 0.5f;
        float imgY = IMAGE_Y - drawH * 0.5f;
        batch.draw(victoryTexture, imgX, imgY, drawW, drawH);

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
        creditsFont.dispose();
        AssetLoader.unloadVictoryAssets();
    }
}