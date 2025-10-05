package com.arkanoid;

import com.arkanoid.controller.GameController;
import com.arkanoid.view.GameView;
import com.arkanoid.model.GameModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 600;

    @Override
    public void start(Stage stage) throws Exception {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView(gameModel);
        GameController gameController = new GameController(gameModel);

    }
}
