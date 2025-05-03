package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.entities.CombatEntity;

public class CombatHUD {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final CombatEntity player;
    private final CombatEntity enemy;

    public CombatHUD(CombatEntity player, CombatEntity enemy) {
        this.player = player;
        this.enemy = enemy;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
    }

    public void render(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float barWidth = 300f;
        float barHeight = 24f;
        float padding = 20f;
    
        float playerBarX = padding;
        float playerBarY = screenHeight - padding - barHeight;
        float enemyBarX = screenWidth - padding - barWidth;
        float enemyBarY = screenHeight - padding - barHeight;
    
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    
        // Barra de vida jugador
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(playerBarX, playerBarY, barWidth, barHeight);
    
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(playerBarX, playerBarY, barWidth * player.getCurrentHealth() / player.getMaxHealth(), barHeight);
    
        // Barra de vida enemigo
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(enemyBarX, enemyBarY, barWidth, barHeight);
    
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(enemyBarX, enemyBarY, barWidth * enemy.getCurrentHealth() / enemy.getMaxHealth(), barHeight);
    
        shapeRenderer.end();
    
        // Texto: nombres y números
        batch.begin();
        font.setColor(Color.GOLD);
    
        // Jugador: nombre + números
        String playerName = player.getName();
        String playerHealthText = player.getCurrentHealth() + " / " + player.getMaxHealth();
        font.draw(batch, playerName, playerBarX, playerBarY + barHeight + 20f);
        font.draw(batch, playerHealthText, playerBarX + barWidth - 60f, playerBarY + barHeight - 5f);
    
        // Enemigo: nombre + números
        String enemyName = enemy.getName();
        String enemyHealthText = enemy.getCurrentHealth() + " / " + enemy.getMaxHealth();
        font.draw(batch, enemyName, enemyBarX, enemyBarY + barHeight + 20f);
        font.draw(batch, enemyHealthText, enemyBarX + barWidth - 60f, enemyBarY + barHeight - 5f);
    
        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
