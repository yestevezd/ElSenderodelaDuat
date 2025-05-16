package com.yestevezd.elsenderodeladuat.core.game;

public class EventFlags {
    public static boolean artesanoEventoCompletado = false;
    public static boolean lorePapiroArtesanoMostrado = false;
    public static boolean sacerdoteEventoCompletado = false;
    public static boolean dialogoGuardiaKarnakinicialMostrado = false;
    public static boolean dialogoGuardiaKarnakPostEventoMostrado = false;
    public static boolean puertaErronea1Intentada = false;
    public static boolean puertaErronea2Intentada = false;
    public static boolean saqueadorEventoCompletado = false;
    public static boolean sacerdoteSobornado = false;
    public static boolean saqueadorSobornado = false;
    public static boolean personaje_miente_penalizacion_sacerdote = false;
    public static boolean personaje_miente_penalizacion_saqueador = false;
    public static boolean dialogoGuardiaPostEventoKv9Mostrado = false;

    public static void resetAll() {
        dialogoGuardiaKarnakinicialMostrado = false;
        dialogoGuardiaKarnakPostEventoMostrado = false;
        artesanoEventoCompletado = false;
        lorePapiroArtesanoMostrado = false;
        sacerdoteEventoCompletado = false;
        puertaErronea1Intentada = false;
        puertaErronea2Intentada = false;
        saqueadorEventoCompletado = false;
        sacerdoteSobornado = false;
        saqueadorSobornado = false;
        personaje_miente_penalizacion_sacerdote = false;
        personaje_miente_penalizacion_saqueador = false;
        dialogoGuardiaPostEventoKv9Mostrado = false;
    }
}