package com.arkanoid.view.paddle;

import com.arkanoid.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.view.SpriteAnimator;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PaddleRenderer {
    private final GameModel gameModel;
    private final ImageView paddleSprite;
    private final SpriteAnimator animator;
    private final Image[] images;

    public PaddleRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        Paddle paddle = gameModel.getPaddle();

        images = AssetsManager.getFrames("VIPPaddle");
        animator = new SpriteAnimator(images, 2);
        paddleSprite = new ImageView(animator.getCurrentFrame());

        paddleSprite.setFitWidth(paddle.getWidth());
        paddleSprite.setFitHeight(paddle.getHeight());
        paddleSprite.setX(paddle.getX());
        paddleSprite.setY(paddle.getY());
    }

    public void render() {
        animator.update();
        paddleSprite.setImage(animator.getCurrentFrame());
        updatePosition();
    }

    public void updatePosition() {
        Paddle paddle = gameModel.getPaddle();
        paddleSprite.setX(paddle.getX());
        paddleSprite.setY(paddle.getY());
    }

    public Node getNode() {
        return paddleSprite;
    }
}
