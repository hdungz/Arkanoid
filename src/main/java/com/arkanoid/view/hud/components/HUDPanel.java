package com.arkanoid.view.hud.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;


public class HUDPanel {
    private final double x, y, width, height;

    public HUDPanel(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(GraphicsContext gc, double time) {
        Stop[] bgStops = new Stop[] {
                new Stop(0, Color.rgb(10, 20, 40, 0.8)),
                new Stop(1, Color.rgb(20, 30, 60, 0.9))
        };
        LinearGradient bgGradient = new LinearGradient(
                0, y, 0, y + height,
                false, CycleMethod.NO_CYCLE, bgStops
        );
        gc.setFill(bgGradient);
        gc.fillRoundRect(x, y, width, height, 8, 8);

        double pulse = 0.5 + 0.3 * Math.sin(time * 2);
        gc.setStroke(Color.CYAN.deriveColor(0, 1, 1, pulse));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + 1, y + 1, width - 2, height - 2, 8, 8);

        drawCornerAccents(gc, pulse);
    }

    private void drawCornerAccents(GraphicsContext gc, double pulse) {
        gc.setStroke(Color.CYAN.deriveColor(0, 1, 1.5, pulse));
        gc.setLineWidth(3);

        gc.strokeLine(x + 5, y + 2, x + 20, y + 2);
        gc.strokeLine(x + 2, y + 5, x + 2, y + 20);

        gc.strokeLine(x + width - 20, y + 2, x + width - 5, y + 2);
        gc.strokeLine(x + width - 2, y + 5, x + width - 2, y + 20);

        gc.strokeLine(x + 5, y + height - 2, x + 20, y + height - 2);
        gc.strokeLine(x + 2, y + height - 20, x + 2, y + height - 5);

        gc.strokeLine(x + width - 20, y + height - 2, x + width - 5, y + height - 2);
        gc.strokeLine(x + width - 2, y + height - 20, x + width - 2, y + height - 5);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}