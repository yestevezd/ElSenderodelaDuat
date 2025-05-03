package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Escarabajo implements Item {

    @Override
    public String getId() {
        return "escarabajo";
    }

    @Override
    public String getDisplayName() {
        return "Amuleto en forma de escarabajo";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/escarabajo.png", Texture.class);
    }
}

