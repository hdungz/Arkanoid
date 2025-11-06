package com.arkanoid.view.background.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class Nebula {
    private final double x, y, size;
    private final double speed;
    private double currentSize;

    public Nebula(double x, double y, double size, Color color, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.currentSize = size;
    }

    public void update(double time) {
        double pulse = 1 + 0.15 * Math.sin(time * speed);
        currentSize = size * pulse;
    }

    public void render(GraphicsContext gc, Color themeColor) {
        Color nebulaColor = Color.rgb(
                (int)(themeColor.getRed() * 255),
                (int)(themeColor.getGreen() * 255),
                (int)(themeColor.getBlue() * 255),
                0.15
        );

        Stop[] stops = new Stop[] {
                new Stop(0, nebulaColor.deriveColor(0, 1, 1.2, 1)),
                new Stop(0.4, nebulaColor.deriveColor(0, 1, 1, 0.7)),
                new Stop(1, Color.TRANSPARENT)
        };
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE, stops
        );
        gc.setFill(gradient);
        gc.fillOval(x - currentSize, y - currentSize, currentSize * 2, currentSize * 2);
    }

    public void render(GraphicsContext gc) {
        render(gc, Color.CYAN);
    }
}