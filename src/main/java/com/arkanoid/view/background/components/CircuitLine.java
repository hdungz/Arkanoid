package com.arkanoid.view.background.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class CircuitLine {
    private final double x1, y1, x2, y2, speed;
    private double currentPulse;
    private double currentProgress;

    public CircuitLine(double x1, double y1, double x2, double y2, double speed) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.speed = speed;
        this.currentPulse = 1.0;
        this.currentProgress = 0;
    }

    public void update(double time) {
        currentPulse = 0.3 + 0.7 * (0.5 + 0.5 * Math.sin(time * speed));
        currentProgress = (time * speed * 0.3) % 1.0;
    }

    public void render(GraphicsContext gc) {
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