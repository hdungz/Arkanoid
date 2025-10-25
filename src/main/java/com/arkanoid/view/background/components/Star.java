package com.arkanoid.view.background.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Star {
    private final double x, y, size;
    private final double baseAlpha, speed;
    private double currentBrightness;

    public Star(double x, double y, double size, double brightness, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.baseAlpha = brightness;
        this.speed = speed;
        this.currentBrightness = brightness;
    }

    public void update(double time) {
        currentBrightness = baseAlpha * (0.4 + 0.6 * Math.sin(time * speed + x * 0.1));
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, currentBrightness));
        gc.fillOval(x - size/2, y - size/2, size, size);

        if (currentBrightness > 0.8 && size > 1.5) {
            gc.setFill(Color.CYAN.deriveColor(0, 1, 1, (currentBrightness - 0.8) * 0.4));
            gc.fillOval(x - size * 1.5, y - size * 1.5, size * 3, size * 3);
        }
    }
}