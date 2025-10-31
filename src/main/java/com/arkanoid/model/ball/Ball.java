package com.arkanoid.model.ball;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameModel.WallCollisionSide;
import com.arkanoid.model.paddle.Paddle;
import javafx.geometry.Rectangle2D;
import static com.arkanoid.CONSTANT.*;

public class Ball {

    private double x, y;
    private double velocityX, velocityY;
    private double radius;
    private double speed;
    private boolean isLaunched;
    private double prevY;
    private double prevX;
    private double damage;
    private double hitSpotMultiplier;
    private Paddle paddle ;
    private boolean isVisible = true;
    private boolean blink = false;
    private int PierceBall = GameModel.getInstance().getCheckpierce();

    public Ball() {
        this.radius = BALL_RADIUS;
        this.speed = INITIAL_SPEED;
        this.damage = BRICK_DAMAGE;
        this.hitSpotMultiplier = HITSPOT_MULTIPLIER;
    }

    public Ball(double x, double y, double radius, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.speed = INITIAL_SPEED;
        this.damage = BRICK_DAMAGE;
        this.hitSpotMultiplier = HITSPOT_MULTIPLIER;
        this.isLaunched = true;
        System.out.println(PierceBall);
    }

    public void resetPosition(Paddle paddle) {
        isLaunched = false;
        x = paddle.getX() + paddle.getWidth() / 2;
        y = paddle.getY() - radius;
        velocityY = 0;
        velocityX = 0;
    }

    public void move(double deltaTime) {
        prevX = x;
        prevY = y;
        x += velocityX * speed * deltaTime * 144;
        y += velocityY * speed * deltaTime * 144;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public void checkWallCollision(double leftWall, double rightWall, double topWall, GameModel gameModel) {
        if (x - radius < leftWall) {
            x = leftWall + radius;
            velocityX = -velocityX;
            reflectionAngleAdjustment();
            gameModel.setLastWallCollision(WallCollisionSide.LEFT);
            Paddle.onBallHit();
        }
        if (x + radius > rightWall) {
            x = rightWall - radius;
            velocityX = -velocityX;
            reflectionAngleAdjustment();
            gameModel.setLastWallCollision(WallCollisionSide.RIGHT);
            Paddle.onBallHit();
        }
        if (y - radius < topWall) {
            y = topWall + radius;
            velocityY = -velocityY;
            reflectionAngleAdjustment();
            gameModel.setLastWallCollision(WallCollisionSide.TOP);
            Paddle.onBallHit();
        }
    }

    private void reflectionAngleAdjustment() {
        double minAngle = 15.0;
        double minAngleRad = Math.toRadians(minAngle);
        double angle = Math.atan2(Math.abs(velocityY), Math.abs(velocityX));
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        if (angle < minAngleRad) {
            angle = minAngleRad;
            velocityX = Math.signum(velocityX) * speed * Math.cos(angle);
            velocityY = Math.signum(velocityY) * speed * Math.sin(angle);
        } else if (angle > Math.PI / 2 - minAngleRad) {
            angle = Math.PI / 2 - minAngleRad;
            velocityX = Math.signum(velocityX) * speed * Math.cos(angle);
            velocityY = Math.signum(velocityY) * speed * Math.sin(angle);
        }
    }

    public void handlePaddleCollision(Paddle paddle) {
        double paddleLeft = paddle.getX();
        double paddleRight = paddle.getX() + paddle.getWidth();
        double paddleTop = paddle.getY();
        double paddleBottom = paddle.getY() + paddle.getHeight();

        double ballCenterX = x;
        double ballCenterY = y;
        double ballPrevY = prevY;
        double ballPrevX = prevX;

        boolean wasAbovePaddle = (ballPrevY + radius) <= paddleTop;
        boolean wasLeftOfPaddle = (ballPrevX + radius) <= paddleLeft;
        boolean wasRightOfPaddle = (ballPrevX - radius) >= paddleRight;

        if (wasAbovePaddle) {
            handleTopCollision(paddle);
        } else if (wasLeftOfPaddle || wasRightOfPaddle) {
            handleSideCollision(paddle, wasLeftOfPaddle);
        } else {
            double distanceToTop = Math.abs(ballCenterY - paddleTop);
            double distanceToLeft = Math.abs(ballCenterX - paddleLeft);
            double distanceToRight = Math.abs(ballCenterX - paddleRight);

            if (distanceToTop < Math.min(distanceToLeft, distanceToRight)) {
                handleTopCollision(paddle);
            } else {
                handleSideCollision(paddle, distanceToLeft < distanceToRight);
            }
        }
    }

    private void handleTopCollision(Paddle paddle) {
        velocityY = -Math.abs(velocityY);
        double hitSpot = (x - (paddle.getX() + paddle.getWidth() / 2)) / (paddle.getWidth() / 2);
        velocityX = hitSpot * hitSpotMultiplier;
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        double targetSpeed = 1;
        velocityX = velocityX * targetSpeed / currentSpeed;
        velocityY = velocityY * targetSpeed / currentSpeed;
        y = paddle.getY() - radius;
    }

    private void handleSideCollision(Paddle paddle, boolean hitLeftSide) {
        velocityY = Math.abs(velocityY);
        if (hitLeftSide) {
            velocityX = -Math.abs(velocityX);
            x = paddle.getX() - radius;
        } else {
            velocityX = Math.abs(velocityX);
            x = paddle.getX() + paddle.getWidth() + radius;
        }

        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        double targetSpeed = 1;
        velocityX = velocityX * targetSpeed / currentSpeed;
        velocityY = velocityY * targetSpeed / currentSpeed;
    }

    public void handleBrickCollision(boolean isVerticalCollision) {
        if (isVerticalCollision) velocityY = -velocityY;
        else velocityX = -velocityX;
    }

    public void launch() {
        isLaunched = true;
        velocityX = 0;
        velocityY = -1;
    }

    // Getters và Setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getVelocityX() { return velocityX; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public Boolean getLaunched() { return isLaunched; }
    public void setLaunched(Boolean launched) { isLaunched = launched; }
    public boolean isLaunched() { return isLaunched; }
    public double getPrevY() { return prevY; }
    public void setPrevY(double prevY) { this.prevY = prevY; }
    public double getPrevX() { return prevX; }
    public void setPrevX(double prevX) { this.prevX = prevX; }
    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }
    public double getHitSpotMultiplier() { return hitSpotMultiplier; }
    public void setHitSpotMultiplier(double hitSpotMultiplier) { this.hitSpotMultiplier = hitSpotMultiplier; }
    public int getPierceBall() {  return PierceBall; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public boolean isBlink() { return blink; }
    public void setBlink(boolean setBlink) { this.blink = blink; }
}
