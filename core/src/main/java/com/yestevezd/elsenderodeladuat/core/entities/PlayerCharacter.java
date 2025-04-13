package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;

/**
 * Representa al personaje principal controlado por el jugador.
 * Hereda de {@link BaseCharacter} y gestiona el movimiento mediante InputManager.
 */
public class PlayerCharacter extends BaseCharacter {

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
        if (InputManager.isMoveUpPressed()) {
            dy += speed * delta;
            direction = Direction.UP;
        }
        if (InputManager.isMoveDownPressed()) {
            dy -= speed * delta;
            direction = Direction.DOWN;
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
}