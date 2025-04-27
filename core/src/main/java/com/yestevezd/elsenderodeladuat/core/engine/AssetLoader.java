package com.yestevezd.elsenderodeladuat.core.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

/**
 * AssetLoader centralizado para gestionar los recursos del juego de forma modular.
 * Organizado por contexto: intro, menú, escenarios, etc.
 */
public class AssetLoader {

    private static final AssetManager manager = new AssetManager();

    //INTRODUCCIÓN
    public static void loadIntroAssets() {
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class); 
    }

    // MENÚ PRINCIPAL

    public static void loadMenuAssets() {
        manager.load("menu/fondo_menu.jpg", Texture.class);
        manager.load("sounds/sonido_desplazarse_menu.mp3", Sound.class);
        manager.load("sounds/click_menu.mp3", Sound.class); 
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);   
    }

    //CONFIGURACIÓN
    public static void loadConfigAssets() {
        manager.load("others/fondo_configuracion.jpg", Texture.class);
        manager.load("sounds/sonido_desplazarse_menu.mp3", Sound.class);
        manager.load("sounds/click_menu.mp3", Sound.class);    
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);  
    }

    //CONTEXTUALIZACIÓN
    public static void loadContextAssets() {
        manager.load("others/fondo_contextualizacion.jpg", Texture.class); 
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);
        manager.load("fonts/jeroglificos1_font.fnt", BitmapFont.class);
        manager.load("fonts/jeroglificos1_font.png", Texture.class);
        manager.load("fonts/jeroglificos2_font.fnt", BitmapFont.class);
        manager.load("fonts/jeroglificos2_font.png", Texture.class);
        manager.load("fonts/jeroglificos3_font.fnt", BitmapFont.class);
        manager.load("fonts/jeroglificos3_font.png", Texture.class);
    }

    // CASA DEL ARTESANO

    public static void loadHouseAssets() {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        manager.load("maps/casa_deir_el_medina.tmx", TiledMap.class);
        manager.load("characters/personaje_principal.png", Texture.class);  
        manager.load("sounds/sonido_puerta.mp3", Sound.class); 
        manager.load("others/papiro_hieratic.png", Texture.class);
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);
    }
    // DEIR EL MEDINA
    public static void loadDeirElMedinaAssets() {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        manager.load("maps/pueblo_deir_el_medina.tmx", TiledMap.class); 
        manager.load("characters/personaje_aleatorio.png", Texture.class); 
        manager.load("sounds/sonido_puerta.mp3", Sound.class);
        manager.load("sounds/sonido_viento.mp3", Sound.class);
        manager.load("others/fondo_configuracion.jpg", Texture.class); 
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);
    }

    // TEMPLO DE KARNAK

    public static void loadKarnakAssets() {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        manager.load("maps/karnak_templo.tmx", TiledMap.class);
        manager.load("characters/personaje_principal.png", Texture.class);  
        manager.load("sounds/sonido_viento.mp3", Sound.class);
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);

    }

    // TEMPLO DE KARNAK (SALA HIPOSTILA)
    public static void loadKarnakSalaHipostilaAssets() {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        manager.load("maps/sala_hipostila.tmx", TiledMap.class);
        manager.load("characters/personaje_principal.png", Texture.class);
        manager.load("characters/sacerdote.png", Texture.class);   
        manager.load("fonts/ui_font.fnt", BitmapFont.class);
        manager.load("fonts/ui_font.png", Texture.class);
    }

    // VALLE DE LOS REYES

    public static void loadValleyAssets() {
        
    }

    // DUAT (INFIERNO EGIPCIO)

    public static void loadDuatAssets() {
        
    }

    // HUD / INTERFAZ

    public static void loadHUDAssets() {
        manager.load("others/espada.jpg", Texture.class);
    }

// MÉTODOS DE UNLOAD

public static void unloadIntroAssets() {
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadMenuAssets() {
    unload("others/fondo_menu.jpg");
    unload("sounds/sonido_desplazarse_menu.mp3");  
    unload("sounds/click_menu.mp3");  
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadConfigAssets() {
    unload("others/fondo_configuracion.jpg");
    unload("sounds/sonido_desplazarse_menu.mp3");  
    unload("sounds/click_menu.mp3");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");  
}

public static void unloadContextAssets() {
    unload("others/fondo_contextualizacion.jpg");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
    unload("fonts/jeroglificos1_font.fnt");
    unload("fonts/jeroglificos1_font.png");
    unload("fonts/jeroglificos2_font.fnt");
    unload("fonts/jeroglificos2_font.png");
    unload("fonts/jeroglificos3_font.fnt");
    unload("fonts/jeroglificos3_font.png");
}

public static void unloadHouseAssets() {
    unload("maps/casa_deir_el_medina.tmx");
    unload("characters/personaje_principal.png");
    unload("sounds/sonido_puerta.mp3");
    unload("otehrs/papiro_hieratic.png");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadDeirElMedinaAssets() {
    unload("maps/pueblo_deir_el_medina.tmx");
    unload("characters/personaje_aleatorio.png");
    unload("sounds/sonido_puerta.mp3");
    unload("sounds/sonido_viento.mp3");
    unload("others/fondo_configuracion.jpg");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadKarnakAssets() {
    unload("maps/karnak_templo.tmx");
    unload("characters/personaje_principal.png");
    unload("sounds/sonido_viento.mp3");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadKarnakSalaHipostilaAssets() {
    unload("maps/sala_hipostila.tmx");
    unload("characters/personaje_principal.png");
    unload("fonts/ui_font.fnt");
    unload("fonts/ui_font.png");
}

public static void unloadValleyAssets() {
    
}

public static void unloadDuatAssets() {
    
}

public static void unloadHUDAssets() {
    unload("others/espada.jpg");
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

    public static String loadInternalText(String internalPath) {
        FileHandle file = Gdx.files.internal(internalPath);
        return file.readString("UTF-8");
    }
}