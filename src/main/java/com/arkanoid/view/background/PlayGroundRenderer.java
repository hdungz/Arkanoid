package com.arkanoid.view.background;


import com.arkanoid.utils.AssetsManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.arkanoid.CONSTANT.*;

public class PlayGroundRenderer {
    private final ImageView playGroundSprite;
    private final Image[] images;

    public PlayGroundRenderer() {
        playGroundSprite = new ImageView();
        images = AssetsManager.getFrames("PlayGround");

        this.playGroundSprite.setImage(images[0]);
        this.playGroundSprite.setX(GAME_AREA_X);
        this.playGroundSprite.setY(BALL_RADIUS);
        this.playGroundSprite.setFitWidth(GAME_AREA_WIDTH);
        this.playGroundSprite.setFitHeight(WINDOW_HEIGHT);
        this.playGroundSprite.setOpacity(0.8);
    }

    public Node getNode() {
        return playGroundSprite;
    }
}
