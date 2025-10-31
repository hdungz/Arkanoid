package com.arkanoid.view.playground;

import com.arkanoid.model.GameModel;
import com.arkanoid.view.playground.components.CornerLight;
import com.arkanoid.view.playground.components.FloatingParticle;
import com.arkanoid.view.playground.components.GridLine;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.arkanoid.CONSTANT.*;


public class PlayGroundRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final List<GridLine> gridLines;
    private final List<FloatingParticle> particles;
    private final List<CornerLight> cornerLights;
    private final Random random;
    private final GameModel gameModel;
    private AnimationTimer animationTimer;
    private double time = 0;

    private double topFlash = 0;
    private double leftFlash = 0;
    private double rightFlash = 0;
    private GameModel.WallCollisionSide lastCheckedCollision = GameModel.WallCollisionSide.NONE;

    public PlayGroundRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        canvas = new Canvas(GAME_AREA_WIDTH, WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setLayoutX(GAME_AREA_X);
        canvas.setLayoutY(0);

        random = new Random();
        gridLines = new ArrayList<>();
        particles = new ArrayList<>();
        cornerLights = new ArrayList<>();

        initializeComponents();
        startAnimation();
    }

    private void initializeComponents() {
        cornerLights.add(new CornerLight(20, 20, CornerLight.CornerPosition.TOP_LEFT));
        cornerLights.add(new CornerLight(GAME_AREA_WIDTH - 20, 20, CornerLight.CornerPosition.TOP_RIGHT));
        cornerLights.add(new CornerLight(20, WINDOW_HEIGHT - 20, CornerLight.CornerPosition.BOTTOM_LEFT));
        cornerLights.add(new CornerLight(GAME_AREA_WIDTH - 20, WINDOW_HEIGHT - 20, CornerLight.CornerPosition.BOTTOM_RIGHT));

        for (int x = 0; x < GAME_AREA_WIDTH; x += 50) {
            gridLines.add(new GridLine(x, 0, x, WINDOW_HEIGHT, true));
        }
        for (int y = 0; y < WINDOW_HEIGHT; y += 50) {
            gridLines.add(new GridLine(0, y, GAME_AREA_WIDTH, y, false));
        }

        for (int i = 0; i < 50; i++) {
            particles.add(new FloatingParticle(
                    random.nextDouble() * GAME_AREA_WIDTH,
                    random.nextDouble() * WINDOW_HEIGHT,
                    random.nextDouble() * 2.5 + 1,
                    random.nextDouble() * 20 + 10
            ));
        }
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
                updateFlash(deltaTime);
                render();
            }
        };
        animationTimer.start();
    }

    private void updateFlash(double deltaTime) {
        GameModel.WallCollisionSide currentCollision = gameModel.getLastWallCollision();

        if (currentCollision != lastCheckedCollision && currentCollision != GameModel.WallCollisionSide.NONE) {
            switch (currentCollision) {
                case TOP:
                    topFlash = 1.0;
                    break;
                case LEFT:
                    leftFlash = 1.0;
                    break;
                case RIGHT:
                    rightFlash = 1.0;
                    break;
            }
            lastCheckedCollision = currentCollision;
        }

        if (currentCollision == GameModel.WallCollisionSide.NONE) {
            lastCheckedCollision = GameModel.WallCollisionSide.NONE;
        }

        if (topFlash > 0) topFlash -= deltaTime * 3;
        if (leftFlash > 0) leftFlash -= deltaTime * 3;
        if (rightFlash > 0) rightFlash -= deltaTime * 3;

        topFlash = Math.max(0, topFlash);
        leftFlash = Math.max(0, leftFlash);
        rightFlash = Math.max(0, rightFlash);
    }

    private void render() {
        gc.clearRect(0, 0, GAME_AREA_WIDTH, WINDOW_HEIGHT);

        drawBackground();

        gc.setGlobalAlpha(0.25);
        for (GridLine line : gridLines) {
            line.update(time);
            line.render(gc);
        }
        gc.setGlobalAlpha(1.0);

        gc.setGlobalAlpha(0.5);
        for (FloatingParticle particle : particles) {
            particle.update(time);
            particle.render(gc);
        }
        gc.setGlobalAlpha(1.0);

        for (CornerLight light : cornerLights) {
            light.update(time);
            light.render(gc);
        }

        drawBorderGlow();
        drawCollisionFlash();
        drawScanLines();
        drawFrameBorder();
    }

    private void drawBackground() {
        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(20, 35, 70, 0.92)),
                new Stop(0.5, Color.rgb(25, 45, 90, 0.95)),
                new Stop(1, Color.rgb(18, 38, 75, 0.93))
        };
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, WINDOW_HEIGHT,
                false, CycleMethod.NO_CYCLE, stops
        );
        gc.setFill(gradient);
        gc.fillRect(0, 0, GAME_AREA_WIDTH, WINDOW_HEIGHT);
    }

    private void drawBorderGlow() {
        double pulse = 0.4 + 0.3 * Math.sin(time * 2);

        Stop[] leftStops = new Stop[] {
                new Stop(0, Color.CYAN.deriveColor(0, 1, 1.2, pulse * 0.6)),
                new Stop(1, Color.TRANSPARENT)
        };
        LinearGradient leftGlow = new LinearGradient(
                0, 0, 20, 0,
                false, CycleMethod.NO_CYCLE, leftStops
        );
        gc.setFill(leftGlow);
        gc.fillRect(0, 0, 20, WINDOW_HEIGHT);

        Stop[] rightStops = new Stop[] {
                new Stop(0, Color.TRANSPARENT),
                new Stop(1, Color.CYAN.deriveColor(0, 1, 1.2, pulse * 0.6))
        };
        LinearGradient rightGlow = new LinearGradient(
                GAME_AREA_WIDTH - 20, 0, GAME_AREA_WIDTH, 0,
                false, CycleMethod.NO_CYCLE, rightStops
        );
        gc.setFill(rightGlow);
        gc.fillRect(GAME_AREA_WIDTH - 20, 0, 20, WINDOW_HEIGHT);

        Stop[] topStops = new Stop[] {
                new Stop(0, Color.CYAN.deriveColor(0, 1, 1.2, pulse * 0.6)),
                new Stop(1, Color.TRANSPARENT)
        };
        LinearGradient topGlow = new LinearGradient(
                0, 0, 0, 20,
                false, CycleMethod.NO_CYCLE, topStops
        );
        gc.setFill(topGlow);
        gc.fillRect(0, 0, GAME_AREA_WIDTH, 20);
    }

    private void drawCollisionFlash() {
        gc.setGlobalBlendMode(BlendMode.ADD);

        if (topFlash > 0) {
            Stop[] stops = new Stop[] {
                    new Stop(0, Color.WHITE.deriveColor(0, 1, 1, topFlash * 0.8)),
                    new Stop(0.5, Color.CYAN.deriveColor(0, 1, 1.5, topFlash * 0.6)),
                    new Stop(1, Color.TRANSPARENT)
            };
            LinearGradient flash = new LinearGradient(
                    0, 0, 0, 40,
                    false, CycleMethod.NO_CYCLE, stops
            );
            gc.setFill(flash);
            gc.fillRect(0, 0, GAME_AREA_WIDTH, 40);
        }

        if (leftFlash > 0) {
            Stop[] stops = new Stop[] {
                    new Stop(0, Color.WHITE.deriveColor(0, 1, 1, leftFlash * 0.8)),
                    new Stop(0.5, Color.CYAN.deriveColor(0, 1, 1.5, leftFlash * 0.6)),
                    new Stop(1, Color.TRANSPARENT)
            };
            LinearGradient flash = new LinearGradient(
                    0, 0, 40, 0,
                    false, CycleMethod.NO_CYCLE, stops
            );
            gc.setFill(flash);
            gc.fillRect(0, 0, 40, WINDOW_HEIGHT);
        }

        if (rightFlash > 0) {
            Stop[] stops = new Stop[] {
                    new Stop(0, Color.TRANSPARENT),
                    new Stop(0.5, Color.CYAN.deriveColor(0, 1, 1.5, rightFlash * 0.6)),
                    new Stop(1, Color.WHITE.deriveColor(0, 1, 1, rightFlash * 0.8))
            };
            LinearGradient flash = new LinearGradient(
                    GAME_AREA_WIDTH - 40, 0, GAME_AREA_WIDTH, 0,
                    false, CycleMethod.NO_CYCLE, stops
            );
            gc.setFill(flash);
            gc.fillRect(GAME_AREA_WIDTH - 40, 0, 40, WINDOW_HEIGHT);
        }

        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    private void drawFrameBorder() {
        double pulse = 0.5 + 0.3 * Math.sin(time * 2);

        gc.setStroke(Color.CYAN.deriveColor(0, 1, 1.3, pulse));
        gc.setLineWidth(3);
        gc.strokeRect(1.5, 1.5, GAME_AREA_WIDTH - 3, WINDOW_HEIGHT - 3);

        gc.setStroke(Color.WHITE.deriveColor(0, 1, 1, pulse * 0.5));
        gc.setLineWidth(1);
        gc.strokeRect(3, 3, GAME_AREA_WIDTH - 6, WINDOW_HEIGHT - 6);
    }

    private void drawScanLines() {
        gc.setGlobalAlpha(0.08);
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        for (int y = 0; y < WINDOW_HEIGHT; y += 3) {
            gc.strokeLine(0, y, GAME_AREA_WIDTH, y);
        }
        gc.setGlobalAlpha(1.0);
    }

    public Node getNode() {
        return canvas;
    }

    public void cleanup() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}