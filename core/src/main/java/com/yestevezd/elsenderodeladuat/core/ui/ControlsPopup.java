package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ControlsPopup {

    private boolean visible = false;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;

    private final String[] lines = {
        "CONTROLES DEL JUEGO",
        "",
        "[MENÚ]",
        "Flecha arriba / Flecha abajo: Navegar opciones",
        "Flecha izquierda / Flecha derecha: Cambiar valores",
        "",
        "[JUEGO]",
        "W / A / S / D o Flechas: Moverse",
        "E: Interactuar",
        "F: Atacar",
        "",
        "Pulsa ENTER o ESC para cerrar"
    };

    public ControlsPopup() {
        font = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        font.getData().setScale(1.1f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        batch.end();

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float lineHeight = 36f;
        float padding = 40f;
        float textAreaHeight = lines.length * lineHeight;
        float popupHeight = textAreaHeight + padding;
        float popupWidth = screenWidth * 0.65f;

        float popupX = (screenWidth - popupWidth) / 2f;
        float popupY = (screenHeight - popupHeight) / 2f;

        Color egyptianBrown = new Color(0.29f, 0.2f, 0.13f, 0.95f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(egyptianBrown);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();

        batch.begin();

        float currentY = popupY + popupHeight - 30f;

        for (String line : lines) {
            if (line.equals("Pulsa ENTER o ESC para cerrar")) {
                font.setColor(Color.RED);
            } else if (line.startsWith("[") || line.startsWith("CONTROLES")) {
                font.setColor(Color.GOLD);
            } else {
                font.setColor(Color.WHITE);
            }
            drawCentered(batch, font, line, currentY);
            currentY -= lineHeight;
        }
    }

    private void drawCentered(SpriteBatch batch, BitmapFont font, String text, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        font.draw(batch, layout, x, y);
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
