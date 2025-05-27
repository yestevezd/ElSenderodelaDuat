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
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorTrigger;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

/**
 * Pantalla del Valle de los Reyes.
 * Carga el mapa y gestiona colisiones del jugador.
 * Compatible con cualquier resolución mediante cámara y viewport.
 */
public class ValleDeLosReyesScreen extends BaseScreen {
    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    private PauseOverlay pauseOverlay;

    private String entradaDesdePuerta = null;

    private float spawnX, spawnY;

    /**
     * Constructor con posición de aparición.
     * @param game instancia del juego principal
     * @param spawnX coordenada X inicial
     * @param spawnY coordenada Y inicial
     */
    public ValleDeLosReyesScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public ValleDeLosReyesScreen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }

    @Override
    public void show() {
        AssetLoader.loadValleyAssets();
        AssetLoader.finishLoading();
        // Inicializar overlay de pausa
        pauseOverlay = new PauseOverlay(getGame());

        // Cargar mapa y colisiones
        MapLoader mapLoader = new MapLoader("maps/valle_de_los_reyes.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Configurar cámara y viewport
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        // Obtener jugador y posición inicial
        player = getGame().getPlayer();
        player.setPosition(spawnX, spawnY);

        if ("puerta_camino_2".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.UP);
        }else{
            player.setDirection(Direction.DOWN);
        }

        // Sistema de colisiones con polígonos del mapa
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Inicializar puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Música ambiental (opcional)
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gestionar pausa
        if (pauseOverlay.isVisible()) {
            pauseOverlay.update();
            pauseOverlay.render(batch);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseOverlay.show();
            return;
        }

        // Actualizar jugador con colisiones
        Vector2 oldPos = player.getPosition().cpy();
        if (!pauseOverlay.isVisible()) {
            player.update(delta);
        }
        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPos.x, oldPos.y);
        }

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }
        if(EventFlags.saqueadorEventoCompletado && !EventFlags.mensajeValleDeLosReyesMostrado){
            EventFlags.mensajeValleDeLosReyesMostrado = true;
            getGame().getHUD().showPopupMessage("El guardián del Templo de Karnak quiere verte.");
        }
        // Dibujar mapa
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Renderizar jugador
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        DoorTrigger nearby = doorManager.getNearbyInteractiveDoor(player.getCollisionBounds());
        if (nearby != null) {
            String name = nearby.getName();
            boolean isErroneousTomb =
                "puerta_tumba_erronea_1".equals(name) ||
                "puerta_tumba_erronea_2".equals(name);
            // Sólo ocultamos el prompt en esas dos puertas erróneas
            // cuando ya esté completado el saqueador.
            if (!(isErroneousTomb && EventFlags.saqueadorEventoCompletado)) {
                doorManager.renderInteractionMessage(
                    player.getCollisionBounds(),
                    batch,
                    camera
                );
            }
        }
        batch.end();

        getGame().getHUD().render(batch);
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
        AssetLoader.unloadValleyAssets();
        
    }
}
