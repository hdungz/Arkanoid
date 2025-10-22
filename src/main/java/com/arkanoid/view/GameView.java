package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.Item;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.view.background.BackgroundRenderer;
import com.arkanoid.view.background.PlayGroundRenderer;
import com.arkanoid.view.ball.MultiBallRenderer;
import com.arkanoid.view.border.BorderRenderer;
import com.arkanoid.view.brick.BrickRenderer;
import com.arkanoid.view.hud.HUDRenderer;
import com.arkanoid.view.paddle.PaddleRenderer;
import com.arkanoid.view.effects.EffectRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class GameView extends Pane {
    private final GameModel gameModel;
    private final MultiBallRenderer multiBallRenderer;
    private final PaddleRenderer paddleRenderer;
    private final BrickRenderer brickRenderer;
    private final HUDRenderer hudRenderer;
    private final BorderRenderer borderRenderer;
    private final PlayGroundRenderer playGroundRenderer;
    private final BackgroundRenderer backgroundRenderer;
    private final EffectRenderer effectRenderer;


    private final Canvas itemCanvas;
    private final GraphicsContext gcItem;

    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;


        multiBallRenderer = new MultiBallRenderer(gameModel);
        paddleRenderer = new PaddleRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);
        borderRenderer = new BorderRenderer(gameModel);
        effectRenderer = new EffectRenderer(gameModel);
        playGroundRenderer = new PlayGroundRenderer();
        backgroundRenderer = new BackgroundRenderer();


        itemCanvas = new Canvas(800, 800);
        gcItem = itemCanvas.getGraphicsContext2D();


        Ball mainBall = gameModel.getBall();
        multiBallRenderer.addBall(mainBall);
        multiBallRenderer.addBall(mainBall);

        /*Ball ball1 = new Ball(400, 400, 10, 3, -3);
        Ball ball2 = new Ball(420, 400, 10, -3, -3);
        multiBallRenderer.addBall(ball1);
        multiBallRenderer.addBall(ball2);*/


        getChildren().add(backgroundRenderer.getNode());
        getChildren().add(playGroundRenderer.getNode());
        getChildren().add(multiBallRenderer.getNode());
        getChildren().add(paddleRenderer.getNode());
        getChildren().addAll(brickRenderer.getNodes());
        getChildren().addAll(hudRenderer.getNodes());
        getChildren().addAll(borderRenderer.getNode());
        getChildren().add(effectRenderer.getCanvas());
        getChildren().add(itemCanvas);
    }

    public void render() {

        multiBallRenderer.render();
        paddleRenderer.render();
        brickRenderer.render();
        effectRenderer.render();
        hudRenderer.render();
        borderRenderer.render();


        gcItem.clearRect(0, 0, itemCanvas.getWidth(), itemCanvas.getHeight());
        for (Item vatPham : gameModel.getItems()) {
            vatPham.ve(gcItem);
        }
    }
}
