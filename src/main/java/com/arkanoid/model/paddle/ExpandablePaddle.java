package com.arkanoid.model.paddle;

import com.arkanoid.CONSTANT;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.arkanoid.CONSTANT.*;

public class ExpandablePaddle extends Paddle {

    private static final double EXPAND_FACTOR = 1.25;
    private static final Duration POWERUP_DURATION = Duration.seconds(4);
    private static final Duration BLINK_START_TIME = POWERUP_DURATION.subtract(Duration.seconds(1));
    private PauseTransition timer;

    private static final Duration BLINK_INTERVAL = Duration.millis(150);

    private PauseTransition startBlinkingTimer;
    private Timeline blinkTimer;
    private boolean isBlinking = false;


    public ExpandablePaddle(double x, double y, boolean isMovingLeft, boolean isMovingRight, PowerUpPaddleType paddleType, Consumer<Paddle> onRevert) {
        super(paddleType);
        setWidth(getWidth() * EXPAND_FACTOR);

        this.setX(x);
        this.setY(y);
        this.setMovingLeft(isMovingLeft);
        this.setMovingRight(isMovingRight);

        if(getX() + getWidth() > GAME_AREA_X + GAME_AREA_WIDTH) {
            setX(GAME_AREA_X + GAME_AREA_WIDTH - getWidth());
        }

        this.timer = new PauseTransition(POWERUP_DURATION);
        this.timer.setOnFinished(e -> {
            Paddle normalPaddle = new Paddle(PowerUpPaddleType.Normal);
            normalPaddle.setX(this.getX());
            normalPaddle.setY(this.getY());
            normalPaddle.setMovingLeft(this.isMovingLeft());
            normalPaddle.setMovingRight(this.isMovingRight());
            onRevert.accept(normalPaddle);
        });
        this.timer.play();

        this.startBlinkingTimer = new PauseTransition(BLINK_START_TIME);
        startBlinkingTimer.setOnFinished(e -> startBlinking());
        startBlinkingTimer.play();
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
    }



}
