package com.yestevezd.elsenderodeladuat.core.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Clase de utilidades para trabajar con mapas Tiled.
 * Proporciona métodos para obtener el tamaño del mapa en píxeles
 * y para configurar la cámara y el viewport centrados en el mapa.
 */
public class MapUtils {

    /**
     * Calcula el ancho total del mapa en píxeles usando la primera capa.
     *
     * @param map Mapa Tiled del que se obtiene la información.
     * @return Ancho en píxeles del mapa.
     */
    public static float getMapPixelWidth(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getTileWidth() * layer.getWidth();
    }

    /**
     * Calcula la altura total del mapa en píxeles usando la primera capa.
     *
     * @param map Mapa Tiled del que se obtiene la información.
     * @return Altura en píxeles del mapa.
     */
    public static float getMapPixelHeight(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getTileHeight() * layer.getHeight();
    }

    /**
     * Configura la cámara y el viewport centrados en el mapa.
     * Usa StretchViewport para adaptarse a diferentes resoluciones sin mostrar bordes vacíos.
     *
     * @param map            Mapa Tiled que se va a visualizar.
     * @param camera         Cámara ortográfica que se va a configurar.
     * @param virtualWidth   Anchura virtual de la pantalla (por ejemplo 1280).
     * @param virtualHeight  Altura virtual de la pantalla (por ejemplo 720).
     * @return Viewport ya aplicado y listo para usarse en resize().
     */
    public static Viewport setupCameraAndViewport(TiledMap map, OrthographicCamera camera, float virtualWidth, float virtualHeight) {
        float mapWidth = getMapPixelWidth(map);
        float mapHeight = getMapPixelHeight(map);

        // Configurar cámara ortográfica con el tamaño virtual
        camera.setToOrtho(false, virtualWidth, virtualHeight);

        // Centrar la cámara en el centro del mapa
        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.update();

        // Usar StretchViewport para que se escale 
        StretchViewport viewport = new StretchViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();

        return viewport;
    }
}