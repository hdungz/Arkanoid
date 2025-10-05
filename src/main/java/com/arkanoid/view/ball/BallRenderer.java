package com.arkanoid.view.ball;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.*;



public class BallRenderer {
    private final GameModel gameModel;
    private final Ball ball;
    private final Circle ballShape;

    public BallRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        ball = gameModel.getBall();
        this.ballShape = new Circle(ball.getX(), ball.getY(), ball.getRadius(), Color.WHITE);
    }

    public void render() {
        Ball ball = gameModel.getBall();
        ballShape.setCenterX(ball.getX());
        ballShape.setCenterY(ball.getY());
    }

    public Node getNode() {
        return ballShape;
    }


}
