package com.arkanoid.model.PowerUp;

import com.arkanoid.model.PowerUp.PowerUp;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    private PowerUp powerUp;

    @BeforeEach
    void setUp() {
        powerUp = new PowerUp(100, 50);
    }

    @Test
    void testPowerUpInitialization() {
        assertNotNull(powerUp);
        assertTrue(powerUp.isActive());
        assertNotNull(powerUp.getType());
        assertEquals(25, powerUp.getWidth());
        assertEquals(25, powerUp.getHeight());
    }

    @Test
    void testPowerUpWithSpecificType() {
        PowerUp laserPowerUp = new PowerUp(200, 100, PowerUp.PowerUpType.LASER_PADDLE);

        assertEquals(PowerUp.PowerUpType.LASER_PADDLE, laserPowerUp.getType());
        assertTrue(laserPowerUp.isActive());
    }

    @Test
    void testUpdate() {
        double initialY = powerUp.getY();

        powerUp.update(0.1);

        assertTrue(powerUp.getY() > initialY);
        assertTrue(powerUp.isActive());
    }

    @Test
    void testUpdateDeactivatesWhenOffScreen() {
        powerUp.setY(1000);

        powerUp.update(1.0);

        assertFalse(powerUp.isActive());
    }

    @Test
    void testGetBoundary() {
        powerUp.setX(100);
        powerUp.setY(200);

        Rectangle2D boundary = powerUp.getBoundary();

        assertNotNull(boundary);
        assertEquals(100, boundary.getMinX(), 0.01);
        assertEquals(200, boundary.getMinY(), 0.01);
        assertEquals(25, boundary.getWidth(), 0.01);
        assertEquals(25, boundary.getHeight(), 0.01);
    }

    @Test
    void testCheckCollisionWithPaddle() {
        powerUp.setX(100);
        powerUp.setY(100);

        Rectangle2D paddleBoundary = new Rectangle2D(95, 95, 100, 20);

        assertTrue(powerUp.checkCollision(paddleBoundary));
    }

    @Test
    void testCheckCollisionNoOverlap() {
        powerUp.setX(100);
        powerUp.setY(100);

        Rectangle2D paddleBoundary = new Rectangle2D(500, 500, 100, 20);

        assertFalse(powerUp.checkCollision(paddleBoundary));
    }

    @Test
    void testCheckCollisionWhenInactive() {
        powerUp.setActive(false);

        Rectangle2D paddleBoundary = new Rectangle2D(powerUp.getX(), powerUp.getY(), 100, 20);

        assertFalse(powerUp.checkCollision(paddleBoundary));
    }

    @Test
    void testCollect() {
        assertTrue(powerUp.isActive());

        powerUp.collect();

        assertFalse(powerUp.isActive());
    }

    @Test
    void testUpdateDoesNothingWhenInactive() {
        powerUp.setActive(false);
        double initialY = powerUp.getY();

        powerUp.update(0.1);

        assertEquals(initialY, powerUp.getY());
    }

    @Test
    void testGettersAndSetters() {
        powerUp.setX(150);
        assertEquals(150, powerUp.getX());

        powerUp.setY(250);
        assertEquals(250, powerUp.getY());

        powerUp.setWidth(30);
        assertEquals(30, powerUp.getWidth());

        powerUp.setHeight(30);
        assertEquals(30, powerUp.getHeight());

        powerUp.setActive(false);
        assertFalse(powerUp.isActive());

        powerUp.setVelocityY(200);
        powerUp.update(0.1);
    }

    @Test
    void testAllPowerUpTypes() {
        PowerUp.PowerUpType[] types = PowerUp.PowerUpType.values();

        assertTrue(types.length > 0);

        for (PowerUp.PowerUpType type : types) {
            PowerUp testPowerUp = new PowerUp(100, 100, type);
            assertEquals(type, testPowerUp.getType());
        }
    }

    @Test
    void testSpawnRandomWithChance() {
        PowerUp noPowerUp = PowerUp.spawnRandomWithChance(100, 100, 0);
        assertNull(noPowerUp);

        PowerUp guaranteedPowerUp = PowerUp.spawnRandomWithChance(100, 100, 1.0);
        assertNotNull(guaranteedPowerUp);
        assertTrue(guaranteedPowerUp.isActive());
    }

    @Test
    void testSpawnWithChance() {
        PowerUp noPowerUp = PowerUp.spawnWithChance(100, 100, PowerUp.PowerUpType.LASER_PADDLE, 0);
        assertNull(noPowerUp);

        PowerUp guaranteedPowerUp = PowerUp.spawnWithChance(100, 100, PowerUp.PowerUpType.MULTI_BALL, 1.0);
        assertNotNull(guaranteedPowerUp);
        assertEquals(PowerUp.PowerUpType.MULTI_BALL, guaranteedPowerUp.getType());
    }

    @Test
    void testVelocityY() {
        powerUp.setVelocityY(100);
        powerUp.setY(50);

        powerUp.update(1.0);

        assertEquals(150, powerUp.getY(), 1.0);
    }

    @Test
    void testDimensions() {
        assertEquals(25, powerUp.getWidth());
        assertEquals(25, powerUp.getHeight());

        powerUp.setWidth(40);
        powerUp.setHeight(40);

        assertEquals(40, powerUp.getWidth());
        assertEquals(40, powerUp.getHeight());
    }
}