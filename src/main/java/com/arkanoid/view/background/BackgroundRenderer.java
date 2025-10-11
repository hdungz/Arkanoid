package com.arkanoid.view.background;

import com.arkanoid.CONSTANT;
import com.arkanoid.utils.AssetsManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.arkanoid.CONSTANT.*;

public class BackgroundRenderer {

    private final ImageView backgroundSprite;

    public BackgroundRenderer() {
        Image[] frames = AssetsManager.getFrames("BackGround1");

        this.backgroundSprite = new ImageView(frames[0]);

        this.backgroundSprite.setX(0);
        this.backgroundSprite.setY(0);
        this.backgroundSprite.setFitWidth(WINDOW_WIDTH);
        this.backgroundSprite.setFitHeight(WINDOW_HEIGHT);

    }

    public void render() {
        // cập nhật lớp này nếu có animation trong background

    }

    public Node getNode() {
        return backgroundSprite;
    }
}

