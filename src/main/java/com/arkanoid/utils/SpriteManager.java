package com.arkanoid.utils;

import com.arkanoid.model.ball.BallType;
import com.arkanoid.model.paddle.PaddleType;

public class SpriteManager {
    private static PaddleType selectedPaddle = PaddleType.Default;
    private static BallType selectedBall = BallType.Default;

    // --- Paddle ---
    public static PaddleType getSelectedPaddle() {
        return selectedPaddle;
    }

    public static void setSelectedPaddle(PaddleType paddleType) {
        if (paddleType != null) {
            selectedPaddle = paddleType;
        }
    }

    // --- Ball ---
    public static BallType getSelectedBall() {
        return selectedBall;
    }

    public static void setSelectedBall(BallType ballType) {
        if (ballType != null) {
            selectedBall = ballType;
        }
    }
}
