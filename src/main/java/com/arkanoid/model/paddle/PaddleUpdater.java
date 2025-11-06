package com.arkanoid.model.paddle;

import com.arkanoid.utils.SpriteManager;
import com.arkanoid.view.paddle.NormalPaddleRenderer;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;


public class PaddleUpdater {

    private static NormalPaddleRenderer paddleRenderer;


    public static void setPaddleRenderer(NormalPaddleRenderer renderer) {
        paddleRenderer = renderer;
    }


    public static void updatePaddleFromStore(Paddle paddle) {
        if (paddle == null) {
            System.err.println("✗ Paddle is null, cannot update");
            return;
        }

        PaddleType selectedPaddle = SpriteManager.getSelectedPaddle();

        double newWidth = selectedPaddle.getWidth();
        double newSpeed = selectedPaddle.getSpeed();

        double oldWidth = paddle.getWidth();
        double oldSpeed = paddle.getSpeed();

        paddle.setWidth(newWidth);
        paddle.setSpeed(newSpeed);

        paddle.setX((WINDOW_WIDTH - newWidth) / 2);

        if (paddleRenderer != null) {

            paddleRenderer.refreshPaddleAsset();
        }
    }
}