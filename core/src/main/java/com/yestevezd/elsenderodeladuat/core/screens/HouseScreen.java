package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.interaction.*;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.save.SaveManager;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.ui.FloatingTextPrompt;
import com.yestevezd.elsenderodeladuat.core.ui.LoreOverlayManager;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

public class HouseScreen extends BaseScreen {

    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private float mapWidth;
    private float mapHeight;

    private CollisionSystem collisionSystem;
    private DoorManager doorManager;
    private InteractionManager interactionManager;
    private ShapeRenderer shapeRenderer;

    private float spawnX, spawnY;

    private LoreOverlayManager loreOverlay;

    private FloatingTextPrompt mesaCasaPrompt;
    private FloatingTextPrompt cocinaCasaPrompt;
    private FloatingTextPrompt camaCasaPrompt;

    private PauseOverlay pauseOverlay;

    private boolean sleeping = false;
    private float fadeAlpha = 0f;
    private int fadeDir = 0;
    private static final float FADE_SPEED = 1f;

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

        pauseOverlay = new PauseOverlay(game);

        MapLoader mapLoader = new MapLoader("maps/casa_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        mapWidth = MapUtils.getMapPixelWidth(map);
        mapHeight = MapUtils.getMapPixelHeight(map);

        float initialX = spawnX != 0 ? spawnX : mapWidth / 2f;
        float initialY = spawnY != 0 ? spawnY : mapHeight / 2f;

        player = game.getPlayer();
        player.setPosition(initialX, initialY);

        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        interactionManager = new InteractionManager(5);
        for (InteractableObject obj : mapLoader.getInteractableObjects()) {
            interactionManager.addInteractable(obj);
        }

        mesaCasaPrompt = new FloatingTextPrompt("Pulsa E para abrir el papiro", new Vector2());
        cocinaCasaPrompt = new FloatingTextPrompt("Pulsa E para comer y curarte", new Vector2());
        camaCasaPrompt = new FloatingTextPrompt("Pulsa E para dormir y guardar", new Vector2());

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
        if (sleeping && fadeDir != 0) {
            fadeAlpha = MathUtils.clamp(fadeAlpha + fadeDir * delta * FADE_SPEED, 0f, 1f);

            // Llegó a negro
            if (fadeAlpha >= 1f && fadeDir == 1) {
                SaveManager.saveGame(game);
                game.getHUD().showPopupMessage("Has dormido y guardado la partida.");
                fadeDir = -1; // empezar fade-in
            }
            // Volvió a la escena
            if (fadeAlpha <= 0f && fadeDir == -1) {
                fadeDir = 0;
                sleeping = false;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pauseOverlay.isVisible()) {
            pauseOverlay.update();
            pauseOverlay.render(batch);
            return;
        }
    
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseOverlay.show();
            return;
        }

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

            boolean mesaCerca = false;
            boolean cocinaCerca = false;
            boolean camaCerca = false;

            for (InteractableObject obj : interactionManager.getHighlightedObjects()) {
                Vector2 center = obj.getCenter();

                if ("mesa_casa".equals(obj.getName())) {
                    mesaCasaPrompt.setPosition(new Vector2(center.x, center.y + 40));
                    mesaCasaPrompt.setVisible(true);
                    mesaCerca = true;
                }

                if ("cocina_casa".equals(obj.getName())) {
                    cocinaCasaPrompt.setPosition(new Vector2(center.x, center.y + 40));

                    if (player.getCurrentHealth() < 100) {
                        cocinaCasaPrompt.setText("Pulsa E para comer y curarte");
                    } else {
                        cocinaCasaPrompt.setText("Pulsa E para comer");
                    }

                    cocinaCasaPrompt.setVisible(true);
                    cocinaCerca = true;
                }

                if("cama_casa".equals(obj.getName())){
                    camaCasaPrompt.setPosition(new Vector2(center.x, center.y + 80));
                    camaCasaPrompt.setVisible(true);
                    camaCerca = true;
                }
            }

            if (!mesaCerca) mesaCasaPrompt.setVisible(false);
            if (!cocinaCerca) cocinaCasaPrompt.setVisible(false);
            if (!camaCerca) camaCasaPrompt.setVisible(false);

            // Activar el lore al pulsar E
            if (Gdx.input.isKeyJustPressed(Input.Keys.E) && loreOverlay == null) {
                for (InteractableObject obj : interactionManager.getHighlightedObjects()) {
                    String name = obj.getName();
            
                    if ("mesa_casa".equals(name)) {
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
            
                    } else if ("cocina_casa".equals(name)) {
                        boolean estabaHerido = player.getCurrentHealth() < 100;

                        player.restoreFullHealth();
                        getGame().getHUD().setHealth(player.getCurrentHealth());
                        
                        if (estabaHerido) {
                            getGame().getHUD().showPopupMessage("Has comido y recuperado toda la vida.");
                        } else {
                            getGame().getHUD().showPopupMessage("Has comido. ¡Qué rico!");
                        }
                        break;
                    }else if("cama_casa".equals(name)){
                        sleeping = true;
                        fadeDir = 1;
                        break;
                }
                }
            }
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);

        interactionManager.render(shapeRenderer);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        
        if (loreOverlay == null || !loreOverlay.isBlocking()) {
            mesaCasaPrompt.render(batch, AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class), camera);
            cocinaCasaPrompt.render(batch, AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class), camera);
            camaCasaPrompt.render(batch, AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class), camera);}
        batch.end();

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        if (loreOverlay != null) {
            loreOverlay.render(batch, shapeRenderer, camera);
        }

        // Proyección a pantalla para HUD
        getGame().getHUD().render(batch); 

        if (sleeping || fadeAlpha>0f) {
            if(batch.isDrawing()){batch.end();}
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0,0,0, fadeAlpha);
            shapeRenderer.rect(0,0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            if(batch.isDrawing()){batch.begin();}
        }
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
        shapeRenderer.dispose();
        AssetLoader.unloadHouseAssets();
        if (loreOverlay != null) loreOverlay.dispose();
    }
}
