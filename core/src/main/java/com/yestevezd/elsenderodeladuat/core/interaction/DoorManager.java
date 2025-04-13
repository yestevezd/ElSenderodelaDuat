package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

/**
 * Administra las puertas del mapa. Se encarga de comprobar colisiones entre
 * el jugador y las zonas de transición (puertas), y de gestionar la lógica
 * para determinar si se debe cambiar de pantalla.
 */
public class DoorManager {

    private final List<DoorTrigger> doors;
    private static final float COLLISION_MARGIN = 5f;

    /**
     * Constructor que recibe la lista de puertas definidas en el mapa.
     *
     * @param doors Lista de puertas (DoorTrigger) activas en la pantalla.
     */
    public DoorManager(List<DoorTrigger> doors) {
        this.doors = doors;
    }

    /**
     * Comprueba si el jugador está lo suficientemente cerca de alguna puerta para activarla.
     * Si la puerta requiere pulsar una tecla, espera a que se presione 'E'.
     * Si no requiere interacción, cambia automáticamente.
     *
     * @param playerBounds El área de colisión del jugador.
     * @return La puerta que se ha activado, o null si ninguna.
     */
    public DoorTrigger checkForTransition(Rectangle playerBounds) {
        // Se expande el área de colisión para facilitar la detección
        Rectangle expanded = new Rectangle(
            playerBounds.x - COLLISION_MARGIN,
            playerBounds.y - COLLISION_MARGIN,
            playerBounds.width + 2 * COLLISION_MARGIN,
            playerBounds.height + 2 * COLLISION_MARGIN
        );

        Polygon playerPoly = rectToPolygon(expanded);

        for (DoorTrigger door : doors) {
            if (Intersector.overlapConvexPolygons(playerPoly, door.getShape())) {
                if (door.requiresInteraction()) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        return door;
                    }
                } else {
                    return door;
                }
            }
        }

        return null;
    }

    /**
     * Convierte un rectángulo en un polígono para comprobar colisiones poligonales.
     *
     * @param rect Rectángulo de entrada (como el del jugador).
     * @return Polígono equivalente.
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
     * Devuelve la lista de puertas activas en la pantalla.
     *
     * @return Lista de DoorTrigger.
     */
    public List<DoorTrigger> getDoors() {
        return doors;
    }
}