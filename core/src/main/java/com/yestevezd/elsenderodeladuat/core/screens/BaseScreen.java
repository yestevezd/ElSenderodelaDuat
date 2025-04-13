package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Screen;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Clase base abstracta para todas las pantallas del juego.
 * Proporciona acceso directo al juego principal y al SpriteBatch global.
 * 
 * Heredar de esta clase permite unificar comportamientos comunes entre pantallas
 * como acceso al batch o al juego, así como métodos vacíos opcionales de ciclo de vida.
 */
public abstract class BaseScreen implements Screen {

    protected final MainGame game;
    protected final SpriteBatch batch;

    /**
     * Constructor base que inicializa referencias comunes.
     *
     * @param game Instancia principal del juego que maneja esta pantalla.
     */
    public BaseScreen(MainGame game) {
        this.game = game;
        this.batch = game.getBatch();
    }

    /**
     * Devuelve la instancia del juego principal.
     * 
     * @return Objeto MainGame
     */
    public MainGame getGame() {
        return game;
    }

    /**
     * Se llama cuando cambia el tamaño de la ventana o del viewport.
     * Puede ser sobreescrito por subclases si es necesario.
     *
     * @param width  Nuevo ancho en píxeles
     * @param height Nuevo alto en píxeles
     */
    @Override
    public void resize(int width, int height) {
        // Opcionalmente implementado en subclases
    }

    /**
     * Llamado cuando el juego se pausa (por ejemplo al minimizar).
     */
    @Override
    public void pause() {}

    /**
     * Llamado cuando el juego se reanuda tras una pausa.
     */
    @Override
    public void resume() {}

    /**
     * Llamado cuando esta pantalla deja de estar activa.
     */
    @Override
    public void hide() {}

    /**
     * Libera recursos utilizados por la pantalla.
     * Debe ser implementado por subclases si cargan recursos manualmente.
     */
    @Override
    public void dispose() {}
}