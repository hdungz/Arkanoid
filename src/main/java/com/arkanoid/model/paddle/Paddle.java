package com.arkanoid.model.paddle;
import com.arkanoid.CONSTANT;
import javafx.animation.PauseTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import static com.arkanoid.CONSTANT.*;

public class Paddle {



    private double speed;
    private double width, height;
    private double x, y;
    private boolean glowing = false;
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;

    public Paddle() {
        this.speed = PADDLE_SPEED;
        this.width = PADDLE_WIDTH;
        this.height = PADDLE_HEIGHT;
    }

    public void resetPosition() {
        x = (WINDOW_WIDTH - width) / 2;
        y = WINDOW_HEIGHT - height - 30;
    }

    public void move(double deltaTime ) {
        if(isMovingLeft) {
            movingLeft(deltaTime);
        }
        if(isMovingRight) {
            movingRight(deltaTime);
        }
    }

    public void movingLeft(double deltaTime) {
        if(x - speed > GAME_AREA_X ) x -= speed * deltaTime * 144;
        else x = (GAME_AREA_X);
    }

    public void movingRight(double deltaTime) {
        if(x + speed + this.width <= GAME_AREA_X + CONSTANT.GAME_AREA_WIDTH) x += speed * deltaTime * 144 ;
        else x = GAME_AREA_X + CONSTANT.GAME_AREA_WIDTH - this.width;
    }
    public void onBallHit() {
        playHitSound();

    }

    private void playHitSound() {
        try {
            String soundPath = getClass().getResource("/com/arkanoid/music/waterdrop-on-metal-406648.mp3").toExternalForm();
            AudioClip hitSound = new AudioClip(soundPath);
            hitSound.play();
        } catch (Exception e) {
            System.out.println("Không thể phát âm thanh paddle: " + e.getMessage());
        }
    }

    // ✨ Hiệu ứng sáng nhẹ
    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

}
