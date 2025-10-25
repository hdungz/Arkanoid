package com.arkanoid.model.paddle;

import javafx.geometry.Rectangle2D;

public class Laser {
    private double x, y;
    private double width = 4;
    private double height = 15;
    private double speed = 400;
    private boolean active = true;

    public Laser(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(double deltaTime) {

        y -= speed * deltaTime;


        if (y + height < 0) {
            active = false;
        }
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}