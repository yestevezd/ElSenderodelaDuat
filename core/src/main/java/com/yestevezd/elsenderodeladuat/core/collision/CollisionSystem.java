package com.yestevezd.elsenderodeladuat.core.collision;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionSystem {

    private final List<Polygon> colliders = new ArrayList<>();

    public void setPolygons(List<Polygon> polygons) {
        colliders.clear();
        colliders.addAll(polygons);
    }

    public boolean isColliding(Vector2 position) {
        for (Polygon polygon : colliders) {
            if (polygon.contains(position.x, position.y)) {
                return true;
            }
        }
        return false;
    }
}
