package com.yestevezd.elsenderodeladuat.core.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.MaatSystem;
import com.yestevezd.elsenderodeladuat.core.entities.Item; // interfaz o clase base
import com.yestevezd.elsenderodeladuat.core.game.MainGame;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveManager {
    private static final String PREF_NAME = "ElSenderoSave";
    private static final String SAVE_KEY = "saveData";

    public static void saveGame(MainGame game) {
        SaveData data = new SaveData();

        // 1) ¿Dónde estamos?
        data.screenId   = game.getCurrentScreenId();  
        data.spawnX     = game.getPlayer().getPosition().x;
        data.spawnY     = game.getPlayer().getPosition().y;
        data.entryDoor  = game.getCurrentEntryDoor();

        // 2) Estado del jugador
        data.playerHealth  = game.getPlayer().getCurrentHealth();
        data.maatCorazon   = MaatSystem.get().getCorazon();

        // 3) Inventario
        data.inventoryItems = new ArrayList<>();
        for (Item item : game.getInventory().getItems()) {
            if (item != null) {
                data.inventoryItems.add(item.getId());
            }
        }

        // 4) Flags
        data.eventFlags = new HashMap<>();
        data.eventFlags.put("artesanoEventoCompletado", EventFlags.artesanoEventoCompletado);
        data.eventFlags.put("lorePapiroArtesanoMostrado", EventFlags.lorePapiroArtesanoMostrado);
        data.eventFlags.put("sacerdoteEventoCompletado", EventFlags.sacerdoteEventoCompletado);
        data.eventFlags.put("dialogoGuardiaKarnakinicialMostrado", EventFlags.dialogoGuardiaKarnakinicialMostrado);
        data.eventFlags.put("dialogoGuardiaKarnakPostEventoMostrado", EventFlags.dialogoGuardiaKarnakPostEventoMostrado);
        data.eventFlags.put("puertaErronea1Intentada", EventFlags.puertaErronea1Intentada);
        data.eventFlags.put("puertaErronea2Intentada", EventFlags.puertaErronea2Intentada);

        // Serializar y guardar
        Json json = new Json();
        String str = json.toJson(data);
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putString(SAVE_KEY, str);
        prefs.flush();
    }

    public static boolean hasSave() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        return prefs.contains(SAVE_KEY);
    }

    public static void loadGame(MainGame game) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        if (!prefs.contains(SAVE_KEY)) return;

        String str = prefs.getString(SAVE_KEY);
        Json json = new Json();
        SaveData data = json.fromJson(SaveData.class, str);

        // 1) Restaurar flags
        EventFlags.dialogoGuardiaKarnakinicialMostrado = data.eventFlags.getOrDefault("dialogoGuardiaKarnakinicialMostrado", false);
        EventFlags.dialogoGuardiaKarnakPostEventoMostrado = data.eventFlags.getOrDefault("dialogoGuardiaKarnakPostEventoMostrado", false);
        EventFlags.artesanoEventoCompletado = data.eventFlags.getOrDefault("artesanoEventoCompletado", false);
        EventFlags.lorePapiroArtesanoMostrado = data.eventFlags.getOrDefault("lorePapiroArtesanoMostrado", false);
        EventFlags.sacerdoteEventoCompletado = data.eventFlags.getOrDefault("sacerdoteEventoCompletado", false);
        EventFlags.puertaErronea1Intentada = data.eventFlags.getOrDefault("puertaErronea1Intentada", false);
        EventFlags.puertaErronea2Intentada = data.eventFlags.getOrDefault("puertaErronea2Intentada", false);

        // 2) Restaurar Maat
        MaatSystem.get().reset();
        MaatSystem.get().addCorazon(data.maatCorazon);

        // 3) Inventario
        game.getInventory().clear();
        for (String id : data.inventoryItems) {
            game.getInventory().addItem(ItemFactory.createById(id));
        }

        // 4) Salud
        game.getPlayer().setCurrentHealth(data.playerHealth);

        // 5) Pantalla y posición
        game.setScreen(ScreenFactory.createById(data.screenId, game, data.spawnX, data.spawnY, data.entryDoor));
    }

    public static void deleteSave() {
        Gdx.app.getPreferences(PREF_NAME).clear();
        Gdx.app.getPreferences(PREF_NAME).flush();
    }
}
