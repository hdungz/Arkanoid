package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.ExpandPaddleType;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PaddleType;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import com.arkanoid.utils.SpriteManager;
import javafx.scene.image.Image;

public class ExpandablePaddleRenderer extends BasePaddleRenderer {
    private SpriteAnimator animator;
    private ExpandPaddleType currentExpandType;

    public ExpandablePaddleRenderer(GameModel gameModel) {
        super(gameModel);
        updatePaddleAsset();
    }

    private void updatePaddleAsset() {

        PaddleType selectedPaddle = SpriteManager.getSelectedPaddle();


        ExpandPaddleType expandType = ExpandPaddleType.fromPaddleType(selectedPaddle);
        this.currentExpandType = expandType;


        String assetKey = expandType.getAssetKey();
        Image[] images = AssetsManager.getFrames(assetKey);

        this.animator = new SpriteAnimator(images, 2);
        paddleSprite.setImage(animator.getCurrentFrame());

//        System.out.println("✓ ExpandablePaddle updated to: " + expandType.getDisplayName() +
//                " (asset: " + assetKey + ")");
    }

    @Override
    protected void customRender() {
        if (animator != null) {
            animator.update();
            paddleSprite.setImage(animator.getCurrentFrame());
        }


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


    public void refreshPaddleAsset() {
        updatePaddleAsset();
    }
}