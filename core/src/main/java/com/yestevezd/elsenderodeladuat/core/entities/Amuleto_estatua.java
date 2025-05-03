package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class Amuleto_estatua implements Item {

    @Override
    public String getId() {
        return "estatua_dios_amon";
    }

    @Override
    public String getDisplayName() {
        return "Amuleto de la estatua del dios Amon";
    }

    @Override
    public Texture getTexture() {
        return AssetLoader.get("items/estatua.png", Texture.class);
    }
}
