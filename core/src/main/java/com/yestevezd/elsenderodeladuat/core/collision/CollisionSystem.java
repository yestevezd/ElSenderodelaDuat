package com.yestevezd.elsenderodeladuat.core.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistema que gestiona colisiones con los polígonos definidos en el mapa.
 */
public class CollisionSystem {

    private final List<Polygon> colliders = new ArrayList<>();

    /**
     * Recibe la lista de polígonos del mapa y los guarda para comprobar colisiones.
     * @param polygons Lista de polígonos
     */
    public void setPolygons(List<Polygon> polygons) {
        colliders.clear();
        colliders.addAll(polygons);
    }

    /**
     * Comprueba si el área proporcionada colisiona con algún polígono del mapa.
     * @param area El rectángulo del personaje (posición + tamaño escalado)
     * @return true si hay colisión, false si el área es libre
     */
    public boolean isColliding(Rectangle area) {
        for (Polygon poly : colliders) {
            if (Intersector.overlapConvexPolygons(toPolygon(area), poly)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte un Rectangle a Polygon para poder usar overlapConvexPolygons.
     */
    private Polygon toPolygon(Rectangle rect) {
        float[] vertices = new float[]{
                rect.x, rect.y,
                rect.x + rect.width, rect.y,
                rect.x + rect.width, rect.y + rect.height,
                rect.x, rect.y + rect.height
        };
        return new Polygon(vertices);
    }

    public List<Polygon> getColliders() {
        return colliders;
    }
}
