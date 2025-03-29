package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Administra la reproducción de música y efectos de sonido.
 */
public class AudioManager {

    private static Music currentMusic;

    /**
     * Reproduce música de fondo desde archivo (no se precarga).
     *
     * @param path    Ruta relativa en assets
     * @param looping Si debe reproducirse en bucle
     */
    public static void playMusic(String path, boolean looping) {
        stopMusic();
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setLooping(looping);
        currentMusic.setVolume(GameConfig.MUSIC_VOLUME);
        currentMusic.play();
    }

    /**
     * Detiene y libera la música actual.
     */
    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    /**
     * Reproduce un sonido. Si está precargado en AssetLoader, se usa desde ahí.
     * Si no está precargado, se carga de forma directa.
     *
     * @param path Ruta del sonido
     */
    public static void playSound(String path) {
        Sound sound;

        if (AssetLoader.isLoaded(path, Sound.class)) {
            sound = AssetLoader.get(path, Sound.class);
        } else {
            sound = Gdx.audio.newSound(Gdx.files.internal(path));
        }

        sound.play(GameConfig.SOUND_VOLUME);
    }

    /**
     * Ajusta el volumen de la música actual.
     *
     * @param volume Valor entre 0 y 1
     */
    public static void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }
}