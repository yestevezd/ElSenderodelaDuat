package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class LoreOverlayManager {

    private enum State { INACTIVE, INITIALIZING, ACTIVE, CLOSING, FINISHED }
    private State state = State.INACTIVE;

    private Texture loreTexture;
    private Texture backgroundTexture;
    private BitmapFont font;
    private GlyphLayout layout;

    private float alpha = 0f;
    private float delayTimer = 0f;
    private float fadeSpeed = 1.5f;

    private float closeTimer = 0f;
    private final float startDelay = 1f;
    private final float closeDelay = 3f;
    private final String closePrompt = "Pulsa E para cerrar";

    private final int padding = 30;
    private final int promptMargin = 20;
    private final int boxWidth = 800;
    private final int screenWidth = 1920;
    private final Color dialogueColor = new Color(0.2f, 0.1f, 0f, 1);

    public LoreOverlayManager() {}

    public void trigger(String texturePath, String text) {
        loreTexture = new Texture(Gdx.files.internal(texturePath));
        backgroundTexture = AssetLoader.get("others/fondo_configuracion.jpg", Texture.class);
        font = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        font.getData().setScale(0.7f);
        font.setColor(dialogueColor);

        layout = new GlyphLayout(font, text, dialogueColor, boxWidth - 2 * padding, Align.left, true);

        state = State.INITIALIZING;
        alpha = 0f;
        delayTimer = 0f;
        closeTimer = 0f;
    }

    public void update(float delta) {
        if (state == State.FINISHED || state == State.INACTIVE) return;
        delayTimer += delta;
        switch (state) {
            case INITIALIZING:
                if (delayTimer >= startDelay) {
                    state = State.ACTIVE;
                    closeTimer = 0f;
                }
                break;
            case ACTIVE:
                closeTimer += delta;
                if (alpha < 1f) alpha = Math.min(1f, alpha + fadeSpeed * delta);
                if (closeTimer >= closeDelay && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    state = State.CLOSING;
                }
                break;
            case CLOSING:
                alpha -= fadeSpeed * delta;
                if (alpha <= 0f) {
                    alpha = 0f;
                    state = State.FINISHED;
                    dispose();
                }
                break;
            default:
                break;
        }
    }

    public void render(Batch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        if (state != State.ACTIVE && state != State.CLOSING) return;

        // Papiro
        float loreWidth = loreTexture.getWidth() * 0.6f;
        float loreHeight = loreTexture.getHeight() * 0.6f;
        float loreX = camera.position.x - loreWidth / 2;
        float loreY = camera.position.y - 140f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(loreTexture, loreX, loreY, loreWidth, loreHeight);
        batch.setColor(1, 1, 1, 1);
        batch.end();

        if (state == State.ACTIVE && alpha >= 1f) {
            // Crear layout para prompt cada frame (para capturar color dinámico)
            GlyphLayout promptLayout = new GlyphLayout(font, closePrompt);
            float promptHeight = promptLayout.height;
            float textHeight = layout.height;
            float boxHeight = textHeight + promptHeight + promptMargin + 2 * padding;

            float boxX = (screenWidth - boxWidth) / 2f;
            float boxY = loreY - (-120f) - boxHeight - 10f; // visibleBottomOffset

            // Fondos
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(boxX - 6, boxY - 6, boxWidth + 12, boxHeight + 12);
            shapeRenderer.setColor(Color.ROYAL);
            shapeRenderer.rect(boxX - 3, boxY - 3, boxWidth + 6, boxHeight + 6);
            shapeRenderer.end();

            batch.begin();
            batch.setColor(1, 1, 1, alpha);
            batch.draw(backgroundTexture, boxX, boxY, boxWidth, boxHeight);
            batch.setColor(1, 1, 1, 1);

            // Texto principal en diálogo
            font.setColor(dialogueColor.r, dialogueColor.g, dialogueColor.b, alpha);
            float textX = boxX + padding;
            float textY = boxY + boxHeight - padding;
            font.draw(batch, layout, textX, textY);

            // Mostrar prompt tras delay
            if (closeTimer >= closeDelay) {
                font.setColor(1f, 0f, 0f, alpha);
                float promptX = boxX + (boxWidth - promptLayout.width) / 2f;
                float promptY = textY - textHeight - promptMargin;
                font.draw(batch, closePrompt, promptX, promptY);
                // Restaurar color
                font.setColor(dialogueColor);
            }
            batch.end();
        }
    }

    public boolean isBlocking() {
        return state == State.INITIALIZING || state == State.ACTIVE || state == State.CLOSING;
    }

    public boolean isFinished() {
        return state == State.FINISHED;
    }

    public void dispose() {
        if (loreTexture != null) loreTexture.dispose();
        if (font != null) font.dispose();
    }
}