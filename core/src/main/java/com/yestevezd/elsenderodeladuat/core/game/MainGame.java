package com.yestevezd.elsenderodeladuat.core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorRegistry;
import com.yestevezd.elsenderodeladuat.core.screens.IntroScreen;

/**
 * Clase principal del juego.
 * Se encarga de inicializar los recursos globales y gestionar las pantallas.
 */
public class MainGame extends Game {

    // SpriteBatch compartido para dibujar texturas en todo el juego
    private SpriteBatch batch;

    /**
     * Método llamado al iniciar el juego. Carga recursos globales, registra puertas y muestra la primera pantalla.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();

        // Registrar las transiciones entre puertas y pantallas
        DoorRegistry.registerDefaultDoors();

        // Establecer la pantalla inicial (intro)
        setScreen(new IntroScreen(this));
    }

    /**
     * Llamado automáticamente por LibGDX en cada frame.
     * Se delega al método render() de la pantalla actual.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Libera recursos globales al cerrar el juego.
     */
    @Override
    public void dispose() {
        batch.dispose();
        AssetLoader.dispose();
    }

    /**
     * Devuelve el SpriteBatch compartido para su uso en distintas pantallas.
     * @return SpriteBatch global del juego
     */
    public SpriteBatch getBatch() {
        return batch;
    }
}