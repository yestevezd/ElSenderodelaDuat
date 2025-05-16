package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Libro_de_los_muertos implements Item {

    @Override
    public String getId() {
        return "libro_de_los_muertos";
    }

    @Override
    public String getDisplayName() {
        return "Libro de los Muertos";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/papiro_libromuertos.png", Texture.class);
    }
}