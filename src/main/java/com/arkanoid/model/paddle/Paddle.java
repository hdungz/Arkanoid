package com.arkanoid.model.paddle;
import com.arkanoid.main;

public class Paddle {
    private static final double PADDLE_WIDTH = 120;
    private static final double PADDLE_HEIGHT = 20;
    private static final double PADDLE_SPEED = 8.0;

    private double speed;
    private double width, height;
    private double x, y;

    public Paddle() {
        this.speed = PADDLE_SPEED;
        this.width = PADDLE_WIDTH;
        this.height = PADDLE_HEIGHT;
    }

    public void resetPosition() {
        x = (main.WINDOW_WIDTH - width) / 2;
        y = main.WINDOW_HEIGHT - height - 30;
    }

    public void movingLeft() {
        if(x - speed > 0) x -= speed;
        else x = 0;
    }

    public void movingRight() {
        if(x + speed <= main.WINDOW_WIDTH - width) x += speed;
        else x = main.WINDOW_WIDTH - width;
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
}
