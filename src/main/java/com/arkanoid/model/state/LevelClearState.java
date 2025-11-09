package com.arkanoid.model.state;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.LevelTransitionManager;

public class LevelClearState implements LevelTransitionState {
    private final GameModel gameModel;
    private final LevelTransitionManager manager;
    private double elapsedTime = 0;
    private static final double CLEAR_DURATION = 2.5;

    public LevelClearState(GameModel gameModel, LevelTransitionManager manager) {
        this.gameModel = gameModel;
        this.manager = manager;
    }

    @Override
    public void enter() {
        elapsedTime = 0;
        manager.setClearActive(true);
        manager.setClearAlpha(0);
        System.out.println("Level Clear!");
    }

    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime < 0.2) {
            manager.setClearAlpha(elapsedTime / 0.5);
        } else if (elapsedTime < 2.0) {
            manager.setClearAlpha(1.0);
        } else {
            manager.setClearAlpha(1.0 - (elapsedTime - 2.0) / 0.5);
        }

        manager.updateGlitch(deltaTime);
    }

    @Override
    public void exit() {
        manager.setClearActive(false);
        manager.setReadyForNextLevel(true);
        System.out.println("Level Clear Complete - Ready for next level");
    }

    @Override
    public boolean isComplete() {
        return elapsedTime >= CLEAR_DURATION;
    }

    @Override
    public String getStateName() {
        return "LEVEL_CLEAR";
    }
}