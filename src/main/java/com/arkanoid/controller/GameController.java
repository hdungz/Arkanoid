package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.security.KeyStore;

public class GameController implements BaseController {
    private final GameModel gameModel;
    private final Paddle paddle;
    private Scene scene;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.paddle = gameModel.getPaddle();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void onEnterScene() {
        if (scene != null) {
            scene.setOnKeyPressed(this::handleKeyPressed);
            scene.setOnKeyReleased(this::handleKeyReleased);
        }
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
        if(keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(false);
        }
        else if(keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(false);
        }
    }
}
