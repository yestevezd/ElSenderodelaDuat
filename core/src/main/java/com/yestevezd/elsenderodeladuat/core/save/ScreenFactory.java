package com.yestevezd.elsenderodeladuat.core.save;

import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.screens.*;

public class ScreenFactory {
    public static com.badlogic.gdx.Screen createById(
        String screenId,
        MainGame game,
        float spawnX,
        float spawnY,
        String entryDoor
    ) {
        switch(screenId) {
            case "HouseScreen":
                return new HouseScreen(game, spawnX, spawnY);
            case "DeirElMedinaScreen":
                return new DeirElMedinaScreen(game, spawnX, spawnY, entryDoor);
            case "KarnakScreen":
                return new KarnakScreen(game, spawnX, spawnY, entryDoor);
            case "SalaHipostilaScreen":
                return new SalaHipostilaScreen(game, spawnX, spawnY, entryDoor);
            case "ValleDeLosReyesScreen":
                return new ValleDeLosReyesScreen(game, spawnX, spawnY, entryDoor);
            case "TumbaKv9Screen":
                return new TumbaKv9Screen(game, spawnX, spawnY, entryDoor);
            default:
                throw new IllegalArgumentException("Pantalla desconocida: " + screenId);
        }
    }
}
