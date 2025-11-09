package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameState;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.view.GameView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController implements BaseController {
    private final GameModel gameModel;
    private final GameView gameView;
    private final PauseController pauseController;
    private final GameOverController gameOverController;
    private Scene scene;

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.pauseController = new PauseController(gameModel, gameView.getPauseView());
        this.gameOverController = new GameOverController(gameModel, gameView.getGameOverView());

        setupGameOverCallbacks();
    }

    private void setupGameOverCallbacks() {
        gameOverController.setOnRetry(() -> {
            System.out.println("Retrying game...");
            gameView.synchronizeView();
        });


    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void onEnterScene() {
        if (scene != null) {
            scene.setOnKeyPressed(this::handleKeyPressed);
            scene.setOnKeyReleased(this::handleKeyReleased);
        }
        gameModel.loadCurrentLevel();
        gameView.synchronizeView();
        pauseController.forceHide();
        gameOverController.hide();
        System.out.println("Entering Gameplay Scene");
    }

    @Override
    public void onExitScene() {
        if (scene != null) {
            scene.setOnKeyPressed(null);
            scene.setOnKeyReleased(null);
        }
        pauseController.forceHide();
        gameOverController.hide();
        System.out.println("Exiting Gameplay Scene");
    }

    public void checkGameOver() {
        if (gameModel.getGameState() == GameState.GameOver) {

            int currentLevel = gameModel.getCurrentLevel();
            if (currentLevel > 20) {

                gameView.getGameOverView().setGameComplete(true);
            } else {

                gameView.getGameOverView().setGameComplete(false);
            }
            gameView.showGameOver();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();

        // Check if game over
        if (gameModel.getGameState() == GameState.GameOver) {
            return;
        }

        if (keyCode == KeyCode.ESCAPE || keyCode == KeyCode.P) {
            pauseController.togglePause();
            return;
        }

        if (pauseController.isPaused()) {
            return;
        }

        Paddle paddle = gameModel.getPaddle();

        if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(true);
        } else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(true);
        } else if (keyCode == KeyCode.SPACE) {
            gameModel.launchBall();
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        if (gameModel.getGameState() == GameState.GameOver || pauseController.isPaused()) {
            return;
        }

        KeyCode keyCode = keyEvent.getCode();
        Paddle paddle = gameModel.getPaddle();

        if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(false);
        } else if (keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(false);
        }
    }

    public PauseController getPauseController() {
        return pauseController;
    }

    public GameOverController getGameOverController() {
        return gameOverController;
    }
}