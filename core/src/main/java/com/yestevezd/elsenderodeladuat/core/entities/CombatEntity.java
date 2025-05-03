package com.yestevezd.elsenderodeladuat.core.entities;

/**
 * Interfaz para cualquier entidad que participe en un combate.
 */
public interface CombatEntity {
    int getCurrentHealth();
    int getMaxHealth();
    String getName();
    boolean isAlive();
}