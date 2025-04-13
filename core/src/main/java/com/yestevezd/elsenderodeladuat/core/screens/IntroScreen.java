package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;

/**
 * Pantalla de introducción animada del juego.
 * Reproduce una secuencia de imágenes como una animación de video,
 * muestra un mensaje para continuar al finalizar y permite
 * avanzar al menú principal pulsando ENTER.
 */
public class IntroScreen extends BaseScreen {

    private Animation<TextureRegion> animation;
    private float stateTime;
    private Array<TextureRegion> frames;
    private boolean showPressEnter = false;

    private BitmapFont font;

    /**
     * Constructor de la pantalla de introducción.
     *
     * @param game Instancia principal del juego
     */
    public IntroScreen(MainGame game) {
        super(game);
    }

    /**
     * Método llamado al mostrar la pantalla. Inicializa los recursos.
     */
    @Override
    public void show() {
        // Cargar recursos necesarios para la intro
        AssetLoader.loadIntroAssets();
        AssetLoader.finishLoading();

        // Cargar secuencia de fotogramas para el "video"
        frames = AssetLoader.loadVideoFrames("video_intro", 105);
        animation = new Animation<>(1f / 15f, frames, Animation.PlayMode.NORMAL);
        stateTime = 0f;
        showPressEnter = false;

        // Reproducir música de introducción sin loop
        AudioManager.playMusic("sounds/musica_intro.mp3", false);

        // Configurar fuente
        font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.getData().setScale(0.9f);
    }

    /**
     * Renderiza cada frame de la animación y el mensaje de "Pulsa ENTER".
     *
     * @param delta Tiempo transcurrido desde el último frame
     */
    @Override
    public void render(float delta) {
        stateTime += delta;
        ScreenUtils.clear(0, 0, 0, 1);

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);

        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Si la animación ha finalizado, mostrar texto para continuar
        if (animation.isAnimationFinished(stateTime)) {
            showPressEnter = true;

            TextUtils.drawBlinkingTextCentered(
                batch,
                font,
                "PULSA ENTER PARA CONTINUAR",
                80f,
                stateTime,
                1.0f // Parpadeo cada 1 segundo
            );
        }
        batch.end();

        // Si se presiona ENTER y la animación terminó, avanzar al menú principal
        if (showPressEnter && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            AudioManager.stopMusic();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    /**
     * Libera los recursos utilizados por esta pantalla.
     */
    @Override
    public void dispose() {
        batch.dispose();
        for (TextureRegion region : frames) {
            region.getTexture().dispose(); 
        }

        font.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadIntroAssets();
    }
}