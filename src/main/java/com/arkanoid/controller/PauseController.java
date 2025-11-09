package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.PauseView;

public class PauseController {
    private final GameModel gameModel;
    private final PauseView pauseView;
    private boolean isPaused = false;

    public PauseController(GameModel gameModel, PauseView pauseView) {
        this.gameModel = gameModel;
        this.pauseView = pauseView;
        initializeButtons();
    }

    private void initializeButtons() {
        pauseView.getResumeButton().setOnAction(e -> resumeGame());
        pauseView.getLevelButton().setOnAction(e -> returnToLevelSelection());
        pauseView.getMenuButton().setOnAction(e -> returnToMenu());
    }

    public void togglePause() {
        isPaused = !isPaused;
        gameModel.setPaused(isPaused);

        if (isPaused) {
            pauseView.show();
        } else {
            pauseView.hide();
        }
    }

    public void resumeGame() {
        isPaused = false;
        gameModel.setPaused(false);
        pauseView.hide();
    }

    public void returnToLevelSelection() {
        isPaused = false;
        gameModel.setPaused(false);
        pauseView.hide();
        SceneManager.getInstance().switchTo(SceneType.LevelSelection);
    }

    public void returnToMenu() {
        isPaused = false;
        gameModel.setPaused(false);
        pauseView.hide();
        SceneManager.getInstance().switchTo(SceneType.Menu);
    }

    public void forceHide() {
        isPaused = false;
        pauseView.hide();
    }

    public boolean isPaused() {
        return isPaused;
    }
}