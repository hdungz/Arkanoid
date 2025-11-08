package com.arkanoid.utils.transitioneffect;

import javafx.scene.Scene;


public class SceneTransition {

    public enum TransitionType {
        DARK_FADE
    }


    public static void fade(Scene currentScene, Scene nextScene, Runnable onComplete) {
        DarkTransitionOverlay.darkFade(currentScene, nextScene, onComplete);
    }

    public static void apply(Scene currentScene, Scene nextScene, TransitionType type, Runnable onComplete) {
        fade(currentScene, nextScene, onComplete);
    }
}