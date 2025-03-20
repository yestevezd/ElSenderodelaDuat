package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;

public class MainMenuScreen extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private TextButton startButton, exitButton;

    public MainMenuScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin();
        startButton = new TextButton("Nueva Partida", skin);
        exitButton = new TextButton("Salir", skin);
        stage.addActor(startButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}