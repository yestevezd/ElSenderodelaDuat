package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

/**
 * Pantalla de la tumba KV9 (tumba_kv9.tmx).
 * Carga el mapa, gestiona colisiones, puertas y pausa.
 */
public class TumbaKv9Screen extends BaseScreen {
    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    private PauseOverlay pauseOverlay;

    private String entradaDesdePuerta;
    private float spawnX, spawnY;

    /**
     * Constructor con posición de aparición.
     */
    public TumbaKv9Screen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    /**
     * Constructor con posición de aparición y nombre de puerta de entrada.
     */
    public TumbaKv9Screen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }

    @Override
    public void show() {
        AudioManager.stopMusic();

        // Carga de assets específicos
        AssetLoader.loadTumbaKv9Assets();
        AssetLoader.finishLoading();

        pauseOverlay = new PauseOverlay(getGame());

        // Cargar mapa y colisiones
        MapLoader mapLoader = new MapLoader("maps/tumba_kv9.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Configurar cámara y viewport
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        // Inicializar jugador
        player = getGame().getPlayer();
        player.setPosition(spawnX, spawnY);
        if ("puerta_tumba_correcta".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.RIGHT);
        }

        // Colisiones
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pausa
        if (pauseOverlay.isVisible()) {
            pauseOverlay.update();
            pauseOverlay.render(batch);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseOverlay.show();
            return;
        }

        // Movimiento y colisión
        Vector2 oldPos = player.getPosition().cpy();
        player.update(delta);
        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPos.x, oldPos.y);
        }

        // Transición de puertas
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        // Render mapa
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Render jugador y mensaje de interacción
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        doorManager.renderInteractionMessage(
            player.getCollisionBounds(),
            batch,
            camera
        );
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
        game.getUiCamera().setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadTumbaKv9Assets();
    }
}
