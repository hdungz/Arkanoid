package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.view.ball.BallRenderer;
import com.arkanoid.view.border.BorderRenderer;
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
    private final BorderRenderer borderRenderer;
    // Renderer vẽ hiệu ứng nổ
    private final com.arkanoid.view.effects.EffectRenderer effectRenderer;
    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;
        ballRenderer = new BallRenderer(gameModel);
        paddleRenderer = new PaddleRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);
        borderRenderer = new BorderRenderer(gameModel);
        effectRenderer = new com.arkanoid.view.effects.EffectRenderer(gameModel);

        getChildren().add(ballRenderer.getNode());
        getChildren().add(paddleRenderer.getNode());
        getChildren().addAll(brickRenderer.getNodes());
        getChildren().addAll(hudRenderer.getNodes());
        getChildren().addAll(borderRenderer.getNode());
        getChildren().add(effectRenderer.getCanvas());
    }
    public void render() {
        ballRenderer.render();
        paddleRenderer.render();
        brickRenderer.render();
        effectRenderer.render();
        hudRenderer.render();
        borderRenderer.render();
    }


}
