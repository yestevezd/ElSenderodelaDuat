package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.graphics.Texture;

public interface Item {
    String getId();
    String getDisplayName();
    Texture getTexture();
}