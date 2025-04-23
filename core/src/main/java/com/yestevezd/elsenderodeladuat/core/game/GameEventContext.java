package com.yestevezd.elsenderodeladuat.core.game;

import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;

public interface GameEventContext {
    void onNPCReachedPlayer(NPCCharacter npc);
}