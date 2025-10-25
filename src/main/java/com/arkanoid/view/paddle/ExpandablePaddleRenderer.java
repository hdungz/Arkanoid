package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.image.Image;


public class ExpandablePaddleRenderer extends BasePaddleRenderer {
    private final SpriteAnimator animator;

    public ExpandablePaddleRenderer(GameModel gameModel) {
        super(gameModel);
        Image[] images = AssetsManager.getFrames("VIPPaddleExtendable");
        this.animator = new SpriteAnimator(images, 2);
    }


    @Override
    protected void customRender() {
        animator.update();
        paddleSprite.setImage(animator.getCurrentFrame());
        paddleSprite.setVisible(true);
        Paddle paddle = gameModel.getPaddle();
        if (paddle instanceof ExpandablePaddle) {
            ExpandablePaddle ep = (ExpandablePaddle) paddle;
            if (ep.isBlinking()) {
                paddleSprite.setVisible(false);
            } else {
                paddleSprite.setVisible(true);
            }
        } else {
            paddleSprite.setVisible(true);
        }
    }
}
