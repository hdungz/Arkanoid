package com.arkanoid.utils.transitioneffect;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class DarkTransitionOverlay {

    private static final Color BLACK = Color.BLACK;


    public static Rectangle createDarkOverlay(double width, double height) {
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(BLACK);
        return overlay;
    }


    public static void darkFade(Scene currentScene, Scene nextScene, Runnable onComplete) {
        if (currentScene == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        Pane currentRoot = (Pane) currentScene.getRoot();
        double width = currentScene.getWidth();
        double height = currentScene.getHeight();


        Rectangle overlay = createDarkOverlay(width, height);
        overlay.setOpacity(0);

        currentRoot.getChildren().add(overlay);


        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), overlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        fadeIn.setOnFinished(e -> {

            if (onComplete != null) onComplete.run();


            Pane nextRoot = (Pane) nextScene.getRoot();
            Rectangle nextOverlay = createDarkOverlay(width, height);
            nextOverlay.setOpacity(1);
            nextRoot.getChildren().add(0, nextOverlay); // Add ở index 0 để che hết


            currentRoot.getChildren().remove(overlay);


            FadeTransition fadeOut = new FadeTransition(Duration.millis(600), nextOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                nextRoot.getChildren().remove(nextOverlay);
            });
            fadeOut.play();
        });

        fadeIn.play();
    }
}