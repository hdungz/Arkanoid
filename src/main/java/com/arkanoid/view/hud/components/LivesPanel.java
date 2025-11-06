package com.arkanoid.view.hud.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LivesPanel {
    private final HUDPanel panel;
    private final Font labelFont;

    public LivesPanel(double x, double y, double width, double height) {
        this.panel = new HUDPanel(x, y, width, height);
        this.labelFont = Font.font("Consolas", FontWeight.NORMAL, 14);
    }

    public void render(GraphicsContext gc, double time, int lives, Color themeColor) {
        panel.render(gc, time, themeColor);

        double panelX = panel.getX();
        double panelY = panel.getY();

        gc.setFont(labelFont);
        gc.setFill(themeColor);
        gc.fillText("LIVES", panelX + 15, panelY + 20);

        drawLivesIcons(gc, panelX, panelY, lives, themeColor);
    }

    private void drawLivesIcons(GraphicsContext gc, double panelX, double panelY, int lives, Color themeColor) {
        double iconSize = 18;
        double iconSpacing = 25;
        double startX = panelX + 15;
        double iconY = panelY + 31;

        for (int i = 0; i < 3; i++) {
            double x = startX + i * iconSpacing;

            if (i < lives) {
                drawActiveLife(gc, x, iconY, iconSize, themeColor);
            } else {
                drawLostLife(gc, x, iconY, iconSize);
            }
        }
    }

    private void drawActiveLife(GraphicsContext gc, double x, double y, double size, Color themeColor) {
        gc.setGlobalBlendMode(BlendMode.ADD);
        RadialGradient glow = new RadialGradient(
                0, 0, 0.5, 0.5, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, themeColor.deriveColor(0, 1, 1, 0.5)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(glow);
        gc.fillOval(x + size/2 - size, y + size/2 - size, size * 2, size * 2);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(themeColor);
        gc.fillOval(x, y, size, size);

        gc.setFill(Color.WHITE);
        gc.fillOval(x + 3, y + 3, size - 6, size - 6);
    }

    private void drawLostLife(GraphicsContext gc, double x, double y, double size) {
        gc.setStroke(Color.rgb(50, 50, 80));
        gc.setLineWidth(2);
        gc.strokeOval(x, y, size, size);
    }

    public void render(GraphicsContext gc, double time, int lives) {
        render(gc, time, lives, Color.CYAN);
    }
}