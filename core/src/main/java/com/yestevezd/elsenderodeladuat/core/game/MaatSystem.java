package com.yestevezd.elsenderodeladuat.core.game;

import com.badlogic.gdx.Gdx;

public class MaatSystem {
    private static MaatSystem instance;
    private int corazon = 0;

    private MaatSystem() {}

    public static MaatSystem get() {
        if (instance == null) instance = new MaatSystem();
        return instance;
    }

    /** Suma/resta puntos de Maat al “corazon” */
    public void addCorazon(int delta) {
        corazon += delta;
        Gdx.app.log("MaatSystem", "Corazon actual: " + corazon);
    }

    /** Devuelve valor acumulado */
    public int getCorazon() {
        return corazon;
    }

    /** Reinicia al arrancar una nueva partida */
    public void reset() {
        corazon = 0;
    }
}
