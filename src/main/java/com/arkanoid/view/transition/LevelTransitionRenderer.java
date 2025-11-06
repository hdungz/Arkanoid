package com.arkanoid.view.transition;

import com.arkanoid.utils.ThemeManager;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LevelTransitionRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final ThemeManager themeManager;

    private TransitionState currentState = TransitionState.NONE;
    private double transitionProgress = 0;
    private int displayLevel = 1;
    private AnimationTimer animationTimer;
    private Runnable onComplete;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final double TRANSITION_DURATION = 2.5;

    public enum TransitionState {
        NONE,
        LEVEL_START,
        LEVEL_CLEAR
    }

    public LevelTransitionRenderer() {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        themeManager = ThemeManager.getInstance();
        canvas.setMouseTransparent(true);
        canvas.setVisible(false);
    }

    public void showLevelStart(int level, Runnable onComplete) {
        this.displayLevel = level;
        this.currentState = TransitionState.LEVEL_START;
        this.transitionProgress = 0;
        this.onComplete = onComplete;
        canvas.setVisible(true);
        startAnimation();
    }

    public void showLevelClear(Runnable onComplete) {
        this.currentState = TransitionState.LEVEL_CLEAR;
        this.transitionProgress = 0;
        this.onComplete = onComplete;
        canvas.setVisible(true);
        startAnimation();
    }

    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

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

                transitionProgress += deltaTime;

                if (transitionProgress >= TRANSITION_DURATION) {
                    stop();
                    canvas.setVisible(false);
                    currentState = TransitionState.NONE;
                    if (onComplete != null) {
                        onComplete.run();
                    }
                    return;
                }

                render();
            }
        };
        animationTimer.start();
    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        Color themeColor = themeManager.getPrimaryColor();

        if (currentState == TransitionState.LEVEL_START) {
            renderLevelStart(themeColor);
        } else if (currentState == TransitionState.LEVEL_CLEAR) {
            renderLevelClear(themeColor);
        }
    }

    private void renderLevelStart(Color themeColor) {
        double progress = transitionProgress / TRANSITION_DURATION;

        if (progress < 0.3) {
            double fadeIn = progress / 0.3;
            renderBackground(themeColor, fadeIn * 0.8);
            renderLevelText(themeColor, fadeIn);
        } else if (progress < 0.7) {
            renderBackground(themeColor, 0.8);
            renderLevelText(themeColor, 1.0);
        } else {
            double fadeOut = 1.0 - ((progress - 0.7) / 0.3);
            renderBackground(themeColor, fadeOut * 0.8);
            renderLevelText(themeColor, fadeOut);
        }
    }

    private void renderLevelClear(Color themeColor) {
        double progress = transitionProgress / TRANSITION_DURATION;

        if (progress < 0.3) {
            double fadeIn = progress / 0.3;
            renderBackground(themeColor, fadeIn * 0.8);
            renderClearText(themeColor, fadeIn);
        } else if (progress < 0.7) {
            renderBackground(themeColor, 0.8);
            renderClearText(themeColor, 1.0);
            renderParticles(themeColor, (progress - 0.3) / 0.4);
        } else {
            double fadeOut = 1.0 - ((progress - 0.7) / 0.3);
            renderBackground(themeColor, fadeOut * 0.8);
            renderClearText(themeColor, fadeOut);
            renderParticles(themeColor, 1.0 - fadeOut);
        }
    }

    private void renderBackground(Color themeColor, double alpha) {
        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(0, 0, 0, alpha)),
                new Stop(0.5, Color.rgb(
                        (int)(themeColor.getRed() * 255 * 0.1),
                        (int)(themeColor.getGreen() * 255 * 0.1),
                        (int)(themeColor.getBlue() * 255 * 0.1),
                        alpha * 0.8
                )),
                new Stop(1, Color.rgb(0, 0, 0, alpha))
        };

        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.8, true,
                CycleMethod.NO_CYCLE, stops
        );

        gc.setFill(gradient);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void renderLevelText(Color themeColor, double alpha) {
        double centerX = WIDTH / 2.0;
        double centerY = HEIGHT / 2.0;

        Font titleFont = Font.font("Arial", FontWeight.BOLD, 80);
        Font levelFont = Font.font("Arial", FontWeight.BOLD, 120);

        double pulse = 1.0 + 0.1 * Math.sin(transitionProgress * 8);

        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(themeColor.deriveColor(0, 1, 1.5, alpha * 0.5));
        gc.setFont(titleFont);
        String startText = "LEVEL";
        double startWidth = getTextWidth(startText, titleFont);
        gc.fillText(startText, centerX - startWidth / 2 - 2, centerY - 60 - 2);
        gc.fillText(startText, centerX - startWidth / 2 + 2, centerY - 60 + 2);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, alpha));
        gc.fillText(startText, centerX - startWidth / 2, centerY - 60);

        gc.setFont(levelFont);
        String levelText = String.valueOf(displayLevel);
        double levelWidth = getTextWidth(levelText, levelFont);

        gc.save();
        gc.translate(centerX, centerY + 40);
        gc.scale(pulse, pulse);

        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(themeColor.deriveColor(0, 1, 1.5, alpha * 0.6));
        gc.fillText(levelText, -levelWidth / 2 - 3, 3);
        gc.fillText(levelText, -levelWidth / 2 + 3, -3);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(themeColor.deriveColor(0, 1, 1.2, alpha));
        gc.fillText(levelText, -levelWidth / 2, 0);

        gc.restore();

        renderBorder(centerX, centerY, themeColor, alpha);
    }

    private void renderClearText(Color themeColor, double alpha) {
        double centerX = WIDTH / 2.0;
        double centerY = HEIGHT / 2.0;

        Font clearFont = Font.font("Arial", FontWeight.BOLD, 100);

        double pulse = 1.0 + 0.15 * Math.sin(transitionProgress * 10);

        gc.save();
        gc.translate(centerX, centerY);
        gc.scale(pulse, pulse);

        String clearText = "LEVEL CLEAR!";
        double textWidth = getTextWidth(clearText, clearFont);

        gc.setFont(clearFont);

        gc.setGlobalBlendMode(BlendMode.ADD);
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                gc.setFill(themeColor.deriveColor(0, 1, 1.5, alpha * 0.3));
                gc.fillText(clearText, -textWidth / 2 + i * 2, j * 2);
            }
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        LinearGradient textGradient = new LinearGradient(
                0, -50, 0, 50, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE.deriveColor(0, 1, 1, alpha)),
                new Stop(0.5, themeColor.deriveColor(0, 1, 1.3, alpha)),
                new Stop(1, Color.WHITE.deriveColor(0, 1, 1, alpha))
        );
        gc.setFill(textGradient);
        gc.fillText(clearText, -textWidth / 2, 0);

        gc.restore();

        renderBorder(centerX, centerY, themeColor, alpha);
    }

    private void renderParticles(Color themeColor, double progress) {
        gc.setGlobalBlendMode(BlendMode.ADD);

        for (int i = 0; i < 30; i++) {
            double angle = (i / 30.0) * Math.PI * 2 + transitionProgress * 2;
            double distance = 150 + progress * 300;
            double x = WIDTH / 2 + Math.cos(angle) * distance;
            double y = HEIGHT / 2 + Math.sin(angle) * distance;

            double size = 8 * (1 - progress);
            double alpha = (1 - progress) * 0.8;

            RadialGradient glow = new RadialGradient(
                    0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, themeColor.deriveColor(0, 1, 1.5, alpha)),
                    new Stop(1, Color.TRANSPARENT)
            );

            gc.setFill(glow);
            gc.fillOval(x - size * 2, y - size * 2, size * 4, size * 4);

            gc.setFill(Color.WHITE.deriveColor(0, 1, 1, alpha));
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }

        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    private void renderBorder(double centerX, double centerY, Color themeColor, double alpha) {
        double width = 600;
        double height = 300;
        double x = centerX - width / 2;
        double y = centerY - height / 2;

        double pulse = 0.7 + 0.3 * Math.sin(transitionProgress * 5);

        gc.setStroke(themeColor.deriveColor(0, 1, 1.3, alpha * pulse));
        gc.setLineWidth(4);
        gc.strokeRoundRect(x, y, width, height, 20, 20);

        gc.setStroke(Color.WHITE.deriveColor(0, 1, 1, alpha * pulse * 0.5));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + 4, y + 4, width - 8, height - 8, 16, 16);

        double cornerSize = 30;
        gc.setLineWidth(5);
        gc.setStroke(themeColor.deriveColor(0, 1, 1.5, alpha));

        gc.strokeLine(x + 10, y, x + 10 + cornerSize, y);
        gc.strokeLine(x, y + 10, x, y + 10 + cornerSize);

        gc.strokeLine(x + width - 10 - cornerSize, y, x + width - 10, y);
        gc.strokeLine(x + width, y + 10, x + width, y + 10 + cornerSize);

        gc.strokeLine(x + 10, y + height, x + 10 + cornerSize, y + height);
        gc.strokeLine(x, y + height - 10 - cornerSize, x, y + height - 10);

        gc.strokeLine(x + width - 10 - cornerSize, y + height, x + width - 10, y + height);
        gc.strokeLine(x + width, y + height - 10 - cornerSize, x + width, y + height - 10);
    }

    private double getTextWidth(String text, Font font) {
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        tempText.setFont(font);
        return tempText.getLayoutBounds().getWidth();
    }

    public Node getNode() {
        return canvas;
    }

    public void cleanup() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public boolean isPlaying() {
        return currentState != TransitionState.NONE;
    }
}