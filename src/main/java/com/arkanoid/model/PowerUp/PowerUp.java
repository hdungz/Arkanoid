package com.arkanoid.model.PowerUp;

import com.arkanoid.CONSTANT;
import javafx.geometry.Rectangle2D;

public class PowerUp {

    public enum PowerUpType {
        MULTI_BALL,
        EXTENDED_PADDLE,
        LASER_PADDLE,
        STICKY_PADDLE,
        PIERCING_BALL
    }

    private double x;
    private double y;
    private double width = 25;
    private double height = 25;
    private double velocityY = 150;
    private boolean active = true;
    private PowerUpType type;


    public PowerUp(double x, double y) {
        this.x = x - width / 2;
        this.y = y;

        PowerUpType[] types = PowerUpType.values();
        this.type = types[(int) (Math.random() * types.length)];
    }


    public PowerUp(double x, double y, PowerUpType type) {
        this.x = x - width / 2;
        this.y = y;
        this.type = type;
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

    public PowerUpType getType() {
        return type;
    }


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public static PowerUp spawnRandomWithChance(double x, double y, double chance) {
        if (Math.random() < chance) {
            return new PowerUp(x, y);
        }
        return null;
    }

    public static PowerUp spawnWithChance(double x, double y, PowerUpType type, double chance) {
        if (Math.random() < chance) {
            return new PowerUp(x, y, type);
        }
        return null;
    }
}