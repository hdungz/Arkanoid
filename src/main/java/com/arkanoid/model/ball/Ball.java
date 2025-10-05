package com.arkanoid.model.ball;

import com.arkanoid.model.paddle.Paddle;
import javafx.geometry.Rectangle2D;

public class Ball {
     private static final double BALL_RADIUS = 8;
     private static final double INITIAL_SPEED = 2.5;

     private double x, y;
     private double velocityX, velocityY;
     private double radius;
     private double speed;
     private boolean isLaunched;
     private double prevY;
     private double prevX;

     public Ball() {
         this.radius = BALL_RADIUS;
         this.speed = INITIAL_SPEED;
     }

     public void resetPosition(Paddle paddle) {
         isLaunched = false;
         x = paddle.getX() + paddle.getWidth() / 2;
         y = paddle.getY() - radius;
         velocityY = 0;
         velocityX = 0;
     }

     public void move() {
         prevX = x;
         prevY = y;
         x += velocityX * speed;
         y += velocityY * speed;
     }

     public Rectangle2D getBoundary() {
         return new Rectangle2D(x - radius, y - radius, 2 * radius, 2 * radius);
     }

    public void checkWallCollision(double leftWall, double rightWall, double topWall) {
        if (x - radius < leftWall) {
            x = leftWall + radius;  // Fixed: was just 'radius'
            velocityX = -velocityX;
        }
        if (x + radius > rightWall) {
            x = rightWall - radius;
            velocityX = -velocityX;
        }
        if (y - radius < topWall) {
            y = topWall + radius;  // Fixed: was just 'radius'
            velocityY = -velocityY;
        }
    }

    public void handlePaddleCollision(Paddle paddle) {
        System.out.println("Paddle collision");
        velocityY = -Math.abs(velocityY);
        double hitSpot = (x - (paddle.getX() + paddle.getWidth() / 2)) / (paddle.getWidth() / 2);
        velocityX = hitSpot;  // Changed from hitSpot * 5 to just hitSpot
    }

    public void handleBrickCollision(boolean isVerticalCollision) {
        System.out.println("Brick collision");
        if (isVerticalCollision) {
            velocityY = -velocityY;
        } else {
            velocityX = -velocityX;
        }
    }


    public void launch() {
        isLaunched = true;
        velocityX = 0;  // Added: start with no horizontal velocity
        velocityY = -1;  // Changed from -speed to -1 (direction only)
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

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Boolean getLaunched() {
        return isLaunched;
    }

    public void setLaunched(Boolean launched) {
        isLaunched = launched;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void setLaunched(boolean launched) {
        isLaunched = launched;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }
}
