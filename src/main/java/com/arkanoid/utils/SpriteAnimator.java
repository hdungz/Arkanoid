package com.arkanoid.utils;

import javafx.scene.image.Image;

public class SpriteAnimator {
    private final Image[] frames;
    private int currentFrameIndex = 0;
    private final long frameDuration;
    private long lastFrameTime;
    private final boolean isAnimated;
    private final boolean loop;
    private boolean finished = false;

    public SpriteAnimator(Image[] frames, int fps) {
        this(frames, fps, true); // default loop = true
    }
    public SpriteAnimator(Image[] frames, int fps, boolean loop) {
        this.frames = frames;
        this.loop = loop;
        this.isAnimated = frames != null && frames.length > 1;
        if (isAnimated) {
            this.frameDuration = (long) (1e9 / fps);
            this.lastFrameTime = System.nanoTime();
        } else {
            this.frameDuration = 0;
        }
    }

    public void update() {
        if (!isAnimated || finished) return;
        long now = System.nanoTime();
        if (now - lastFrameTime > frameDuration) {
            currentFrameIndex++;
            lastFrameTime = now;
            if (currentFrameIndex >= frames.length) {
                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    currentFrameIndex = frames.length - 1;
                    finished = true;
                }
            }
        }
    }

    public Image getCurrentFrame() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[Math.min(currentFrameIndex, frames.length - 1)];
    }

    public boolean isFinished() {
        return finished;
    }
}
