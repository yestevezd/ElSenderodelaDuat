package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;

public class DefeatScreen extends ScreenAdapter {
    private MainGame game;
    private float timer = 0f;
    private final float DURATION = 3f;
    private OrthographicCamera camera;
    private BitmapFont font;

    public DefeatScreen(MainGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false);
        this.font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        timer += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(camera.combined);

        // Centrado con GlyphLayout
        String message = "Has sido derrotado!";
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout(font, message);

        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;

        game.getBatch().begin();
        font.draw(game.getBatch(), layout, x, y);
        game.getBatch().end();

        if (timer >= DURATION) {
            font.getData().setScale(0.8f);
            game.setScreen(new HouseScreen(game));
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
