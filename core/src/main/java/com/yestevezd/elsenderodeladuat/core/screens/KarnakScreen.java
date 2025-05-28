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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.collision.CollisionSystem;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Amuleto_estatua;
import com.yestevezd.elsenderodeladuat.core.entities.Escarabajo;
import com.yestevezd.elsenderodeladuat.core.entities.Libro_de_los_muertos;
import com.yestevezd.elsenderodeladuat.core.entities.Anj;
import com.yestevezd.elsenderodeladuat.core.entities.Sandalias;
import com.yestevezd.elsenderodeladuat.core.entities.BaseCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.Direction;
import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.NPCState;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MaatSystem;
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
import com.yestevezd.elsenderodeladuat.core.ui.LoreOverlayManager;
import com.yestevezd.elsenderodeladuat.core.ui.PauseOverlay;

/**
 * Pantalla del templo de Karnak
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
    private LoreOverlayManager loreOverlay;
    private boolean freezePlayer;
    private FloatingTextPrompt guardianPrompt;

    // Local flags for inventory actions
    private boolean objetosEntregados;
    private boolean objetosEntregadosKv9;
    private boolean amuletoDevuelto;
    private boolean libroDeLosMuertosEntregado;
    private boolean papiroCartuchosMostrado;

    private PauseOverlay pauseOverlay;
    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();

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
        npcGuardian = new NPCCharacter(tex, 480, 650, 0f,null,null,0);
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

        // Si hay un LoreOverlay activo, actualizar y renderizar solo él
        if (loreOverlay != null) {
            loreOverlay.update(delta);
            if (loreOverlay.isFinished()) {
                // Ya lo hemos cerrado, permitimos de nuevo mover al jugador
                loreOverlay = null;
                freezePlayer = false;
            } else {
                // Mientras el overlay esté visible, se bloquea al jugador
                freezePlayer = true;
            }
        }

        // Movimiento del jugador y colisiones
        Vector2 oldPos = player.getPosition().cpy();
        if (!freezePlayer && (textBox == null || !textBox.isVisible())) {
            player.update(delta);
        }
        if (collisionSystem.isColliding(player.getCollisionBounds())
        || npcGuardian.getCollisionBounds().overlaps(player.getCollisionBounds())) {
            player.setPosition(oldPos.x, oldPos.y);
        }

        // Trigger de diálogo con el guardián
        float dist = player.getPosition().dst(npcGuardian.getPosition());
        boolean canTalk = dist < 60f &&
            (!EventFlags.dialogoGuardiaKarnakinicialMostrado
            || (!EventFlags.dialogoGuardiaKarnakPostEventoMostrado && EventFlags.sacerdoteEventoCompletado)
            || (!EventFlags.dialogoGuardiaPostEventoKv9Mostrado && EventFlags.saqueadorEventoCompletado));
        guardianPrompt.setVisible(canTalk);
        if (canTalk && InputManager.isInteractPressed()) {
            if (!EventFlags.dialogoGuardiaKarnakinicialMostrado) {
                startDialogue("dialogo_guardia_entrada_karnak");
                EventFlags.dialogoGuardiaKarnakinicialMostrado = true;
            } else if (EventFlags.sacerdoteEventoCompletado
                    && !EventFlags.dialogoGuardiaKarnakPostEventoMostrado) {
                startDialogue("dialogo_guardia_post_evento_karnak");
                EventFlags.dialogoGuardiaKarnakPostEventoMostrado = true;
            } else if (EventFlags.saqueadorEventoCompletado
                    && !EventFlags.dialogoGuardiaPostEventoKv9Mostrado) {
                startDialogue("dialogo_guardia_post_evento_kv9");
                EventFlags.dialogoGuardiaPostEventoKv9Mostrado = true;
            }
        }

        // Actualizar diálogo si existe
        if (dialogueManager != null) {
            dialogueManager.update(delta);
            String node = dialogueManager.getCurrentNodeId();

            // Mostrar el papiro de cartuchos
            if ("aceptar_ver_papiro_cartuchos".equals(node) && !papiroCartuchosMostrado) {
                loreOverlay = new LoreOverlayManager();
                loreOverlay.trigger(
                    "others/papiro_cartuchos.png",
                    "Presta mucha atención a todo los detalles que muestra este papiro y recuerda que la carta del familiar hablaba" + 
                    " de un juicio de un dios, el cual tendrás que recibir en la Duat"
                );
                papiroCartuchosMostrado = true;
            }

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

            // Entrega de Anj, Sandalias y libro de los muertos tras Kv9
            if ("entregar_objetos_kv9".equals(node) && !objetosEntregadosKv9) {
                getGame().getInventory().removeItem(Anj.class);
                getGame().getInventory().removeItem(Sandalias.class);
                getGame().getInventory().removeItem(Libro_de_los_muertos.class);
                getGame().getHUD().showPopupMessage(
                    "Has entregado los objetos sagrados",
                    new Anj(), new Sandalias(), new Libro_de_los_muertos()
                );
                objetosEntregadosKv9 = true;
            }
            // Recompensa: Libro de los Muertos
            if ("recompensa_libro_muertos".equals(node) && !libroDeLosMuertosEntregado && !EventFlags.papiroLibroDeLosMuertosMostrado) {
                getGame().getInventory().addItem(new Libro_de_los_muertos());
                getGame().getHUD().showPopupMessage(
                    "El guardia te ha regalado el Libro de los Muertos en agradecimiento",
                    new Libro_de_los_muertos()
                );

                loreOverlay = new LoreOverlayManager();
                loreOverlay.trigger(
                    "others/libro_de_los_muertos.png",
                    "Este papiro podría contener información decisiva, es muy importante que lo observes bien y memorices cualquier detalle que te parezca importante"
                );

                EventFlags.papiroLibroDeLosMuertosMostrado = true;
                libroDeLosMuertosEntregado = true;
            }

            // Lógica de honestidad (Maat)
            if (!EventFlags.personaje_miente_penalizacion_sacerdote) {
                if ("respuesta_combate".equals(node) && EventFlags.sacerdoteSobornado) {
                    MaatSystem.get().addCorazon(+4);
                    EventFlags.personaje_miente_penalizacion_sacerdote = true;
                }
                if ("respuesta_soborno".equals(node) && !EventFlags.sacerdoteSobornado) {
                    MaatSystem.get().addCorazon(+1);
                    EventFlags.personaje_miente_penalizacion_sacerdote = true;
                }
            }
            if (!EventFlags.personaje_miente_penalizacion_saqueador) {
                if ("respuesta_combate_saqueador".equals(node) && EventFlags.saqueadorSobornado) {
                    MaatSystem.get().addCorazon(+4);
                    EventFlags.personaje_miente_penalizacion_saqueador = true;
                }
                if ("respuesta_soborno_saqueador".equals(node) && !EventFlags.saqueadorSobornado) {
                    MaatSystem.get().addCorazon(+1);
                    EventFlags.personaje_miente_penalizacion_saqueador = true;
                }
            }

            if (!dialogueManager.isActive()) {
                dialogueManager = null;
                freezePlayer   = false;
            }
        }

        // Transición de puertas
        if (DoorHandler.handleDoorTransition(
                getGame(), doorManager, player.getCollisionBounds())) {
            return;
        }

        // Render del mundo y personajes
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        npcGuardian.update(delta);
        batch.begin();
        Array<BaseCharacter> chars = new Array<>();
        chars.add(player);
        chars.add(npcGuardian);
        chars.sort((a,b) -> Float.compare(b.getPosition().y, a.getPosition().y));
        for (BaseCharacter c : chars) {
            c.render(batch);
        }
        doorManager.renderInteractionMessage(
            player.getCollisionBounds(), batch, camera
        );
        batch.setProjectionMatrix(game.getUiCamera().combined);
        textBox.render(batch);
        batch.setProjectionMatrix(camera.combined);
        guardianPrompt.render(
            batch,
            AssetLoader.get("fonts/ui_font.fnt", BitmapFont.class),
            camera
        );
        batch.end();

        // HUD
        getGame().getHUD().render(batch);

        if (loreOverlay != null && !loreOverlay.isFinished()) {
            loreOverlay.render(batch, shapeRenderer, camera);
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