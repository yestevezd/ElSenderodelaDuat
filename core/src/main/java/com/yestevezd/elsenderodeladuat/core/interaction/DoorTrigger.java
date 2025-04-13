package com.yestevezd.elsenderodeladuat.core.interaction;

import com.badlogic.gdx.math.Polygon;

/**
 * Representa una puerta en el mapa del juego.
 * Cada puerta tiene un nombre único, una forma (polígono) y puede requerir o no interacción del jugador.
 */
public class DoorTrigger {

    private final String name;
    private final Polygon shape;
    private final boolean requiresInteraction;

    /**
     * Crea una nueva puerta (trigger de transición).
     *
     * @param name                Nombre de la puerta 
     * @param shape               Forma de colisión de la puerta
     * @param requiresInteraction Indica si se necesita pulsar tecla para activarla
     */
    public DoorTrigger(String name, Polygon shape, boolean requiresInteraction) {
        this.name = name;
        this.shape = shape;
        this.requiresInteraction = requiresInteraction;
    }

    /** @return Nombre de la puerta. */
    public String getName() {
        return name;
    }

    /** @return Forma poligonal para detección de colisión. */
    public Polygon getShape() {
        return shape;
    }

    /** @return true si la puerta requiere interacción manual (tecla E, etc.), false si es automática. */
    public boolean requiresInteraction() {
        return requiresInteraction;
    }
}