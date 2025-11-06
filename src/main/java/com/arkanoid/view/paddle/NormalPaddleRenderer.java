package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.PaddleType;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import com.arkanoid.utils.SpriteManager;
import javafx.scene.image.Image;

public class NormalPaddleRenderer extends BasePaddleRenderer {

    private SpriteAnimator animator;

    public NormalPaddleRenderer(GameModel gameModel) {
        super(gameModel);
        updatePaddleAsset();
    }

    private void updatePaddleAsset() {

        PaddleType selectedPaddle = SpriteManager.getSelectedPaddle();

        String assetKey = selectedPaddle.getAssetKey();

        Image[] images = AssetsManager.getFrames(assetKey);

        this.animator = new SpriteAnimator(images, 2);

        paddleSprite.setImage(animator.getCurrentFrame());
        //System.out.println("set "+assetKey);

    }

    @Override
    protected void customRender() {
        if (animator != null) {
            animator.update();
            paddleSprite.setImage(animator.getCurrentFrame());
            paddleSprite.setVisible(true);
        }
    }

    public void refreshPaddleAsset() {
        updatePaddleAsset();

    }
}