package com.arkanoid.view;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class MenuView extends StackPane {

    private final Button playButton;
    private final Button storeButton;
    private final Button highscoreButton;
    private final Button exitButton;
    private final VBox menuBox;
    private final Canvas backgroundCanvas;
    private final AnimationTimer backgroundAnimation;
    private double time = 0;

    // Background elements
    private final List<Star> stars = new ArrayList<>();
    private final List<CircuitLine> circuits = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    public MenuView() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Background canvas
        backgroundCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        initializeBackground();
        backgroundAnimation = createBackgroundAnimation();
        backgroundAnimation.start();

        getChildren().add(backgroundCanvas);

        // Title
        Text titleText = createTitle();

        // Menu buttons
        menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        playButton = createStyledButton("START GAME", Color.CYAN);
        storeButton = createStyledButton("STORE", Color.rgb(150, 100, 255));
        highscoreButton = createStyledButton("HIGH SCORES", Color.rgb(100, 255, 150));
        exitButton = createStyledButton("EXIT", Color.rgb(255, 100, 100));

        menuBox.getChildren().addAll(playButton, storeButton, highscoreButton, exitButton);

        VBox content = new VBox(60, titleText, menuBox);
        content.setAlignment(Pos.CENTER);
        getChildren().add(content);
    }

    private void initializeBackground() {
        // Create stars
        for (int i = 0; i < 200; i++) {
            stars.add(new Star(
                    random.nextDouble() * WINDOW_WIDTH,
                    random.nextDouble() * WINDOW_HEIGHT,
                    random.nextDouble() * 2.5 + 0.5,
                    random.nextDouble() * 0.6 + 0.4,
                    random.nextDouble() * 2 + 0.8
            ));
        }

        // Create circuit lines
        circuits.add(new CircuitLine(50, 100, 300, 100, 2));
        circuits.add(new CircuitLine(300, 100, 300, 400, 1.5));
        circuits.add(new CircuitLine(WINDOW_WIDTH - 300, 150, WINDOW_WIDTH - 50, 150, 1.8));
        circuits.add(new CircuitLine(WINDOW_WIDTH - 300, 150, WINDOW_WIDTH - 300, 500, 1.6));
        circuits.add(new CircuitLine(150, WINDOW_HEIGHT - 200, 400, WINDOW_HEIGHT - 200, 2.2));
        circuits.add(new CircuitLine(WINDOW_WIDTH - 400, WINDOW_HEIGHT - 250, WINDOW_WIDTH - 150, WINDOW_HEIGHT - 250, 1.9));

        // Create floating particles
        for (int i = 0; i < 40; i++) {
            particles.add(new Particle(
                    random.nextDouble() * WINDOW_WIDTH,
                    random.nextDouble() * WINDOW_HEIGHT,
                    random.nextDouble() * 2 + 1,
                    random.nextDouble() * 30 + 20
            ));
        }
    }

    private AnimationTimer createBackgroundAnimation() {
        return new AnimationTimer() {
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
                renderBackground();
            }
        };
    }

    private void renderBackground() {
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();

        // Clear and draw gradient background
        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(5, 10, 25)),
                new Stop(0.5, Color.rgb(10, 20, 40)),
                new Stop(1, Color.rgb(5, 10, 30))
        };
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.8, true,
                CycleMethod.NO_CYCLE, stops
        );
        gc.setFill(gradient);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Draw stars
        for (Star star : stars) {
            star.update(time);
            star.render(gc);
        }

        // Draw circuit lines
        gc.setGlobalBlendMode(BlendMode.ADD);
        for (CircuitLine circuit : circuits) {
            circuit.update(time);
            circuit.render(gc);
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        // Draw particles
        gc.setGlobalAlpha(0.5);
        for (Particle particle : particles) {
            particle.update(time);
            particle.render(gc);
        }
        gc.setGlobalAlpha(1.0);

        // Scan lines
        gc.setGlobalAlpha(0.03);
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        for (int y = 0; y < WINDOW_HEIGHT; y += 4) {
            gc.strokeLine(0, y, WINDOW_WIDTH, y);
        }
        gc.setGlobalAlpha(1.0);
    }

    private Text createTitle() {
        Text title = new Text("ARKANOID");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 80));
        title.setFill(Color.WHITE);

        // Gradient fill
        Stop[] stops = new Stop[] {
                new Stop(0, Color.CYAN),
                new Stop(0.5, Color.WHITE),
                new Stop(1, Color.CYAN)
        };
        LinearGradient titleGradient = new LinearGradient(
                0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops
        );
        title.setFill(titleGradient);

        // Add glow effect
        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(Color.CYAN);
        glow.setRadius(30);
        glow.setSpread(0.6);
        title.setEffect(glow);

        return title;
    }

    private Button createStyledButton(String text, Color accentColor) {
        Button button = new Button(text);

        // Font
        button.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        button.setPrefWidth(300);
        button.setPrefHeight(50);

        // Style
        updateButtonStyle(button, accentColor, false);

        // Hover effects
        button.setOnMouseEntered(e -> {
            updateButtonStyle(button, accentColor, true);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            updateButtonStyle(button, accentColor, false);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }

    private void updateButtonStyle(Button button, Color accentColor, boolean hover) {
        String baseStyle = "-fx-background-color: linear-gradient(to bottom, " +
                "rgba(20, 40, 70, 0.8), rgba(10, 25, 50, 0.9)); " +
                "-fx-text-fill: %s; " +
                "-fx-border-color: %s; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, %s, %d, 0.0, 0, 0);";

        String textColor = hover ? "white" : toWebColor(accentColor);
        String borderColor = toWebColor(accentColor);
        String glowColor = toWebColor(accentColor);
        int glowRadius = hover ? 20 : 10;

        button.setStyle(String.format(baseStyle, textColor, borderColor, glowColor, glowRadius));
    }

    private String toWebColor(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                color.getOpacity()
        );
    }

    public Button getPlayButton() { return playButton; }
    public Button getStoreButton() { return storeButton; }
    public Button getHighscoreButton() { return highscoreButton; }
    public Button getExitButton() { return exitButton; }

    public void cleanup() {
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
    }

    // Star class
    private static class Star {
        double x, y, size, baseAlpha, speed;
        double currentBrightness;

        Star(double x, double y, double size, double brightness, double speed) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.baseAlpha = brightness;
            this.speed = speed;
            this.currentBrightness = brightness;
        }

        void update(double time) {
            currentBrightness = baseAlpha * (0.4 + 0.6 * Math.sin(time * speed + x * 0.1));
        }

        void render(GraphicsContext gc) {
            gc.setFill(Color.WHITE.deriveColor(0, 1, 1, currentBrightness));
            gc.fillOval(x - size/2, y - size/2, size, size);

            if (currentBrightness > 0.8 && size > 1.5) {
                gc.setFill(Color.CYAN.deriveColor(0, 1, 1, (currentBrightness - 0.8) * 0.4));
                gc.fillOval(x - size * 1.5, y - size * 1.5, size * 3, size * 3);
            }
        }
    }

    // Circuit line class
    private static class CircuitLine {
        double x1, y1, x2, y2, speed;
        double currentPulse;
        double currentProgress;

        CircuitLine(double x1, double y1, double x2, double y2, double speed) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.speed = speed;
            this.currentPulse = 1.0;
            this.currentProgress = 0;
        }

        void update(double time) {
            currentPulse = 0.3 + 0.7 * (0.5 + 0.5 * Math.sin(time * speed));
            currentProgress = (time * speed * 0.3) % 1.0;
        }

        void render(GraphicsContext gc) {
            gc.setStroke(Color.CYAN.deriveColor(0, 1, 1, 0.2 * currentPulse));
            gc.setLineWidth(3);
            gc.strokeLine(x1, y1, x2, y2);

            gc.setStroke(Color.CYAN.deriveColor(0, 1, 1, 0.5 * currentPulse));
            gc.setLineWidth(1);
            gc.strokeLine(x1, y1, x2, y2);

            double px = x1 + (x2 - x1) * currentProgress;
            double py = y1 + (y2 - y1) * currentProgress;

            gc.setFill(Color.CYAN.deriveColor(0, 1, 1.5, 0.8));
            gc.fillOval(px - 2, py - 2, 4, 4);
            gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.6));
            gc.fillOval(px - 4, py - 4, 8, 8);
        }
    }

    // Particle class
    private static class Particle {
        double x, y, size, speed;
        double baseY;

        Particle(double x, double y, double size, double speed) {
            this.x = x;
            this.baseY = y;
            this.y = y;
            this.size = size;
            this.speed = speed;
        }

        void update(double time) {
            y = baseY + Math.sin(time * speed * 0.1 + x * 0.01) * 20;
        }

        void render(GraphicsContext gc) {
            gc.setFill(Color.CYAN.deriveColor(0, 1, 1.2, 1));
            gc.fillOval(x - size/2, y - size/2, size, size);

            RadialGradient glow = new RadialGradient(
                    0, 0, 0.5, 0.5, 1, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.CYAN.deriveColor(0, 1, 1.3, 0.4)),
                    new Stop(1, Color.TRANSPARENT)
            );
            gc.setFill(glow);
            gc.fillOval(x - size * 2, y - size * 2, size * 4, size * 4);
        }
    }
}