package com.arkanoid.view.ball;

import com.arkanoid.utils.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BallRenderer {
    private final GameModel gameModel;
    private final Ball ball;
    private final ImageView ballSprite;
    private final Image[] images;

    private final SpriteAnimator animator;
    public BallRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        ball = gameModel.getBall();
        images = AssetsManager.getFrames("Basketball");
        animator = new SpriteAnimator(images, images.length);
        ballSprite = new ImageView(animator.getCurrentFrame());
        double diameter = ball.getRadius() * 2;
        this.ballSprite.setFitWidth(diameter);
        this.ballSprite.setFitHeight(diameter);

    }

    public void render() {
        animator.update();
        ballSprite.setImage(animator.getCurrentFrame());
        updatePosition();
    }

    private void updatePosition() {
        Ball ball = gameModel.getBall();
        ballSprite.setX(ball.getX() - ball.getRadius());
        ballSprite.setY(ball.getY() - ball.getRadius());
    }

    public Node getNode() {
        return ballSprite;
    }

}
