package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeManager;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeType;
import java.util.List;

public class DeathContextScreen extends BaseScreen {

    private BitmapFont bodyFont;
    private BitmapFont titleFont;
    private NarrativeManager narrativeManager;
    private float timeAccumulator;
    private Texture estelaTexture;

    public DeathContextScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Carga assets
        AssetLoader.loadContextAssets();
        AssetLoader.finishLoading();

        // Fuentes
        titleFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), false);
        titleFont.getData().setScale(1.5f);
        titleFont.setColor(Color.RED);

        bodyFont = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        bodyFont.getData().setScale(1f);
        bodyFont.setColor(Color.WHITE);

        // Imagen
        estelaTexture = AssetLoader.get("others/estela_inhotep.png", Texture.class);

        // Narrativa
        narrativeManager = new NarrativeManager();
        narrativeManager.addLine(new NarrativeLine(null, null,
            "Durante la noche Inhotep ha fallecido.", NarrativeType.TRANSLATED_ONLY));
        narrativeManager.addLine(new NarrativeLine(null, null,
            "Su viaje continúa ahora en la Duat donde tendrá que superar un último desafío.", NarrativeType.TRANSLATED_ONLY));
        narrativeManager.addLine(new NarrativeLine(null, null,
            "Así, por fin sabrá su destino final en el Juicio de Osiris.", NarrativeType.TRANSLATED_ONLY));

        AudioManager.playMusic("sounds/musica_context.mp3", true);
        timeAccumulator = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        float uiW = game.getUiCamera().viewportWidth;
        float uiH = game.getUiCamera().viewportHeight;
        float margin = uiH * 0.05f;

        // Dibujar título
        String title = "Inhotep ha fallecido";
        GlyphLayout titleLayout = new GlyphLayout(titleFont, title);
        float titleX = (uiW - titleLayout.width) / 2f;
        float titleY = uiH * 0.85f;
        batch.setProjectionMatrix(game.getUiCamera().combined);
        batch.begin();
        titleFont.draw(batch, titleLayout, titleX, titleY);
        batch.end();

        // Dibujar imagen
        float maxImgW = uiW * 0.5f;
        float scale = maxImgW / estelaTexture.getWidth();
        float imgH = estelaTexture.getHeight() * scale;
        float maxImgH = uiH * 0.3f;
        if (imgH > maxImgH) {
            scale = maxImgH / estelaTexture.getHeight();
            maxImgW = estelaTexture.getWidth() * scale;
            imgH = maxImgH;
        }
        float imgX = (uiW - maxImgW) / 2f;
        float imgY = titleY - imgH - margin;
        batch.begin();
        batch.draw(estelaTexture, imgX, imgY, maxImgW, imgH);
        batch.end();

        // Actualizar narrativa
        timeAccumulator += delta;
        narrativeManager.update(delta);

        // Dibujar narrativa en un solo batch
        List<NarrativeLine> visibles = narrativeManager.getVisibleLines();
        float lineHeight = bodyFont.getLineHeight() * 1.3f; // 30% extra para espacios
        // Ajuste vertical: sumar capHeight para permitir ascendentes
        float textY = imgY - margin + bodyFont.getCapHeight();
        batch.begin();
        for (NarrativeLine line : visibles) {
            String full = line.getTranslation();
            int chars = narrativeManager.getCharIndexForLine(visibles.indexOf(line));
            String part = full.substring(0, Math.min(chars, full.length()));
            GlyphLayout layout = new GlyphLayout(bodyFont, part);
            float x = (uiW - layout.width) / 2f;
            bodyFont.draw(batch, layout, x, textY);
            textY -= lineHeight;
        }
        batch.end();

        // Prompt final
        if (narrativeManager.isFinished()) {
            String prompt = "Pulsa ENTER para continuar";
            GlyphLayout pLayout = new GlyphLayout(bodyFont, prompt);
            float pX = (uiW - pLayout.width) / 2f;
            float pY = textY - (bodyFont.getLineHeight() * 0.5f);
            batch.begin();
            if ((timeAccumulator % 1f) < 0.5f) {
                bodyFont.draw(batch, pLayout, pX, pY);
            }
            batch.end();
        }

        // Avanzar pantalla
        if (narrativeManager.isFinished() && InputManager.isSelectPressed()) {
            AudioManager.stopMusic();
            game.setScreen(new InframundoScreen(game, 200, 150));
        }
    }

    @Override
    public void dispose() {
        AudioManager.stopMusic();
        AssetLoader.unloadContextAssets();
        titleFont.dispose();
    }
}