package com.yestevezd.elsenderodeladuat.core.combat;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.ui.CombatHUD;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;

public class CombatManager {
    private CombatState state;
    private float preparationTimer;
    private PlayerCharacter player;
    private NPCCharacter enemy;
    private CombatController controller;
    private CombatHUD hud;
    private CollisionSystem collisionSystem;
    private BitmapFont transitionFont;
    private Texture espadaTexture;
    private boolean musicStarted = false;
    private OrthographicCamera camera;

    public CombatManager(PlayerCharacter player, NPCCharacter enemy, CollisionSystem collisionSystem, OrthographicCamera camera) {
        this.player = player;
        this.enemy = enemy;
        this.collisionSystem = collisionSystem;
        this.camera = camera;
        this.state = CombatState.PREPARING;
        this.preparationTimer = 3f;
        this.transitionFont = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        this.transitionFont.getData().setScale(3f);
        this.espadaTexture = AssetLoader.get("items/espada.png", Texture.class);

        this.controller = new CombatController(player, enemy, collisionSystem);
        this.hud = new CombatHUD(player, enemy, camera);

        float centerX = 960;
        float centerY = 300;

        player.switchToCombatSprite();
        enemy.switchToCombatSprite();
        player.setPosition(centerX - 200, centerY);
        enemy.setPosition(centerX + 200, centerY);

        enemy.getStateMachine().changeState(NPCState.COMBATIR);

        player.setDirection(com.yestevezd.elsenderodeladuat.core.entities.Direction.RIGHT);
        enemy.setDirection(com.yestevezd.elsenderodeladuat.core.entities.Direction.LEFT);
    }

    public void update(float delta) {
        if (state == CombatState.PREPARING) {
            if (!musicStarted) {
                AudioManager.playMusic("sounds/musica_combate.mp3", true);
                musicStarted = true;
            }
            preparationTimer -= delta;
            if (preparationTimer <= 0) {
                state = CombatState.IN_COMBAT;
            }
            return;
        }

        if (state == CombatState.IN_COMBAT) {
            controller.update(delta);

            if (!player.isAlive()) {
                state = CombatState.DEFEAT;
                player.restoreOriginalSprite();
                enemy.restoreOriginalSprite();
                AudioManager.stopMusic();
            } else if (!enemy.isAlive()) {
                state = CombatState.VICTORY;
                player.restoreOriginalSprite();
                enemy.restoreOriginalSprite();
                AudioManager.stopMusic();
            }
        }
    }

    public void render(SpriteBatch batch, OrthogonalTiledMapRenderer mapRenderer, OrthographicCamera camera) {
        if (state == CombatState.PREPARING) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
            batch.begin();
        
            transitionFont.setColor(Color.WHITE);
            String text = "¡COMBATE!";
            float centerX = camera.viewportWidth / 2f;
            float centerY = camera.viewportHeight / 2f;
        
            GlyphLayout layout = new GlyphLayout(transitionFont, text);
            float textWidth = layout.width;
            float textHeight = layout.height;
        
            float swordScale = 2.5f;
            float swordWidth = espadaTexture.getWidth() * swordScale;
            float swordHeight = espadaTexture.getHeight() * swordScale;
            float ySword = centerY - swordHeight / 2f;
        
            batch.draw(espadaTexture, centerX - textWidth / 2f - swordWidth - 30f, ySword, swordWidth, swordHeight);
            batch.draw(espadaTexture, centerX + textWidth / 2f + 30f, ySword, swordWidth, swordHeight);
        
            transitionFont.draw(batch, layout, centerX - textWidth / 2f, centerY + textHeight / 2f);
        
            batch.end();
            return;
        }
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        controller.render(batch);
        batch.end();

        hud.render(batch);
    }

    public CombatState getState() {
        return state;
    }
}
