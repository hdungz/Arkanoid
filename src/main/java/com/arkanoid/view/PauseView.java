package com.arkanoid.view;

import com.arkanoid.utils.AssetsManager;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class PauseView extends StackPane {

    private final Button resumeButton;
    private final Button levelButton;
    private final Button menuButton;
    private final VBox pauseBox;
    private Timeline glowAnimation;

    public PauseView() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(false);

        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        getChildren().add(overlay);

        pauseBox = new VBox(15);
        pauseBox.setAlignment(Pos.CENTER);
        pauseBox.setMaxWidth(300);
        pauseBox.setMaxHeight(300);


        createAnimatedBorder();

        ImageView pauseTitle = createPauseTitleImage();
        VBox.setMargin(pauseTitle, new javafx.geometry.Insets(-30, 0, 0, 0));

        resumeButton = createButton("BtnResumeNormal", "BtnResumeHover", "RESUME");
        levelButton = createButton("BtnLevelNormal", "BtnLevelHover", "LEVELS");
        menuButton = createButton("BtnMenuNormal", "BtnMenuHover", "MENU");

        pauseBox.getChildren().addAll(pauseTitle, resumeButton, levelButton, menuButton);
        getChildren().add(pauseBox);
    }

    private void createAnimatedBorder() {

        pauseBox.setStyle("-fx-background-color: rgba(20, 20, 40, 0.3); " +
                "-fx-border-color: rgba(0, 150, 255, 1); " +
                "-fx-border-width: 3; " +
                "-fx-background-radius: 0; " +
                "-fx-border-radius: 0; " +
                "-fx-padding: 50;");


        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.rgb(0, 150, 255, 0.8));
        borderGlow.setRadius(20);
        borderGlow.setSpread(0.3);


        glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(borderGlow.radiusProperty(), 15),
                        new KeyValue(borderGlow.spreadProperty(), 0.2)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(borderGlow.radiusProperty(), 25),
                        new KeyValue(borderGlow.spreadProperty(), 0.4)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(borderGlow.radiusProperty(), 15),
                        new KeyValue(borderGlow.spreadProperty(), 0.2))
        );
        glowAnimation.setCycleCount(Timeline.INDEFINITE);

        pauseBox.setEffect(borderGlow);
    }

    private ImageView createPauseTitleImage() {
        ImageView pauseImageView = new ImageView();

        try {
            Image pauseImage = AssetsManager.getFrames("PauseTitle")[0];
            pauseImageView.setImage(pauseImage);
            pauseImageView.setFitWidth(200);
            pauseImageView.setPreserveRatio(true);

            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.rgb(0, 200, 255, 0.8));
            textShadow.setRadius(15);
            pauseImageView.setEffect(textShadow);

        } catch (Exception e) {
            System.out.println("Could not load pause title image, using text fallback");

            Text pauseText = new Text("PAUSED");
            pauseText.setFont(Font.font("Arial", 48));
            pauseText.setFill(Color.WHITE);
            pauseText.setStyle("-fx-font-weight: bold;");

            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.rgb(0, 200, 255, 0.8));
            textShadow.setRadius(15);
            pauseText.setEffect(textShadow);

            ImageView fallbackView = new ImageView();
            fallbackView.setUserData(pauseText);
        }

        return pauseImageView;
    }

    private Button createButton(String normalKey, String hoverKey, String fallbackText) {
        Button button = new Button();
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-cursor: hand;");
        button.setMinWidth(250);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(10);
        button.setEffect(shadow);

        try {
            Image normalImg = AssetsManager.getFrames(normalKey)[0];
            Image hoverImg = AssetsManager.getFrames(hoverKey)[0];

            ImageView imageView = new ImageView(normalImg);
            imageView.setFitWidth(250);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(false);
            button.setGraphic(imageView);

            button.setOnMouseEntered(e -> {
                imageView.setImage(hoverImg);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                scale.setToX(1.1);
                scale.setToY(1.1);
                scale.play();
            });

            button.setOnMouseExited(e -> {
                imageView.setImage(normalImg);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            });

        } catch (Exception e) {
            System.out.println("Could not load button images for " + normalKey + ", using text button");

            Text text = new Text(fallbackText);
            text.setFont(Font.font("Arial", 24));
            text.setFill(Color.WHITE);
            button.setGraphic(text);
            button.setStyle("-fx-cursor: hand; -fx-background-color: #2a2a5a; " +
                    "-fx-background-radius: 10; -fx-padding: 15 50;");

            button.setOnMouseEntered(ev -> {
                button.setStyle("-fx-cursor: hand; -fx-background-color: #3a3a7a; " +
                        "-fx-background-radius: 10; -fx-padding: 15 50;");
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                scale.setToX(1.1);
                scale.setToY(1.1);
                scale.play();
            });

            button.setOnMouseExited(ev -> {
                button.setStyle("-fx-cursor: hand; -fx-background-color: #2a2a5a; " +
                        "-fx-background-radius: 10; -fx-padding: 15 50;");
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            });
        }

        return button;
    }

    public void show() {
        setVisible(true);
        toFront();
        if (glowAnimation != null) {
            glowAnimation.play();
        }
    }

    public void hide() {
        setVisible(false);
        if (glowAnimation != null) {
            glowAnimation.pause();
        }
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getLevelButton() {
        return levelButton;
    }

    public Button getMenuButton() {
        return menuButton;
    }
}