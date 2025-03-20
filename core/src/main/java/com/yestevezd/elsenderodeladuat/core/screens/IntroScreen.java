package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class IntroScreen extends BaseScreen {
    private Texture logo;
    private float timeElapsed;

    public IntroScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        logo = AssetLoader.getTexture("assets/intro_logo.png");
        timeElapsed = 0;
    }

    @Override
    public void render(float delta) {
        timeElapsed += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(logo, 500, 300);
        batch.end();

        if (timeElapsed > 3) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        
    }
}