package com.arkanoid.model.state;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.LevelTransitionManager;

public class LevelIntroState implements LevelTransitionState {
    private final GameModel gameModel;
    private final LevelTransitionManager manager;
    private double elapsedTime = 0;
    private static final double INTRO_DURATION = 2.5;

    public LevelIntroState(GameModel gameModel, LevelTransitionManager manager) {
        this.gameModel = gameModel;
        this.manager = manager;
    }

    @Override
    public void enter() {
        elapsedTime = 0;
        manager.setIntroActive(true);
        manager.setIntroAlpha(0);
        manager.setCurrentLevel(gameModel.getCurrentLevel());
        System.out.println("Level Intro Started - Level " + manager.getCurrentLevel());
    }

    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime < 0.5) {
            manager.setIntroAlpha(elapsedTime / 0.5);
        } else if (elapsedTime < 2.0) {
            manager.setIntroAlpha(1.0);
        } else {
            manager.setIntroAlpha(1.0 - (elapsedTime - 2.0) / 0.5);
        }

        manager.updateGlitch(deltaTime);
    }

    @Override
    public void exit() {
        manager.setIntroActive(false);
        System.out.println("Level Intro Completed");
    }

    @Override
    public boolean isComplete() {
        return elapsedTime >= INTRO_DURATION;
    }

    @Override
    public String getStateName() {
        return "LEVEL_INTRO";
    }
}