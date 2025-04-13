package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistema que gestiona la interacción del jugador con objetos del entorno.
 * Determina si algún objeto está lo suficientemente cerca como para ser resaltado.
 */
public class InteractionManager {

    private final List<InteractableObject> objects = new ArrayList<>();
    private float proximityMargin = 5f;

    /**
     * Constructor con margen de proximidad personalizado.
     *
     * @param proximityMargin margen alrededor del jugador para detectar objetos cercanos
     */
    public InteractionManager(float proximityMargin) {
        this.proximityMargin = proximityMargin;
    }

    /**
     * Agrega un objeto interactuable a la lista.
     *
     * @param obj objeto que puede ser resaltado al estar cerca
     */
    public void addInteractable(InteractableObject obj) {
        objects.add(obj);
    }

    /**
     * Llama cada frame para comprobar si el jugador está cerca de algún objeto.
     * Actualiza el estado de resaltado de los objetos.
     *
     * @param playerBounds área de colisión del jugador
     */
    public void update(Rectangle playerBounds) {
        // Expandir el área de colisión del jugador con el margen de proximidad
        Rectangle expanded = new Rectangle(
                playerBounds.x - proximityMargin,
                playerBounds.y - proximityMargin,
                playerBounds.width + 2 * proximityMargin,
                playerBounds.height + 2 * proximityMargin
        );

        Polygon playerPoly = rectToPolygon(expanded);

        // Verificar superposición con cada objeto interactuable
        for (InteractableObject obj : objects) {
            boolean overlapping = Intersector.overlapConvexPolygons(playerPoly, obj.getShape());
            obj.setHighlighted(overlapping);
        }
    }

    /**
     * Dibuja los objetos resaltados usando líneas blancas.
     *
     * @param renderer ShapeRenderer ya configurado con proyección
     */
    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for (InteractableObject obj : objects) {
            if (obj.isHighlighted()) {
                renderer.setColor(Color.WHITE);
                obj.renderOutline(renderer);
            }
        }
        renderer.end();
    }

    /**
     * Convierte un rectángulo a un polígono para poder usar Intersector.
     */
    private Polygon rectToPolygon(Rectangle rect) {
        float[] vertices = {
                rect.x, rect.y,
                rect.x + rect.width, rect.y,
                rect.x + rect.width, rect.y + rect.height,
                rect.x, rect.y + rect.height
        };
        return new Polygon(vertices);
    }

    /**
     * Cambia el margen de proximidad dinámicamente (en tiempo de ejecución).
     */
    public void setProximityMargin(float margin) {
        this.proximityMargin = margin;
    }

    /**
     * Reemplaza todos los objetos interactuables actuales con una nueva lista.
     *
     * @param newObjects nueva lista de objetos del mapa actual
     */
    public void clearAndAddAll(List<InteractableObject> newObjects) {
        objects.clear();
        objects.addAll(newObjects);
    }

    /**
     * Devuelve una lista con los objetos actualmente resaltados.
     *
     * @return lista de objetos cercanos al jugador
     */
    public List<InteractableObject> getHighlightedObjects() {
        List<InteractableObject> highlighted = new ArrayList<>();
        for (InteractableObject obj : objects) {
            if (obj.isHighlighted()) {
                highlighted.add(obj);
            }
        }
        return highlighted;
    }
}