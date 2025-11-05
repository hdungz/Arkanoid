package com.arkanoid.utils;

import com.arkanoid.Coin.CoinStorage;
import com.arkanoid.model.coin.Coin;
import com.arkanoid.model.GameModel;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoinManager {
    private List<Coin> coins;
    private GameModel gameModel;

    private double spawnChance = 1;

    private int sessionCoins = 0;

    public CoinManager(GameModel gameModel) {
        this.gameModel = gameModel;
        this.coins = new ArrayList<>();
    }

    public void update(double deltaTime) {
        Iterator<Coin> iterator = coins.iterator();

        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            coin.update(deltaTime);

            if (!coin.isActive()) {
                iterator.remove();
                continue;
            }

            if (coin.checkCollision(gameModel.getPaddle().getBoundary())) {
                onCoinCollected(coin);
                coin.collect();
                iterator.remove();
            }
        }
    }

    public void spawnCoin(double x, double y) {
        Coin coin = Coin.spawnWithChance(x, y, spawnChance);
        if (coin != null) {
            coins.add(coin);
        }
    }

    private void onCoinCollected(Coin coin) {
        int coinValue = coin.getValue();
        sessionCoins += coinValue;
        CoinStorage.addCoins(coinValue);
        gameModel.addScore(coinValue * 5);
    }

    public int getSessionCoins() {
        return sessionCoins;
    }

    public void resetSessionCoins() {
        sessionCoins = 0;
    }

    public void clear() {
        coins.clear();
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setSpawnChance(double chance) {
        this.spawnChance = Math.max(0.0, Math.min(1.0, chance));
    }

    public double getSpawnChance() {
        return spawnChance;
    }

    public int getActiveCoinCount() {
        return coins.size();
    }
}