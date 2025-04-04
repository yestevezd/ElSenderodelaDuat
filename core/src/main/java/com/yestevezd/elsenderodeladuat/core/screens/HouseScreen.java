package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.maps.MapLoader;
import com.yestevezd.elsenderodeladuat.core.maps.MapUtils;

/**
 * HouseScreen representa la pantalla donde se muestra la casa del artesano.
 * Extiende de BaseScreen, por lo que hereda el batch y el control de ciclo de vida.
 */
public class HouseScreen extends BaseScreen {

    /** Cámara 2D para visualizar la escena */
    private OrthographicCamera camera;

    /** Viewport que adapta la resolución a la ventana del juego */
    private Viewport viewport;

    /** Mapa cargado desde Tiled (.tmx) */
    private TiledMap map;

    /** Renderizador para el mapa Tiled */
    private OrthogonalTiledMapRenderer mapRenderer;

    /** Personaje principal controlado por el jugador */
    private PlayerCharacter player;

    /** Textura (spritesheet) del personaje principal */
    private Texture playerTexture;

    /** Ancho del mapa en píxeles */
    private float mapWidth;

    /** Alto del mapa en píxeles */
    private float mapHeight;

    /**
     * Constructor de la pantalla.
     * @param game Instancia principal del juego (MainGame)
     */
    public HouseScreen(MainGame game) {
        super(game);
    }

    /**
     * Método llamado una única vez cuando se muestra esta pantalla.
     * Carga los recursos, configura la cámara y el viewport, y posiciona al jugador.
     */
    @Override
    public void show() {
        // Cargar los recursos necesarios para esta pantalla
        AssetLoader.loadHouseAssets();
        AssetLoader.finishLoading();

        // Cargar el mapa y preparar su renderizador
        MapLoader mapLoader = new MapLoader("maps/casa_deir_el_medina.tmx");
        map = mapLoader.getTiledMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Obtener dimensiones del mapa
        mapWidth = MapUtils.getMapPixelWidth(map);
        mapHeight = MapUtils.getMapPixelHeight(map);

        // Cargar textura del personaje
        playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);

        // Crear personaje y posicionarlo en el centro del mapa
        player = new PlayerCharacter(playerTexture, mapWidth / 2f, mapHeight / 2f, 100f);
        player.setScale(2.5f); // Ajustar tamaño visible

        // Inicializar cámara y viewport (resolución virtual 1280x720)
        camera = new OrthographicCamera();
        viewport = MapUtils.setupCameraAndViewport(map, camera, 1280, 720);
    }

    /**
     * Método que se llama cada frame del juego.
     * Se encarga de limpiar la pantalla, actualizar la cámara y renderizar el mapa y personaje.
     * @param delta Tiempo transcurrido desde el último frame.
     */
    @Override
    public void render(float delta) {
        // Limpiar pantalla con color negro
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar cámara antes de renderizar
        camera.update();

        // Renderizar el mapa en pantalla
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Actualizar la lógica del personaje
        player.update(delta);

        // Dibujar al personaje con la cámara actual
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();
    }

    /**
     * Método llamado automáticamente cuando se cambia el tamaño de la ventana.
     * @param width Nuevo ancho de la ventana.
     * @param height Nueva altura de la ventana.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    /**
     * Libera recursos del mapa y del renderizador cuando la pantalla ya no se necesita.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        AssetLoader.unloadHouseAssets();
    }
}