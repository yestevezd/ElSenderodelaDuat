package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;

public class IntroScreen extends BaseScreen {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Array<TextureRegion> frames;
    private boolean showPressEnter = false;

    private BitmapFont font;
    private GlyphLayout layout;

    public IntroScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        frames = AssetLoader.loadVideoFrames("video_intro", 105);
        animation = new Animation<>(1f / 15f, frames, Animation.PlayMode.NORMAL);
        stateTime = 0f;
        showPressEnter = false;

        AudioManager.playMusic("sounds/musica_intro.mp3", false);

        font = new BitmapFont();
        layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        ScreenUtils.clear(0, 0, 0, 1);

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);

        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (animation.isAnimationFinished(stateTime)) {
            showPressEnter = true;

            TextUtils.drawBlinkingTextCentered(
                batch,
                font,
                "PULSA ENTER PARA CONTINUAR",
                80f,
                stateTime,
                1.0f // velocidad del parpadeo
            );
        }
        batch.end();

        // Si ya terminó y se pulsa ENTER, pasamos de pantalla de menu
        if (showPressEnter && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            AudioManager.stopMusic();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (TextureRegion region : frames) {
            region.getTexture().dispose();
        }  
        
        AudioManager.stopMusic();
    }
}