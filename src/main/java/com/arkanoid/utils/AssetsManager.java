package com.arkanoid.utils;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetsManager {
    private static final Map<String, Image[]> assets = new HashMap<>();

    public static void loadAssets() {
        loadImages("NormalBrickRed","/com/arkanoid/Brick/07-Breakout-Tiles.png");
        loadImages("CrackedNormalBrickRed","/com/arkanoid/Brick/08-Breakout-Tiles.png");
        loadImages("DurableBrick","/com/arkanoid/Brick/09-Breakout-Tiles.png");
        loadImages("CrackedDurableBrick","/com/arkanoid/Brick/10-Breakout-Tiles.png");
        loadImages("SuperDurableBrick","/com/arkanoid/Brick/13-Breakout-Tiles.png");
        loadImages("CrackedSuperDurableBrick","/com/arkanoid/Brick/14-Breakout-Tiles.png");
        loadImages("BoomBrick","/com/arkanoid/Brick/bom-brick.png");
        loadImages("CrackedBoomBrick","/com/arkanoid/Brick/cracked-boombrick.png");
        loadImages("MovingBrick","/com/arkanoid/Brick/19-Breakout-Tiles.png");
        loadImages("CrackedMovingBrick","/com/arkanoid/Brick/20-Breakout-Tiles.png");
        loadImages("SponseBrick","/com/arkanoid/Brick/05-Breakout-Tiles.png");
        loadImages("CrackedSponseBrick","/com/arkanoid/Brick/06-Breakout-Tiles.png");
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
        loadAnimationFrames("Explosion","/com/arkanoid/Effect/explosion",63);
    }

    private static void loadAnimationFrames(String key, String basePath, int frameCount) {
        Image[] frames = new Image[frameCount];
        String path;
        for (int i = 0; i < frameCount; i++) {
            if (basePath.equals("/com/arkanoid/Effect/explosion")){
                path = String.format("%s/frame_%d.png", basePath, i + 2);
            }else {
                path = String.format("%s/%d.png", basePath, i + 1);
            }
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
