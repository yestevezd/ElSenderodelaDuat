package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.game.GameEventContext;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
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

public class InframundoScreen extends BaseScreen implements GameEventContext {

    // Cámara y viewport
    private OrthographicCamera camera;
    private Viewport viewport;

    // Mapa y renderizador
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Jugador
    private PlayerCharacter player;

    // Sistemas de colisión y puertas
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;

    // Spawn inicial
    private float spawnX, spawnY;

    // NPCs y diálogo
    private NPCCharacter demonio;
    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean freezePlayer = false;

    private boolean demonioEventoDisparado = false;

    // Overlay de pausa
    private PauseOverlay pauseOverlay;

    // Delays para transición tras fin de diálogo
    private boolean pendingGameOver = false;
    private boolean pendingJudgment = false;
    private float delayTimer = 0f;
    private static final float FIN_DELAY = 3f;

    public InframundoScreen(MainGame game) {
        super(game);
    }
    /**
     * Constructor con posición de spawn.
     */
    public InframundoScreen(MainGame game, float spawnX, float spawnY) {
        super(game);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    @Override
    public void show() {
        // Cargamos assets específicos del inframundo
        AssetLoader.loadDuatAssets();
        AssetLoader.finishLoading();

        // Overlay de pausa
        pauseOverlay = new PauseOverlay(getGame());

        // Carga de mapa y colisiones
        MapLoader mapLoader = new MapLoader("maps/inframundo.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Cámara y viewport
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Inicializamos jugador
        player = game.getPlayer();
        player.setPosition(spawnX, spawnY);
        player.setDirection(Direction.RIGHT);

        // Sistema de colisiones
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());

        // Gestión de puertas
        doorManager = new DoorManager(mapLoader.getDoorTriggers());

         // Inicializar saqueador en estado ESPERAR (quieto mirando arriba)
        Texture tx = AssetLoader.get("characters/neha-her.png", Texture.class);
        demonio = new NPCCharacter(
            tx, 1500, 150, 100f,null,null,0
        );
        demonio.setName("Neha-her");
        demonio.setScale(2.5f);
        demonio.getStateMachine().changeState(NPCState.ESPERAR);
        demonio.setDirection(Direction.LEFT);
        demonio.setGameContext(this);


        // Inicialización de NPCs (si los hubiera)
        textBox = new DialogueBox();

        // Música de fondo
        AudioManager.playMusic("sounds/musica_inframundo.mp3", true);
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pausa
        if (pauseOverlay.isVisible()) {
            pauseOverlay.update();
            pauseOverlay.render(batch);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseOverlay.show();
            return;
        }

        // Actualizar jugador y diálogo
        Vector2 oldPos = player.getPosition().cpy();
        if (!freezePlayer && (dialogueManager == null || !dialogueManager.isActive())) {
            player.update(delta);
        }

        // Actualizar diálogo si existe
        if (dialogueManager != null) {
            dialogueManager.update(delta);
            String node = dialogueManager.getCurrentNodeId();

            // Cuando aparece fin_fallo, arrancamos temporizador
            if ("fin_fallo".equals(node) && !pendingGameOver) {
                pendingGameOver = true;
                delayTimer = 0f;
            }
            // Cuando aparece fin_acierto, arrancamos temporizador
            else if ("fin_acierto".equals(node) && !pendingJudgment) {
                pendingJudgment = true;
                delayTimer = 0f;
            }

            if (!dialogueManager.isActive()) {
                dialogueManager = null;
                freezePlayer   = false;
            }
        }

        // 4) Si hay pendiente de transición, contamos el delay
        if (pendingGameOver || pendingJudgment) {
            delayTimer += delta;
            if (delayTimer >= FIN_DELAY) {
                // detiene la música y salta de pantalla
                AudioManager.stopMusic();
                if (pendingGameOver) {
                    game.setScreen(new GameOverScreen(game));
                } else {
                    game.setScreen(new JudgmentScreen(game));
                }
                return;
            }
        }

        // Colisiones con el mapa
        if (collisionSystem.isColliding(player.getCollisionBounds())) {
            player.setPosition(oldPos.x, oldPos.y);
        }

        if (demonio.getCollisionBounds().overlaps(player.getCollisionBounds())) {
            player.setPosition(oldPos.x, oldPos.y);
        }

        if (!demonioEventoDisparado
            && demonio.getStateMachine().getCurrentState() == NPCState.ESPERAR
            && demonio.getPosition().dst(player.getPosition()) < 200f) {
            
            demonio.setTargetPosition(player.getPosition().cpy());
            demonio.getStateMachine().changeState(NPCState.ACERCARSE_AL_JUGADOR);
            freezePlayer = true;
            demonioEventoDisparado = true;
        }


        // Transición por puertas
        if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        demonio.updateConColision(delta, player.getCollisionBounds());

        // Renderizar mapa y entidades
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Array<BaseCharacter> characters = new Array<>();
        characters.add(player);
        characters.add(demonio);
        characters.sort((a, b) -> Float.compare(b.getPosition().y, a.getPosition().y));
        for (BaseCharacter character : characters) {
            character.render(game.getBatch());
        }

        // Renderizar diálogo y mensajes de interacción
        batch.setProjectionMatrix(game.getUiCamera().combined);
        textBox.render(batch);
        doorManager.renderInteractionMessage(player.getCollisionBounds(), batch, camera);
        batch.end();

        // Renderizar HUD
        game.getHUD().render(batch);
    }

    @Override
    public void onNPCReachedPlayer(NPCCharacter npc) {
        // Una vez que llega al jugador, congelamos y lanzamos diálogo
        freezePlayer = true;

        // Orientamos al jugador para mirarle
        Vector2 dir = npc.getPosition().cpy().sub(player.getPosition());
        if (Math.abs(dir.x) > Math.abs(dir.y)) {
            player.setDirection(dir.x > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
            player.setDirection(dir.y > 0 ? Direction.UP : Direction.DOWN);
        }

        // Carga tu diálogo. Ajusta ruta e ID a tus ficheros.
        DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", "dialogo_demonio");
        dialogueManager = new DialogueManager(tree, textBox);
        dialogueManager.start();
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
        AssetLoader.loadDuatAssets();
    }
}