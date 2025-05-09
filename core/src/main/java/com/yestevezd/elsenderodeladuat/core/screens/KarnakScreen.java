package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;
import com.yestevezd.elsenderodeladuat.core.ui.FloatingTextPrompt;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueManager;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueTree;

public class KarnakScreen extends BaseScreen {

    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    private float spawnX, spawnY;
    private String entradaDesdePuerta = null;

    private NPCCharacter npcGuardian;
    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean freezePlayer = false;
    private boolean guardiaDialogoIniciado = false;
    private FloatingTextPrompt guardianPrompt;

    private PauseOverlay pauseOverlay;

    public KarnakScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public KarnakScreen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }

    @Override
    public void show() {
        // Cargar recursos específicos de Karnak
        AssetLoader.loadKarnakAssets(); 
        AssetLoader.finishLoading();

        pauseOverlay = new PauseOverlay(getGame());

        // Cargar mapa
        MapLoader mapLoader = new MapLoader("maps/karnak_templo.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Cámara y viewport
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        // Jugador
        player = game.getPlayer();
        player.setPosition(spawnX, spawnY);

        // Crear NPC guardian en estado HABLAR
        Texture guardianTexture = AssetLoader.get("characters/guardian.png", Texture.class);
        npcGuardian = new NPCCharacter(guardianTexture, 480, 650, 0f);
        npcGuardian.setName("Guardián de Karnak");
        npcGuardian.setScale(2.5f);
        npcGuardian.getStateMachine().changeState(NPCState.HABLAR);

        guardianPrompt = new FloatingTextPrompt("Pulsa E para hablar", npcGuardian.getPosition().cpy().add(0, 60));

        if ("puerta_camino_1".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.UP);
        }else{
            player.setDirection(Direction.LEFT);
        }

        // Colisiones
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        textBox = new DialogueBox();

        // Música de fondo
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    @Override
    public void render(float delta) {
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

        // Movimiento del jugador y colisión
        Vector2 oldPosition = player.getPosition().cpy();
        if (!freezePlayer && (textBox == null || !textBox.isVisible())) {
            player.update(delta);
        }

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }
        if (npcGuardian.getCollisionBounds().overlaps(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        // Iniciar diálogo con el guardia al pulsar E si está cerca
        float distancia = player.getPosition().dst(npcGuardian.getPosition());

        if (!guardiaDialogoIniciado && !EventFlags.dialogoGuardiaKarnakinicialMostrado && distancia < 60f) { // Ajusta el rango si quieres
            if (InputManager.isInteractPressed()) {
                DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_guardia_entrada_karnak");
                dialogueManager = new DialogueManager(tree, textBox);
                dialogueManager.start();
                guardiaDialogoIniciado = true;
                freezePlayer = true;
                EventFlags.dialogoGuardiaKarnakinicialMostrado = true; 
            }
        }

        if (dialogueManager != null) {
            dialogueManager.update(delta);
            if (!dialogueManager.isActive()) {
                dialogueManager = null;
                freezePlayer = false;
            }
        }

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        // Renderizar mapa y jugador
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        game.getBatch().setProjectionMatrix(camera.combined);
        npcGuardian.update(delta);
        game.getBatch().begin();
        Array<BaseCharacter> characters = new Array<>();
        characters.add(player);
        characters.add(npcGuardian);

        // Ordenar por Y (de arriba a abajo)
        characters.sort((a, b) -> Float.compare(b.getPosition().y, a.getPosition().y));

        for (BaseCharacter character : characters) {
            character.render(game.getBatch());
        }
        doorManager.renderInteractionMessage(player.getCollisionBounds(), game.getBatch(), camera);
        game.getBatch().setProjectionMatrix(game.getUiCamera().combined); 
        textBox.render(game.getBatch());
        game.getBatch().setProjectionMatrix(camera.combined);
        guardianPrompt.setVisible(!guardiaDialogoIniciado && distancia < 60f);
        guardianPrompt.render(game.getBatch(), AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class), camera);
        game.getBatch().end();

        // Proyección a pantalla para HUD
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
        AssetLoader.unloadKarnakAssets();
    }
}
