package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InteractableObject {

    private final String name;
    private final Polygon shape;
    private boolean highlighted = false;

    public InteractableObject(String name, Polygon shape) {
        this.name = name;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public Polygon getShape() {
        return shape;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void updateHighlight(Vector2 playerPosition, float margin) {
        Rectangle bounds = shape.getBoundingRectangle();
        Rectangle expanded = new Rectangle(
            bounds.x - margin,
            bounds.y - margin,
            bounds.width + 2 * margin,
            bounds.height + 2 * margin
        );
        this.highlighted = expanded.contains(playerPosition);
    }

    public void renderHighlight(ShapeRenderer renderer) {
        if (!highlighted) return;

        renderer.setColor(Color.WHITE);
        float[] vertices = shape.getTransformedVertices();
        int count = vertices.length / 2;
        for (int i = 0; i < count; i++) {
            float x1 = vertices[i * 2];
            float y1 = vertices[i * 2 + 1];
            float x2 = vertices[((i + 1) % count) * 2];
            float y2 = vertices[((i + 1) % count) * 2 + 1];
            renderer.line(x1, y1, x2, y2);
        }
    }
}