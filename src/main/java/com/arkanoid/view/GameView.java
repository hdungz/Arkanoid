package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameState;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.utils.LevelManager;
import com.arkanoid.utils.ThemeManager;
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
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.arkanoid.view.LevelTransition.LevelTransitionRenderer;

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
    private final PauseView pauseView;
    private final LevelTransitionRenderer transitionRenderer;
    private final GameOverView gameOverView;

    private BasePaddleRenderer currentPaddleRenderer;
    private final NormalPaddleRenderer normalRenderer;
    private final ExpandablePaddleRenderer expandableRenderer;
    private final LaserPaddleRenderer laserRenderer;
    private final StickyPaddleRenderer stickyRenderer;

    private List<Node> currentBrickNodes = new ArrayList<>();
    private int lastBallCount = 0;
    private Set<Ball> trackedBalls = new HashSet<>();

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
        pauseView = new PauseView();
        gameOverView = new GameOverView();
        transitionRenderer = new LevelTransitionRenderer(gameModel.getTransitionManager()); 

        Ball mainBall = gameModel.getBall();
        multiBallRenderer.addBall(mainBall);
        trackedBalls.add(mainBall);

        lastBallCount = 1;

        currentPaddleRenderer = normalRenderer;

        getChildren().add(backgroundRenderer.getNode());
        getChildren().add(playGroundRenderer.getNode());
        getChildren().add(effectRenderer.getCanvas());
        getChildren().add(powerUpRenderer.getCanvas());
        getChildren().add(coinRenderer.getNode());
        getChildren().add(multiBallRenderer.getNode());
        getChildren().addAll(hudRenderer.getNodes());
        getChildren().add(currentPaddleRenderer.getNode());
        getChildren().add(transitionRenderer.getNode()); 
        getChildren().add(pauseView);
        getChildren().add(gameOverView);
    }

    public void synchronizeView() {
        getChildren().removeAll(currentBrickNodes);
        currentBrickNodes = brickRenderer.createAndGetNodes();

        ThemeManager.getInstance().setThemeForLevel(LevelManager.getInstance().getCurrentLevel());

        int playGroundIndex = getChildren().indexOf(playGroundRenderer.getNode());
        getChildren().addAll(playGroundIndex + 1, currentBrickNodes);

        multiBallRenderer.cleanup();
        trackedBalls.clear();

        Ball mainBall = gameModel.getBall();
        multiBallRenderer.addBall(mainBall);
        trackedBalls.add(mainBall);
        lastBallCount = 1;
    }

    private void resetBallTracking() {
        trackedBalls.clear();
        Ball mainBall = gameModel.getBall();
        trackedBalls.add(mainBall);
        lastBallCount = 1;
    }

    private void syncBalls() {
        ArrayList<Ball> extraBalls = gameModel.getExtraBalls();

        for (Ball ball : extraBalls) {
            if (!trackedBalls.contains(ball)) {
                multiBallRenderer.addBall(ball);
                trackedBalls.add(ball);
                System.out.println("Added new ball to renderer. Total tracked: " + trackedBalls.size());
            }
        }

        List<Ball> ballsToRemove = new ArrayList<>();
        for (Ball ball : trackedBalls) {
            if (ball != gameModel.getBall() && !extraBalls.contains(ball)) {
                ballsToRemove.add(ball);
            }
        }

        for (Ball ball : ballsToRemove) {
            multiBallRenderer.removeBall(ball);
            trackedBalls.remove(ball);
            System.out.println("Removed ball from renderer. Total tracked: " + trackedBalls.size());
        }

        lastBallCount = gameModel.getTotalBallCount();
    }

    public void showPause() {
        pauseView.show();
    }

    public void hidePause() {
        pauseView.hide();
    }

    public void showGameOver() {
        gameOverView.setScore(gameModel.getScore());
        gameOverView.setGameComplete(false);
        gameOverView.show();
    }

    public void showGameComplete() {
        gameOverView.setScore(gameModel.getScore());
        gameOverView.setGameComplete(true);
        gameOverView.show();
    }

    public void hideGameOver() {
        gameOverView.hide();
    }

    public PauseView getPauseView() {
        return pauseView;
    }

    public GameOverView getGameOverView() {
        return gameOverView;
    }

    public void render() {
        if (gameModel.checkAndConsumeViewSync()) {
            synchronizeView();
        }

        // Tự động kiểm tra và hiển thị Game Over
        if (gameModel.getGameState() == GameState.GameOver && !gameOverView.isVisible()) {
            int currentLevel = gameModel.getCurrentLevel();
            if (currentLevel > 20) {
                showGameComplete();
            } else {
                showGameOver();
            }
        }

        int currentBallCount = gameModel.getTotalBallCount();
        if (currentBallCount != lastBallCount) {
            syncBalls();
        }

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

            int effectIndex = getChildren().indexOf(effectRenderer.getCanvas());
            getChildren().add(effectIndex, newRenderer.getNode());

            currentPaddleRenderer = newRenderer;
        }

        currentPaddleRenderer.render();

        normalRenderer.refreshPaddleAsset();
        laserRenderer.refreshPaddleAssetLaser();
        stickyRenderer.refreshPaddleAssetSticky();
        expandableRenderer.refreshPaddleAsset();

        // Render transition effects
        transitionRenderer.render();
    }

    public void cleanup() {
        backgroundRenderer.cleanup();
        powerUpRenderer.cleanup();
        coinRenderer.cleanup();
//        transitionRenderer.cleanup();
        trackedBalls.clear();
    }
}