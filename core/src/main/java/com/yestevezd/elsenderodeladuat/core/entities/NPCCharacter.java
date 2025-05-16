package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext;

/**
 * Representa un NPC con comportamiento autónomo usando gdx-ai.
 */
public class NPCCharacter extends BaseCharacter implements CombatEntity{

    private DefaultStateMachine<NPCCharacter, NPCState> stateMachine;
    private final String animBasePath;
    private final String animPrefix;
    private final int animFrameCount;
    private Vector2 velocity;
    private boolean blocked = false;
    private Vector2 targetPosition;
    private GameEventContext gameContext;
    private boolean visible = true;
    private Vector2 customDestination;
    private String name = "NPC";
    private int currentHealth = 100;
    private int maxHealth = 100;

    public NPCCharacter(Texture texture, float x, float y, float speed, String animBasePath, String animPrefix, int animFrameCount) {
        super(texture, x, y, speed);
        this.velocity = new Vector2();
        this.animBasePath    = animBasePath;
        this.animPrefix      = animPrefix;
        this.animFrameCount  = animFrameCount;

        stateMachine = new DefaultStateMachine<>(this, NPCState.PATRULLAR);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateMachine.update();
    }

    public void updateConColision(float delta, Rectangle jugadorBounds) {
        update(delta);
    
        Vector2 nextPos = position.cpy().add(velocity.x * delta, velocity.y * delta);
        Rectangle futureBounds = getCollisionBounds();
        futureBounds.setPosition(nextPos.x, nextPos.y);
    
        if (!futureBounds.overlaps(jugadorBounds)) {
            position.set(nextPos);
        } else {
            velocity.setZero(); 
            lastDx = 0;
            lastDy = 0;
            blocked = true;
        }
    
        updateDirectionFromVelocity();
    }


    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public DefaultStateMachine<NPCCharacter, NPCState> getStateMachine() {
        return stateMachine;
    }

    private void updateDirectionFromVelocity() {
        if (velocity.len2() < 0.01f) return;

        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            direction = velocity.x > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            direction = velocity.y > 0 ? Direction.UP : Direction.DOWN;
        }

        lastDx = velocity.x;
        lastDy = velocity.y;
    }

    public boolean isBlocked() {
        return blocked;
    }
    
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }
    
    public Vector2 getTargetPosition() {
        return targetPosition;
    }
    
    public void setGameContext(GameEventContext context) {
        this.gameContext = context;
    }
    
    public GameEventContext getGameContext() {
        return gameContext;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public void render(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (!visible) return;
        super.render(batch);
    }

    public void receiveDamage(int amount, Vector2 attackerPos) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    
        float retroceso = 70f;
        Vector2 directionFromAttacker = this.position.cpy().sub(attackerPos).nor();
        Vector2 newPosition = this.position.cpy().add(directionFromAttacker.scl(retroceso));
    
        this.position.set(newPosition);
    
        blinkingTimer = 0.8f;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Rectangle getAttackBounds() {
        Rectangle base = getCollisionBounds();
        float attackWidth = base.width + 60f;
        return new Rectangle(
            direction == Direction.LEFT ? base.x - (attackWidth - base.width) : base.x,
            base.y,
            attackWidth,
            base.height
        );
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public void setCustomDestination(Vector2 dest) {
        this.customDestination = dest;
    }
    
    public Vector2 getCustomDestination() {
        return customDestination;
    }

    public String getAnimBasePath() {
        return animBasePath;
    }
    public String getAnimPrefix() {
        return animPrefix;
    }
    public int getAnimFrameCount() {
        return animFrameCount;
    }

}