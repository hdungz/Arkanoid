package com.arkanoid.view.playground.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class FloatingParticle {
    private final double x;
    private final double baseY;
    private final double size;
    private final double speed;
    private double y;

    public FloatingParticle(double x, double y, double size, double speed) {
        this.x = x;
        this.baseY = y;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public void update(double time) {
        y = baseY + Math.sin(time * speed * 0.1 + x * 0.01) * 15;
    }

    public void render(GraphicsContext gc, Color themeColor) {
        gc.setFill(themeColor.deriveColor(0, 1, 1.2, 1));
        gc.fillOval(x - size/2, y - size/2, size, size);

        RadialGradient glow = new RadialGradient(
                0, 0, 0.5, 0.5, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, themeColor.deriveColor(0, 1, 1.3, 0.4)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(glow);
        gc.fillOval(x - size * 2, y - size * 2, size * 4, size * 4);
    }

    public void render(GraphicsContext gc) {
        render(gc, Color.CYAN);
    }
}