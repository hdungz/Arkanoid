package com.arkanoid.utils;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetsManager {
    private static final Map<String, Image[]> assets = new HashMap<>();

    public static void loadAssets() {
        loadImages("EggBlue", "/com/arkanoid/Ball/EggBlue.png");
        loadImages("EnBallRed", "/com/arkanoid/Ball/EnBallRed.png");
        loadAnimationFrames("RedBall", "/com/arkanoid/Ball/red/keyframes", 6);
        loadAnimationFrames("VIPPaddle", "/com/arkanoid/Paddle/VIPPaddle", 2);
        loadImages("Brick1_4","/com/arkanoid/Brick/Brick1_4.png");
        loadImages("TestingBrick", "/com/arkanoid/Brick/Sprite-0002.png");
        loadImages("LeftBorder", "/com/arkanoid/Border/LeftBorder.png");
        loadImages("RightBorder", "/com/arkanoid/Border/RightBorder.png");
        loadImages("TopBorder", "/com/arkanoid/Border/TopBorder.png");
        loadImages("Basketball", "/com/arkanoid/Ball/basket-ball.png");
        loadImages("PlayGround", "/com/arkanoid/Background/PlayGround.png");
        loadImages("BackGround1", "/com/arkanoid/Background/BackGround1.png");

        loadImages("BackGround3", "/com/arkanoid/Background/BackGround3.png");

    }

    private static void loadAnimationFrames(String key, String basePath, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String path = String.format("%s/%d.png", basePath, i + 1);
            try {
                InputStream stream = AssetsManager.class.getResourceAsStream(path);
                if (stream == null) {
                    throw new IllegalArgumentException("Không thể tìm thấy tài nguyên: " + path);
                }
                frames[i] = new Image(stream);
            } catch (Exception e) {
                System.err.println("Lỗi khi tải frame animation: " + path);
                e.printStackTrace();
            }
        }
        assets.put(key, frames);
    }

    public static void loadImages(String key, String path) {
        try {
            InputStream stream = AssetsManager.class.getResourceAsStream(path);
            if (stream == null) {
                throw new IllegalArgumentException("Không thể tìm thấy tài nguyên: " + path);
            }
            Image[] frames = new Image[1];
            frames[0] = new Image(stream);
            assets.put(key, frames);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải sprite: " + path);
            e.printStackTrace();
        }
    }

    public static Image[] getFrames(String key) {
        return assets.getOrDefault(key, new Image[0]);
    }


}
