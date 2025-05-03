package com.yestevezd.elsenderodeladuat.core.game;

import com.yestevezd.elsenderodeladuat.core.entities.NPCCharacter;
import com.yestevezd.elsenderodeladuat.core.entities.PlayerCharacter;

public interface GameEventContext {
    void onNPCReachedPlayer(NPCCharacter npc);
    PlayerCharacter getPlayer();
}