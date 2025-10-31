package com.arkanoid.view.border;

import com.arkanoid.CONSTANT.*;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;



import static com.arkanoid.CONSTANT.BORDER_WIDTH;
import static com.arkanoid.CONSTANT.*;

public class BorderRenderer {

    private final GameModel gameModel;
    private final Image[] topBorderImages, leftBorderImages, rightBorderImages;
    private final List<ImageView> spriteList = new ArrayList<>();
    private final ImageView leftBorder, rightBorder, topBorder;
    private ImageView lastHitBorder = null;

    private final DropShadow defaultGlow, impactGlow;
    private int glowTimer = 16;
    private final int glowDurationFrames = 60;



    public BorderRenderer(GameModel gameModel) {
        this.gameModel = gameModel;

        leftBorderImages = AssetsManager.getFrames("LeftBorder");
        rightBorderImages = AssetsManager.getFrames("RightBorder");
        topBorderImages = AssetsManager.getFrames("TopBorder");

        leftBorder = new ImageView(leftBorderImages[0]);
        rightBorder = new ImageView(rightBorderImages[0]);
        topBorder = new ImageView(topBorderImages[0]);

        final int GAME_AREA_OFFSET_X = (WINDOW_WIDTH - GAME_AREA_WIDTH) / 2;

        leftBorder.setX(GAME_AREA_OFFSET_X - BORDER_WIDTH);
        leftBorder.setY(-5);
        leftBorder.setFitWidth(BORDER_WIDTH);
        leftBorder.setFitHeight(WINDOW_HEIGHT + 5 );

        rightBorder.setX(GAME_AREA_OFFSET_X + GAME_AREA_WIDTH);
        rightBorder.setY(-5);
        rightBorder.setFitWidth(BORDER_WIDTH);
        rightBorder.setFitHeight(WINDOW_HEIGHT + 5);

        topBorder.setX(GAME_AREA_OFFSET_X);
        topBorder.setY(0);
        topBorder.setFitWidth(GAME_AREA_WIDTH);
        topBorder.setFitHeight(BORDER_WIDTH);

        spriteList.add(leftBorder);
        spriteList.add(rightBorder);
        spriteList.add(topBorder);

        Color borderColor = Color.rgb(0, 150, 200);

        defaultGlow = new DropShadow();
        defaultGlow.setColor(borderColor.deriveColor(0, 1, 1, 0.7)); // Lấy màu của border và làm nó sáng hơn
        defaultGlow.setRadius(15);
        defaultGlow.setSpread(0.4);

        impactGlow = new DropShadow();
        impactGlow.setColor(borderColor.brighter());
        impactGlow.setOffsetX(0);
        impactGlow.setOffsetY(0);
        impactGlow.setRadius(0);

        leftBorder.setEffect(defaultGlow);
        rightBorder.setEffect(defaultGlow);
        topBorder.setEffect(defaultGlow);
    }

    public void render() {
        GameModel.WallCollisionSide collision = gameModel.getLastWallCollision();
        if (collision != GameModel.WallCollisionSide.NONE) {
            glowTimer = glowDurationFrames;

            if (lastHitBorder != null) {
                lastHitBorder.setEffect(defaultGlow);
            }

            switch (collision) {
                case LEFT:
                    lastHitBorder = leftBorder;
                    break;
                case RIGHT:
                    lastHitBorder = rightBorder;
                    break;
                case TOP:
                    lastHitBorder = topBorder;
                    break;
            }
            if (lastHitBorder != null) {
                lastHitBorder.setEffect(impactGlow);
            }
        }

        if (glowTimer > 0) {
            glowTimer--;
            double glowRadius = (double) glowTimer / glowDurationFrames * 60.0;
            impactGlow.setRadius(glowRadius);

            if (glowTimer == 0 && lastHitBorder != null) {
                lastHitBorder.setEffect(defaultGlow);
            }
        }


    }

    public List<Node> getNode() {
        return new ArrayList<>(spriteList);
    }

}
