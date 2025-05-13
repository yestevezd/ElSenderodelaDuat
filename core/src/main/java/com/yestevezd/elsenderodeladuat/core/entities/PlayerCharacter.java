package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;

/**
 * Representa al personaje principal controlado por el jugador.
 * Hereda de {@link BaseCharacter} y gestiona el movimiento mediante InputManager.
 */
public class PlayerCharacter extends BaseCharacter implements CombatEntity {

    private boolean enCombate = false;
    private int currentHealth = 100;
    private int maxHealth = 100;

    /**
     * Constructor del personaje principal.
     *
     * @param spriteSheet Spritesheet con las animaciones del personaje.
     * @param x           Posición X inicial.
     * @param y           Posición Y inicial.
     * @param speed       Velocidad de movimiento.
     */
    public PlayerCharacter(Texture spriteSheet, float x, float y, float speed) {
        super(spriteSheet, x, y, speed);
    }

    /**
     * Actualiza el personaje en cada frame.
     * Lee la entrada del jugador desde {@link InputManager} y actualiza la posición y dirección.
     *
     * @param delta Tiempo en segundos desde el último frame.
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        float dx = 0;
        float dy = 0;

        // Movimiento vertical
        if (!enCombate) {
            if (InputManager.isMoveUpPressed()) {
                dy += speed * delta;
                direction = Direction.UP;
            }
    
            if (InputManager.isMoveDownPressed()) {
                dy -= speed * delta;
                direction = Direction.DOWN;
            }
        }

        // Movimiento horizontal
        if (InputManager.isMoveLeftPressed()) {
            dx -= speed * delta;
            direction = Direction.LEFT;
        }
        if (InputManager.isMoveRightPressed()) {
            dx += speed * delta;
            direction = Direction.RIGHT;
        }

        // Aplicar movimiento
        position.add(dx, dy);
        lastDx = dx;
        lastDy = dy;
    }

    public Rectangle getAttackBounds() {
        Rectangle base = getCollisionBounds();
        float attackWidth = base.width + 40f;
        return new Rectangle(
            direction == Direction.RIGHT ? base.x : (base.x + base.width - attackWidth),
            base.y,
            attackWidth,
            base.height
        );
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

    @Override
    public String getName() {
        return "Imhotep";
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setEnCombate(boolean valor) {
        this.enCombate = valor;
    }

    public void restoreFullHealth() {
        this.currentHealth = this.maxHealth;
    }

    public void setCurrentHealth(int hp) {
        this.currentHealth = MathUtils.clamp(hp, 0, this.maxHealth);
    }
    
}