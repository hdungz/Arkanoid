package com.arkanoid.view.background.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class Planet {
    private final double x, y, radius;
    private final Color color;

    public Planet(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public void render(GraphicsContext gc) {
        Stop[] stops = new Stop[] {
                new Stop(0, color.brighter().brighter()),
                new Stop(0.6, color),
                new Stop(1, color.darker().darker())
        };
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.25, 0.25, 0.7, true,
                CycleMethod.NO_CYCLE, stops
        );
        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        gc.setFill(color.deriveColor(0, 1, 1, 0.15));
        gc.fillOval(x - radius * 1.4, y - radius * 1.4, radius * 2.8, radius * 2.8);
    }
}