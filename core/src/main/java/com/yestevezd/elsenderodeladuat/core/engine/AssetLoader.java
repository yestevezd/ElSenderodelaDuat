package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    private static final AssetManager manager = new AssetManager();

    public static void load() {
        manager.load("others/imagen_demonio.jpeg", Texture.class);
        manager.finishLoading();
    }

    public static Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}