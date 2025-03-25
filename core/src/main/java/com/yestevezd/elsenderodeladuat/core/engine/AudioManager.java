package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private static Music currentMusic;

    public static void playMusic(String path, boolean looping) {
        stopMusic(); // detiene la música anterior
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setLooping(looping);
        currentMusic.setVolume(1f);
        currentMusic.play();
    }

    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    public static void playSound(String path) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(path));
        sfx.play(1f);
    }

    public static void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }
}