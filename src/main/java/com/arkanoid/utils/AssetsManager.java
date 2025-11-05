package com.arkanoid.utils;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsManager {
    private static final Map<String, Object> assets = new HashMap<>();

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
        loadImages("playbutton1", "/com/arkanoid/ChooseLevel/playbutton1.png");
        loadImages("playbutton2", "/com/arkanoid/ChooseLevel/playbutton2.png");
        loadImages("backbutton1", "/com/arkanoid/ChooseLevel/backbutton1.png");
        loadImages("backbutton2", "/com/arkanoid/ChooseLevel/backbutton2.png");
        loadImages("backtomenu1", "/com/arkanoid/ChooseLevel/backtomenu1.png");
        loadImages("backtomenu2", "/com/arkanoid/ChooseLevel/backtomenu2.png");
        loadImages("backgroundchooselevel", "/com/arkanoid/ChooseLevel/backgroundchooselevel.png");
        loadImages("PurpleBall", "/com/arkanoid/Ball/PurpleBall.png");
        loadAnimationFrames("RedBall", "/com/arkanoid/Ball/red/keyframes", 6);
        loadAnimationFrames("VIPPaddle", "/com/arkanoid/Paddle/VIPPaddle", 2);
        loadImages("Brick1_4","/com/arkanoid/Brick/Brick1_4.png");
        loadImages("TestingBrick", "/com/arkanoid/Brick/Sprite-0002.png");
        loadImages("LeftBorder", "/com/arkanoid/Border/LeftBorder.png");
        loadImages("RightBorder", "/com/arkanoid/Border/RightBorder.png");
        loadImages("TopBorder", "/com/arkanoid/Border/TopBorder.png");
        loadImages("Basketball", "/com/arkanoid/Ball/basket-ball.png");
        loadImages("BackGround1","/com/arkanoid/Background/BackGround2.png");
        loadImages("PlayGround","/com/arkanoid/Background/PlayGround.png");
        loadAnimationFrames("Explosion","/com/arkanoid/Effect/explosion",63);
        loadImages("BackGround2", "/com/arkanoid/Background/gradient-cyber-futuristic-background_23-2149117429.jpg");
        loadImages("BackGround3", "/com/arkanoid/Background/GIF_4FPS/space1_4-frames.gif");
        loadImages("menu", "/com/arkanoid/Background/menu.png");
        loadVideo("Vid1", "/com/arkanoid/Background/Video/Vid1.mp4");
        loadAnimationFrames("VIPPaddleExtendable", "/com/arkanoid/Paddle/VIPPaddleExtended", 2);

        loadVideo("Menu", "/com/arkanoid/video/menu3.mp4");
        loadVideo("Vid2", "/com/arkanoid/Background/Video/Vid2.mp4");
//        loadVideo("Vid3", "/com/arkanoid/Background/Video/Vid3.mp4");

        loadImages("BtnStartNormal", "/com/arkanoid/buttons/start_normal.png");
        loadImages("BtnStartHover", "/com/arkanoid/buttons/start_hover.png");
        loadImages("BtnStoreNormal", "/com/arkanoid/buttons/store_normal.png");
        loadImages("BtnStoreHover", "/com/arkanoid/buttons/store_hover.png");
        loadImages("BtnHighNormal", "/com/arkanoid/buttons/highscore_normal.png");
        loadImages("BtnHighHover", "/com/arkanoid/buttons/highscore_hover.png");
        loadImages("BtnExitNormal", "/com/arkanoid/buttons/exit_normal.png");
        loadImages("BtnExitHover", "/com/arkanoid/buttons/exit_hover.png");

        loadImages("GIF1", "/com/arkanoid/Background/GIF_4FPS/Glow Dark Matter GIF by ESAHubble Space Telescope.gif");
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
                System.err.println("Cảnh báo: Không thể tìm thấy tài nguyên: " + path);
                assets.put(key, new Image[0]);
                return;
            }
            Image[] frames = new Image[1];
            frames[0] = new Image(stream);
            assets.put(key, frames);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải sprite: " + path);
            e.printStackTrace();
            assets.put(key, new Image[0]);
        }
    }

    public static void loadVideo(String key, String path) {
        try {
            URL url = AssetsManager.class.getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("Không thể tìm thấy tài nguyên video: " + path);
            }
            Media media = new Media(url.toExternalForm());
            assets.put(key, media);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải video: " + path);
            e.printStackTrace();
        }
    }

    public static Image[] getFrames(String key) {
        return (Image[]) assets.getOrDefault(key, new Image[0]);
    }

    public static MediaPlayer getMediaPlayer(String key) {
        Object asset = assets.get(key);
        if (asset instanceof Media) {
            return new MediaPlayer((Media) asset);
        }
        return null;
    }
}
