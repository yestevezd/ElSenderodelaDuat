package com.yestevezd.elsenderodeladuat.core.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;

public class CombatController {
    private final PlayerCharacter player;
    private final NPCCharacter enemy;
    private final CombatAnimation playerAttackAnim;
    private final CombatAnimation enemyAttackAnim;
    private final CollisionSystem collisionSystem;

    private boolean playerAttacking = false;
    private boolean enemyAttacking = false;

    private float enemyAttackCooldown = 0f;
    private final float minDistanceBetween = 60f;

    public CombatController(PlayerCharacter player, NPCCharacter enemy, CollisionSystem collisionSystem) {
        this.collisionSystem = collisionSystem;
        this.player = player;
        this.enemy = enemy;
        this.playerAttackAnim = new CombatAnimation(
            "characters/characterAnimations/personajePrincipal",
            "atac_prin_",
            5,
            0.1f
        );
        this.enemyAttackAnim = new CombatAnimation(
            "characters/characterAnimations/sacerdote",
            "atac_sac_",
            6,
            0.1f
        );
    }

    public void update(float delta) {
        Vector2 oldPlayerPos = player.getPosition().cpy();
        Vector2 oldEnemyPos = enemy.getPosition().cpy();

        if (!playerAttacking) {
            player.update(delta);
        }
        enemy.updateConColision(delta, player.getCollisionBounds());

        // Evitar solapamientos exagerados
        float distance = Math.abs(player.getPosition().x - enemy.getPosition().x);
        if (distance < minDistanceBetween) {
            float push = (minDistanceBetween - distance) / 2f;
            if (player.getPosition().x < enemy.getPosition().x) {
                player.setPosition(player.getPosition().x - push, player.getPosition().y);
                enemy.setPosition(enemy.getPosition().x + push, enemy.getPosition().y);
            } else {
                player.setPosition(player.getPosition().x + push, player.getPosition().y);
                enemy.setPosition(enemy.getPosition().x - push, enemy.getPosition().y);
            }
        }

        handlePlayerAttack(delta);
        handleEnemyAttack(delta);

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPlayerPos.x, oldPlayerPos.y);
        }

        if (collisionSystem.isColliding(enemy.getCollisionBounds())) {
            enemy.setPosition(oldEnemyPos.x, oldEnemyPos.y);
        }
    }

    private void handlePlayerAttack(float delta) {
        if (InputManager.isAttackPressed() && !playerAttacking) {
            playerAttacking = true;
            playerAttackAnim.start();
        }

        if (playerAttacking) {
            playerAttackAnim.update(delta);
            if (playerAttackAnim.isFinished()) {
                playerAttacking = false;
                if (player.getAttackBounds().overlaps(enemy.getCollisionBounds())) {
                    enemy.receiveDamage(5, player.getPosition());
                }
            }
        }
    }

    private void handleEnemyAttack(float delta) {
        float distance = Math.abs(enemy.getPosition().x - player.getPosition().x);
        float attackRange = 65f;

        if (!enemyAttacking && distance <= attackRange && enemyAttackCooldown <= 0f) {
            enemyAttacking = true;
            enemyAttackAnim.start();
            enemyAttackCooldown = 1f;
        }

        if (!enemyAttacking) {
            enemyAttackCooldown -= delta;
        }

        if (enemyAttacking) {
            enemyAttackAnim.update(delta);
            if (enemyAttackAnim.isFinished()) {
                enemyAttacking = false;
                if (enemy.getAttackBounds().overlaps(player.getCollisionBounds())) {
                    player.receiveDamage(10, enemy.getPosition());
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (playerAttacking) {
            TextureRegion frame = playerAttackAnim.getCurrentFrame();
            batch.draw(
                frame,
                player.getPosition().x,
                player.getPosition().y,
                frame.getRegionWidth() * player.getScaleX(),
                frame.getRegionHeight() * player.getScaleY()
            );
        } else {
            player.render(batch);
        }

        if (enemyAttacking) {
            TextureRegion frame = enemyAttackAnim.getCurrentFrame();
            float offsetX = 15f * enemy.getScaleX();
            float offsetY = 5f * enemy.getScaleX();
            batch.draw(
                frame,
                enemy.getPosition().x - offsetX,
                enemy.getPosition().y - offsetY,
                frame.getRegionWidth() * enemy.getScaleX(),
                frame.getRegionHeight() * enemy.getScaleY()
            );
        } else {
            enemy.render(batch);
        }
    }
}
