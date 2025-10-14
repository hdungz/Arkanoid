package com.arkanoid.utils;

import com.arkanoid.controller.GameController;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class GameLoop extends Application {
    private final GameModel gameModel;
    private final GameView gameView;

    public GameLoop(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    @Override
    public void start(Stage stage) throws Exception {
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
                deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                double timeSinceLastFpsUpdate = (now - lastFpsUpdate) / 1_000_000_000.0;
                frameCount++;
                if (timeSinceLastFpsUpdate >= 1.0) {
                    double fps = frameCount / timeSinceLastFpsUpdate;
                    System.out.println("FPS: " + String.format("%.2f", fps));
                    frameCount = 0;
                    lastFpsUpdate = now;
                }
                if(SceneManager.getInstance().isCurrentScene() == SceneType.GamePlay) {
                    gameModel.update(deltaTime);
                    gameView.render();
                }
            }
        };
        gameLoop.start();
    }
}