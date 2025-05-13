package com.yestevezd.elsenderodeladuat.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yestevezd.elsenderodeladuat.core.engine.AudioManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.save.SaveManager;
import com.yestevezd.elsenderodeladuat.core.screens.MainMenuScreen;

public class PauseOverlay {

    private boolean visible = false;
    private final String[] options = {
        "CONTINUAR",
        "CONFIGURAR SONIDO",
        "CONTROLES",
        "GUARDAR PARTIDA",
        "SALIR AL MENÚ"
    };
    private int selected = 0;

    private final MainGame game;
    private final BitmapFont font;
    private final SoundPopup soundPopup;
    private final ControlsPopup controlsPopup;
    private final GlyphLayout layout = new GlyphLayout();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private String saveMessage = "";
    private float saveMessageTimer = 0f;
    private static final float SAVE_MSG_DURATION = 2f;

    public PauseOverlay(MainGame game) {
        this.game = game;
        this.font = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        this.soundPopup = new SoundPopup();
        this.controlsPopup = new ControlsPopup();
    }

    public void show() { visible = true; }
    public void hide() { visible = false; }
    public boolean isVisible() {
        return visible || soundPopup.isVisible() || controlsPopup.isVisible();
    }

    public void update() {
        if (soundPopup.isVisible()) {
            soundPopup.handleInput();
            return;
        }

        if (controlsPopup.isVisible()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                controlsPopup.hide();
            }
            return;
        }

        if (saveMessageTimer > 0) {
            saveMessageTimer -= Gdx.graphics.getDeltaTime();
            if (saveMessageTimer <= 0) {
                saveMessage = "";
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selected = (selected - 1 + options.length) % options.length;
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selected = (selected + 1) % options.length;
            AudioManager.playSound("sounds/sonido_desplazarse_menu.mp3");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            AudioManager.playSound("sounds/click_menu.mp3");
            switch (options[selected]) {
                case "CONTINUAR": hide(); break;
                case "CONFIGURAR SONIDO": soundPopup.show(); break;
                case "CONTROLES": controlsPopup.show(); break;
                case "GUARDAR PARTIDA":
                    SaveManager.saveGame(game);
                    saveMessage = "La partida se ha guardado correctamente";
                    saveMessageTimer = SAVE_MSG_DURATION;
                    break;
                case "SALIR AL MENÚ": game.setScreen(new MainMenuScreen(game)); break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!visible && !soundPopup.isVisible() && !controlsPopup.isVisible()) return;
    
        // Asegurarse de que no haya un begin() abierto antes de pasar el control a los popups
        if (batch.isDrawing()) batch.end();
    
        if (soundPopup.isVisible()) {
            soundPopup.render(batch);
            return;
        }
    
        if (controlsPopup.isVisible()) {
            controlsPopup.render(batch);
            return;
        }
    
        batch.setProjectionMatrix(game.getUiCamera().combined);
    
        // Dibujar fondo y borde
        float originalScaleX = font.getData().scaleX;
        float originalScaleY = font.getData().scaleY;
        font.getData().setScale(1.1f);
    
        float maxWidth = 0f;
        for (String option : options) {
            layout.setText(font, "> " + option + " <");
            maxWidth = Math.max(maxWidth, layout.width);
        }
    
        float padding = 60f;
        float spacing = 60f;
        float popupWidth = maxWidth + padding * 2;
        float popupHeight = spacing * options.length + padding * 2;
        float x = (game.getUiCamera().viewportWidth - popupWidth) / 2f;
        float y = (game.getUiCamera().viewportHeight - popupHeight) / 2f;
    
        shapeRenderer.setProjectionMatrix(game.getUiCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.35f, 0.25f, 0.15f, 0.95f));
        shapeRenderer.rect(x, y, popupWidth, popupHeight);
        shapeRenderer.end();
    
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, popupWidth, popupHeight);
        shapeRenderer.end();
    
        batch.begin();
    
        float baseY = y + popupHeight - padding;
        for (int i = 0; i < options.length; i++) {
            font.setColor(i == selected ? Color.YELLOW : Color.WHITE);
            String text = i == selected ? "> " + options[i] + " <" : options[i];
            layout.setText(font, text);
            float textX = x + (popupWidth - layout.width) / 2f;
            float textY = baseY - i * spacing;
            font.draw(batch, layout, textX, textY);
        }

        if (!saveMessage.isEmpty()) {
            // texto más pequeño y verde
            font.setColor(Color.GREEN);
            font.getData().setScale(0.7f);
            layout.setText(font, saveMessage);
            // centrado horizontal: x + (popupWidth - layout.width)/2
            float msgX = x + (popupWidth - layout.width) / 2f;
            // un poco por debajo del menú: y - 20
            float msgY = y - 20;
            font.draw(batch, layout, msgX, msgY);
            // restauramos escala/color
            font.getData().setScale(1.1f);
            font.setColor(Color.WHITE);
        }
    
        batch.end();
    
        font.getData().setScale(originalScaleX, originalScaleY);
    }
    
    

    public void dispose() {
        font.dispose();
        soundPopup.dispose();
        controlsPopup.dispose();
        shapeRenderer.dispose();
    }
}
