package com.arkanoid.view;
import javafx.scene.effect.ColorAdjust;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;

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

        // 🎥 1. Thêm video nền
        MediaView backgroundVideo = createBackgroundVideo();
        getChildren().add(backgroundVideo);




        // 🧩 4. Menu chính

        menuBox = new VBox(25);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(200);
        menuBox.setTranslateX(0);
        playButton = createMenuButton("START");
        storeButton = createMenuButton("STORE");
        highscoreButton = createMenuButton("HIGHSCORE");
        exitButton = createMenuButton("EXIT");
        menuBox.getChildren().addAll(playButton);//, storeButton, highscoreButton, exitButton);

        VBox content = new VBox(80, menuBox);
        content.setAlignment(Pos.CENTER);
        getChildren().add(content);

        // ✨ Hiệu ứng fade-in các nút
        //fadeInButtons();

    }

    // 🎬 Tạo video nền (loop vô hạn)
    private MediaView createBackgroundVideo() {
        String path = getClass().getResource("/com/arkanoid/video/titleScreen.mp4").toExternalForm();
        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setAutoPlay(true);
        player.setVolume(0.0); // Tắt tiếng nền (nếu chỉ cần hiệu ứng hình)
        MediaView view = new MediaView(player);
        ColorAdjust colorAdjust = new ColorAdjust();

        colorAdjust.setBrightness(1);

        view.setFitWidth(WINDOW_WIDTH);
        view.setFitHeight(WINDOW_HEIGHT);
        view.setPreserveRatio(false);
       view.fitWidthProperty().bind(this.widthProperty());
       view.fitHeightProperty().bind(this.heightProperty());

        return view;
    }

    // 🔮 Hiệu ứng plasma phủ trên video


    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(200, 45);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        button.setTextFill(Color.WHITE);

        String baseStyle = "-fx-background-color: linear-gradient(to bottom, #0a4b78, #0f3460);" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #00d9ff;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;";
        button.setStyle(baseStyle);

        DropShadow glow = new DropShadow(25, Color.CYAN);

        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setStyle("-fx-background-color: linear-gradient(to bottom, #00d9ff, #004b8d);" +
                    "-fx-border-color: #00ffff;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 12;");
        });

        button.setOnMouseExited(e -> {
            button.setEffect(null);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setStyle(baseStyle);
        });

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2.5), button);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.03);
        pulse.setToY(1.03);
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
