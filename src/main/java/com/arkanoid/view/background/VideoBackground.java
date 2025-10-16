package com.arkanoid.view.background;

import com.arkanoid.CONSTANT;
import com.arkanoid.utils.AssetsManager;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class VideoBackground extends StackPane {

    private final MediaPlayer mediaPlayerA;

    public VideoBackground(String videoUrl) {
        mediaPlayerA = AssetsManager.getMediaPlayer(videoUrl);
        MediaView mediaView = new MediaView(mediaPlayerA);

        mediaView.setFitWidth(CONSTANT.WINDOW_WIDTH);
        mediaView.setFitHeight(CONSTANT.WINDOW_HEIGHT);
        mediaView.setOpacity(0.55);
        mediaPlayerA.setCycleCount(MediaPlayer.INDEFINITE);

        this.getChildren().add(mediaView);
        mediaPlayerA.setMute(true);
        mediaPlayerA.play();
    }

}
