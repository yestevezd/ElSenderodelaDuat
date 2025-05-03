package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Anj implements Item {

    @Override
    public String getId() {
        return "anj";
    }

    @Override
    public String getDisplayName() {
        return "Amuleto en forma de Anj";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/anj.png", Texture.class);
    }
}

