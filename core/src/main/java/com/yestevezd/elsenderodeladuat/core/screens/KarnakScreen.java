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
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Amuleto_estatua;
import com.yestevezd.elsenderodeladuat.core.entities.Escarabajo;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorHandler;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorManager;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueManager;
import com.yestevezd.elsenderodeladuat.core.narrative.dialogues.DialogueTree;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;
import com.yestevezd.elsenderodeladuat.core.ui.FloatingTextPrompt;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

/**
 * Pantalla del templo de Karnak con diálogo inicial y post-evento
 * sin parpadeo al iniciar el diálogo.
 */
public class KarnakScreen extends BaseScreen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private PlayerCharacter player;
    private CollisionSystem collisionSystem;
    private DoorManager doorManager;
    private float spawnX, spawnY;
    private String entradaDesdePuerta;

    private NPCCharacter npcGuardian;
    private DialogueBox textBox;
    private DialogueManager dialogueManager;
    private boolean freezePlayer;
    private FloatingTextPrompt guardianPrompt;

    // Local flags for inventory actions
    private boolean objetosEntregados;
    private boolean amuletoDevuelto;

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
        AssetLoader.loadKarnakAssets();
        AssetLoader.finishLoading();
        pauseOverlay = new PauseOverlay(getGame());
        MapLoader mapLoader = new MapLoader("maps/karnak_templo.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera);
        player = getGame().getPlayer();
        player.setPosition(spawnX, spawnY);
        Texture tex = AssetLoader.get("characters/guardian.png", Texture.class);
        npcGuardian = new NPCCharacter(tex, 480, 650, 0f);
        npcGuardian.setName("Guardián de Karnak");
        npcGuardian.setScale(2.5f);
        npcGuardian.getStateMachine().changeState(NPCState.HABLAR);
        guardianPrompt = new FloatingTextPrompt("Pulsa E para hablar", npcGuardian.getPosition().cpy().add(0,60));
        if ("puerta_camino_1".equals(entradaDesdePuerta)) player.setDirection(Direction.UP);
        else player.setDirection(Direction.LEFT);
        collisionSystem = new CollisionSystem();
        collisionSystem.setPolygons(mapLoader.getCollisionPolygons());
        doorManager = new DoorManager(mapLoader.getDoorTriggers());
        textBox = new DialogueBox();
        AudioManager.playMusic("sounds/sonido_viento.mp3", true);
    }

    @Override
    public void render(float delta) {
        // clear
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // pause overlay
        if (pauseOverlay.isVisible()) {
            pauseOverlay.update();
            pauseOverlay.render(batch);
        } else {
            // input and movement
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pauseOverlay.show();
            else {
                Vector2 old = player.getPosition().cpy();
                if (!freezePlayer && !textBox.isVisible()) player.update(delta);
                if (collisionSystem.isColliding(player.getCollisionBounds()) ||
                    npcGuardian.getCollisionBounds().overlaps(player.getCollisionBounds())) {
                    player.setPosition(old.x, old.y);
                }

                // dialogue triggers
                float dist = player.getPosition().dst(npcGuardian.getPosition());
                boolean canTalk = dist < 60f &&
                    (!EventFlags.dialogoGuardiaKarnakinicialMostrado || !EventFlags.dialogoGuardiaKarnakPostEventoMostrado);
                guardianPrompt.setVisible(canTalk);
                if (canTalk && InputManager.isInteractPressed()) {
                    if (EventFlags.sacerdoteEventoCompletado && !EventFlags.dialogoGuardiaKarnakPostEventoMostrado) {
                        startDialogue("dialogo_guardia_post_evento_karnak");
                        EventFlags.dialogoGuardiaKarnakPostEventoMostrado = true;
                    } else if (!EventFlags.dialogoGuardiaKarnakinicialMostrado) {
                        startDialogue("dialogo_guardia_entrada_karnak");
                        EventFlags.dialogoGuardiaKarnakinicialMostrado = true;
                    }
                }

                // dialogue update
                if (dialogueManager != null) {
                    dialogueManager.update(delta);
                    String node = dialogueManager.getCurrentNodeId();
                    if ("entregar_objetos".equals(node) && !objetosEntregados) {
                        getGame().getInventory().removeItem(Amuleto_estatua.class);
                        getGame().getInventory().removeItem(Escarabajo.class);
                        getGame().getHUD().showPopupMessage(
                            "Has entregado los objetos sagrados",
                            new Amuleto_estatua(), new Escarabajo()
                        );
                        objetosEntregados = true;
                    }
                    if ("recompensa_verdadera".equals(node) && !amuletoDevuelto) {
                        getGame().getInventory().addItem(new Escarabajo());
                        getGame().getHUD().showPopupMessage(
                            "El guardia te ha regalado el amuleto con forma de escarabajo como muestra de gratitud",
                            new Escarabajo()
                        );
                        amuletoDevuelto = true;
                    }
                    if (!dialogueManager.isActive()) {
                        dialogueManager = null;
                        freezePlayer = false;
                    }
                }

                // door transition
                if (DoorHandler.handleDoorTransition(getGame(), doorManager, player.getCollisionBounds())) return;

                // world render
                camera.update();
                mapRenderer.setView(camera);
                mapRenderer.render();
                batch.setProjectionMatrix(camera.combined);
                npcGuardian.update(delta);
                batch.begin();
                Array<BaseCharacter> arr = new Array<>();
                arr.add(player); arr.add(npcGuardian);
                arr.sort((a,b)->Float.compare(b.getPosition().y,a.getPosition().y));
                for (BaseCharacter bc:arr) bc.render(batch);
                doorManager.renderInteractionMessage(player.getCollisionBounds(), batch, camera);
                batch.setProjectionMatrix(game.getUiCamera().combined);
                textBox.render(batch);
                batch.setProjectionMatrix(camera.combined);
                guardianPrompt.render(batch, AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class), camera);
                batch.end();
                getGame().getHUD().render(batch);
            }
        }
    }

    private void startDialogue(String id) {
        textBox.hide();

        DialogueTree tree = DialogueLoader.load("dialogues/dialogos.json", id);
        dialogueManager = new DialogueManager(tree, textBox);
        dialogueManager.start();
        freezePlayer = true;
    }

    @Override public void resize(int w,int h) {
        viewport.update(w,h);
        camera.update();
        game.getUiCamera().setToOrtho(false,w,h);
    }
    @Override public void dispose() {
        map.dispose(); mapRenderer.dispose();
        AudioManager.stopMusic();
        AssetLoader.unloadKarnakAssets();
    }
}