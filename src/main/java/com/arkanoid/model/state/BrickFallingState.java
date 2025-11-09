package com.arkanoid.model.state;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.LevelTransitionManager;

public class BrickFallingState implements LevelTransitionState {
    private final GameModel gameModel;
    private final LevelTransitionManager manager;

    public BrickFallingState(GameModel gameModel, LevelTransitionManager manager) {
        this.gameModel = gameModel;
        this.manager = manager;
    }

    @Override
    public void enter() {
        gameModel.setBrickFalling(true);
        System.out.println("Bricks Falling");
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void exit() {
        gameModel.setBrickFalling(false);
        System.out.println("Bricks Fall Complete");
    }

    @Override
    public boolean isComplete() {
        return !gameModel.isBrickFalling();
    }

    @Override
    public String getStateName() {
        return "BRICK_FALLING";
    }
}