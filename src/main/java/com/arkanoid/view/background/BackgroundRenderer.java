package com.arkanoid.view.background;

import com.arkanoid.utils.ThemeManager;
import com.arkanoid.view.background.components.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackgroundRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final List<Star> stars;
    private final List<Planet> planets;
    private final List<Nebula> nebulae;
    private final List<CircuitLine> circuits;
    private final Random random;
    private final ThemeManager themeManager;
    private AnimationTimer animationTimer;
    private double time = 0;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public BackgroundRenderer() {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        random = new Random();
        themeManager = ThemeManager.getInstance();
        stars = new ArrayList<>();
        planets = new ArrayList<>();
        nebulae = new ArrayList<>();
        circuits = new ArrayList<>();

        initializeComponents();
        startAnimation();
    }

    private void initializeComponents() {
        nebulae.add(new Nebula(200, 150, 180, Color.CYAN, 0.3));
        nebulae.add(new Nebula(900, 400, 200, Color.CYAN, 0.25));
        nebulae.add(new Nebula(500, 550, 150, Color.CYAN, 0.4));
        nebulae.add(new Nebula(1050, 180, 140, Color.CYAN, 0.35));

        circuits.add(new CircuitLine(50, 80, 300, 80, 2));
        circuits.add(new CircuitLine(300, 80, 300, 300, 1.5));
        circuits.add(new CircuitLine(80, 200, 320, 200, 1.8));
        circuits.add(new CircuitLine(150, 350, 150, 600, 1.6));
        circuits.add(new CircuitLine(150, 600, 320, 600, 2.2));
        circuits.add(new CircuitLine(50, 450, 200, 450, 1.9));
        circuits.add(new CircuitLine(200, 450, 200, 680, 1.4));
        circuits.add(new CircuitLine(250, 120, 250, 500, 1.7));

        circuits.add(new CircuitLine(1230, 80, 980, 80, 2.1));
        circuits.add(new CircuitLine(980, 80, 980, 300, 1.7));
        circuits.add(new CircuitLine(1200, 180, 960, 180, 1.6));
        circuits.add(new CircuitLine(1100, 180, 1100, 480, 1.8));
        circuits.add(new CircuitLine(1100, 480, 950, 480, 2.0));
        circuits.add(new CircuitLine(1250, 320, 1000, 320, 1.5));
        circuits.add(new CircuitLine(1150, 380, 1150, 660, 1.9));
        circuits.add(new CircuitLine(1000, 580, 1230, 580, 2.3));
        circuits.add(new CircuitLine(1050, 120, 1050, 420, 1.65));

        circuits.add(new CircuitLine(450, 100, 650, 100, 1.3));
        circuits.add(new CircuitLine(650, 100, 650, 350, 1.4));
        circuits.add(new CircuitLine(500, 250, 750, 250, 1.6));
        circuits.add(new CircuitLine(550, 400, 550, 650, 1.2));
        circuits.add(new CircuitLine(800, 150, 800, 500, 1.5));

        planets.add(new Planet(150, 100, 50, Color.CYAN));
        planets.add(new Planet(1130, 580, 60, Color.CYAN));
        planets.add(new Planet(100, 600, 45, Color.CYAN));

        for (int i = 0; i < 200; i++) {
            stars.add(new Star(
                    random.nextDouble() * WIDTH,
                    random.nextDouble() * HEIGHT,
                    random.nextDouble() * 2.5 + 0.5,
                    random.nextDouble() * 0.6 + 0.4,
                    random.nextDouble() * 2 + 0.8
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
                render();
            }
        };
        animationTimer.start();
    }

    private void render() {
        ThemeManager.LevelTheme theme = themeManager.getCurrentTheme();
        Color themeColor = themeManager.getPrimaryColor();

        Stop[] bgStops = new Stop[] {
                new Stop(0, theme.getBackground1()),
                new Stop(0.4, theme.getBackground2()),
                new Stop(1, theme.getBackground3())
        };
        RadialGradient bgGradient = new RadialGradient(
                0, 0, 0.5, 0.3, 0.9, true,
                CycleMethod.NO_CYCLE, bgStops
        );
        gc.setFill(bgGradient);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setGlobalBlendMode(BlendMode.ADD);
        for (CircuitLine circuit : circuits) {
            circuit.update(time);
            circuit.render(gc, themeColor);
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setGlobalBlendMode(BlendMode.ADD);
        for (Nebula nebula : nebulae) {
            nebula.update(time);
            nebula.render(gc, themeColor);
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        for (Planet planet : planets) {
            planet.render(gc, themeColor);
        }

        for (Star star : stars) {
            star.update(time);
            star.render(gc, themeColor);
        }

        gc.setGlobalAlpha(0.02);
        gc.setStroke(themeColor);
        gc.setLineWidth(1);
        for (int i = 0; i < HEIGHT; i += 3) {
            gc.strokeLine(0, i, WIDTH, i);
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