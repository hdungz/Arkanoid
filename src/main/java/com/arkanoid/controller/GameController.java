package com.arkanoid.controller;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    private final GameModel gameModel;
    private final Paddle paddle;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.paddle = gameModel.getPaddle();
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();

        if(keyCode == KeyCode.LEFT || keyCode == KeyCode.A) {
            paddle.setMovingLeft(true);
//            System.out.println("moving left");
        }
        else if(keyCode == KeyCode.RIGHT || keyCode == KeyCode.D) {
            paddle.setMovingRight(true);
//            System.out.println("moving right");
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
