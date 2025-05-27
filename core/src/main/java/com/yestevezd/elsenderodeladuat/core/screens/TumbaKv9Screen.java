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
import com.badlogic.gdx.utils.Array;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.Libro_de_los_muertos;
import com.yestevezd.elsenderodeladuat.core.entities.Anj;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Sandalias;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.combat.CombatManager;
import com.yestevezd.elsenderodeladuat.core.combat.CombatState;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueManager;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueTree;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext;

/**
 * Pantalla de la tumba KV9 (tumba_kv9.tmx) con evento de combate.
 * Ahora el saqueador arranca quieto mirando la pared superior (estado ESPERAR).
 */
public class TumbaKv9Screen extends BaseScreen implements GameEventContext {
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;
    private float spawnX, spawnY;
    private String entradaDesdePuerta;
    // Combate
    private NPCCharacter saqueador;
    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean freezePlayer = false;
    private boolean saqueadorEventoDisparado = false;
    private CombatManager combatManager;
    private PauseOverlay pauseOverlay;

    public TumbaKv9Screen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public TumbaKv9Screen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }

    @Override
    public void show() {
        AudioManager.stopMusic();
        AssetLoader.loadTumbaKv9Assets();
        AssetLoader.finishLoading();
        pauseOverlay = new PauseOverlay(getGame());

        MapLoader mapLoader = new MapLoader("maps/tumba_kv9.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        player = getGame().getPlayer();
        player.setPosition(spawnX, spawnY);
        player.setEnCombate(false);
        if ("puerta_tumba_correcta".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.RIGHT);
        }

        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Inicializar saqueador en estado ESPERAR (quieto mirando arriba)
        Texture tx = AssetLoader.get("characters/saqueador.png", Texture.class);
        saqueador = new NPCCharacter(
            tx, 1500, 400, 100f,
            "characters/characterAnimations/saqueador", "atac_saq_", 4
        );
        saqueador.setName("Saqueador de tumbas");
        saqueador.setScale(2.5f);
        saqueador.setGameContext(this);
        saqueador.getStateMachine().changeState(NPCState.ESPERAR);
        saqueador.setDirection(Direction.UP);

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

        if (dialogueManager != null) dialogueManager.update(delta);

        // Combate activo
        if (combatManager != null) {
            combatManager.update(delta);
            if (combatManager.getState() == CombatState.VICTORY) {
                EventFlags.saqueadorEventoCompletado = true;
                player.setEnCombate(false);
                saqueador.setVelocity(Vector2.Zero);
                saqueador.getStateMachine().changeState(NPCState.HABLAR);
                player.setPosition(800, 200);
                saqueador.setPosition(880, 200);
                player.setDirection(Direction.RIGHT);
                saqueador.setDirection(Direction.LEFT);
                getGame().getHUD().setHealth(player.getCurrentHealth());
                DialogueTree winTree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_postcombate_saqueador_victoria");
                dialogueManager = new DialogueManager(winTree, textBox);
                dialogueManager.start();
                textBox.setFadeInActive(true);
                combatManager = null;
                freezePlayer = true;
            } else if (combatManager.getState() == CombatState.DEFEAT) {
                EventFlags.saqueadorEventoCompletado = false;
                player.setEnCombate(false);
                saqueador.getStateMachine().changeState(NPCState.PATRULLAR);
                player.restoreFullHealth();
                getGame().getHUD().setHealth(player.getCurrentHealth());
                getGame().setScreen(new DefeatScreen(getGame()));
                combatManager = null;
                freezePlayer = false;
                return;
            } else {
                camera.update();
                mapRenderer.setView(camera);
                mapRenderer.render();
                batch.setProjectionMatrix(camera.combined);
                combatManager.render(batch, mapRenderer, camera);
                return;
            }
        }

        // Exploración
        Vector2 oldPos = player.getPosition().cpy();
        if (!freezePlayer && (textBox == null || !textBox.isVisible())) player.update(delta);
        if (collisionSystem.isColliding(player.getCollisionBounds()))
            player.setPosition(oldPos.x, oldPos.y);
        if (saqueador.getCollisionBounds().overlaps(player.getCollisionBounds()))
            player.setPosition(oldPos.x, oldPos.y);

        saqueador.updateConColision(delta, player.getCollisionBounds());

        // Disparo evento al acercarse solo desde ESPERAR
        if (!EventFlags.saqueadorEventoCompletado
            && saqueador.getStateMachine().getCurrentState() == NPCState.ESPERAR
            && saqueador.getPosition().dst(player.getPosition()) < 800f) {
            saqueador.setTargetPosition(player.getPosition().cpy());
            saqueador.getStateMachine().changeState(NPCState.ACERCARSE_AL_JUGADOR);
            freezePlayer = true;
            saqueadorEventoDisparado = true;
        }

        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) return;

        // Fin diálogo inicial
        if (dialogueManager != null && !dialogueManager.isActive() && saqueadorEventoDisparado) {
            String last = dialogueManager.getCurrentNodeId();
            if ("fin_soborno".equals(last)) {
                Anj anj = new Anj(); 
                Sandalias sand = new Sandalias();
                Libro_de_los_muertos libro = new Libro_de_los_muertos(); 
                getGame().getInventory().addItem(anj); 
                getGame().getInventory().addItem(sand);
                getGame().getInventory().addItem(libro);
                getGame().getHUD().showPopupMessage("¡Has conseguido recuperar objetos sagrados sobornando!", anj, sand,libro);
                EventFlags.saqueadorEventoCompletado = true;
                EventFlags.saqueadorSobornado = true;
                saqueador.getStateMachine().changeState(NPCState.PATRULLAR);
                textBox.hide(); 
                
                freezePlayer = false; 
                dialogueManager = null;
            } else if ("fin_pelea_saqueador".equals(last)) {
                player.setCombatSpriteSheet(AssetLoader.get("characters/personaje_principal_espada.png", Texture.class));
                saqueador.setCombatSpriteSheet(AssetLoader.get("characters/saqueador_arma.png", Texture.class));
                combatManager = new CombatManager(player, saqueador, collisionSystem, camera);
                player.setEnCombate(true); AudioManager.stopMusic();
                freezePlayer = true; 
                dialogueManager = null; 
                return;
            } else if ("fin_postcombate_saqueador_victoria".equals(last)) {
                Anj anj = new Anj(); 
                Sandalias sand = new Sandalias();
                Libro_de_los_muertos libro = new Libro_de_los_muertos(); 
                getGame().getInventory().addItem(anj); 
                getGame().getInventory().addItem(sand);
                getGame().getInventory().addItem(libro);
                getGame().getHUD().showPopupMessage("¡Has recuperado los objetos sagrados!", anj, sand, libro);
                saqueador.setCustomDestination(new Vector2(1600, 280));
                saqueador.getStateMachine().changeState(NPCState.DESAPARECER);
                EventFlags.saqueadorEventoCompletado = true;
                EventFlags.saqueadorSobornado = false;
                dialogueManager = null; 
                freezePlayer = false;
            }
        }

        camera.update(); 
        mapRenderer.setView(camera); 
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined); 

        // Dibujar personajes ordenados por Y (de mayor a menor)
        game.getBatch().begin();
        Array<BaseCharacter> characters = new Array<>();
        characters.add(player);
        characters.add(saqueador);
        characters.sort((a, b) -> Float.compare(b.getPosition().y, a.getPosition().y));
        for (BaseCharacter character : characters) {
            character.render(game.getBatch());
        }

        // Diálogo y puertas
        game.getBatch().setProjectionMatrix(game.getUiCamera().combined);
        textBox.render(game.getBatch());
        game.getBatch().setProjectionMatrix(camera.combined);
        doorManager.renderInteractionMessage(player.getCollisionBounds(), game.getBatch(), camera);
        game.getBatch().end();
        
        getGame().getHUD().render(batch);
    }

    @Override public void onNPCReachedPlayer(NPCCharacter npc) {
        freezePlayer = true;
        Vector2 dir = npc.getPosition().cpy().sub(player.getPosition());
        if (Math.abs(dir.x) > Math.abs(dir.y))
            player.setDirection(dir.x>0?Direction.RIGHT:Direction.LEFT);
        else
            player.setDirection(dir.y>0?Direction.UP:Direction.DOWN);
        DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_saqueador");
        dialogueManager = new DialogueManager(tree, textBox);
        dialogueManager.start(); textBox.setFadeInActive(true);
        saqueadorEventoDisparado = true;
    }

    @Override public PlayerCharacter getPlayer() { 
        return player; 
    }
    
    @Override public void resize(int w,int h) { 
        viewport.update(w,h); 
        camera.update(); 
        game.getUiCamera().setToOrtho(false,w,h); 
    }

    @Override public void dispose() { 
        map.dispose(); 
        mapRenderer.dispose(); 
        AudioManager.stopMusic(); 
        AssetLoader.unloadTumbaKv9Assets(); 
    }
}