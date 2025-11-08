package com.arkanoid.view.Effect;

import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosionEffect {
    private final SpriteAnimator animator;
    private final double centerX, centerY;
    private final double effectSize;
    private boolean finished = false;

    public ExplosionEffect(double centerX, double centerY, double size) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.effectSize = size;
        Image[] frames = AssetsManager.getFrames("Explosion");
        if (frames.length == 0) {
            System.err.println("Không tìm thấy sprite explosion!");
        }
        this.animator = new SpriteAnimator(frames, 60, false);

    }

    public void update() {
        animator.update();
        if (animator.isFinished()) {
            finished = true;
        }
    }

    public void render(GraphicsContext gc) {
        Image frame = animator.getCurrentFrame();
        if (frame == null) return;

        gc.drawImage(
                frame,
                centerX - effectSize / 2,
                centerY - effectSize / 2,
                effectSize,
                effectSize
        );
    }

    public boolean isFinished() {
        return finished;
    }
}