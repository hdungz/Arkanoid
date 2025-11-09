package com.arkanoid.utils;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
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
        if (powerUp != null) {
            powerUps.add(powerUp);
            System.out.println("PowerUp spawned: " + powerUp.getType());
        }
    }

    public void spawnPowerUp(double x, double y, PowerUp.PowerUpType type) {
        PowerUp powerUp = new PowerUp(x, y, type);
        powerUps.add(powerUp);
        System.out.println("PowerUp spawned: " + type);
    }

    private void onPowerUpCollected(PowerUp powerUp) {
        System.out.println("PowerUp collected: " + powerUp.getType());

        switch (powerUp.getType()) {
            case MULTI_BALL:
                activateMultiBall();
                break;
            case EXTENDED_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.ExpandablePaddle);
                break;
            case LASER_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.LaserPaddle);
                break;
            case STICKY_PADDLE:
                gameModel.activateSpecialPaddle(PowerUpPaddleType.StickyPaddle);
                break;
            case PIERCING_BALL:
                activatePiercingBall();
                break;
        }
    }

    private void activatePiercingBall() {
        gameModel.setCheckPierce(1);
        gameModel.setPierceTimer(5); // Active for 10 seconds
        System.out.println("Piercing Ball activated! Duration: 10 seconds");
    }

    private void activateMultiBall() {
        Ball mainBall = gameModel.getBall();
        ArrayList<Ball> extraBalls = gameModel.getExtraBalls();

        List<Ball> activeBalls = new ArrayList<>();

        if (mainBall.isVisible() && mainBall.isLaunched()) {
            activeBalls.add(mainBall);
        }

        for (Ball ball : extraBalls) {
            if (ball.isVisible()) {
                activeBalls.add(ball);
            }
        }

        if (activeBalls.isEmpty()) {
            System.out.println("No active balls to split!");
            return;
        }

        for (Ball sourceBall : activeBalls) {
            Ball ball2 = new Ball(
                    sourceBall.getX(),
                    sourceBall.getY(),
                    sourceBall.getRadius(),
                    -0.6,
                    -0.8
            );
            ball2.setSpeed(sourceBall.getSpeed());
            ball2.setVisible(true);

            Ball ball3 = new Ball(
                    sourceBall.getX(),
                    sourceBall.getY(),
                    sourceBall.getRadius(),
                    0.6,
                    -0.8
            );
            ball3.setSpeed(sourceBall.getSpeed());
            ball3.setVisible(true);

            gameModel.addBall(ball2);
            gameModel.addBall(ball3);
        }

        System.out.println("Multi-ball activated! Total balls: " + gameModel.getTotalBallCount());
    }

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