package com.arkanoid.model.ball;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BallTest {

    private Ball ball;
    private Paddle paddle;

    @BeforeEach
    void setUp() {
        ball = new Ball();
        paddle = new Paddle(PowerUpPaddleType.Normal);
    }

    @Test
    void testBallInitialization() {
        assertNotNull(ball);
        assertEquals(0, ball.getX());
        assertEquals(0, ball.getY());
        assertFalse(ball.isLaunched());
    }

    @Test
    void testBallConstructorWithParameters() {
        Ball customBall = new Ball(100, 200, 10, 5, -5);

        assertEquals(100, customBall.getX());
        assertEquals(200, customBall.getY());
        assertEquals(10, customBall.getRadius());
        assertEquals(5, customBall.getVelocityX());
        assertEquals(-5, customBall.getVelocityY());
        assertTrue(customBall.isLaunched());
    }

    @Test
    void testLaunch() {
        assertFalse(ball.isLaunched());

        ball.launch();

        assertTrue(ball.isLaunched());
        assertEquals(0, ball.getVelocityX());
        assertEquals(-1, ball.getVelocityY());
    }

    @Test
    void testMove() {
        ball.setX(100);
        ball.setY(100);
        ball.setVelocityX(1);
        ball.setVelocityY(1);
        ball.setSpeed(1);

        double initialX = ball.getX();
        double initialY = ball.getY();

        ball.move(0.1);

        assertNotEquals(initialX, ball.getX());
        assertNotEquals(initialY, ball.getY());
    }

    @Test
    void testGetBoundary() {
        ball.setX(100);
        ball.setY(100);
        ball.setRadius(10);

        Rectangle2D boundary = ball.getBoundary();

        assertNotNull(boundary);
        assertEquals(90, boundary.getMinX(), 0.01);
        assertEquals(90, boundary.getMinY(), 0.01);
        assertEquals(20, boundary.getWidth(), 0.01);
        assertEquals(20, boundary.getHeight(), 0.01);
    }

    @Test
    void testResetPosition() {
        paddle.setX(200);
        paddle.setY(500);
        paddle.setWidth(100);

        ball.setX(50);
        ball.setY(50);
        ball.setLaunched(true);
        ball.setVelocityX(5);
        ball.setVelocityY(5);

        ball.resetPosition(paddle);

        assertFalse(ball.isLaunched());
        assertEquals(0, ball.getVelocityX());
        assertEquals(0, ball.getVelocityY());
    }

    @Test
    void testHandleBrickCollisionVertical() {
        ball.setVelocityY(5);

        ball.handleBrickCollision(true);

        assertEquals(-5, ball.getVelocityY());
    }

    @Test
    void testHandleBrickCollisionHorizontal() {
        ball.setVelocityX(5);

        ball.handleBrickCollision(false);

        assertEquals(-5, ball.getVelocityX());
    }

    @Test
    void testPreviousPositionTracking() {
        ball.setPrevX(50);
        ball.setPrevY(60);

        assertEquals(50, ball.getPrevX());
        assertEquals(60, ball.getPrevY());
    }

    @Test
    void testVelocitySetters() {
        ball.setVelocityX(3.5);
        ball.setVelocityY(-4.5);

        assertEquals(3.5, ball.getVelocityX());
        assertEquals(-4.5, ball.getVelocityY());
    }
}