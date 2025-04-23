package com.yestevezd.elsenderodeladuat.core.interaction;

import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

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

    public DoorTrigger getNearbyInteractiveDoor(Rectangle playerBounds) {
        Rectangle expanded = new Rectangle(
            playerBounds.x - COLLISION_MARGIN,
            playerBounds.y - COLLISION_MARGIN,
            playerBounds.width + 2 * COLLISION_MARGIN,
            playerBounds.height + 2 * COLLISION_MARGIN
        );
    
        Polygon playerPoly = rectToPolygon(expanded);
    
        for (DoorTrigger door : doors) {
            if (door.requiresInteraction() && Intersector.overlapConvexPolygons(playerPoly, door.getShape())) {
                return door;
            }
        }
    
        return null;
    }

    public void renderInteractionMessage(Rectangle playerBounds, SpriteBatch batch, OrthographicCamera camera) {
        DoorTrigger nearbyDoor = getNearbyInteractiveDoor(playerBounds);
        if (nearbyDoor == null || nearbyDoor.getInteractionMessage() == null) return;

        String mensajePuerta = nearbyDoor.getInteractionMessage();
        BitmapFont font = AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class);
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout(font, mensajePuerta);

        float x = (1920 - layout.width) / 2f;
        float y = 130;

        // Fondo oscuro semitransparente
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(x - 10, y - layout.height - 6, layout.width + 20, layout.height + 12);
        shapeRenderer.end();
        shapeRenderer.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

        font.draw(batch, layout, x, y);
    }
}