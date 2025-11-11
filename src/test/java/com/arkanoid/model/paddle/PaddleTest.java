package com.arkanoid.model.paddle;

import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class PaddleTest {

    private Paddle paddle;

    @BeforeEach
    void setUp() {
        paddle = new Paddle(PowerUpPaddleType.Normal);
    }

    @Test
    void testPaddleInitialization() {
        assertNotNull(paddle);
        assertTrue(paddle.getWidth() > 0);
        assertTrue(paddle.getHeight() > 0);
        assertTrue(paddle.getSpeed() > 0);
    }

    @Test
    void testResetPosition() {
        paddle.setX(100);
        paddle.setY(100);

        paddle.resetPosition();

        assertTrue(paddle.getX() >= 0);
        assertTrue(paddle.getY() > 0);
    }

    @Test
    void testGetBoundary() {
        paddle.setX(200);
        paddle.setY(500);
        paddle.setWidth(100);
        paddle.setHeight(20);

        Rectangle2D boundary = paddle.getBoundary();

        assertNotNull(boundary);
        assertEquals(200, boundary.getMinX(), 0.01);
        assertEquals(500, boundary.getMinY(), 0.01);
        assertEquals(100, boundary.getWidth(), 0.01);
        assertEquals(20, boundary.getHeight(), 0.01);
    }

    @Test
    void testMovingRight() {
        paddle.setX(200);
        double initialX = paddle.getX();

        paddle.movingRight(0.1);

        assertTrue(paddle.getX() > initialX);
    }

    @Test
    void testMoveWithRightFlag() {
        paddle.setX(200);
        paddle.setMovingLeft(false);
        paddle.setMovingRight(true);

        double initialX = paddle.getX();
        paddle.move(0.1);

        assertTrue(paddle.getX() > initialX);
    }

    @Test
    void testMoveWithNoFlags() {
        paddle.setX(200);
        paddle.setMovingLeft(false);
        paddle.setMovingRight(false);

        double initialX = paddle.getX();
        paddle.move(0.1);

        assertEquals(initialX, paddle.getX());
    }

    @Test
    void testGettersAndSetters() {
        paddle.setX(150);
        assertEquals(150, paddle.getX());

        paddle.setY(250);
        assertEquals(250, paddle.getY());

        paddle.setWidth(120);
        assertEquals(120, paddle.getWidth());

        paddle.setHeight(25);
        assertEquals(25, paddle.getHeight());

        paddle.setSpeed(5.5);
        assertEquals(5.5, paddle.getSpeed());
    }

    @Test
    void testMovementFlags() {
        paddle.setMovingLeft(true);
        assertTrue(paddle.isMovingLeft());

        paddle.setMovingLeft(false);
        assertFalse(paddle.isMovingLeft());

        paddle.setMovingRight(true);
        assertTrue(paddle.isMovingRight());

        paddle.setMovingRight(false);
        assertFalse(paddle.isMovingRight());
    }

    @Test
    void testPositiveSpeed() {
        paddle.setSpeed(10);
        assertTrue(paddle.getSpeed() > 0);
    }

    @Test
    void testPositiveDimensions() {
        paddle.setWidth(100);
        paddle.setHeight(20);

        assertTrue(paddle.getWidth() > 0);
        assertTrue(paddle.getHeight() > 0);
    }
}