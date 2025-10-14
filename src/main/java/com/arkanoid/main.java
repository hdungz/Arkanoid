package com.arkanoid;

import com.arkanoid.controller.GameController;
import com.arkanoid.view.GameView;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        AssetsManager.loadAssets();
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
            private long lastUpdate = 0;
            private long lastFpsUpdate = 0;
            private int frameCount = 0;
            double deltaTime = 1.0;
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    lastFpsUpdate = now;
                    return;
                }
//
                deltaTime = (now - lastUpdate) / 1_000_000_000.0;
//                System.out.println(deltaTime);
                lastUpdate = now;
//                deltaTime = 1;
                double timeSinceLastFpsUpdate = (now - lastFpsUpdate) / 1_000_000_000.0;
                frameCount++;
                if (timeSinceLastFpsUpdate >= 1.0) {
                    double fps = frameCount / timeSinceLastFpsUpdate;
                    System.out.println("FPS: " + String.format("%.2f", fps));
                    frameCount = 0;
                    lastFpsUpdate = now;
                }
//
                gameModel.update(deltaTime);
                gameView.render();
            }
        };
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
