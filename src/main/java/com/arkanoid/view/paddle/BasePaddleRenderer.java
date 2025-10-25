package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public abstract class BasePaddleRenderer {
    protected final GameModel gameModel;
    protected final ImageView paddleSprite;

    public BasePaddleRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.paddleSprite = new ImageView();

        updateProperties();
    }

    public void render() {
        updateProperties();
        customRender();
    }

    protected void updateProperties() {
        Paddle paddle = gameModel.getPaddle();
        paddleSprite.setX(paddle.getX());
        paddleSprite.setY(paddle.getY());
        paddleSprite.setFitWidth(paddle.getWidth());
        paddleSprite.setFitHeight(paddle.getHeight());
    }

    protected abstract void customRender();

    public Node getNode() {
        return paddleSprite;
    }
}
