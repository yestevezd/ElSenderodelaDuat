package com.yestevezd.elsenderodeladuat.core.screens;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.Espada;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueManager;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueTree;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

/**
 * Pantalla del exterior del pueblo de Deir el-Medina.
 * Se encarga de cargar el mapa, gestionar el personaje, colisiones y transiciones por puertas.
 */
public class DeirElMedinaScreen extends BaseScreen implements GameEventContext {

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

    private List<NPCCharacter> npcs = new ArrayList<>();

    private NPCCharacter npcArtesano;;
    private boolean freezePlayer = false;

    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean npcArtesanoEventoDisparado = false;
    private boolean artesanoEnTransicionACasa = false;

    private String entradaDesdePuerta = null;

    private PauseOverlay pauseOverlay;


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

    public DeirElMedinaScreen(MainGame game, float spawnX, float spawnY, String entradaDesdePuerta) {
        this(game, spawnX, spawnY);
        this.entradaDesdePuerta = entradaDesdePuerta;
    }

    /**
     * Inicializa la pantalla: carga mapa, jugador, colisiones y música.
     */
    @Override
    public void show() {

        pauseOverlay = new PauseOverlay(getGame());

        // Cargar mapa y colisiones
        MapLoader mapLoader = new MapLoader("maps/pueblo_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Configurar cámara
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);

        // Crear personaje
        player = game.getPlayer();
        player.setPosition(spawnX, spawnY);

        if ("puerta_camino_3".equals(entradaDesdePuerta)) {
            player.setDirection(Direction.LEFT);
        }

        // Inicializar sistema de colisiones
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Inicializar puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

        // Música ambiental
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);

        // Cargar NPC
        if (!EventFlags.artesanoEventoCompletado) {
            Texture artesanoTexture = AssetLoader.get("characters/personaje_aleatorio.png", Texture.class);
            npcArtesano = new NPCCharacter(artesanoTexture, 800, 300, 100f,null,null,0);
            npcArtesano.setScale(2.5f);
            npcArtesano.setGameContext(this);
            npcs.add(npcArtesano);
        }

        textBox = new DialogueBox();
    }

    /**
     * Actualiza la lógica del juego y renderiza la escena.
     *
     * @param delta Tiempo transcurrido desde el último frame
     */
    @Override
    public void render(float delta) {
        // Limpia la pantalla
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

        Vector2 oldPosition = player.getPosition().cpy();
        if (!freezePlayer && !textBox.isVisible()) {
            player.update(delta);
        }

        if (dialogueManager != null) {
            dialogueManager.update(delta);
        }

        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPosition.x, oldPosition.y);
        }

        // Evitar que el jugador atraviese NPCs
        for (NPCCharacter npc : npcs) {
            if (npc.getCollisionBounds().overlaps(player.getCollisionBounds())) {
                player.setPosition(oldPosition.x, oldPosition.y);
                break;
            }
        }

        Vector2 playerPos = player.getPosition();
        for (NPCCharacter npc : npcs) {
            if (npc == npcArtesano && !npcArtesanoEventoDisparado) {
                if (npc.getPosition().dst(playerPos) < 500f) {
                    npc.setTargetPosition(playerPos.cpy());
                    npc.getStateMachine().changeState(NPCState.ACERCARSE_AL_JUGADOR);
                    freezePlayer = true;
                    npcArtesanoEventoDisparado = true;
                }
            }

            npc.updateConColision(delta, player.getCollisionBounds());
        }

        if(EventFlags.dialogoGuardiaPostEventoKv9Mostrado && !EventFlags.mensajeDormirMostrado){
            EventFlags.mensajeDormirMostrado = true;
            getGame().getHUD().showPopupMessage("Se ha hecho tarde, deberías ir a casa a dormir.");
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
        for (NPCCharacter npc : npcs) {
            npc.render(game.getBatch());
        }
        game.getBatch().setProjectionMatrix(game.getUiCamera().combined); 
        textBox.render(game.getBatch());
        game.getBatch().setProjectionMatrix(camera.combined);
        doorManager.renderInteractionMessage(player.getCollisionBounds(), game.getBatch(), camera);
        game.getBatch().end();


        if (dialogueManager != null && !dialogueManager.isActive() && npcArtesanoEventoDisparado && !EventFlags.artesanoEventoCompletado && !artesanoEnTransicionACasa) {
            if (textBox.isVisible()) {
                textBox.hide();
            }
        
            npcArtesano.setLastDx(0);
            npcArtesano.setLastDy(0);

            npcArtesano.setSpeed(200f);

            npcArtesano.getStateMachine().changeState(NPCState.ENTRAR_CASA);
        
            Espada espada = new Espada();

            getGame().getInventory().addItem(espada);
            // Mostrar mensaje en HUD
            getGame().getHUD().showPopupMessage("¡Has conseguido una " + espada.getDisplayName() + "!", espada);
        
            artesanoEnTransicionACasa = true;
        }

        if (artesanoEnTransicionACasa && !npcArtesano.isVisible()) {
            freezePlayer = false;
            artesanoEnTransicionACasa = false;
            EventFlags.artesanoEventoCompletado = true;
        }

        // Proyección a pantalla para HUD
        getGame().getHUD().render(batch); 
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

        DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_artesano");
        dialogueManager = new DialogueManager(tree, textBox);
        dialogueManager.start();
    }

    @Override
    public PlayerCharacter getPlayer() {
        return player;
    }

    /**
     * Reajustar la cámara al cambiar el tamaño de la ventana.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
        game.getUiCamera().setToOrtho(false, width, height);
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