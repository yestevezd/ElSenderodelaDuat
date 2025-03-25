package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetLoader {
    private static final AssetManager manager = new AssetManager();

    public static void load() {
        manager.load("others/imagen_demonio.jpeg", Texture.class);
        manager.finishLoading();
    }

    public static Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    //Cargar animaciones desde una secuencia de imágenes
    public static Array<TextureRegion> loadVideoFrames(String folder, int count) {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 1; i <= count; i++) {
            String filename = String.format("%s/frame_%03d.png", folder, i);
            Texture texture = new Texture(Gdx.files.internal(filename));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames.add(new TextureRegion(texture));
        }

        return frames;
    }

    public static void dispose() {
        manager.dispose();
    }
}