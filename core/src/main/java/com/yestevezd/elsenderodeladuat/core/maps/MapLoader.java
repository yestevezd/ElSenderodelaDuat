package com.yestevezd.elsenderodeladuat.core.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.interaction.InteractableObject;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorTrigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Carga y procesa un mapa Tiled (.tmx), extrayendo información útil como:
 * - Polígonos de colisión para el sistema físico
 * - Objetos interactuables con los que el jugador puede interactuar
 * - Triggers de puertas para cambiar de pantalla
 *
 * La información se extrae de la capa "colisiones".
 */
public class MapLoader {

    private final TiledMap map;
    private final List<Polygon> collisionPolygons = new ArrayList<>();
    private final List<InteractableObject> interactables = new ArrayList<>();
    private final List<DoorTrigger> doorTriggers = new ArrayList<>();

    /**
     * Crea un MapLoader y carga automáticamente el mapa especificado desde el AssetLoader.
     * 
     * @param mapPath ruta dentro de assets/ al archivo .tmx
     */
    public MapLoader(String mapPath) {
        this.map = AssetLoader.get(mapPath, TiledMap.class);
        processCollisionLayer("colisiones");
    }

    /**
     * Procesa la capa de colisiones del mapa, identificando polígonos de colisión,
     * objetos interactivos y puertas según su nombre.
     * 
     * @param layerName nombre de la capa que contiene objetos de colisión
     */
    private void processCollisionLayer(String layerName) {
        MapObjects objects = map.getLayers().get(layerName).getObjects();

        for (MapObject obj : objects) {
            if (obj instanceof PolygonMapObject) {
                Polygon polygon = ((PolygonMapObject) obj).getPolygon();
                collisionPolygons.add(polygon);

                String name = obj.getName();
                if (name != null && !name.isEmpty()) {
                    if (name.startsWith("puerta_")) {
                        // Si el nombre contiene "auto", se activa automáticamente sin pulsar tecla
                        boolean isAuto = name.contains("auto");
                        doorTriggers.add(new DoorTrigger(name, polygon, !isAuto));
                    } else {
                        // Si no es una puerta, lo tratamos como un objeto interactuable
                        interactables.add(new InteractableObject(name, polygon));
                    }
                }
            }
        }
    }

    /**
     * @return lista de polígonos utilizados por el sistema de colisiones
     */
    public List<Polygon> getCollisionPolygons() {
        return collisionPolygons;
    }

    /**
     * @return lista de objetos interactuables con nombre, extraídos del mapa
     */
    public List<InteractableObject> getInteractableObjects() {
        return interactables;
    }

    /**
     * @return lista de triggers de puertas definidos en el mapa
     */
    public List<DoorTrigger> getDoorTriggers() {
        return doorTriggers;
    }

    /**
     * @return el mapa Tiled cargado
     */
    public TiledMap getTiledMap() {
        return map;
    }
}