package com.arkanoid.utils;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import com.arkanoid.model.PowerUp.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerUpManager {
    private List<PowerUp> powerUps;
    private GameModel gameModel;

    private double spawnChance = 1;

    public PowerUpManager(GameModel gameModel) {
        this.gameModel = gameModel;
        this.powerUps = new ArrayList<>();
    }

    public void update(double deltaTime) {
        Iterator<PowerUp> iterator = powerUps.iterator();

        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update(deltaTime);

            if (!powerUp.isActive()) {
                iterator.remove();
                continue;
            }

            if (powerUp.checkCollision(gameModel.getPaddle().getBoundary())) {
                onPowerUpCollected(powerUp);
                powerUp.collect();
                iterator.remove();
            }
        }
    }

    public void spawnPowerUp(double x, double y) {
        PowerUp powerUp = PowerUp.spawnRandomWithChance(x, y, spawnChance);
        System.out.println("PowerUp spawned:");
        if (powerUp != null) {
            powerUps.add(powerUp);
        }
    }

    public void spawnPowerUp(double x, double y, PowerUp.PowerUpType type) {
        PowerUp powerUp = new PowerUp(x, y, type);
        powerUps.add(powerUp);
    }


    private void onPowerUpCollected(PowerUp powerUp) {
        switch (powerUp.getType()) {
//            case mulcto:
//                activateMultiBall();
//                break;
            case EXTENDED_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.ExpandablePaddle);
                break;
            case STICKY_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.LaserPaddle);
                break;
            case LASER_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.StickyPaddle);
                break;
        }
    }


//    private void activateMultiBall() {
//        gameModel.setCheckPierce(1);
//        gameModel.setPierceTimer(2.5);
//    }


    public void clear() {
        powerUps.clear();
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setSpawnChance(double chance) {
        this.spawnChance = Math.max(0.0, Math.min(1.0, chance));
    }

    public double getSpawnChance() {
        return spawnChance;
    }

    public int getActivePowerUpCount() {
        return powerUps.size();
    }
}