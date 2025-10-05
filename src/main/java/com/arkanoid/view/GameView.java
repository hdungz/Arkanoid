package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.view.ball.BallRenderer;
import com.arkanoid.view.brick.BrickRenderer;
import com.arkanoid.view.hud.HUDRenderer;
import com.arkanoid.view.paddle.PaddleRenderer;
import javafx.scene.layout.Pane;

public class GameView extends Pane{
    private final GameModel gameModel;
    private final BallRenderer ballRenderer;
    private final PaddleRenderer paddleRenderer;
    private final BrickRenderer brickRenderer;
    private final HUDRenderer hudRenderer;
    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;
        ballRenderer = new BallRenderer(gameModel);
        paddleRenderer = new PaddleRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);

        getChildren().add(ballRenderer.getNode());
        getChildren().add(paddleRenderer.getNode());
        getChildren().addAll(brickRenderer.getNodes());
        getChildren().addAll(hudRenderer.getNodes());
    }
    public void render() {
        ballRenderer.render();
        paddleRenderer.render();
        brickRenderer.render();
        hudRenderer.render();
    }


}
