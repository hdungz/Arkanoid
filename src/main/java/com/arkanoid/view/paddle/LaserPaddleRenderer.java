package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.Laser;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PaddleType;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import com.arkanoid.utils.SpriteManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class LaserPaddleRenderer extends BasePaddleRenderer {
    private  SpriteAnimator animator;
    private final Group renderGroup;

    public LaserPaddleRenderer(GameModel gameModel) {

        super(gameModel);
        updatePaddleAssetLaser();
        this.renderGroup = new Group();
        this.renderGroup.getChildren().add(paddleSprite);
    }

    private void updatePaddleAssetLaser() {

        PaddleType selectedPaddle = SpriteManager.getSelectedPaddle();

        String assetKey = selectedPaddle.getAssetKey();

        Image[] images = AssetsManager.getFrames(assetKey);

        this.animator = new SpriteAnimator(images, 2);

        paddleSprite.setImage(animator.getCurrentFrame());

    }

    @Override
    protected void customRender() {
        animator.update();
        paddleSprite.setImage(animator.getCurrentFrame());

        Paddle paddle = gameModel.getPaddle();

        paddleSprite.setVisible(true);
        if (paddle instanceof LaserPaddle) {
            LaserPaddle lp = (LaserPaddle) paddle;

            if (lp.isBlinking()) {
                paddleSprite.setVisible(!paddleSprite.isVisible());
            } else {
                paddleSprite.setVisible(true);
            }

            renderLasers(lp);
        } else {
            paddleSprite.setVisible(true);
        }
    }

    private void renderLasers(LaserPaddle laserPaddle) {
        renderGroup.getChildren().removeIf(node -> node instanceof Rectangle);

        for (Laser laser : laserPaddle.getLasers()) {
            if (laser.isActive()) {
                Rectangle laserRect = createLaserVisual(laser);
                renderGroup.getChildren().add(laserRect);
            }
        }
    }

    private Rectangle createLaserVisual(Laser laser) {
        Rectangle rect = new Rectangle(
                laser.getX(),
                laser.getY(),
                laser.getWidth(),
                laser.getHeight()
        );

        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.CYAN),
                new Stop(0.5, Color.DEEPSKYBLUE),
                new Stop(1, Color.WHITE)
        );

        rect.setFill(gradient);

        rect.setEffect(new javafx.scene.effect.Glow(0.8));

        rect.setArcWidth(2);
        rect.setArcHeight(2);

        return rect;
    }

    @Override
    public Node getNode() {
        return renderGroup;
    }
    public void refreshPaddleAssetLaser() {
        updatePaddleAssetLaser();

    }
}