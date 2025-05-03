package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Espada implements Item {

    @Override
    public String getId() {
        return "espada";
    }

    @Override
    public String getDisplayName() {
        return "Espada Antigua";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/espada.png", Texture.class);
    }
}
