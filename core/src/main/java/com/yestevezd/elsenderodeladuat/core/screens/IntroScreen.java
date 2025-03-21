package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;

public class IntroScreen extends BaseScreen {
    private Texture fondo;
    private float timeElapsed;

    public IntroScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        fondo = AssetLoader.getTexture("others/imagen_demonio.jpeg");
        timeElapsed = 0;
    }

    @Override
    public void render(float delta) {
        timeElapsed += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        TextureUtils.drawFullScreen(fondo, batch);
        batch.end();

        /*if (timeElapsed > 3) {
            game.setScreen(new MainMenuScreen(game));
        }*/
    }

    @Override
    public void dispose() {
        
    }
}