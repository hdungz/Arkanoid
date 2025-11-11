package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameState;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.GameOverView;

public class GameOverController {
    private final GameModel gameModel;
    private final GameOverView gameOverView;
    private Runnable onRetry;
    private Runnable onMenu;

    public GameOverController(GameModel gameModel, GameOverView gameOverView) {
        this.gameModel = gameModel;
        this.gameOverView = gameOverView;
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        gameOverView.getRetryButton().setOnAction(e -> handleRetry());
        gameOverView.getMenuButton().setOnAction(e -> handleMenu());
    }

    private void handleRetry() {
        System.out.println("Retry button clicked");
        gameOverView.hide();

        // Reset game state
        gameModel.setLives(3);
        gameModel.setScore(0);
        gameModel.setCurrentLevel(0);
        gameModel.setGameState(GameState.Ready);
        gameModel.clearExtraBalls();
        gameModel.getBall().resetPosition(gameModel.getPaddle());
        gameModel.getPaddle().resetPosition();
        gameModel.loadCurrentLevel();

        if (onRetry != null) {
            onRetry.run();
        }
    }

    private void handleMenu() {
        System.out.println("Menu button clicked from Game Over");
        gameOverView.hide();


        SceneManager.getInstance().switchTo(SceneType.Menu, () -> {
            System.out.println("Returned to menu from Game Over");


            if (onMenu != null) {
                onMenu.run();
            }
        });
    }

    public void show() {
        gameOverView.setScore(gameModel.getScore());
        gameOverView.show();
    }

    public void hide() {
        gameOverView.hide();
    }

    public void setOnRetry(Runnable callback) {
        this.onRetry = callback;
    }

    public void setOnMenu(Runnable callback) {
        this.onMenu = callback;
    }
}