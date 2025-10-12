package com.arkanoid;

import com.arkanoid.controller.BaseController;
import com.arkanoid.controller.GameController;
import com.arkanoid.controller.MenuController;
import com.arkanoid.utils.GameLoop;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.GameView;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.view.MenuView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        //init stage, Window, Assets loading
        AssetsManager.loadAssets();
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.init(stage);
        stage.setTitle("Arkanoid");
        stage.setResizable(false);

        //Init GamePlay
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView(gameModel);
        GameController gameController = new GameController(gameModel);
        Scene gameScene = new Scene(gameView, WINDOW_WIDTH, WINDOW_HEIGHT);
        gameController.setScene(gameScene);
        sceneManager.addScene(SceneType.GamePlay, gameScene, gameController);

        //Init Menu
        MenuView menuView = new MenuView();
        MenuController menuController = new MenuController(menuView);
        Scene menuScene = new Scene(menuView, WINDOW_WIDTH, WINDOW_HEIGHT);
        sceneManager.addScene(SceneType.Menu, menuScene, menuController);

        stage.show();

        sceneManager.switchTo(SceneType.Menu);
        GameLoop gameLoop = new GameLoop(gameModel, gameView);
        gameLoop.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
