package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;

public class SalaHipostilaScreen extends BaseScreen {

    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    private float spawnX, spawnY;
    private String entradaDesdePuerta = null;


    public SalaHipostilaScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public SalaHipostilaScreen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }



    @Override
    public void show() {
        // Cargar recursos
        AssetLoader.loadKarnakSalaHipostilaAssets();
        AssetLoader.finishLoading();

        // Cargar mapa
        MapLoader mapLoader = new MapLoader("maps/sala_hipostila.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Cámara
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, 1920, 1080);

        // Jugador
        Texture playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);
        player = new PlayerCharacter(playerTexture, spawnX, spawnY, 200f);
        player.setScale(2.5f);
        player.setDirection(Direction.DOWN); // Dirección inicial

        if ("puerta_templo_karnak".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.RIGHT);
        }

        // Colisiones y puertas
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Música de fondo
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector2 oldPosition = player.getPosition().cpy();
        player.update(delta);

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        player.render(game.getBatch());
        doorManager.renderInteractionMessage(player.getCollisionBounds(), game.getBatch(), camera);
        game.getBatch().end();

        // Proyección a pantalla para HUD
        getGame().getHUD().render(batch); 
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadKarnakSalaHipostilaAssets();
    }
}
