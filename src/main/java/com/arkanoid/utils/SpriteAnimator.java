package com.arkanoid.utils;

import javafx.scene.image.Image;

public class SpriteAnimator {
    private final Image[] frames;
    private int currentFrameIndex = 0;
    private final long frameDuration;
    private long lastFrameTime;
    private final boolean isAnimated;


    public SpriteAnimator(Image[] frames, int fps) {
        this.frames = frames;
        this.isAnimated = frames != null && frames.length > 1;

        if (isAnimated) {
            this.frameDuration = (long) (1e9 / fps);
            this.lastFrameTime = System.nanoTime();
        } else {
            this.frameDuration = 0;
        }
    }

    public void update() {
        if (!isAnimated) {
            return;
        }

        long now = System.nanoTime();
        if (now - lastFrameTime > frameDuration) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
            lastFrameTime = now;
        }
    }

    public Image getCurrentFrame() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[currentFrameIndex];
    }
}
