package com.arkanoid.utils;

import com.arkanoid.controller.BaseController;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    private SceneType currentSceneType;

    private final Map<SceneType, Scene> sceneMap = new HashMap<>();
    private final Map<SceneType, BaseController> controllerMap = new HashMap<>();

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void addScene(SceneType type, Scene scene, BaseController controller) {
        sceneMap.put(type, scene);
        controllerMap.put(type, controller);
    }

    public void switchTo(SceneType type) {
        if (currentSceneType != null && controllerMap.containsKey(currentSceneType)) {
            controllerMap.get(currentSceneType).onExitScene();
        }

        Scene newScene = sceneMap.get(type);
        if (newScene != null) {
            stage.setScene(newScene);
            currentSceneType = type;

            if (controllerMap.containsKey(type)) {
                controllerMap.get(type).onEnterScene();
            }
        }
    }

    public SceneType isCurrentScene() {
        return currentSceneType;
    }
}
