package com.arkanoid.view.hud;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.GameState;
import com.arkanoid.utils.ThemeManager;
import com.arkanoid.view.hud.components.LivesPanel;
import com.arkanoid.view.hud.components.MessagePanel;
import com.arkanoid.view.hud.components.ScorePanel;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.arkanoid.CONSTANT.*;

public class HUDRenderer {
    private final GameModel gameModel;
    private final Canvas hudCanvas;
    private final GraphicsContext gc;
    private AnimationTimer animationTimer;
    private double time = 0;

    private final ScorePanel scorePanel;
    private final LivesPanel livesPanel;
    private final MessagePanel messagePanel;
    private final ThemeManager themeManager;

    private double messagePulse = 0;

    public HUDRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.themeManager = ThemeManager.getInstance();

        hudCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        gc = hudCanvas.getGraphicsContext2D();

        scorePanel = new ScorePanel(10, 10, 200, 60);
        livesPanel = new LivesPanel(WINDOW_WIDTH - 210, 10, 200, 60);
        messagePanel = new MessagePanel(WINDOW_WIDTH, WINDOW_HEIGHT);

        startAnimation();
    }

    private void startAnimation() {
        animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                time += deltaTime;
                messagePulse = 0.7 + 0.3 * Math.sin(time * 3);
            }
        };
        animationTimer.start();
    }

    public void render() {
        gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        Color themeColor = themeManager.getPrimaryColor();

        scorePanel.render(gc, time, gameModel.getScore(), themeColor);
        livesPanel.render(gc, time, gameModel.getLives(), themeColor);

        GameState currentState = gameModel.getGameState();

//        if (currentState == GameState.Ready) {
//            messagePanel.render(gc, "PRESS SPACE TO START", themeColor, messagePulse);
//        }
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(hudCanvas);
        return nodes;
    }

    public void cleanup() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}