package com.yestevezd.elsenderodeladuat.core.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.interaction.InteractableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de cargar mapas Tiled (.tmx) y extraer datos útiles como:
 * - Polígonos de colisión
 * - Objetos interactuables
 * 
 * Usa AssetLoader para una gestión consistente de recursos.
 */
public class MapLoader {

    private final TiledMap map;
    private final List<Polygon> collisionPolygons;
    private final List<InteractableObject> interactables;

    /**
     * Constructor que obtiene el mapa desde AssetLoader y extrae
     * colisiones y objetos interactuables.
     *
     * @param mapPath Ruta relativa dentro de assets/ al archivo .tmx
     */
    public MapLoader(String mapPath) {
        this.map = AssetLoader.get(mapPath, TiledMap.class);
        this.collisionPolygons = extractPolygons("colisiones");
        this.interactables = extractInteractables("objetos");
    }

    /**
     * Extrae los polígonos de colisión de una capa de objetos del mapa.
     * Estos se usan para comprobar colisiones con el jugador u otros elementos.
     *
     * @param layerName Nombre de la capa de objetos en el mapa que contiene las colisiones.
     * @return Lista de polígonos que representan las áreas de colisión.
     */
    private List<Polygon> extractPolygons(String layerName) {
        List<Polygon> polygons = new ArrayList<>();
        MapObjects objects = map.getLayers().get(layerName).getObjects();

        for (MapObject obj : objects) {
            if (obj instanceof PolygonMapObject) {
                polygons.add(((PolygonMapObject) obj).getPolygon());
            }
        }

        return polygons;
    }

    /**
     * Extrae objetos interactuables desde una capa específica del mapa.
     * Los objetos deben ser PolygonMapObjects con nombre para ser válidos.
     *
     * @param layerName Nombre de la capa de objetos que contiene los elementos interactuables.
     * @return Lista de objetos interactuables.
     */
    private List<InteractableObject> extractInteractables(String layerName) {
        List<InteractableObject> result = new ArrayList<>();
        MapObjects objects = map.getLayers().get(layerName).getObjects();

        for (MapObject obj : objects) {
            if (obj instanceof PolygonMapObject) {
                String name = obj.getName();
                if (name != null && !name.isEmpty()) {
                    result.add(new InteractableObject(name, ((PolygonMapObject) obj).getPolygon()));
                }
            }
        }

        return result;
    }

    /**
     * @return Lista de polígonos de colisión extraídos del mapa.
     */
    public List<Polygon> getCollisionPolygons() {
        return collisionPolygons;
    }

    /**
     * @return Lista de objetos interactuables extraídos del mapa.
     */
    public List<InteractableObject> getInteractableObjects() {
        return interactables;
    }

    /**
     * @return El objeto TiledMap completo cargado.
     */
    public TiledMap getTiledMap() {
        return map;
    }

    
    public void dispose() {
        
    }
}