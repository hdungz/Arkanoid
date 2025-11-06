package com.arkanoid.view.background.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class Planet {
    private final double x, y, radius;

    public Planet(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void render(GraphicsContext gc, Color themeColor) {
        Color planetColor = Color.rgb(
                (int)(themeColor.getRed() * 255),
                (int)(themeColor.getGreen() * 255),
                (int)(themeColor.getBlue() * 255),
                0.5
        );

        Stop[] stops = new Stop[] {
                new Stop(0, planetColor.brighter().brighter()),
                new Stop(0.6, planetColor),
                new Stop(1, planetColor.darker().darker())
        };
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.25, 0.25, 0.7, true,
                CycleMethod.NO_CYCLE, stops
        );
        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        gc.setFill(planetColor.deriveColor(0, 1, 1, 0.15));
        gc.fillOval(x - radius * 1.4, y - radius * 1.4, radius * 2.8, radius * 2.8);
    }

    public void render(GraphicsContext gc) {
        render(gc, Color.CYAN);
    }
}