package com.yestevezd.elsenderodeladuat.core.game;

public class EventFlags {
    public static boolean artesanoEventoCompletado = false;
    public static boolean lorePapiroArtesanoMostrado = false;
    public static boolean sacerdoteEventoCompletado = false;
    public static boolean dialogoGuardiaKarnakinicialMostrado = false;
    public static boolean dialogoGuardiaKarnakPostEventoMostrado = false;
    public static boolean puertaErronea1Intentada = false;
    public static boolean puertaErronea2Intentada = false;


    public static void resetAll() {
        dialogoGuardiaKarnakinicialMostrado = false;
        dialogoGuardiaKarnakPostEventoMostrado = false;
        artesanoEventoCompletado             = false;
        lorePapiroArtesanoMostrado           = false;
        sacerdoteEventoCompletado             = false;
        puertaErronea1Intentada              = false;
        puertaErronea2Intentada              = false;
    }
}