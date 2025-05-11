package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.entities.Escarabajo;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.badlogic.gdx.math.Rectangle;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MaatSystem;

public class DoorHandler {

    public static boolean handleDoorTransition(MainGame game, DoorManager doorManager, Rectangle playerBounds) {
        // Intento de puerta errónea
        DoorTrigger nearby = doorManager.getNearbyInteractiveDoor(playerBounds);

        if (nearby != null 
            && ("puerta_tumba_erronea_1".equals(nearby.getName()) 
             || "puerta_tumba_erronea_2".equals(nearby.getName()))
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {

            String name = nearby.getName();
            boolean tried1 = EventFlags.puertaErronea1Intentada;
            boolean tried2 = EventFlags.puertaErronea2Intentada;

            if ("puerta_tumba_erronea_1".equals(name)) {
                if (!tried1 && !tried2) {
                    // Primer intento: muestra aviso
                    game.getHUD().showPopupMessage("¡Tumba errónea! Tienes una opción más");
                    EventFlags.puertaErronea1Intentada = true;
                } else if (!tried1 && tried2) {
                    // Segundo intento sobre la otra puerta: quita escarabajo
                    game.getHUD().showPopupMessage("¡Tumba errónea! Has perdido el amuleto de escarabajo protector!");
                    game.getInventory().removeItem(Escarabajo.class);
                    MaatSystem.get().addCorazon(-1);
                    EventFlags.puertaErronea1Intentada = true;
                }
                
            } else { 
                if (!tried2 && !tried1) {
                    game.getHUD().showPopupMessage("¡Tumba errónea! Tienes una opción más");
                    EventFlags.puertaErronea2Intentada = true;
                } else if (!tried2 && tried1) {
                    game.getHUD().showPopupMessage("¡Tumba errónea! Has perdido el amuleto de escarabajo protector!");
                    game.getInventory().removeItem(Escarabajo.class);
                    MaatSystem.get().addCorazon(-1);
                    EventFlags.puertaErronea2Intentada = true;
                }
            }

            return false;
        }

        DoorTrigger triggered = doorManager.checkForTransition(playerBounds);
        if (triggered != null) {
            String doorName = triggered.getName();
            
            if ("puerta_templo_karnak".equals(doorName) && !EventFlags.dialogoGuardiaKarnakinicialMostrado) {
                game.getHUD().showPopupMessage("Habla con el guardián antes de entrar al templo.");
                return false;
            }else if("puerta_camino_2".equals(doorName) && !EventFlags.dialogoGuardiaKarnakPostEventoMostrado){
                game.getHUD().showPopupMessage("Debes viajar primero al templo de Karnak.");
                return false;
            }
            var dest = DoorRegistry.getScreenForDoor(game, doorName);
            if (dest != null) {
                if (!triggered.requiresInteraction()) {
                    AudioManager.playSound("sounds/sonido_puerta.mp3");
                }
                Gdx.app.postRunnable(() -> game.setScreen(dest));
            } else {
                System.out.println("[WARN] Puerta sin destino: " + doorName);
            }
            return true;
        }
        return false;
    }
}
