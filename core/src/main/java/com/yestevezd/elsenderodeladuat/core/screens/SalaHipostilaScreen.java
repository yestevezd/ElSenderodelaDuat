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
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueManager;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueTree;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;
import com.badlogic.gdx.utils.Array;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext; // Ensure this is the correct package
import com.yestevezd.elsenderodeladuat.core.entities.Espada; // Replace with the correct package for Espada

public class SalaHipostilaScreen extends BaseScreen implements GameEventContext {

    private OrthographicCamera camera;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    private float spawnX, spawnY;
    private String entradaDesdePuerta = null;

    private NPCCharacter sacerdote; // Nuevo NPC

    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean freezePlayer = false;
    private boolean sacerdoteEventoDisparado = false;


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

        // === Sacerdote patrullando ===
        Texture sacerdoteTexture = AssetLoader.get("characters/sacerdote.png", Texture.class);
        sacerdote = new NPCCharacter(sacerdoteTexture, 1400, 300, 100f); // posición inicial
        sacerdote.setScale(2.5f);
        sacerdote.setGameContext(this);
        if (EventFlags.sacerdoteEventoCompletado) {
            sacerdote.getStateMachine().changeState(NPCState.PATRULLAR);
        } else {
            sacerdote.getStateMachine().changeState(NPCState.PATRULLAR);
        }

        textBox = new DialogueBox();

        // Música de fondo
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector2 oldPosition = player.getPosition().cpy();
        if (!freezePlayer && (textBox == null || !textBox.isVisible())) {
            player.update(delta);
        }

        if (dialogueManager != null) {
            dialogueManager.update();
        }

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        if (sacerdote.getCollisionBounds().overlaps(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        // Actualizar NPC
        sacerdote.updateConColision(delta, player.getCollisionBounds());

        // Comprobar distancia al jugador
        Vector2 playerPos = player.getPosition();
        if (!EventFlags.sacerdoteEventoCompletado && sacerdote.getStateMachine().getCurrentState() == NPCState.PATRULLAR) {
            if (sacerdote.getPosition().dst(playerPos) < 300f) {
                sacerdote.setTargetPosition(playerPos.cpy());
                sacerdote.getStateMachine().changeState(NPCState.ACERCARSE_AL_JUGADOR);
                freezePlayer = true;
                sacerdoteEventoDisparado = true;
            }
        }

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        if (dialogueManager != null && !dialogueManager.isActive() && sacerdoteEventoDisparado) {
            String lastNodeId = dialogueManager.getCurrentNodeId(); 
        
            if ("fin_soborno".equals(lastNodeId)) {
                Espada espada1 = new Espada();
                Espada espada2 = new Espada();
        
                getGame().getInventory().addItem(espada1);
                getGame().getInventory().addItem(espada2);
        
                getGame().getHUD().showPopupMessage("¡Has conseguido recuperar objetos sagrados sobornando!", espada1, espada2);
        
                EventFlags.sacerdoteEventoCompletado = true;
                sacerdote.getStateMachine().changeState(NPCState.PATRULLAR);
            } else if ("fin_pelea".equals(lastNodeId)) {
                // iniciar combate aquí
                EventFlags.sacerdoteEventoCompletado = true;
                // si pierdes o ganas, al terminar el combate, volveremos también a PATRULLAR
            }
        
            freezePlayer = false;
            dialogueManager = null;
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        Array<BaseCharacter> characters = new Array<>();
        characters.add(player);
        characters.add(sacerdote);

        characters.sort((a, b) -> Float.compare(b.getPosition().y, a.getPosition().y));

        for (BaseCharacter character : characters) {
            character.render(game.getBatch());
        } 
        textBox.render(game.getBatch());
        doorManager.renderInteractionMessage(player.getCollisionBounds(), game.getBatch(), camera);
        game.getBatch().end();

        getGame().getHUD().render(game.getBatch());
    }

    @Override
    public void onNPCReachedPlayer(NPCCharacter npc) {
        freezePlayer = true;

        Vector2 dir = npc.getPosition().cpy().sub(player.getPosition());
        if (Math.abs(dir.x) > Math.abs(dir.y)) {
            player.setDirection(dir.x > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
            player.setDirection(dir.y > 0 ? Direction.UP : Direction.DOWN);
        }

        DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_sacerdote");
        dialogueManager = new DialogueManager(tree, textBox);
        dialogueManager.start();

        sacerdoteEventoDisparado = true;
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
