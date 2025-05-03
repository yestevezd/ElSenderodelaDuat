package com.yestevezd.elsenderodeladuat.core.combat;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;

public class CombatAnimation {
    private Array<TextureRegion> frames;
    private float frameDuration;
    private float stateTime;
    private int totalFrames;

    public CombatAnimation(String basePath, String filePrefix, int frameCount, float frameDuration) {
        this.frames = new Array<>();
        this.frameDuration = frameDuration;
        this.totalFrames = frameCount;
        this.stateTime = 0f;

        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(basePath + "/" + filePrefix + "%03d.png", i);
            Texture texture = AssetLoader.get(filename, Texture.class);
            frames.add(new TextureRegion(texture));
        }
    }

    public CombatAnimation(Array<TextureRegion> frames, float frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.totalFrames = frames.size;
        this.stateTime = 0f;
    }

    public void start() {
        stateTime = 0f;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        int frameIndex = (int)(stateTime / frameDuration);
        if (frameIndex >= frames.size) frameIndex = frames.size - 1;
        return frames.get(frameIndex);
    }

    public boolean isFinished() {
        return stateTime >= frameDuration * totalFrames;
    }
}