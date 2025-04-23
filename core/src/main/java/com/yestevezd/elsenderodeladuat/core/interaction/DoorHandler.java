package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.Gdx;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase utilitaria para gestionar transiciones de puerta entre pantallas.
 */
public class DoorHandler {

    /**
     * Verifica si el jugador ha activado una puerta y cambia de pantalla si corresponde.
     *
     * @param game        Instancia principal del juego.
     * @param doorManager Sistema que contiene las puertas de la pantalla.
     * @param playerBounds Área de colisión del jugador.
     * @return true si se ha cambiado de pantalla (para cortar el renderizado).
     */
    public static boolean handleDoorTransition(MainGame game, DoorManager doorManager, Rectangle playerBounds) {
        DoorTrigger triggeredDoor = doorManager.checkForTransition(playerBounds);
        if (triggeredDoor != null) {
            String doorName = triggeredDoor.getName();
            var destinationScreen = DoorRegistry.getScreenForDoor(game, doorName);

            if (destinationScreen != null) {
                if (!triggeredDoor.requiresInteraction()) {
                    AudioManager.playSound("sounds/sonido_puerta.mp3");
                }
                Gdx.app.postRunnable(() -> game.setScreen(destinationScreen));
            } else {
                System.out.println("[WARN] Puerta detectada pero sin destino registrado: " + doorName);
            }
            return true;
        }
        return false;
    }
}