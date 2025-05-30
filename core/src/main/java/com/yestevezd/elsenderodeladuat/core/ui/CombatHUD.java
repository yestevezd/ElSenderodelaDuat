package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.entities.CombatEntity;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CombatHUD {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final CombatEntity player;
    private final CombatEntity enemy;
    private final OrthographicCamera camera;

    public CombatHUD(CombatEntity player, CombatEntity enemy, OrthographicCamera camera) {
        this.player = player;
        this.enemy = enemy;
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
    }

    public void render(SpriteBatch batch) {
        float screenWidth = camera.viewportWidth;
        float screenHeight = camera.viewportHeight;

        float barWidth = 300f;
        float barHeight = 24f;
        float padding = 20f;

        float playerBarX = padding;
        float playerBarY = screenHeight - padding - barHeight;
        float enemyBarX = screenWidth - padding - barWidth;
        float enemyBarY = screenHeight - padding - barHeight;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Vida jugador
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(playerBarX, playerBarY, barWidth, barHeight);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(playerBarX, playerBarY, barWidth * player.getCurrentHealth() / player.getMaxHealth(), barHeight);

        // Vida enemigo
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(enemyBarX, enemyBarY, barWidth, barHeight);

        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(enemyBarX, enemyBarY, barWidth * enemy.getCurrentHealth() / enemy.getMaxHealth(), barHeight);

        shapeRenderer.end();

        // Texto
        batch.begin();
        font.setColor(Color.GOLD);

        font.draw(batch, player.getName(), playerBarX, playerBarY + barHeight + 20f);
        font.draw(batch, player.getCurrentHealth() + " / " + player.getMaxHealth(), playerBarX + barWidth - 60f, playerBarY + barHeight - 5f);

        font.draw(batch, enemy.getName(), enemyBarX, enemyBarY + barHeight + 20f);
        font.draw(batch, enemy.getCurrentHealth() + " / " + enemy.getMaxHealth(), enemyBarX + barWidth - 60f, enemyBarY + barHeight - 5f);

        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}

