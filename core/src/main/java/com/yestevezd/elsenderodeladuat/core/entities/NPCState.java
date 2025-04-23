package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

/**
 * Estados posibles para un NPC.
 */
public enum NPCState implements State<NPCCharacter> {
    PATRULLAR {
        private Vector2 pointA = new Vector2(300, 750);
        private Vector2 pointB = new Vector2(600, 750);
        private boolean toB = true;
    
        @Override
        public void update(NPCCharacter npc) {
            Vector2 target = toB ? pointB : pointA;
            Vector2 toTarget = target.cpy().sub(npc.getPosition());
    
            // Si llegó cerca del destino, cambiar dirección
            if (toTarget.len() < 5f) {
                toB = !toB;
                target = toB ? pointB : pointA;
                toTarget = target.cpy().sub(npc.getPosition());
            }
    
            // Detectar colisión con el jugador y cambiar de dirección si choca
            if (npc.isBlocked()) {
                toB = !toB;
                npc.setBlocked(false); 
                return;
            }
    
            npc.setVelocity(toTarget.nor().scl(npc.speed));
        }
    },

    HABLAR {
        @Override
        public void enter(NPCCharacter npc) {
            npc.setVelocity(Vector2.Zero); 
        }

        @Override
        public void update(NPCCharacter npc) {
            
        }
    },

    COMBATIR {
        @Override
        public void enter(NPCCharacter npc) {
            System.out.println("NPC entra en combate");
        }

        @Override
        public void update(NPCCharacter npc) {
            // lógica de persecución o ataque
        }
    },

    ACERCARSE_AL_JUGADOR {
        @Override
        public void enter(NPCCharacter npc) {
            npc.setSpeed(npc.getSpeed() * 2f); 
        }
    
        @Override
        public void update(NPCCharacter npc) {
            Vector2 toPlayer = npc.getTargetPosition().cpy().sub(npc.getPosition());

            if (toPlayer.len() < 60f) {
                npc.setVelocity(Vector2.Zero);
                npc.setLastDx(0);
                npc.setLastDy(0);
                npc.getGameContext().onNPCReachedPlayer(npc);
                npc.getStateMachine().changeState(NPCState.HABLAR);
                return;
            }

            npc.setVelocity(toPlayer.nor().scl(npc.getSpeed()));
        }
    
        @Override
        public void exit(NPCCharacter npc) {
            npc.setSpeed(npc.getSpeed() / 2f); 
        }
    },
    
    ENTRAR_CASA {
        private final Vector2 destinoPuerta = new Vector2(1020, 355); 
        private boolean sonidoReproducido = false;
    
        @Override
        public void enter(NPCCharacter npc) {
            npc.setVelocity(Vector2.Zero);
            sonidoReproducido = false;
        }
    
        @Override
        public void update(NPCCharacter npc) {
            Vector2 toDoor = destinoPuerta.cpy().sub(npc.getPosition());
            if (toDoor.len() < 5f) {
                npc.setVisible(false);
                npc.setVelocity(Vector2.Zero);
                EventFlags.artesanoEventoCompletado = true;

                if (!sonidoReproducido) {
                    Sound doorSound = AssetLoader.get("sounds/sonido_puerta.mp3", Sound.class);
                    doorSound.play(GameConfig.SOUND_VOLUME);
                    sonidoReproducido = true;
                }

                return;
            }
    
            npc.setVelocity(toDoor.nor().scl(npc.getSpeed()));
        }

        @Override
        public void exit(NPCCharacter npc) {
            sonidoReproducido = false;
        }
    };

    @Override
    public void enter(NPCCharacter entity) {}
    @Override
    public void exit(NPCCharacter entity) {}
    @Override
    public boolean onMessage(NPCCharacter entity, Telegram telegram) {
        return false;
    }
}