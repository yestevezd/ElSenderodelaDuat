package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.game.MaatSystem;

/**
 * Pantalla de Juicio de Osiris adaptada a cualquier resolución.
 * - Fullscreen de la balanza (intro y resultado).
 * - Texto en recuadro negro en la zona superior.
 * - Transición automática tras FINAL_DELAY.
 */
public class JudgmentScreen extends BaseScreen {
    private Texture balanzaGanar;
    private Texture balanzaPerder;
    private Texture balanzaNeutra;
    private BitmapFont fontBody;
    private ShapeRenderer shapeRenderer;

    private float animationTimer;
    private boolean showGanar;
    private float introTimer;
    private static final float INTRO_DURATION = 10f;
    private static final float ANIMATION_INTERVAL = 0.9f;

    private boolean introPhase;
    private Texture resultTexture;
    private String resultText;
    private int corValue;

    private float finalDelayTimer;
    private static final float FINAL_DELAY = 4f;
    private boolean transitioned;

    public JudgmentScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadJudgmentAssets();
        AssetLoader.finishLoading();

        balanzaGanar = AssetLoader.get("end/balanza_ganar.png", Texture.class);
        balanzaPerder = AssetLoader.get("end/balanza_perder.png", Texture.class);
        balanzaNeutra = AssetLoader.get("end/balanza_neutra.jpg", Texture.class);

        fontBody = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        fontBody.getData().setScale(1f);
        fontBody.setColor(Color.WHITE);

        shapeRenderer = new ShapeRenderer();

        animationTimer = 0f;
        introTimer = 0f;
        finalDelayTimer = 0f;
        showGanar = true;
        introPhase = true;
        transitioned = false;

        AudioManager.playMusic("sounds/sonido_balanza.mp3", true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        float uiW = game.getUiCamera().viewportWidth;
        float uiH = game.getUiCamera().viewportHeight;

        // Determina la textura de fondo
        Texture background;
        if (introPhase) {
            animationTimer += delta;
            if (animationTimer > ANIMATION_INTERVAL) {
                showGanar = !showGanar;
                animationTimer = 0f;
            }
            background = showGanar ? balanzaGanar : balanzaPerder;
            introTimer += delta;
            if (introTimer >= INTRO_DURATION) {
                introPhase = false;
                corValue = MaatSystem.get().getCorazon();
                if (corValue < 0) {
                    resultTexture = balanzaGanar;
                    resultText = "El corazón de Inhotep pesa menos que la pluma, puede ir a los Campos de Aaru.";
                } else if (corValue == 0) {
                    resultTexture = balanzaNeutra;
                    resultText = "El corazón de Inhotep está en equilibrio con la pluma, puede ir a los Campos de Aaru.";
                } else {
                    resultTexture = balanzaPerder;
                    resultText = "El corazón de Inhotep pesa más que la pluma, será devorado por la bestia Ammit.";
                }
                background = resultTexture;
            }
        } else {
            background = resultTexture;
        }

        // Dibujar background fullscreen centrado
        batch.setProjectionMatrix(game.getUiCamera().combined);
        batch.begin();
        float texW = background.getWidth();
        float texH = background.getHeight();
        float scale = Math.max(uiW / texW, uiH / texH);
        float drawW = texW * scale;
        float drawH = texH * scale;
        float drawX = (uiW - drawW) * 0.5f;
        float drawY = (uiH - drawH) * 0.5f;
        batch.draw(background, drawX, drawY, drawW, drawH);
        batch.end();

        // Dibuja recuadro negro en la parte superior para texto
        shapeRenderer.setProjectionMatrix(game.getUiCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        float boxHeight = fontBody.getLineHeight() * (introPhase ? 1 : 2) + 20f;
        shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
        shapeRenderer.rect(0, uiH - boxHeight, uiW, boxHeight);
        shapeRenderer.end();

        // Dibuja textos en el recuadro superior
        batch.begin();
        String mainText = introPhase ?
            "Se está pesando el corazón contra la pluma..." : resultText;
        GlyphLayout mainLayout = new GlyphLayout(fontBody, mainText);
        float mainX = (uiW - mainLayout.width) * 0.5f;
        float padding = (boxHeight - fontBody.getLineHeight() * (introPhase ? 1 : 2)) / 2f;
        float mainY = uiH - padding;
        fontBody.draw(batch, mainLayout, mainX, mainY);
        batch.end();

        // Lógica de transición tras mostrar texto
        if (!introPhase && !transitioned) {
            finalDelayTimer += delta;
            if (finalDelayTimer >= FINAL_DELAY) {
                transitioned = true;
                if (corValue > 0) {
                    AudioManager.stopMusic();
                    game.setScreen(new BeastAmmitScreen(game));
                } else {
                    AudioManager.stopMusic();
                    game.setScreen(new VictoryScreen(game));
                }
            }
        }
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();
        fontBody.dispose();
        AssetLoader.unloadJudgmentAssets();
    }
}