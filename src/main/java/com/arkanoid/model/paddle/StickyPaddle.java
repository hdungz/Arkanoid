package com.arkanoid.model.paddle;

import com.arkanoid.model.ball.Ball;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.arkanoid.CONSTANT.*;

public class StickyPaddle extends Paddle {

    private static final Duration POWERUP_DURATION = Duration.seconds(15);
    private static final Duration BLINK_START_TIME = POWERUP_DURATION.subtract(Duration.seconds(2));
    private static final Duration BLINK_INTERVAL = Duration.millis(150);

    private PauseTransition timer;
    private PauseTransition startBlinkingTimer;
    private Timeline blinkTimer;
    private boolean isBlinking = false;

    private double arrowAngle = -90;
    private double arrowSpeed = 120;
    private boolean arrowDirection = true;
    private static final double ARROW_MIN_ANGLE = -150;
    private static final double ARROW_MAX_ANGLE = -30;

    private boolean ballStuck = false;

    public StickyPaddle(double x, double y, boolean isMovingLeft, boolean isMovingRight,
                        PowerUpPaddleType paddleType, Consumer<Paddle> onRevert) {
        super(paddleType);

        this.setX(x);
        this.setY(y);
        this.setMovingLeft(isMovingLeft);
        this.setMovingRight(isMovingRight);

        this.timer = new PauseTransition(POWERUP_DURATION);
        this.timer.setOnFinished(e -> {
            Paddle normalPaddle = new Paddle(PowerUpPaddleType.Normal);
            normalPaddle.setX(this.getX());
            normalPaddle.setY(this.getY());
            normalPaddle.setMovingLeft(this.isMovingLeft());
            normalPaddle.setMovingRight(this.isMovingRight());
            onRevert.accept(normalPaddle);
        });
//        this.timer.play();

        this.startBlinkingTimer = new PauseTransition(BLINK_START_TIME);
        startBlinkingTimer.setOnFinished(e -> startBlinking());
//        startBlinkingTimer.play();
    }

    private void startBlinking() {
        blinkTimer = new Timeline(
                new KeyFrame(BLINK_INTERVAL, e -> {
                    this.isBlinking = !this.isBlinking;
                })
        );
        blinkTimer.setCycleCount(Timeline.INDEFINITE);
        blinkTimer.play();
    }

    public void stopBlinking() {
        if (blinkTimer != null) {
            blinkTimer.stop();
        }
        this.isBlinking = false;
    }

    public boolean isBlinking() {
        return isBlinking;
    }

    @Override
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
        if (startBlinkingTimer != null) {
            startBlinkingTimer.stop();
        }
        stopBlinking();
    }

    public void updateArrow(double deltaTime) {
        if (!ballStuck) return;

        if (arrowDirection) {
            arrowAngle += arrowSpeed * deltaTime;
            if (arrowAngle >= ARROW_MAX_ANGLE) {
                arrowAngle = ARROW_MAX_ANGLE;
                arrowDirection = false;
            }
        } else {
            arrowAngle -= arrowSpeed * deltaTime;
            if (arrowAngle <= ARROW_MIN_ANGLE) {
                arrowAngle = ARROW_MIN_ANGLE;
                arrowDirection = true;
            }
        }
    }

    public void stickBall() {
        ballStuck = true;
    }

    public void launchBall() {
        ballStuck = false;
        stopTimer();
        stopBlinking();
    }

    public double getStuckBallX() {
        if (!ballStuck) return 0;
        return getX() + getWidth() / 2;
    }

    public double getStuckBallY() {
        if (!ballStuck) return 0;
        return getY() - BALL_RADIUS;
    }

    public double[] getLaunchVelocity() {
        double angleRad = Math.toRadians(arrowAngle);

        double vx = Math.cos(angleRad);
        double vy = Math.sin(angleRad);

        return new double[]{vx, vy};
    }

    public boolean isBallStuck() {
        return ballStuck;
    }

    public double getArrowAngle() {
        return arrowAngle;
    }

    public double getArrowX() {
        return getStuckBallX();
    }

    public double getArrowY() {
        return getStuckBallY();
    }
}