package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.entities.Item;
import com.yestevezd.elsenderodeladuat.core.inventory.Inventory;

public class HUD {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final int maxHealth = 100;
    private int currentHealth = 100;

    private final Inventory inventory;
    private static final int INVENTORY_SLOTS = 5;

    public HUD(Inventory inventory) {
        this.inventory = inventory;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
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
    
        // === SHAPE RENDERING ===
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    
        // Fondo general
        shapeRenderer.setColor(new Color(0.29f, 0.18f, 0.10f, 1f));
        shapeRenderer.rect(blockX, blockY, blockWidth, blockHeight);
    
        // Corazón
        drawHeart(heartX, heartY, heartSize);
    
        // Barra de vida
        shapeRenderer.setColor(new Color(0.2f, 0.1f, 0.05f, 1));
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(barX + 2, barY + 2, (barWidth - 4) * healthPercent, barHeight - 4);
    
        // Dibujar los 5 slots del inventario (siempre visibles)
        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            float x = inventoryX + i * (slotSize + spacing);
    
            // Marco
            shapeRenderer.setColor(new Color(0.8f, 0.6f, 0.2f, 1));
            shapeRenderer.rect(x, inventoryY, slotSize, slotSize);
    
            // Fondo interior claro
            shapeRenderer.setColor(new Color(0.96f, 0.96f, 0.86f, 1f));
            shapeRenderer.rect(x + 4, inventoryY + 4, slotSize - 8, slotSize - 8);
        }
    
        shapeRenderer.end();
    
        // === TEXTURA Y TEXTO ===
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, currentHealth + " / " + maxHealth, barX + barWidth / 2f - 20, barY + barHeight - 6);
    
        // Dibujar ítems sobre los slots
        Item[] items = inventory.getItems();
        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            float x = inventoryX + i * (slotSize + spacing);
            if (items[i] != null) {
                batch.draw(items[i].getTexture(), x + 8, inventoryY + 8, 32, 32);
            }
        }
    
        batch.end();
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
