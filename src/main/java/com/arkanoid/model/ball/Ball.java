package com.arkanoid.model.ball;

import com.arkanoid.model.paddle.Paddle;

public class Ball {
     private static final double BALL_RADIUS = 8;
     private static final double INITIAL_SPEED = 4.0;

     private double x, y;
     private double velocityX, velocityY;
     private double radius;
     private double speed;
     private Boolean isLaunched;

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


}
