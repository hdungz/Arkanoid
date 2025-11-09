package com.arkanoid.view.PowerUp;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.PowerUp.PowerUp;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arkanoid.CONSTANT.GAME_AREA_X;

public class PowerUpRenderer {
    private final GameModel gameModel;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Map<PowerUp, SpriteAnimator> animators;

    public PowerUpRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.canvas = new Canvas(800, 800);
        this.gc = canvas.getGraphicsContext2D();
        this.canvas.setLayoutX(GAME_AREA_X);
        this.animators = new HashMap<>();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        List<PowerUp> powerUps = gameModel.getPowerUpManager().getPowerUps();


        animators.keySet().removeIf(powerUp -> !powerUps.contains(powerUp));

        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isActive()) continue;
            if (!animators.containsKey(powerUp)) {
                SpriteAnimator animator = createAnimator(powerUp.getType());
                if (animator != null) {
                    animators.put(powerUp, animator);
                }
            }

            SpriteAnimator animator = animators.get(powerUp);
            if (animator != null) {
                animator.update();

                javafx.scene.image.Image currentFrame = animator.getCurrentFrame();
                if (currentFrame != null) {
                    gc.drawImage(
                            currentFrame,
                            powerUp.getX() - GAME_AREA_X,
                            powerUp.getY(),
                            powerUp.getWidth(),
                            powerUp.getHeight()
                    );
                }
            }
        }
    }

    private SpriteAnimator createAnimator(PowerUp.PowerUpType type) {
        javafx.scene.image.Image[] frames = null;

        switch (type) {
            case MULTI_BALL:
                frames = AssetsManager.getFrames("MultiBall");
                break;
            case EXTENDED_PADDLE:
                frames = AssetsManager.getFrames("ExtendedPaddle");
                break;
            case LASER_PADDLE:
                frames = AssetsManager.getFrames("LaserPaddle");
                break;
            case STICKY_PADDLE:
                frames = AssetsManager.getFrames("StickyPaddle");
                break;
            case PIERCING_BALL:
                frames = AssetsManager.getFrames("PiercingBall");
                break;
        }

        if (frames != null && frames.length > 0) {
            return new SpriteAnimator(frames, 10);
        }

        System.err.println("Warning: No frames found for PowerUp type: " + type);
        return null;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void cleanup() {
        animators.clear();
    }
}