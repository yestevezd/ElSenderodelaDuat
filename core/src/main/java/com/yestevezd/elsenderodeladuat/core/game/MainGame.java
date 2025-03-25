package com.yestevezd.elsenderodeladuat.core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.screens.IntroScreen;

public class MainGame extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new IntroScreen(this)); // Iniciar en la pantalla de introducción
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        AssetLoader.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}