package com.yestevezd.elsenderodeladuat.core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorRegistry;
import com.yestevezd.elsenderodeladuat.core.screens.IntroScreen;
import com.yestevezd.elsenderodeladuat.core.ui.HUD;
import com.yestevezd.elsenderodeladuat.core.inventory.Inventory;

public class MainGame extends Game {

    private SpriteBatch batch;
    private HUD hud;
    private Inventory inventory;

    @Override
    public void create() {
        batch = new SpriteBatch();

        AssetLoader.loadHUDAssets();
        AssetLoader.finishLoading();

        // Crear inventario con 5 slots y HUD que lo use
        inventory = new Inventory(5);
        hud = new HUD(inventory);

        // Registrar transiciones
        DoorRegistry.registerDefaultDoors();

        // Pantalla inicial
        setScreen(new IntroScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        AssetLoader.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public HUD getHUD() {
        return hud;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
