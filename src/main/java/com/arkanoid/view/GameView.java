package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.Item;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.view.background.BackgroundRenderer;
import com.arkanoid.view.ball.MultiBallRenderer;
import com.arkanoid.view.border.BorderRenderer;
import com.arkanoid.view.brick.BrickRenderer;
import com.arkanoid.view.hud.HUDRenderer;
import com.arkanoid.view.paddle.BasePaddleRenderer;
import com.arkanoid.view.paddle.ExpandablePaddleRenderer;
import com.arkanoid.view.paddle.LaserPaddleRenderer;
import com.arkanoid.view.paddle.StickyPaddleRenderer;
import com.arkanoid.view.paddle.NormalPaddleRenderer;
import com.arkanoid.view.effects.EffectRenderer;
import com.arkanoid.view.playground.PlayGroundRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class GameView extends Pane {
    private final GameModel gameModel;
    private final MultiBallRenderer multiBallRenderer;
    private final BrickRenderer brickRenderer;
    private final HUDRenderer hudRenderer;
    private final BorderRenderer borderRenderer;
    private final PlayGroundRenderer playGroundRenderer;
    private final BackgroundRenderer backgroundRenderer;
    private final EffectRenderer effectRenderer;

    private BasePaddleRenderer currentPaddleRenderer;
    private final NormalPaddleRenderer normalRenderer;
    private final ExpandablePaddleRenderer expandableRenderer;
    private final LaserPaddleRenderer laserRenderer;
    private final StickyPaddleRenderer stickyRenderer;

    private final Canvas itemCanvas;
    private final GraphicsContext gcItem;

    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;

        multiBallRenderer = new MultiBallRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);
        borderRenderer = new BorderRenderer(gameModel);
        effectRenderer = new EffectRenderer(gameModel);
        playGroundRenderer = new PlayGroundRenderer(gameModel);
        backgroundRenderer = new BackgroundRenderer();
        normalRenderer = new NormalPaddleRenderer(gameModel);
        expandableRenderer = new ExpandablePaddleRenderer(gameModel);
        laserRenderer = new LaserPaddleRenderer(gameModel);
        stickyRenderer = new StickyPaddleRenderer(gameModel);

        itemCanvas = new Canvas(800, 800);
        gcItem = itemCanvas.getGraphicsContext2D();

        Ball mainBall = gameModel.getBall();
        multiBallRenderer.addBall(mainBall);
        multiBallRenderer.addBall(mainBall);

        currentPaddleRenderer = normalRenderer;

        getChildren().add(backgroundRenderer.getNode());
        getChildren().add(playGroundRenderer.getNode());
        getChildren().add(multiBallRenderer.getNode());
        getChildren().addAll(brickRenderer.getNodes());
        getChildren().addAll(hudRenderer.getNodes());
//        getChildren().addAll(borderRenderer.getNode());
        getChildren().add(effectRenderer.getCanvas());
        getChildren().add(currentPaddleRenderer.getNode());
        getChildren().add(itemCanvas);
    }

    public void render() {
        multiBallRenderer.render();
        brickRenderer.render();
        effectRenderer.render();
        hudRenderer.render();
        borderRenderer.render();

        Paddle paddle = gameModel.getPaddle();
        BasePaddleRenderer newRenderer = null;

        if (paddle instanceof LaserPaddle) {
            newRenderer = laserRenderer;
        } else if (paddle instanceof StickyPaddle) {
            newRenderer = stickyRenderer;
        } else if (paddle instanceof ExpandablePaddle) {
            newRenderer = expandableRenderer;
        } else {
            newRenderer = normalRenderer;
        }

        if (newRenderer != currentPaddleRenderer) {
            getChildren().remove(currentPaddleRenderer.getNode());
            getChildren().add(newRenderer.getNode());
            currentPaddleRenderer = newRenderer;
        }

        currentPaddleRenderer.render();

        gcItem.clearRect(0, 0, itemCanvas.getWidth(), itemCanvas.getHeight());
        for (Item vatPham : gameModel.getItems()) {
            vatPham.ve(gcItem);
        }
    }

    public void cleanup() {
        backgroundRenderer.cleanup();
    }
}