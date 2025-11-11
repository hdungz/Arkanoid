package com.arkanoid.model.brick;

import com.arkanoid.model.brick.Brick;
import com.arkanoid.model.brick.BrickType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BrickTest {

    private Brick brick;
    private BrickType testType;

    @BeforeEach
    void setUp() {
        testType = BrickType.NORMAL;
        brick = new Brick(100, 50, 50, 60, 30, testType, 3);
    }

    @Test
    void testBrickInitialization() {
        assertNotNull(brick);
        assertEquals(100, brick.getX());
        assertEquals(50, brick.getY());
        assertEquals(60, brick.getWidth());
        assertEquals(30, brick.getHeight());
        assertEquals(3, brick.getHealth());
        assertTrue(brick.isVisible());
        assertEquals(testType, brick.getType());
    }

    @Test
    void testTakeDamageReducesHealth() {
        int initialHealth = brick.getHealth();

        boolean destroyed = brick.takeDamage();

        assertFalse(destroyed);
        assertEquals(initialHealth - 1, brick.getHealth());
        assertTrue(brick.isVisible());
    }

    @Test
    void testTakeDamageDestroysWhenHealthZero() {
        Brick weakBrick = new Brick(100, 50, 50, 60, 30, testType, 1);

        boolean destroyed = weakBrick.takeDamage();

        assertTrue(destroyed);
        assertEquals(0, weakBrick.getHealth());
        assertFalse(weakBrick.isVisible());
    }

    @Test
    void testMultipleDamageUntilDestroyed() {
        Brick brick3Health = new Brick(100, 50, 50, 60, 30, testType, 3);

        assertFalse(brick3Health.takeDamage());
        assertEquals(2, brick3Health.getHealth());

        assertFalse(brick3Health.takeDamage());
        assertEquals(1, brick3Health.getHealth());

        assertTrue(brick3Health.takeDamage());
        assertEquals(0, brick3Health.getHealth());
        assertFalse(brick3Health.isVisible());
    }

    @Test
    void testGetBoundary() {
        Rectangle2D boundary = brick.getBoundary();

        assertNotNull(boundary);
        assertEquals(100, boundary.getMinX(), 0.01);
        assertEquals(50, boundary.getMinY(), 0.01);
        assertEquals(60, boundary.getWidth(), 0.01);
        assertEquals(30, boundary.getHeight(), 0.01);
    }

    @Test
    void testSetVisible() {
        assertTrue(brick.isVisible());

        brick.setVisible(false);
        assertFalse(brick.isVisible());

        brick.setVisible(true);
        assertTrue(brick.isVisible());
    }

    @Test
    void testGettersAndSetters() {
        brick.setX(200);
        assertEquals(200, brick.getX());

        brick.setY(150);
        assertEquals(150, brick.getY());

        brick.setFinalY(100);
        assertEquals(100, brick.getFinalY());

        brick.setHealth(5);
        assertEquals(5, brick.getHealth());
    }

    @Test
    void testFinalY() {
        assertEquals(50, brick.getFinalY());

        brick.setFinalY(75);
        assertEquals(75, brick.getFinalY());
    }

    @Test
    void testBrickType() {
        assertEquals(testType, brick.getType());
    }

    @Test
    void testDimensionsGetters() {
        assertEquals(60, brick.getWidth());
        assertEquals(30, brick.getHeight());
    }

    @Test
    void testPositionGetters() {
        assertEquals(100, brick.getX());
        assertEquals(50, brick.getY());
    }

    @Test
    void testBrickWithZeroHealth() {
        Brick zeroBrick = new Brick(0, 0, 0, 60, 30, testType, 0);

        boolean destroyed = zeroBrick.takeDamage();

        assertTrue(destroyed);
        assertFalse(zeroBrick.isVisible());
    }
}