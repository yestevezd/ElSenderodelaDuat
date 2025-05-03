package com.yestevezd.elsenderodeladuat.core.entities;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.yestevezd.elsenderodeladuat.core.game.EventFlags;
import com.yestevezd.elsenderodeladuat.core.game.GameConfig;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.badlogic.gdx.Gdx;

/**
 * Estados posibles para un NPC.
 */
public enum NPCState implements State<NPCCharacter> {
    PATRULLAR {
        private Vector2 pointA = new Vector2(1400, 300);
        private Vector2 pointB = new Vector2(1400, 100);
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
            npc.setLastDx(0f);
            npc.setLastDy(0f);
        }

        @Override
        public void update(NPCCharacter npc) {
            
        }
    },

    COMBATIR {
        private float decisionTimer = 0f;
        private boolean escaping = false;
    
        @Override
        public void enter(NPCCharacter npc) {
            decisionTimer = 1.5f;
        }
    
        @Override
        public void update(NPCCharacter npc) {
            PlayerCharacter player = npc.getGameContext().getPlayer();
            Vector2 toPlayer = player.getPosition().cpy().sub(npc.getPosition());
            float distance = toPlayer.len();
    
            decisionTimer -= Gdx.graphics.getDeltaTime();
    
            // IA básica: decidir si escapar o atacar
            if (decisionTimer <= 0f) {
                escaping = Math.random() < 0.2f; // 20% de probabilidad de escapar
                decisionTimer = 1.5f;
            }
    
            Vector2 velocity = new Vector2();
    
            if (escaping) {
                if (distance < 250f) {
                    velocity = npc.getPosition().cpy().sub(player.getPosition()).nor().scl(npc.getSpeed());
                } else {
                    velocity.setZero();
                }
            } else {
                if (distance > 60f) {
                    velocity = toPlayer.nor().scl(npc.getSpeed());
                    velocity.y = 0f;
                } else {
                    velocity.setZero();
                }
            }
    
            velocity.y = 0;
            npc.setVelocity(velocity);
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

            Vector2 velocity = new Vector2();

            if (Math.abs(toPlayer.x) > 5f) {
                velocity.x = Math.signum(toPlayer.x) * npc.getSpeed();
            } else if (Math.abs(toPlayer.y) > 5f) {
                velocity.y = Math.signum(toPlayer.y) * npc.getSpeed();
            }

            npc.setVelocity(velocity);
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
    },
    DESAPARECER {
        @Override
        public void enter(NPCCharacter npc) {
            npc.setVelocity(Vector2.Zero);
        }
    
        @Override
        public void update(NPCCharacter npc) {
            Vector2 destino = npc.getCustomDestination();
            if (destino == null) return; 
    
            Vector2 toTarget = destino.cpy().sub(npc.getPosition());
    
            if (toTarget.len2() <= 4f) {
                npc.setPosition(destino.x, destino.y);
                npc.setVelocity(Vector2.Zero);
                npc.setVisible(false);  
                return;
            }
    
            Vector2 velocity = toTarget.nor().scl(npc.getSpeed());
            npc.setVelocity(velocity);
    
            float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
            npc.setPosition(
                npc.getPosition().x + velocity.x * delta,
                npc.getPosition().y + velocity.y * delta
            );
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