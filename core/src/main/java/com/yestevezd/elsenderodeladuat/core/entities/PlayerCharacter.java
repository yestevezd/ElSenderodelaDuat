package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;

/**
 * Representa al personaje principal jugable. Hereda de BaseCharacter.
 * Controlado por InputManager.
 */
public class PlayerCharacter extends BaseCharacter {

    public PlayerCharacter(Texture spriteSheet, float x, float y, float speed) {
        super(spriteSheet, x, y, speed);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float dx = 0;
        float dy = 0;

        if (InputManager.isMoveUpPressed()) {
            dy += speed * delta;
            direction = Direction.UP;
        }
        if (InputManager.isMoveDownPressed()) {
            dy -= speed * delta;
            direction = Direction.DOWN;
        }
        if (InputManager.isMoveLeftPressed()) {
            dx -= speed * delta;
            direction = Direction.LEFT;
        }
        if (InputManager.isMoveRightPressed()) {
            dx += speed * delta;
            direction = Direction.RIGHT;
        }

        position.add(dx, dy);
        lastDx = dx;
        lastDy = dy;
    }
}