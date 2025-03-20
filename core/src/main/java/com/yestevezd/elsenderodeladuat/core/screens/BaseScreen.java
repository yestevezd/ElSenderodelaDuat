package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Screen;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseScreen implements Screen {
    protected final MainGame game;
    protected final SpriteBatch batch;

    public BaseScreen(MainGame game) {
        this.game = game;
        this.batch = game.getBatch();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}