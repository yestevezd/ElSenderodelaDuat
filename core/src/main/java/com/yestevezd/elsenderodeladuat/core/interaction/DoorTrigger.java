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
    private final String interactionMessage; 

     // Constructor original (sin mensaje)
     public DoorTrigger(String name, Polygon shape, boolean requiresInteraction) {
        this(name, shape, requiresInteraction, null);
    }

    // Constructor extendido
    public DoorTrigger(String name, Polygon shape, boolean requiresInteraction, String interactionMessage) {
        this.name = name;
        this.shape = shape;
        this.requiresInteraction = requiresInteraction;
        this.interactionMessage = interactionMessage;
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

    public String getInteractionMessage() {
        return interactionMessage;
    }
}