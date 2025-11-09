package com.arkanoid.model.state;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.LevelTransitionManager;


public class PreBrickFallState implements LevelTransitionState {
    private final GameModel gameModel;
    private final LevelTransitionManager manager;
    private double elapsedTime = 0;
    private static final double DELAY_DURATION = 0.3;

    public PreBrickFallState(GameModel gameModel, LevelTransitionManager manager) {
        this.gameModel = gameModel;
        this.manager = manager;
    }

    @Override
    public void enter() {
        elapsedTime = 0;
        System.out.println("Pre-Brick Fall Delay");
    }

    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;
    }

    @Override
    public void exit() {
        System.out.println("Starting Brick Fall");
    }

    @Override
    public boolean isComplete() {
        return elapsedTime >= DELAY_DURATION;
    }

    @Override
    public String getStateName() {
        return "PRE_BRICK_FALL";
    }
}