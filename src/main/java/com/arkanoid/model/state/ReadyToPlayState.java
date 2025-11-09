package com.arkanoid.model.state;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameState;
import com.arkanoid.utils.LevelTransitionManager;


public class ReadyToPlayState implements LevelTransitionState {
    private final GameModel gameModel;
    private final LevelTransitionManager manager;

    public ReadyToPlayState(GameModel gameModel, LevelTransitionManager manager) {
        this.gameModel = gameModel;
        this.manager = manager;
    }

    @Override
    public void enter() {
        gameModel.setGameState(GameState.Ready);
        manager.setTransitionComplete(true);
        System.out.println("Ready to Play - Press SPACE to start!");
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void exit() {
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getStateName() {
        return "READY_TO_PLAY";
    }
}