package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;

/**
 * Clase base abstracta para todas las pantallas del juego.
 * Proporciona cámara, viewport y SpriteBatch común.
 */
public abstract class BaseScreen implements Screen {

    protected final MainGame game;
    protected final SpriteBatch batch;

    protected OrthographicCamera camera;
    protected Viewport viewport;

    public BaseScreen(MainGame game) {
        this.game = game;
        this.batch = game.getBatch();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT, camera);
        this.viewport.apply();
        this.camera.position.set(GameConfig.VIRTUAL_WIDTH / 2f, GameConfig.VIRTUAL_HEIGHT / 2f, 0);
        this.camera.update();
    }

    public MainGame getGame() {
        return game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
