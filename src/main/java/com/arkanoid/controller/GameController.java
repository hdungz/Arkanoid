package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.arkanoid.view.GameView;
import javafx.scene.Node;
import java.security.KeyStore;

public class GameController implements BaseController {
    private final GameModel gameModel;
    private final GameView gameView;
    private Scene scene;

    public GameController(GameModel gameModel,GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void onEnterScene() {
        if (scene != null) {
            scene.setOnKeyPressed(this::handleKeyPressed);
            scene.setOnKeyReleased(this::handleKeyReleased);
        }
        // Load level được chọn từ LevelManager
        gameModel.loadCurrentLevel();
        gameView.synchronizeView();
        System.out.println("Entering Gameplay Scene");
    }

    public void onExitScene() {
        if (scene != null) {
            scene.setOnKeyPressed(null);
            scene.setOnKeyReleased(null);
        }
        System.out.println("Exiting Gameplay Scene");
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        Paddle paddle = gameModel.getPaddle(); // Get current paddle from model

        if(keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(true);
        }
        else if(keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(true);
        }
        else if(keyCode == KeyCode.SPACE) {
            gameModel.launchBall();
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        Paddle paddle = gameModel.getPaddle();
        
        if(keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(false);
        }
        else if(keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(false);
        }
    }
}
