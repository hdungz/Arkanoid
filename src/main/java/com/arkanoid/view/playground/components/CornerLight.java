package com.arkanoid.view.playground.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;


public class CornerLight {
    private final double x, y;
    private final CornerPosition position;
    private double intensity;

    public enum CornerPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    public CornerLight(double x, double y, CornerPosition position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public void update(double time) {
        intensity = 0.6 + 0.4 * Math.sin(time * 1.5 + x * 0.01);
    }

    public void render(GraphicsContext gc) {
        // Glow effect
        RadialGradient glow = new RadialGradient(
                0, 0, 0.5, 0.5, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.CYAN.deriveColor(0, 1, 1.5, intensity * 0.7)),
                new Stop(0.5, Color.CYAN.deriveColor(0, 1, 1, intensity * 0.3)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(glow);
        gc.fillOval(x - 30, y - 30, 60, 60);

        // Core light
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, intensity));
        gc.fillOval(x - 3, y - 3, 6, 6);
    }
}