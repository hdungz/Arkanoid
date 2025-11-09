package com.arkanoid.utils;

import com.arkanoid.controller.BaseController;
import com.arkanoid.utils.transitioneffect.SceneTransition;
import com.arkanoid.utils.transitioneffect.TransitionConfig;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    private SceneType currentSceneType;
    private boolean isTransitioning = false;

    private final Map<SceneType, Scene> sceneMap = new HashMap<>();
    private final Map<SceneType, BaseController> controllerMap = new HashMap<>();

    private SceneManager() {
    }

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
        switchTo(type, null);
    }


    public void switchTo(SceneType type, Runnable afterTransition) {
        if (isTransitioning) {
            System.out.println("Transition already in progress, ignoring...");
            return;
        }

        Scene currentScene = (currentSceneType != null) ? sceneMap.get(currentSceneType) : null;
        Scene nextScene = sceneMap.get(type);

        if (nextScene == null) {
            System.out.println("Scene not found: " + type);
            return;
        }


        SceneTransition.TransitionType transitionType = getTransitionType(currentSceneType, type);


        isTransitioning = true;


        if (currentSceneType != null && controllerMap.containsKey(currentSceneType)) {
            controllerMap.get(currentSceneType).onExitScene();
        }


        SceneTransition.apply(currentScene, nextScene, transitionType, () -> {

            stage.setScene(nextScene);
            currentSceneType = type;

            if (controllerMap.containsKey(type)) {
                controllerMap.get(type).onEnterScene();
            }

            isTransitioning = false;

            if (afterTransition != null) {
                afterTransition.run();
            }
        });
    }


    public void switchToImmediate(SceneType type) {
        if (isTransitioning) {
            return;
        }

        // Exit current scene
        if (currentSceneType != null && controllerMap.containsKey(currentSceneType)) {
            controllerMap.get(currentSceneType).onExitScene();
        }

        Scene newScene = sceneMap.get(type);
        if (newScene != null) {
            stage.setScene(newScene);
            currentSceneType = type;

            // Enter new scene
            if (controllerMap.containsKey(type)) {
                controllerMap.get(type).onEnterScene();
            }
        }
    }

    private SceneTransition.TransitionType getTransitionType(SceneType from, SceneType to) {
        return TransitionConfig.getInstance().get(from, to);
    }

    public SceneType isCurrentScene() {
        return currentSceneType;
    }

    public boolean isTransitioning() {
        return isTransitioning;
    }

    public Scene getScene(SceneType type) {
        return sceneMap.get(type);
    }

    public BaseController getController(SceneType type) {
        return controllerMap.get(type);
    }
}