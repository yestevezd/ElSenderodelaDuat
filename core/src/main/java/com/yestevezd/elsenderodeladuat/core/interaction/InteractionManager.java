package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class InteractionManager {

    private final List<InteractableObject> objects = new ArrayList<>();
    private final float margin = 8f; // margen de proximidad

    public void addInteractable(InteractableObject obj) {
        objects.add(obj);
    }

    public void clear() {
        objects.clear();
    }

    public void update(Vector2 playerPosition) {
        for (InteractableObject obj : objects) {
            obj.updateHighlight(playerPosition, margin);
        }
    }

    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for (InteractableObject obj : objects) {
            obj.renderHighlight(renderer);
        }
        renderer.end();
    }

    public InteractableObject getHighlightedObject() {
        for (InteractableObject obj : objects) {
            if (obj.isHighlighted()) {
                return obj;
            }
        }
        return null;
    }
}
