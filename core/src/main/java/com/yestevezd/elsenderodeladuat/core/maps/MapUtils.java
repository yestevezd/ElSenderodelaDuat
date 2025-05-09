package com.yestevezd.elsenderodeladuat.core.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;

/**
 * Clase de utilidades para trabajar con mapas Tiled (.tmx).
 * Proporciona métodos para obtener dimensiones del mapa y configurar
 * cámara y viewport centrados en el mapa.
 */
public class MapUtils {

    /**
     * Calcula el ancho total del mapa en píxeles usando la primera capa del TiledMap.
     *
     * @param map Mapa cargado desde Tiled (.tmx)
     * @return Ancho total del mapa en píxeles
     */
    public static float getMapPixelWidth(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getTileWidth() * layer.getWidth();
    }

    /**
     * Calcula el alto total del mapa en píxeles usando la primera capa del TiledMap.
     *
     * @param map Mapa cargado desde Tiled (.tmx)
     * @return Altura total del mapa en píxeles
     */
    public static float getMapPixelHeight(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        return layer.getTileHeight() * layer.getHeight();
    }

    /**
     * Configura la cámara y el viewport para que se centren en el mapa y se adapten
     * a una resolución virtual específica.
     *
     * Usa un FitViewport, que mantiene la relación de aspecto y añade barras negras si es necesario.
     *
     * @param map           Mapa Tiled para obtener dimensiones
     * @param camera        Cámara ortográfica que será configurada
     * @param virtualWidth  Ancho lógico/virtual deseado
     * @param virtualHeight Alto lógico/virtual deseado
     * @return Viewport ya aplicado y listo para usar en métodos como resize()
     */
    public static Viewport setupCameraAndViewport(TiledMap map, OrthographicCamera camera, float virtualWidth, float virtualHeight) {
        float mapWidth = getMapPixelWidth(map);
        float mapHeight = getMapPixelHeight(map);

        camera.setToOrtho(false, virtualWidth, virtualHeight);
        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.update();

        return new FitViewport(virtualWidth, virtualHeight, camera);
    }

    public static Viewport setupCameraAndViewport(TiledMap map, OrthographicCamera camera) {
        return setupCameraAndViewport(map, camera, GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT);
    }
}