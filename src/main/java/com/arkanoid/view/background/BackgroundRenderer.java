package com.arkanoid.view.background;

import com.arkanoid.utils.AssetsManager;
import javafx.scene.Node;

public class BackgroundRenderer {

    private final VideoBackground videoBackground;

    public BackgroundRenderer() {
        this.videoBackground = new VideoBackground("Vid1");
    }

    public void render() {
    }

    public Node getNode() {
        return videoBackground;
    }
}

