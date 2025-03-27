package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * AssetLoader centralizado para gestionar los recursos del juego de forma modular.
 * Organizado por contexto: intro, menú, escenarios, etc.
 */
public class AssetLoader {

    private static final AssetManager manager = new AssetManager();

    // MENÚ PRINCIPAL

    public static void loadMenuAssets() {
        manager.load("menu/fondo_menu.jpg", Texture.class);
        manager.load("sounds/sonido_desplazarse_menu.mp3", Sound.class);
        manager.load("sounds/click_menu.mp3", Sound.class);    
    }

    // CASA DEL ARTESANO

    public static void loadHouseAssets() {
        
    }

    // TEMPLO DE KARNAK

    public static void loadKarnakAssets() {
       
    }

    // VALLE DE LOS REYES

    public static void loadValleyAssets() {
        
    }

    // DUAT (INFIERNO EGIPCIO)

    public static void loadDuatAssets() {
        
    }

    // HUD / INTERFAZ

    public static void loadHUDAssets() {
        
    }

// MÉTODOS DE UNLOAD

public static void unloadMenuAssets() {
    unload("others/fondo_menu.jpg");
    unload("sounds/sonido_desplazarse_menu.mp3");  
    unload("sounds/click_menu.mp3");  
}

public static void unloadHouseAssets() {
   
}

public static void unloadKarnakAssets() {
    
}

public static void unloadValleyAssets() {
    
}

public static void unloadDuatAssets() {
    
}

public static void unloadHUDAssets() {
    
}

    // MÉTODOS GENERALES

    public static void finishLoading() {
        manager.finishLoading();
    }

    public static <T> T get(String path, Class<T> type) {
        return manager.get(path, type);
    }

    public static boolean isLoaded(String path, Class<?> type) {
        return manager.isLoaded(path, type);
    }

    public static void unload(String path) {
        if (manager.isLoaded(path)) {
            manager.unload(path);
        }
    }

    public static void dispose() {
        manager.dispose();
    }

    // VIDEO FRAMES (para intros animadas)

    /**
     * Carga una secuencia de imágenes desde una carpeta para animaciones tipo video.
     *
     * @param folder Carpeta donde están los frames (relativa a assets/)
     * @param count  Número de imágenes (frame_001.png, frame_002.png...)
     * @return Array con los frames como TextureRegion
     */
    public static Array<TextureRegion> loadVideoFrames(String folder, int count) {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 1; i <= count; i++) {
            String filename = String.format("%s/frame_%03d.png", folder, i);
            Texture texture = new Texture(Gdx.files.internal(filename));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames.add(new TextureRegion(texture));
        }

        return frames;
    }
}