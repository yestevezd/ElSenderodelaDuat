package com.yestevezd.elsenderodeladuat.core.combat;

public enum CombatState {
    PREPARING,    // Pantalla negra, fade-in, música épica empieza
    IN_COMBAT,    // Se puede mover, atacar y combatir
    VICTORY,      // El jugador ha ganado
    DEFEAT        // El jugador ha perdido
}