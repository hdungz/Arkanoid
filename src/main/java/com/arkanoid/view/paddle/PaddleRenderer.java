package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaddleRenderer {
    private final GameModel gameModel;
    private final Rectangle paddleShape;

    public PaddleRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        Paddle paddle = gameModel.getPaddle();
        paddleShape = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        paddleShape.setFill(Color.CORNFLOWERBLUE);
        paddleShape.setStroke(Color.BLACK);
        paddleShape.setArcWidth(15);
        paddleShape.setArcHeight(15);
    }

    public void render() {
        Paddle paddle = gameModel.getPaddle();
        paddleShape.setX(paddle.getX());
        paddleShape.setY(paddle.getY());
    }

    public Node getNode() {
        return paddleShape;
    }
}
