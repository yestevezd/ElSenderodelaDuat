package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.entities.Item;
import com.yestevezd.elsenderodeladuat.core.inventory.Inventory;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class HUD {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final BitmapFont popupFont; 

    private final int maxHealth = 100;
    private int currentHealth = 100;

    private final Inventory inventory;
    private static final int INVENTORY_SLOTS = 5;

    private String popupMessage;
    private float popupTimer = 0f;
    private Item[] popupItems;

    private final OrthographicCamera uiCamera;

    public HUD(Inventory inventory, OrthographicCamera uiCamera) {
        this.inventory = inventory;
        this.uiCamera = uiCamera;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        popupFont = new BitmapFont(
            Gdx.files.internal("fonts/ui_font.fnt"),
            Gdx.files.internal("fonts/ui_font.png"),
            false
        );
        popupFont.getData().setScale(0.8f);
    }

    public void showPopupMessage(String message, Item... items) {
        this.popupMessage = message;
        this.popupTimer = 3f;
        this.popupItems = items;
    }

    public void render(SpriteBatch batch) {
        int screenWidth = Gdx.graphics.getWidth();

        float padding = 16f;
        float slotSize = 50f;
        float spacing = 12f;
        float barWidth = 180f;
        float barHeight = 24f;
        float heartSize = 24f;

        float inventoryWidth = INVENTORY_SLOTS * (slotSize + spacing) - spacing;
        float blockHeight = Math.max(heartSize + barHeight + 8f, slotSize) + 20f;
        float blockY = Gdx.graphics.getHeight() - blockHeight - padding;

        float barX = screenWidth - padding - inventoryWidth - 30f - barWidth;
        float barY = blockY + (blockHeight - barHeight) / 2f - 12f;

        float heartX = barX + (barWidth - heartSize) / 2f;
        float heartY = barY + barHeight + 12f;

        float inventoryX = barX + barWidth + 30f;
        float inventoryY = blockY + (blockHeight - slotSize) / 2f;

        float blockX = barX - 10f;
        float blockWidth = inventoryX + inventoryWidth - blockX + 10f;

        float healthPercent = currentHealth / (float) maxHealth;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.29f, 0.18f, 0.10f, 1f));
        shapeRenderer.rect(blockX, blockY, blockWidth, blockHeight);
        drawHeart(heartX, heartY, heartSize);

        shapeRenderer.setColor(new Color(0.2f, 0.1f, 0.05f, 1));
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(barX + 2, barY + 2, (barWidth - 4) * healthPercent, barHeight - 4);

        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            float x = inventoryX + i * (slotSize + spacing);
            shapeRenderer.setColor(new Color(0.8f, 0.6f, 0.2f, 1));
            shapeRenderer.rect(x, inventoryY, slotSize, slotSize);

            shapeRenderer.setColor(new Color(0.96f, 0.96f, 0.86f, 1f));
            shapeRenderer.rect(x + 4, inventoryY + 4, slotSize - 8, slotSize - 8);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.setProjectionMatrix(uiCamera.combined);

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, currentHealth + " / " + maxHealth, barX + barWidth / 2f - 20, barY + barHeight - 6);

        Item[] items = inventory.getItems();
        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            float x = inventoryX + i * (slotSize + spacing);
            if (items[i] != null) {
                batch.draw(items[i].getTexture(), x + 8, inventoryY + 8, 32, 32);
            }
        }
        batch.end();

        if (popupTimer > 0) {
            popupTimer -= Gdx.graphics.getDeltaTime();
        
            float textMargin = 30f;
            float iconSize = 48f;
            float iconSpacing = 20f;
            int numberOfIcons = (popupItems != null) ? popupItems.length : 0;
        
            float textWidth = 400f;
            float titleHeight = 40f;
            float textHeight = 60f;
            float iconsHeight = (numberOfIcons > 0) ? (iconSize + 20f) : 0f;
        
            float popupWidth = Math.max(300f, textWidth + 2 * textMargin);
            float popupHeight = titleHeight + textHeight + iconsHeight + 2 * textMargin;
        
            float popupX = (Gdx.graphics.getWidth() - popupWidth) / 2f;
            float popupY = Gdx.graphics.getHeight() - popupHeight - 150f;
        
            // Fondo papiro
            Texture papiroTexture = AssetLoader.get("others/fondo_configuracion.jpg", Texture.class);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
            shapeRenderer.end();

            batch.setProjectionMatrix(uiCamera.combined);
            shapeRenderer.setProjectionMatrix(uiCamera.combined);
        
            batch.begin();
            batch.draw(papiroTexture, popupX, popupY, popupWidth, popupHeight);
            batch.end();
        
            // Borde dorado
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(new Color(0.8f, 0.6f, 0.2f, 1f));
            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
            shapeRenderer.end();

            batch.setProjectionMatrix(uiCamera.combined);
            shapeRenderer.setProjectionMatrix(uiCamera.combined);
        
            batch.begin();
        
            // Título (solo si hay objetos)
            if (popupItems != null && popupItems.length > 0) {
                popupFont.setColor(new Color(0.35f, 0.22f, 0.1f, 1f));
                popupFont.draw(batch, "¡Objetos Conseguidos!",
                        popupX + popupWidth / 2f - 120f,
                        popupY + popupHeight - 20f);
            }

            // Texto principal
            popupFont.setColor(new Color(0.35f, 0.22f, 0.1f, 1f));
            popupFont.draw(batch, popupMessage,
                popupX + textMargin,
                popupY + popupHeight - 70f,
                textWidth,
                1,
                true
            );
        
            // Dibujar iconos de objetos
            if (popupItems != null && popupItems.length > 0) {
                float totalIconsWidth = numberOfIcons * iconSize + (numberOfIcons - 1) * iconSpacing;
                float startX = popupX + (popupWidth - totalIconsWidth) / 2f;
                float iconY = popupY + textMargin;
        
                for (int i = 0; i < popupItems.length; i++) {
                    if (popupItems[i] != null) {
                        batch.draw(popupItems[i].getTexture(), startX + i * (iconSize + iconSpacing), iconY, iconSize, iconSize);
                    }
                }
            }
            popupFont.setColor(Color.WHITE);
        
            batch.end();
        }
    }

    private void drawHeart(float x, float y, float size) {
        float radius = size / 4f;
        float triangleHeight = size * 0.6f;

        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.circle(x + radius, y + radius, radius);
        shapeRenderer.circle(x + 3 * radius, y + radius, radius);

        shapeRenderer.triangle(
                x, y + radius,
                x + size, y + radius,
                x + size / 2f, y - triangleHeight + radius
        );
    }

    public void setHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(maxHealth, health));
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}