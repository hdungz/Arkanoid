package com.arkanoid;

import com.arkanoid.controller.GameController;
import com.arkanoid.view.GameView;
import com.arkanoid.model.GameModel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 600;

    @Override
    public void start(Stage stage) throws Exception {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView(gameModel);
        GameController gameController = new GameController(gameModel);
        Scene scene = new Scene(gameView, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(gameController::handleKeyPressed);
        scene.setOnKeyReleased(gameController::handleKeyReleased);
        stage.setScene(scene);
        stage.setTitle("Arkanoid");
        stage.setResizable(false);
        stage.show();
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println(gameModel.getBall().getX() + " " + gameModel.getBall().getY() + " " + gameModel.getBall().getVelocityX() + " " + gameModel.getBall().getVelocityY()
                + " " + gameModel.getBall().getSpeed());
                gameModel.update();
                gameView.render();
            }
        };
        gameLoop.start(); // Bắt đầu vòng lặp
    }

    public static void main(String[] args) {
        launch(args);
    }
}
