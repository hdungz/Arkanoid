package com.arkanoid.model.paddle;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.arkanoid.CONSTANT.*;

public class LaserPaddle extends Paddle {

    private static final Duration POWERUP_DURATION = Duration.seconds(10);
    private static final Duration BLINK_START_TIME = POWERUP_DURATION.subtract(Duration.seconds(2));
    private static final Duration BLINK_INTERVAL = Duration.millis(150);
    private static final double AUTO_FIRE_INTERVAL = 0.5;

    private PauseTransition timer;
    private PauseTransition startBlinkingTimer;
    private Timeline blinkTimer;
    private boolean isBlinking = false;

    private List<Laser> lasers;
    private double timeSinceLastFire = 0;

    public LaserPaddle(double x, double y, boolean isMovingLeft, boolean isMovingRight,
                       PowerUpPaddleType paddleType, Consumer<Paddle> onRevert) {
        super(paddleType);

        this.setX(x);
        this.setY(y);
        this.setMovingLeft(isMovingLeft);
        this.setMovingRight(isMovingRight);

        this.lasers = new ArrayList<>();

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
        if (startBlinkingTimer != null) {
            startBlinkingTimer.stop();
        }
        stopBlinking();
    }

    private void autoFireLaser() {
        double leftLaserX = getX() + 10;
        double rightLaserX = getX() + getWidth() - 14;
        double laserY = getY() - 15;

        lasers.add(new Laser(leftLaserX, laserY));
        lasers.add(new Laser(rightLaserX, laserY));

        playLaserSound();
    }

    public void updateLasers(double deltaTime) {
        timeSinceLastFire += deltaTime;

        if (timeSinceLastFire >= AUTO_FIRE_INTERVAL) {
            autoFireLaser();
            timeSinceLastFire = 0;
        }

        for (Laser laser : lasers) {
            laser.update(deltaTime);
        }

        lasers.removeIf(laser -> !laser.isActive());
    }

    public List<Laser> getLasers() {
        return lasers;
    }

    public void removeLaser(Laser laser) {
        laser.setActive(false);
    }

    private void playLaserSound() {
        try {
            String soundPath = LaserPaddle.class.getResource("/com/arkanoid/music/laser-sound.mp3").toExternalForm();
            AudioClip laserSound = new AudioClip(soundPath);
            laserSound.setVolume(0.3);
            laserSound.play();
        } catch (Exception e) {
        }
    }
}