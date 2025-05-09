package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Representa un objeto interactuable del mapa.
 * Estos objetos pueden resaltar cuando el jugador está cerca.
 */
public class InteractableObject {

    private final String name;
    private final Polygon shape;
    private boolean highlighted = false;

    /**
     * Constructor.
     * 
     * @param name  Nombre del objeto (debe ser único o significativo)
     * @param shape Polígono que representa su forma/colisión
     */
    public InteractableObject(String name, Polygon shape) {
        this.name = name;
        this.shape = shape;
    }

    /**
     * @return El nombre de este objeto interactuable
     */
    public String getName() {
        return name;
    }

    /**
     * @return El polígono que representa su área de interacción
     */
    public Polygon getShape() {
        return shape;
    }

    /**
     * @return true si el objeto está resaltado visualmente
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Establece si el objeto debe estar resaltado (normalmente al estar cerca del jugador).
     *
     * @param highlighted true para resaltar, false para ocultar
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Dibuja el contorno del objeto usando un ShapeRenderer.
     * Se llama desde InteractionManager si el objeto está resaltado.
     *
     * @param renderer ShapeRenderer ya configurado con la proyección
     */
    public void renderOutline(ShapeRenderer renderer) {
        float[] vertices = shape.getTransformedVertices();
        for (int i = 0; i < vertices.length; i += 2) {
            float x1 = vertices[i];
            float y1 = vertices[i + 1];
            float x2 = vertices[(i + 2) % vertices.length];
            float y2 = vertices[(i + 3) % vertices.length];
            renderer.line(x1, y1, x2, y2);
        }
    }

    public Vector2 getCenter() {
    float[] vertices = shape.getTransformedVertices();
    float sumX = 0f, sumY = 0f;
    int count = vertices.length / 2;

    for (int i = 0; i < vertices.length; i += 2) {
        sumX += vertices[i];
        sumY += vertices[i + 1];
    }

    return new Vector2(sumX / count, sumY / count);
}

}