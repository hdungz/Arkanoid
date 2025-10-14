package com.arkanoid.model.brick;

import javafx.geometry.Rectangle2D;

public class Brick {
    private double x;
    private double y;
    private double width;
    private double height;

    protected boolean visible = true;
    private int health;
    private final BrickType type;

    public Brick(double x, double y, double width, double height, BrickType type,int health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.health = health;
    }

    public boolean takeDamage() {
        this.health--;
        if (health <= 0) {
            this.visible = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {this.x = x;}

    public double getY() {
        return y;
    }
    public void setY(double y) {this.y = y;}
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public BrickType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {this.health = health;}
}