package com.yestevezd.elsenderodeladuat.core.narrative;

/**
 * Enum que representa los diferentes tipos de líneas narrativas del juego.
 * Se utiliza para personalizar el renderizado y comportamiento de cada línea.
 */
public enum NarrativeType {

    /**
     * Línea que incluye texto jeroglífico y su traducción al español.
     * Ideal para secciones ambientadas con escritura egipcia.
     */
    CONTEXTUALIZED,

    /**
     * Línea que solo contiene traducción en español, sin texto jeroglífico.
     * Útil para texto de sistema, instrucciones u otras situaciones.
     */
    TRANSLATED_ONLY,

    /**
     * Línea de diálogo entre personajes, se muestra el nombre del hablante.
     * Puede o no incluir jeroglífico dependiendo del diseño.
     */
    DIALOGUE
}