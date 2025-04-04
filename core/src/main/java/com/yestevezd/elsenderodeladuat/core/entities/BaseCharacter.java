package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Clase base para cualquier personaje del juego (jugador, NPCs, enemigos...).
 * Maneja animaciones, posición y escalado común.
 */
public abstract class BaseCharacter {

    protected Vector2 position;
    protected float speed;
    protected Direction direction;

    protected Texture spriteSheet;
    protected Animation<TextureRegion> walkUp;
    protected Animation<TextureRegion> walkDown;
    protected Animation<TextureRegion> walkLeft;
    protected Animation<TextureRegion> walkRight;

    protected float animationTimer = 0f;
    protected float scale = 1.0f;

    // Variables para saber si el personaje se está moviendo
    protected float lastDx = 0;
    protected float lastDy = 0;

    public BaseCharacter(Texture spriteSheet, float x, float y, float speed) {
        this.spriteSheet = spriteSheet;
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.direction = Direction.DOWN;

        initializeAnimations();
    }

    /**
     * Divide el spriteSheet en 4x4 y asigna animaciones por dirección.
     */
    private void initializeAnimations() {
        TextureRegion[][] frames = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / 4,
                spriteSheet.getHeight() / 4);

        walkDown = new Animation<>(0.15f, new Array<>(frames[0]));
        walkLeft = new Animation<>(0.15f, new Array<>(frames[1]));
        walkRight = new Animation<>(0.15f, new Array<>(frames[2]));
        walkUp = new Animation<>(0.15f, new Array<>(frames[3]));
    }

    public void update(float delta) {
        animationTimer += delta;
    }

    public void render(Batch batch) {
        Animation<TextureRegion> currentAnimation = getAnimationForDirection(direction);

        TextureRegion frame;
        if (isMoving()) {
            frame = currentAnimation.getKeyFrame(animationTimer, true);
        } else {
            frame = currentAnimation.getKeyFrames()[0]; // Primer frame
        }

        batch.draw(frame, position.x, position.y,
                frame.getRegionWidth() * scale,
                frame.getRegionHeight() * scale);
    }

    private boolean isMoving() {
        return lastDx != 0 || lastDy != 0;
    }

    private Animation<TextureRegion> getAnimationForDirection(Direction dir) {
        switch (dir) {
            case UP:
                return walkUp;
            case DOWN:
                return walkDown;
            case LEFT:
                return walkLeft;
            case RIGHT:
                return walkRight;
            default:
                return walkDown;
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}