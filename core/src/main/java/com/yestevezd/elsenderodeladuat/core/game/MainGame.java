package com.yestevezd.elsenderodeladuat.core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;
import com.yestevezd.elsenderodeladuat.core.interaction.DoorRegistry;
import com.yestevezd.elsenderodeladuat.core.screens.IntroScreen;
import com.yestevezd.elsenderodeladuat.core.ui.HUD;
import com.yestevezd.elsenderodeladuat.core.inventory.Inventory;
import com.badlogic.gdx.Screen;

public class MainGame extends Game {

    private SpriteBatch batch;
    private HUD hud;
    private Inventory inventory;
    private PlayerCharacter player;
    private OrthographicCamera uiCamera;
    private String currentScreenId;
    private String currentEntryDoor;

    @Override
    public void create() {
        batch = new SpriteBatch();

        AssetLoader.loadGlobalAssets();
        AssetLoader.loadHUDAssets(); 
        AssetLoader.finishLoading();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Crear inventario con 5 slots y HUD que lo use
        inventory = new Inventory(6);
        hud = new HUD(inventory,uiCamera);

        // Registrar transiciones
        DoorRegistry.registerDefaultDoors();

        Texture playerTexture = AssetLoader.get("characters/personaje_principal.png", Texture.class);
        player = new PlayerCharacter(playerTexture, 0, 0, 200f);
        player.setScale(2.5f);

        MaatSystem.get().reset();

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

    public PlayerCharacter getPlayer() {
        return player;
    }

    public OrthographicCamera getUiCamera() {
        return uiCamera;
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        // Guarda el ID de pantalla cada vez que cambias de pantalla
        this.currentScreenId = screen.getClass().getSimpleName();
    }

    public String getCurrentScreenId() {
        return currentScreenId;
    }

    public String getCurrentEntryDoor() {
        return currentEntryDoor;
    }

    public void setCurrentEntryDoor(String doorName) {
        this.currentEntryDoor = doorName;
    }
}
