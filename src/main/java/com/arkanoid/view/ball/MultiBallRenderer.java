package com.arkanoid.view.ball;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import javafx.scene.Group;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class MultiBallRenderer {
    private final GameModel gameModel;
    private final List<Ball> balls = new ArrayList<>();
    private final List<BallRenderer> renderers = new ArrayList<>();
    private final Group group = new Group();

    public MultiBallRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
        BallRenderer renderer = new BallRenderer(gameModel, ball);
        renderers.add(renderer);
        group.getChildren().add(renderer.getNode());
    }


    public void removeBall(Ball ball) {
        int index = balls.indexOf(ball);
        if (index >= 0) {
            balls.remove(index);
            BallRenderer renderer = renderers.remove(index);
            group.getChildren().remove(renderer.getNode());
        }
    }


    public void render() {
        for (BallRenderer renderer : renderers) {
            renderer.render();
        }

    }

    public void cleanup() {
        for (BallRenderer renderer : renderers) {

        }
        group.getChildren().clear();
        balls.clear();
        renderers.clear();
    }

    public Node getNode() {
        return group;
    }

    public int getBallCount() {
        return balls.size();
    }
}
