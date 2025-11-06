package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.view.PowerUp.PowerUpRenderer;
import com.arkanoid.view.background.BackgroundRenderer;
import com.arkanoid.view.ball.MultiBallRenderer;
import com.arkanoid.view.brick.BrickRenderer;
import com.arkanoid.view.Coin.CoinRenderer;
import com.arkanoid.view.hud.HUDRenderer;
import com.arkanoid.view.paddle.BasePaddleRenderer;
import com.arkanoid.view.paddle.ExpandablePaddleRenderer;
import com.arkanoid.view.paddle.LaserPaddleRenderer;
import com.arkanoid.view.paddle.StickyPaddleRenderer;
import com.arkanoid.view.paddle.NormalPaddleRenderer;
import com.arkanoid.view.effects.EffectRenderer;
import com.arkanoid.view.playground.PlayGroundRenderer;
import com.arkanoid.view.transition.LevelTransitionRenderer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameView extends Pane {
    private final GameModel gameModel;
    private final MultiBallRenderer multiBallRenderer;
    private final BrickRenderer brickRenderer;
    private final HUDRenderer hudRenderer;
    private final PlayGroundRenderer playGroundRenderer;
    private final BackgroundRenderer backgroundRenderer;
    private final EffectRenderer effectRenderer;
    private final PowerUpRenderer powerUpRenderer;
    private final CoinRenderer coinRenderer;
    private final LevelTransitionRenderer transitionRenderer;

    private BasePaddleRenderer currentPaddleRenderer;
    private final NormalPaddleRenderer normalRenderer;
    private final ExpandablePaddleRenderer expandableRenderer;
    private final LaserPaddleRenderer laserRenderer;
    private final StickyPaddleRenderer stickyRenderer;
    private List<Node> currentBrickNodes = new ArrayList<>();

    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;

        multiBallRenderer = new MultiBallRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);
        effectRenderer = new EffectRenderer(gameModel);
        playGroundRenderer = new PlayGroundRenderer(gameModel);
        backgroundRenderer = new BackgroundRenderer();
        normalRenderer = new NormalPaddleRenderer(gameModel);
        expandableRenderer = new ExpandablePaddleRenderer(gameModel);
        laserRenderer = new LaserPaddleRenderer(gameModel);
        stickyRenderer = new StickyPaddleRenderer(gameModel);
        powerUpRenderer = new PowerUpRenderer(gameModel);
        coinRenderer = new CoinRenderer(gameModel);
        transitionRenderer = new LevelTransitionRenderer();

        Ball mainBall = gameModel.getBall();
        multiBallRenderer.addBall(mainBall);
        multiBallRenderer.addBall(mainBall);

        currentPaddleRenderer = normalRenderer;

        getChildren().add(backgroundRenderer.getNode());
        getChildren().add(playGroundRenderer.getNode());
        getChildren().add(multiBallRenderer.getNode());
        getChildren().addAll(hudRenderer.getNodes());
        getChildren().add(effectRenderer.getCanvas());
        getChildren().add(currentPaddleRenderer.getNode());
        getChildren().add(powerUpRenderer.getCanvas());
        getChildren().add(coinRenderer.getNode());
        getChildren().add(transitionRenderer.getNode());
    }

    public void synchronizeView() {
        getChildren().removeAll(currentBrickNodes);
        currentBrickNodes = brickRenderer.createAndGetNodes();

        int playGroundIndex = getChildren().indexOf(playGroundRenderer.getNode());
        getChildren().addAll(playGroundIndex + 1, currentBrickNodes);
    }

    public void showLevelStart(int level, Runnable onComplete) {
        transitionRenderer.showLevelStart(level, onComplete);
    }

    public void showLevelClear(Runnable onComplete) {
        transitionRenderer.showLevelClear(onComplete);
    }

    public void render() {
        multiBallRenderer.render();
        brickRenderer.render();
        effectRenderer.render();
        hudRenderer.render();
        powerUpRenderer.render();
        coinRenderer.render();

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

            int powerUpIndex = getChildren().indexOf(powerUpRenderer.getCanvas());
            getChildren().add(powerUpIndex, newRenderer.getNode());

            currentPaddleRenderer = newRenderer;
        }

        currentPaddleRenderer.render();
    }

    public void cleanup() {
        backgroundRenderer.cleanup();
        powerUpRenderer.cleanup();
        coinRenderer.cleanup();
        transitionRenderer.cleanup();
    }
}