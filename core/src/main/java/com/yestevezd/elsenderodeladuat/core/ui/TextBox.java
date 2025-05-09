package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class TextBox {

    private String text;
    private boolean visible = false;

    private final int padding = 30;
    private final int boxWidth = 800;

    private float boxHeight;
    private BitmapFont font;
    private Texture backgroundTexture;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;
    private int extraOptionLines = 0;

    public TextBox() {
        // Cargar fuente personalizada desde AssetLoader
        font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.getData().setScale(0.7f);
        font.setColor(new Color(0.2f, 0.1f, 0f, 1));

        backgroundTexture = AssetLoader.get("others/fondo_configuracion.jpg", Texture.class);
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();
    }

    public void show(String text) {
        this.text = text;
        this.visible = true;
        this.extraOptionLines = 0;
    
        updateLayoutAndHeight();
    }

    public void showWithOptions(String text, int numberOfOptions) {
        this.text = text;
        this.visible = true;
        this.extraOptionLines = numberOfOptions;
    
        updateLayoutAndHeight();
    }

    private void updateLayoutAndHeight() {
        // Separar speaker y mensaje
        String[] parts = text.split(":", 2);
        String speaker = parts[0].trim();
        String message = parts.length > 1 ? parts[1].trim() : "";
    
        // Medir speaker
        GlyphLayout speakerLayout = new GlyphLayout(font, speaker + ": ");
        float speakerWidth = speakerLayout.width;
        
        font.setColor(new Color(0.2f, 0.1f, 0f, 1));
        
        // Medir mensaje
        layout.setText(
            font,
            message,
            0,
            message.length(),
            font.getColor(),
            boxWidth - 2 * padding - speakerWidth,
            1,
            true,
            null
        );
    
        float textHeight = Math.max(speakerLayout.height, layout.height);
        float optionsHeight = extraOptionLines * 35f;
    
        // Ajustar altura total
        boxHeight = textHeight + optionsHeight + 2 * padding;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

   
    public void render(SpriteBatch batch) {
        if (!visible) return;

        int x = getBoxX();
        int y = 50;

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Bordes
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(x - 6, y - 6, boxWidth + 12, boxHeight + 12);
        shapeRenderer.setColor(Color.ROYAL);
        shapeRenderer.rect(x - 3, y - 3, boxWidth + 6, boxHeight + 6);
        shapeRenderer.end();

        batch.begin();

        // Fondo
        batch.draw(backgroundTexture, x, y, boxWidth, boxHeight);

        // Speaker y mensaje
        String[] parts = text.split(":", 2);
        String speaker = parts[0].trim();

        BitmapFont font = getFont();

        float totalTextHeight = Math.max(new GlyphLayout(font, speaker + ": ").height, layout.height);
        float baseY = y + boxHeight - padding;

        if (extraOptionLines == 0) {
            float contentHeight = totalTextHeight;
            baseY = y + (boxHeight + contentHeight) / 2f - font.getDescent();
        }

        float textX = x + padding;

        // Speaker
        font.setColor(Color.ROYAL);
        GlyphLayout speakerLayout = new GlyphLayout(font, speaker + ": ");
        font.draw(batch, speakerLayout, textX, baseY);

        // Mensaje
        font.setColor(new Color(0.2f, 0.1f, 0f, 1));
        font.draw(batch, layout, textX + speakerLayout.width, baseY);
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getScreenWidth() {
        return Gdx.graphics.getWidth();
    }
    
    public float getBoxHeight() {
        return boxHeight;
    }

    public int getBoxX() {
        return (Gdx.graphics.getWidth() - boxWidth) / 2;
    }
    
    public int getBoxY() {
        return 50; 
    }

    public float getTextHeight() {
        return layout.height;
    }
    public int getPadding() {
        return padding;
    }

    public void setExtraOptionLines(int lines) {
        this.extraOptionLines = lines;
        updateLayoutAndHeight();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
