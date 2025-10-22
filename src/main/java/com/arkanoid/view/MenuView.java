package com.arkanoid.view;

import com.arkanoid.utils.AssetsManager;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.util.Duration;



import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class MenuView extends StackPane {

    private final Button playButton;
    private final Button storeButton;
    private final Button highscoreButton;
    private final Button exitButton;
    private final VBox menuBox;

    public MenuView() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);


        MediaView backgroundVideo = createBackgroundVideo();
        getChildren().add(backgroundVideo);


        menuBox = new VBox(5);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(220);

        playButton = createImageButton("BtnStartNormal", "BtnStartHover");
        storeButton = createImageButton("BtnStoreNormal", "BtnStoreHover");
        highscoreButton = createImageButton("BtnHighNormal", "BtnHighHover");
        exitButton = createImageButton("BtnExitNormal", "BtnExitHover");

        menuBox.getChildren().addAll(playButton, storeButton, highscoreButton, exitButton);

        VBox content = new VBox(80, menuBox);

        content.setAlignment(Pos.CENTER);
        getChildren().add(content);
    }


    private MediaView createBackgroundVideo() {
        /*MediaPlayer player = AssetsManager.getMediaPlayer("Menu");

        if (player == null) {
            System.err.println("⚠️ Không tìm thấy video 'Menu' trong AssetsManager!");
            return new MediaView();
        }*/

        String path = getClass().getResource("/com/arkanoid/video/menu.mp4").toExternalForm();
        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);

        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setAutoPlay(true);
        player.setVolume(0.0);

        MediaView mediaView = new MediaView(player);
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
        imageView.setFitWidth(150);
        imageView.setFitHeight(40);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-cursor: hand;");


        button.setOnMouseEntered(e -> imageView.setImage(hoverImg));
        button.setOnMouseExited(e -> imageView.setImage(normalImg));


        ScaleTransition pulse = new ScaleTransition(Duration.seconds(5), button);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        return button;
    }


    public Button getPlayButton() { return playButton; }
    public Button getStoreButton() { return storeButton; }
    public Button getHighscoreButton() { return highscoreButton; }
    public Button getExitButton() { return exitButton; }
}
