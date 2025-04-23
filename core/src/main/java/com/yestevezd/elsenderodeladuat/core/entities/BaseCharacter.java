package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Clase base abstracta para todos los personajes del juego (jugador, NPCs, enemigos).
 * Maneja la posición, movimiento, dirección, animaciones y colisiones básicas.
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

    protected float lastDx = 0;
    protected float lastDy = 0;

    /**
     * Constructor del personaje base.
     *
     * @param spriteSheet Textura que contiene todas las animaciones del personaje.
     * @param x           Posición X inicial.
     * @param y           Posición Y inicial.
     * @param speed       Velocidad de movimiento.
     */
    public BaseCharacter(Texture spriteSheet, float x, float y, float speed) {
        this.spriteSheet = spriteSheet;
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.direction = Direction.DOWN;

        initializeAnimations();
    }

    /**
     * Divide el spritesheet en una cuadrícula 4x4 y crea una animación por dirección.
     * Se asume que las filas representan DOWN, LEFT, RIGHT, UP en ese orden.
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

    /**
     * Actualiza el temporizador de animación.
     *
     * @param delta Tiempo transcurrido desde el último frame (en segundos).
     */
    public void update(float delta) {
        animationTimer += delta;
    }

    /**
     * Renderiza el personaje en pantalla usando la animación actual según dirección y movimiento.
     *
     * @param batch SpriteBatch del juego para dibujar.
     */
    public void render(Batch batch) {
        Animation<TextureRegion> currentAnimation = getAnimationForDirection(direction);

        TextureRegion frame;
        if (isMoving()) {
            frame = currentAnimation.getKeyFrame(animationTimer, true);
        } else {
            frame = currentAnimation.getKeyFrames()[0]; // frame estático
        }

        batch.draw(frame, position.x, position.y,
                frame.getRegionWidth() * scale,
                frame.getRegionHeight() * scale);
    }

    /**
     * Indica si el personaje está en movimiento.
     *
     * @return true si se está desplazando; false si está quieto.
     */
    private boolean isMoving() {
        return lastDx != 0 || lastDy != 0;
    }

    /**
     * Obtiene la animación correspondiente a la dirección actual del personaje.
     *
     * @param dir Dirección a evaluar.
     * @return Animación correspondiente.
     */
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

    /**
     * Define la escala del personaje.
     *
     * @param scale Factor de escala (1.0f es sin escalar).
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * @return Posición actual del personaje.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Asigna una nueva posición al personaje.
     *
     * @param x Nueva coordenada X.
     * @param y Nueva coordenada Y.
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    /**
     * @return Dirección actual del personaje.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Define la dirección visual/movimiento del personaje.
     *
     * @param direction Nueva dirección.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Devuelve un rectángulo representando todo el cuerpo del personaje.
     *
     * @return Rectángulo de colisión completo.
     */
    public Rectangle getBounds() {
        float width = (spriteSheet.getWidth() / 4f) * scale;
        float height = (spriteSheet.getHeight() / 4f) * scale;
        return new Rectangle(position.x, position.y, width, height);
    }

    /**
     * Devuelve un rectángulo más pequeño (solo pies) para colisiones precisas.
     *
     * @return Rectángulo reducido para colisión.
     */
    public Rectangle getCollisionBounds() {
        float width = getWidth();
        float height = getHeight();

        float collisionWidth = width * 0.6f;
        float collisionHeight = height * 0.25f; // solo pies
        float offsetX = (width - collisionWidth) / 2f;
        float offsetY = 10; // altura desde la base

        return new Rectangle(
            position.x + offsetX,
            position.y + offsetY,
            collisionWidth,
            collisionHeight
        );
    }

    /**
     * @return Ancho del personaje, ajustado al escalado.
     */
    public float getWidth() {
        return (spriteSheet.getWidth() / 4f) * scale;
    }

    /**
     * @return Alto del personaje, ajustado al escalado.
     */
    public float getHeight() {
        return (spriteSheet.getHeight() / 4f) * scale;
    }

    public void setLastDx(float dx) {
        this.lastDx = dx;
    }
    
    public void setLastDy(float dy) {
        this.lastDy = dy;
    }
}