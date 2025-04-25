package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.interaction.*;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.ui.LoreOverlayManager;

public class HouseScreen extends BaseScreen {

    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private Texture playerTexture;
    private float mapWidth;
    private float mapHeight;

    private CollisionSystem collisionSystem;
    private DoorManager doorManager;
    private InteractionManager interactionManager;
    private ShapeRenderer shapeRenderer;

    private float spawnX, spawnY;

    private LoreOverlayManager loreOverlay;

    public HouseScreen(MainGame game) {
        super(game);
    }

    public HouseScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    @Override
    public void show() {
        AssetLoader.loadHouseAssets();
        AssetLoader.loadDeirElMedinaAssets();
        AssetLoader.finishLoading();

        MapLoader mapLoader = new MapLoader("maps/casa_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        mapWidth = MapUtils.getMapPixelWidth(map);
        mapHeight = MapUtils.getMapPixelHeight(map);

        playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);

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

        AudioManager.stopMusic();

        if (!EventFlags.lorePapiroArtesanoMostrado) {
            loreOverlay = new LoreOverlayManager();
            loreOverlay.trigger("others/papiro_hieratic.png",
                "Mi querido familiar:\n" +
                "El tiempo que los dioses te han concedido entre los vivos se acorta.\n" +
                "Debes prepararte para tu viaje hacia el más allá. Para cruzar las puertas de la Duat y alcanzar los Campos de Aaru,\n" +
                "deberás obrar con rectitud y reunir los artefactos que te servirán en el juicio ante Osiris.\n" +
                "Las señales están claras: tu camino se entrelaza con fuerzas ocultas y decisiones que marcarán tu alma.\n" +
                "Actúa con sabiduría y honor.\n" +
                "Que tu alma llegue ligera ante el trono de Osiris."
            );
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (loreOverlay != null) {
            loreOverlay.update(delta);

            if (loreOverlay.isFinished()) {
                if (!EventFlags.lorePapiroArtesanoMostrado) {
                    EventFlags.lorePapiroArtesanoMostrado = true;
                }
                loreOverlay = null;
            }
        }

        if (loreOverlay == null || !loreOverlay.isBlocking()) {
            Vector2 oldPosition = player.getPosition().cpy();
            player.update(delta);

            if (collisionSystem.isColliding(player.getCollisionBounds())) {
                player.setPosition(oldPosition.x, oldPosition.y);
            }

            interactionManager.update(player.getCollisionBounds());

            if (Gdx.input.isKeyJustPressed(Input.Keys.E) && loreOverlay == null) {
                for (InteractableObject obj : interactionManager.getHighlightedObjects()) {
                    if ("mesa_casa".equals(obj.getName())) {
                        loreOverlay = new LoreOverlayManager();
                        loreOverlay.trigger("others/papiro_hieratic.png",
                            "Mi querido familiar:\n" +
                            "El tiempo que los dioses te han concedido entre los vivos se acorta.\n" +
                            "Debes prepararte para tu viaje hacia el más allá. Para cruzar las puertas de la Duat y alcanzar los Campos de Aaru,\n" +
                            "deberás obrar con rectitud y reunir los artefactos que te servirán en el juicio ante Osiris.\n" +
                            "Las señales están claras: tu camino se entrelaza con fuerzas ocultas y decisiones que marcarán tu alma.\n" +
                            "Actúa con sabiduría y honor.\n" +
                            "Que tu alma llegue ligera ante el trono de Osiris."
                        );
                        break;
                    }
                }
            }
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        interactionManager.render(shapeRenderer);

        // === DIBUJAR EL MUNDO (jugador, etc.) ===
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        // === Transiciones ===
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        // === Renderizar LoreOverlay ===
        if (loreOverlay != null) {
            loreOverlay.render(batch, shapeRenderer, camera);
        }

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
        shapeRenderer.dispose();
        AssetLoader.unloadHouseAssets();
        if (loreOverlay != null) loreOverlay.dispose();
    }
}
