package com.arkanoid.model.coin;

import com.arkanoid.CONSTANT;
import javafx.geometry.Rectangle2D;

public class Coin {
    private double x;
    private double y;
    private double width = 25;
    private double height = 25;
    private double velocityY = 120;
    private boolean active = true;
    private int value = 1;

    public Coin(double x, double y) {
        this.x = x - width / 2;
        this.y = y;
    }

    public void update(double deltaTime) {
        if (!active) return;


        y += velocityY * deltaTime;

        if (y > CONSTANT.WINDOW_HEIGHT) {
            active = false;
        }
    }


    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }


    public boolean checkCollision(Rectangle2D paddleBoundary) {
        if (!active) return false;
        return getBoundary().intersects(paddleBoundary);
    }


    public void collect() {
        active = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isActive() {
        return active;
    }

    public int getValue() {
        return value;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }


    public static Coin spawnWithChance(double x, double y, double chance) {
        if (Math.random() < chance) {
            return new Coin(x, y);
        }
        return null;
    }
}