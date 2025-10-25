package com.arkanoid.view.playground.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridLine {
    private final double x1, y1, x2, y2;
    private final boolean isVertical;
    private final double baseAlpha = 0.4;
    private double currentAlpha;

    public GridLine(double x1, double y1, double x2, double y2, boolean isVertical) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.isVertical = isVertical;
        this.currentAlpha = baseAlpha;
    }

    public void update(double time) {
        currentAlpha = baseAlpha + 0.15 * Math.sin(time * 0.5 + (isVertical ? x1 : y1) * 0.01);
    }

    public void render(GraphicsContext gc) {
        gc.setStroke(Color.CYAN.deriveColor(0, 1, 1, currentAlpha));
        gc.setLineWidth(1);
        gc.strokeLine(x1, y1, x2, y2);
    }
}