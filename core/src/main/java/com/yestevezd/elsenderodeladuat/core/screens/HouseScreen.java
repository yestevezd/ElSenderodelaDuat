package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.interaction.*;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;

/**
 * Pantalla correspondiente al interior de la casa en Deir el-Medina.
 * Controla la carga del mapa, el personaje, colisiones, interacción y transiciones por puertas.
 */
public class HouseScreen extends BaseScreen {

    // Cámara y vista
    private OrthographicCamera camera;
    private Viewport viewport;

    // Mapa y renderizador
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Jugador
    private PlayerCharacter player;
    private Texture playerTexture;
    private float mapWidth;
    private float mapHeight;

    // Sistemas de colisiones, puertas e interacción
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;
    private InteractionManager interactionManager;
    private ShapeRenderer shapeRenderer;

    // Posición de entrada desde otra pantalla
    private float spawnX, spawnY;

    /**
     * Constructor sin posición inicial explícita (usa el centro del mapa por defecto).
     */
    public HouseScreen(MainGame game) {
        super(game);
    }

    /**
     * Constructor con coordenadas de aparición personalizadas.
     *
     * @param game Instancia principal del juego
     * @param spawnX Coordenada X de inicio del jugador
     * @param spawnY Coordenada Y de inicio del jugador
     */
    public HouseScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    /**
     * Carga y configura todos los elementos de la pantalla.
     */
    @Override
    public void show() {
        AssetLoader.loadHouseAssets();                 // Recursos del interior de la casa
        AssetLoader.loadDeirElMedinaAssets();         // (opcional) por si se usan elementos comunes
        AssetLoader.finishLoading();

        MapLoader mapLoader = new MapLoader("maps/casa_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        mapWidth = MapUtils.getMapPixelWidth(map);
        mapHeight = MapUtils.getMapPixelHeight(map);

        playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);

        // Determinar punto de aparición del jugador
        float initialX = spawnX != 0 ? spawnX : mapWidth / 2f;
        float initialY = spawnY != 0 ? spawnY : mapHeight / 2f;

        player = new PlayerCharacter(playerTexture, initialX, initialY, 200f);
        player.setScale(2.5f);

        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, 1920, 1080);

        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        interactionManager = new InteractionManager(5);
        for (InteractableObject obj : mapLoader.getInteractableObjects()) {
            interactionManager.addInteractable(obj);
        }

        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Lógica y renderizado de cada frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Fondo negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector2 oldPosition = player.getPosition().cpy();
        player.update(delta);

        // Evita que el jugador atraviese colisiones
        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        interactionManager.update(player.getCollisionBounds());

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        interactionManager.render(shapeRenderer); // Dibujo de contornos interactivos

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch); // Dibuja al jugador
        batch.end();

        // Comprobación de transición de puerta mediante clase reutilizable DoorHandler
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return; // Si hay transición, no seguimos renderizando
        }
    }

    /**
     * Ajusta la vista si cambia el tamaño de pantalla.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    /**
     * Libera todos los recursos de esta pantalla.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        shapeRenderer.dispose();
        AssetLoader.unloadHouseAssets();
    }
}