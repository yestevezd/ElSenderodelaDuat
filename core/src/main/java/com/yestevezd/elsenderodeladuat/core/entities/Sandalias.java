package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Sandalias implements Item {

    @Override
    public String getId() {
        return "sandalias";
    }

    @Override
    public String getDisplayName() {
        return "Sandalias de oro antiguas";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/sandalias.png", Texture.class);
    }
}
