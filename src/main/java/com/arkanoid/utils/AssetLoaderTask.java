package com.arkanoid.utils;

import javafx.concurrent.Task;

/**
 * Một Task chạy trên luồng nền để tải tất cả các tài sản của game.
 * Báo cáo tiến trình và thông điệp về cho luồng giao diện.
 */
public class AssetLoaderTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        final int totalSteps = 6;

        updateMessage("Loading brick assets...");
        AssetsManager.loadImages("NormalBrickRed", "/com/arkanoid/Brick/07-Breakout-Tiles.png");
        AssetsManager.loadImages("CrackedNormalBrickRed", "/com/arkanoid/Brick/08-Breakout-Tiles.png");
        AssetsManager.loadImages("DurableBrick", "/com/arkanoid/Brick/09-Breakout-Tiles.png");
        AssetsManager.loadImages("CrackedDurableBrick", "/com/arkanoid/Brick/10-Breakout-Tiles.png");
        AssetsManager.loadImages("SuperDurableBrick", "/com/arkanoid/Brick/13-Breakout-Tiles.png");
        AssetsManager.loadImages("CrackedSuperDurableBrick", "/com/arkanoid/Brick/14-Breakout-Tiles.png");
        AssetsManager.loadImages("BoomBrick", "/com/arkanoid/Brick/bom-brick.png");
        AssetsManager.loadImages("CrackedBoomBrick", "/com/arkanoid/Brick/cracked-boombrick.png");
        AssetsManager.loadImages("MovingBrick", "/com/arkanoid/Brick/19-Breakout-Tiles.png");
        AssetsManager.loadImages("CrackedMovingBrick", "/com/arkanoid/Brick/20-Breakout-Tiles.png");
        AssetsManager.loadImages("SponseBrick", "/com/arkanoid/Brick/05-Breakout-Tiles.png");
        AssetsManager.loadImages("CrackedSponseBrick", "/com/arkanoid/Brick/06-Breakout-Tiles.png");
        AssetsManager.loadImages("Brick1_4","/com/arkanoid/Brick/Brick1_4.png");
        AssetsManager.loadImages("TestingBrick", "/com/arkanoid/Brick/Sprite-0002.png");
        updateProgress(1, totalSteps);

        updateMessage("Loading ball assets...");
        AssetsManager.loadImages("EggBlue", "/com/arkanoid/Ball/EggBlue.png");
        AssetsManager.loadImages("EnBallRed", "/com/arkanoid/Ball/EnBallRed.png");
        AssetsManager.loadImages("Basketball", "/com/arkanoid/Ball/basket-ball.png");
        // AssetsManager.loadAnimationFrames("RedBall", "/com/arkanoid/Ball/red/keyframes", 6); // Giả sử loadAnimationFrames có thể được gọi
        updateProgress(2, totalSteps);

        updateMessage("Loading paddle assets...");
        // AssetsManager.loadAnimationFrames("VIPPaddle", "/com/arkanoid/Paddle/VIPPaddle", 2);
        updateProgress(3, totalSteps);

        updateMessage("Loading game borders...");
        AssetsManager.loadImages("LeftBorder", "/com/arkanoid/Border/LeftBorder.png");
        AssetsManager.loadImages("RightBorder", "/com/arkanoid/Border/RightBorder.png");
        AssetsManager.loadImages("TopBorder", "/com/arkanoid/Border/TopBorder.png");
        AssetsManager.loadImages("PlayGround","/com/arkanoid/Background/PlayGround.png");
        updateProgress(4, totalSteps);

        updateMessage("Loading special effects...");
        // AssetsManager.loadAnimationFrames("Explosion","/com/arkanoid/Effect/explosion",63);
        updateProgress(5, totalSteps);

        updateMessage("Loading backgrounds and videos...");
        AssetsManager.loadImages("BackGround1","/com/arkanoid/Background/BackGround2.png");
        AssetsManager.loadImages("BackGround2", "/com/arkanoid/Background/gradient-cyber-futuristic-background_23-2149117429.jpg");
        AssetsManager.loadImages("BackGround3", "/com/arkanoid/Background/GIF_4FPS/space1_4-frames.gif");
        AssetsManager.loadImages("GIF1", "/com/arkanoid/Background/GIF_4FPS/Glow Dark Matter GIF by ESAHubble Space Telescope.gif");
        AssetsManager.loadVideo("Vid1", "/com/arkanoid/Background/Video/Vid1.mp4");
        AssetsManager.loadVideo("Vid2", "/com/arkanoid/Background/Video/Vid2.mp4");
        updateProgress(6, totalSteps);

        updateMessage("Loading complete!");
        Thread.sleep(500);

        return null;
    }
}
