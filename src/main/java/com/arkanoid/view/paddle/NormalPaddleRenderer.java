package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.image.Image;

public class NormalPaddleRenderer extends BasePaddleRenderer {

    private final SpriteAnimator animator;

    public NormalPaddleRenderer(GameModel gameModel) {
        super(gameModel);

        Image[] images = AssetsManager.getFrames("VIPPaddle");
        this.animator = new SpriteAnimator(images, 2);

        this.paddleSprite.setImage(animator.getCurrentFrame());
    }

    @Override
    protected void customRender() {
        animator.update();
        paddleSprite.setImage(animator.getCurrentFrame());
        paddleSprite.setVisible(true);
    }
}
