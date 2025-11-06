package com.arkanoid.utils;

import com.arkanoid.model.ball.BallType;
import com.arkanoid.model.paddle.PaddleType;
import com.arkanoid.view.paddle.NormalPaddleRenderer;

public class SpriteManager {

    private static PaddleType selectedPaddle = PaddleType.Default;
    private static BallType selectedBall = BallType.Default;

    public static void initialize(int paddleIndex, int ballIndex) {
        setPaddleByIndex(paddleIndex);
        setBallByIndex(ballIndex);
    }



    public static void setSelectedPaddle(PaddleType paddleType) {
        if (paddleType != null && !selectedPaddle.equals(paddleType)) {
            PaddleType oldPaddle = selectedPaddle;
            selectedPaddle = paddleType;
            System.out.println("JFKSJFK: "+ selectedPaddle);
        }
    }

    public static PaddleType getSelectedPaddle() {
        return selectedPaddle;

    }

    public static void setPaddleByIndex(int index) {
        PaddleType newPaddle = getPaddleByIndex(index);
        setSelectedPaddle(newPaddle);
    }

    public static PaddleType getPaddleByIndex(int index) {
        return switch (index) {
            case 0 -> PaddleType.Default;
            case 1 -> PaddleType.GreenHell;
            case 2 -> PaddleType.ForestIce;
            default -> PaddleType.Default;
        };
    }


    public static BallType getSelectedBall() {
        return selectedBall;
    }

    public static void setSelectedBall(BallType ballType) {
        if (ballType != null && !selectedBall.equals(ballType)) {
            BallType oldBall = selectedBall;
            selectedBall = ballType;
            System.out.println("✓ Ball changed: " + oldBall.getName() +
                    " → " + selectedBall.getName());
        }
    }

    public static void setBallByIndex(int index) {
        BallType newBall = getBallByIndex(index);
        setSelectedBall(newBall);

    }
    public static BallType getBallByIndex(int index) {
        return switch (index) {
            case 0 -> BallType.Default;
            case 1 -> BallType.BasketBall;
            default -> BallType.Default;
        };
    }



}