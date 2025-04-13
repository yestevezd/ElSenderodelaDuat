package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;

/**
 * Pantalla del exterior del pueblo de Deir el-Medina.
 * Se encarga de cargar el mapa, gestionar el personaje, colisiones y transiciones por puertas.
 */
public class DeirElMedinaScreen extends BaseScreen {

    // Cámara y vista
    private OrthographicCamera camera;
    private Viewport viewport;

    // Mapa y renderizador
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Jugador y sistemas de interacción
    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    // Posición de aparición del jugador
    private float spawnX, spawnY;

    /**
     * Constructor con posición inicial personalizada.
     *
     * @param game Instancia del juego principal
     * @param spawnX Coordenada X de aparición del jugador
     * @param spawnY Coordenada Y de aparición del jugador
     */
    public DeirElMedinaScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    /**
     * Inicializa la pantalla: carga mapa, jugador, colisiones y música.
     */
    @Override
    public void show() {
        // Cargar mapa y colisiones
        MapLoader mapLoader = new MapLoader("maps/pueblo_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Configurar cámara
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, 1920, 1080);

        // Crear personaje
        Texture playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);
        player = new PlayerCharacter(playerTexture, spawnX, spawnY, 200f);
        player.setScale(2.5f);

        // Inicializar sistema de colisiones
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Inicializar puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Música ambiental
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    /**
     * Actualiza la lógica del juego y renderiza la escena.
     *
     * @param delta Tiempo transcurrido desde el último frame
     */
    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Guardar posición anterior por si hay colisión
        Vector2 oldPosition = player.getPosition().cpy();
        player.update(delta);

        // Colisión física con entorno
        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        // Comprobar si el jugador ha activado alguna puerta
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return; // Interrumpir renderizado si se produce una transición de pantalla
        }

        // Renderizar mapa y jugador
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        player.render(game.getBatch());
        game.getBatch().end();
    }

    /**
     * Reajustar la cámara al cambiar el tamaño de la ventana.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    /**
     * Libera los recursos de esta pantalla.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        AssetLoader.unloadDeirElMedinaAssets();
        AudioManager.stopMusic();
    }
}