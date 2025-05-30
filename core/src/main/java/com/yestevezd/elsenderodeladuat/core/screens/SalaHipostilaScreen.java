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
import com.yestevezd.elsenderodeladuat.core.combat.CombatManager;
import com.yestevezd.elsenderodeladuat.core.combat.CombatState;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.Amuleto_estatua;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.Escarabajo;
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
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext; 
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

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

    private CombatManager combatManager;

    private PauseOverlay pauseOverlay;


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
        AudioManager.stopMusic();
        // Cargar recursos
        AssetLoader.loadKarnakSalaHipostilaAssets();
        AssetLoader.finishLoading();

        pauseOverlay = new PauseOverlay(game);

        // Cargar mapa
        MapLoader mapLoader = new MapLoader("maps/sala_hipostila.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Cámara
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        // Jugador
        player = game.getPlayer();
        player.setPosition(spawnX, spawnY);
        player.setDirection(Direction.DOWN);

        if ("puerta_templo_karnak".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.RIGHT);
        }

        // Colisiones y puertas
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Sacerdote patrullando
        Texture sacerdoteTexture = AssetLoader.get("characters/sacerdote.png", Texture.class);
        sacerdote = new NPCCharacter(sacerdoteTexture, 1400, 300, 100f,"characters/characterAnimations/sacerdote", "atac_sac_", 6);
        sacerdote.setName("Sacerdote de Karnak");
        sacerdote.setScale(2.5f);
        sacerdote.setGameContext(this);

        textBox = new DialogueBox();
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

        // COMBATE
        if (combatManager != null) {
            combatManager.update(delta);

            if (combatManager.getState() == CombatState.VICTORY) {
                EventFlags.sacerdoteEventoCompletado = true;
                player.setEnCombate(false);

                sacerdote.setVelocity(Vector2.Zero);
                sacerdote.getStateMachine().changeState(NPCState.HABLAR);
            
                player.setPosition(900, 300);
                sacerdote.setPosition(960, 300);
                player.setDirection(Direction.RIGHT);
                sacerdote.setDirection(Direction.LEFT);

                getGame().getHUD().setHealth(player.getCurrentHealth());
            
                DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_postcombate_sacerdote_victoria");
                dialogueManager = new DialogueManager(tree, textBox);
                dialogueManager.start();
                textBox.setFadeInActive(true);
            
                combatManager = null;
                freezePlayer = true;
            
            } else if (combatManager.getState() == CombatState.DEFEAT) {
                // Derrota
                EventFlags.sacerdoteEventoCompletado = false;
                player.setEnCombate(false);
                sacerdote.getStateMachine().changeState(NPCState.PATRULLAR);

                player.restoreFullHealth();
                getGame().getHUD().setHealth(player.getCurrentHealth());

                game.setScreen(new DefeatScreen(game));

                combatManager = null;
                freezePlayer = false;
                return;
            } else {
                camera.update();
                mapRenderer.setView(camera);
                mapRenderer.render();

                game.getBatch().setProjectionMatrix(camera.combined);
                combatManager.render(game.getBatch(), mapRenderer, camera);

                return; 
            }
        }

        // NORMAL
        Vector2 oldPosition = player.getPosition().cpy();
        if (!freezePlayer && (textBox == null || !textBox.isVisible())) {
            player.update(delta);
        }

        if (dialogueManager != null) {
            dialogueManager.update(delta);
        }

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        if (sacerdote.getCollisionBounds().overlaps(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        // Actualizar NPC
        sacerdote.updateConColision(delta, player.getCollisionBounds());

        // Comprobar distancia para que el sacerdote nos persiga
        Vector2 playerPos = player.getPosition();
        if (!EventFlags.sacerdoteEventoCompletado && sacerdote.getStateMachine().getCurrentState() == NPCState.PATRULLAR) {
            if (sacerdote.getPosition().dst(playerPos) < 800f) {
                sacerdote.setTargetPosition(playerPos.cpy());
                sacerdote.getStateMachine().changeState(NPCState.ACERCARSE_AL_JUGADOR);
                freezePlayer = true;
                sacerdoteEventoDisparado = true;
            }
        }

        // Puertas
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        // Eventos de final de diálogo
        if (dialogueManager != null && !dialogueManager.isActive() && sacerdoteEventoDisparado) {
            String lastNodeId = dialogueManager.getCurrentNodeId();
            
            if ("fin_soborno".equals(lastNodeId)) {
                Amuleto_estatua estatua = new Amuleto_estatua();
                Escarabajo escarabajo = new Escarabajo();
                getGame().getInventory().addItem(estatua);
                getGame().getInventory().addItem(escarabajo);
                getGame().getHUD().showPopupMessage("¡Has conseguido recuperar objetos sagrados sobornando!", estatua, escarabajo);
                EventFlags.sacerdoteEventoCompletado = true;
                EventFlags.sacerdoteSobornado = true;
                sacerdote.getStateMachine().changeState(NPCState.PATRULLAR);
                textBox.hide();
                freezePlayer = false;
                dialogueManager = null;

            }else if ("fin_pelea".equals(lastNodeId)) {
                // CREAR EL COMBAT MANAGER
                player.setCombatSpriteSheet(AssetLoader.get("characters/personaje_principal_espada.png", Texture.class));
                sacerdote.setCombatSpriteSheet(AssetLoader.get("characters/sacerdote_baston.png", Texture.class));
                combatManager = new CombatManager(player, sacerdote,collisionSystem, camera);
                player.setEnCombate(true);
                AudioManager.stopMusic();
                freezePlayer = true;
                dialogueManager = null;

                return; 
            }else if ("fin_postcombate_victoria".equals(lastNodeId)) {
                Amuleto_estatua estatua = new Amuleto_estatua();
                Escarabajo escarabajo = new Escarabajo();
                getGame().getInventory().addItem(estatua);
                getGame().getInventory().addItem(escarabajo);
            
                getGame().getHUD().showPopupMessage("¡Has recuperado los objetos sagrados!", estatua, escarabajo);
            
                sacerdote.setCustomDestination(new Vector2(1600, 280));
                sacerdote.getStateMachine().changeState(NPCState.DESAPARECER);
                EventFlags.sacerdoteSobornado = false;
                dialogueManager = null;
                freezePlayer = false;
            }
        }

        // RENDERIZADO NORMAL
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

        game.getBatch().setProjectionMatrix(game.getUiCamera().combined); 
        textBox.render(game.getBatch());
        game.getBatch().setProjectionMatrix(camera.combined);
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
    public PlayerCharacter getPlayer() {
        return player;
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
        AssetLoader.unloadKarnakSalaHipostilaAssets();
    }
}
