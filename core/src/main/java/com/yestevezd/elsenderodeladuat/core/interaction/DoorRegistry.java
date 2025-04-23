package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.Screen;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.screens.DeirElMedinaScreen;
import com.yestevezd.elsenderodeladuat.core.screens.HouseScreen;
import com.yestevezd.elsenderodeladuat.core.screens.KarnakScreen;
import com.yestevezd.elsenderodeladuat.core.screens.SalaHipostilaScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Registro global de puertas del juego.
 * Asocia el nombre de cada puerta con una pantalla de destino.
 * Permite configurar transiciones entre pantallas de forma flexible y centralizada.
 */
public class DoorRegistry {

    private static final Map<String, Function<MainGame, Screen>> doorMap = new HashMap<>();

    /**
     * Registra una nueva puerta en el sistema.
     *
     * @param doorName       Nombre único de la puerta (debe coincidir con el nombre en el mapa).
     * @param screenSupplier Función que genera la pantalla destino con el juego como argumento.
     */
    public static void register(String doorName, Function<MainGame, Screen> screenSupplier) {
        doorMap.put(doorName, screenSupplier);
    }

    /**
     * Devuelve la pantalla de destino asociada a una puerta específica.
     *
     * @param game     Instancia del juego actual.
     * @param doorName Nombre de la puerta que se ha activado.
     * @return Pantalla correspondiente o null si no está registrada.
     */
    public static Screen getScreenForDoor(MainGame game, String doorName) {
        Function<MainGame, Screen> supplier = doorMap.get(doorName);
        return supplier != null ? supplier.apply(game) : null;
    }

    /**
     * Registra las puertas principales del juego con su lógica de pantalla.
     * Aquí se definen los nombres de las puertas tal como aparecen en Tiled.
     */
    public static void registerDefaultDoors() {
        // Casa → Pueblo
        register("puerta_auto_casa_dentro", game -> new DeirElMedinaScreen(game, 608, 300));
        // Pueblo → Casa
        register("puerta_auto_casa_fuera", game -> new HouseScreen(game, 620, 300));
        // Pueblo → Karnak
        register("puerta_camino_1", game -> new KarnakScreen(game, 595, 10, "puerta_camino_1"));
        // Karnak → Pueblo
        register("puerta_camino_3", game -> new DeirElMedinaScreen(game, 1880, 300, "puerta_camino_3"));
        // Karnak → Sala_hipostila
        register("puerta_templo_karnak", game -> new SalaHipostilaScreen(game, 300, 300, "puerta_templo_karnak"));
        // Sala_hipostila → Karnak
        register("puerta_templo_karnak_dentro", game -> new KarnakScreen(game, 700, 605, "puerta_templo_karnak_dentro"));
    }
}