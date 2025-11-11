package com.arkanoid.view;

import com.arkanoid.utils.AssetsManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class MenuView extends StackPane {

    private final Button playButton;
    private final Button storeButton;
    private final Button highscoreButton;
    private final Button exitButton;
    private MediaPlayer backgroundMediaPlayer;

    public MenuView() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Image[] backgroundFrames = AssetsManager.getFrames("menu");
        if (backgroundFrames != null && backgroundFrames.length > 0) {
            Image backgroundImage = backgroundFrames[0];
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(WINDOW_WIDTH);
            backgroundView.setFitHeight(WINDOW_HEIGHT);
            backgroundView.setPreserveRatio(false);
            getChildren().add(backgroundView);
        }


        MediaView backgroundVideo = createBackgroundVideo();
        if (backgroundVideo != null) {
            getChildren().add(backgroundVideo);
        }


        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(255, 255, 255, -0.1);");
        overlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        getChildren().add(overlay);


        VBox menuBox = new VBox(0);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(100);


        playButton = createImageButton("BtnStartNormal", "BtnStartHover");
        storeButton = createImageButton("BtnStoreNormal", "BtnStoreHover");
        highscoreButton = createImageButton("BtnHighNormal", "BtnHighHover");
        exitButton = createImageButton("BtnExitNormal", "BtnExitHover");

        menuBox.getChildren().addAll(playButton, storeButton, highscoreButton, exitButton);
        getChildren().add(menuBox);
    }

    private MediaView createBackgroundVideo() {
        String path = getClass().getResource("/com/arkanoid/video/menu3.mp4").toExternalForm();
        Media media = new Media(path);
        backgroundMediaPlayer = new MediaPlayer(media);

        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMediaPlayer.setAutoPlay(true);
        backgroundMediaPlayer.setVolume(0.0);

        MediaView mediaView = new MediaView(backgroundMediaPlayer);
        mediaView.setFitWidth(WINDOW_WIDTH);
        mediaView.setFitHeight(WINDOW_HEIGHT);
        mediaView.setPreserveRatio(false);
        mediaView.fitWidthProperty().bind(this.widthProperty());
        mediaView.fitHeightProperty().bind(this.heightProperty());

        return mediaView;
    }

    private Button createImageButton(String normalKey, String hoverKey) {
        Image normalImg = AssetsManager.getFrames(normalKey)[0];
        Image hoverImg = AssetsManager.getFrames(hoverKey)[0];

        ImageView imageView = new ImageView(normalImg);
        imageView.setFitWidth(250);
        imageView.setFitHeight(50);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-cursor: hand;");


        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(10);
        button.setEffect(shadow);


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

        return button;
    }

    public void cleanup() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
            backgroundMediaPlayer.dispose();
        }
    }

    // Getters
    public Button getPlayButton() { return playButton; }
    public Button getStoreButton() { return storeButton; }
    public Button getHighscoreButton() { return highscoreButton; }
    public Button getExitButton() { return exitButton; }
}