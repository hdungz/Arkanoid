package com.arkanoid.view.hud.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScorePanel {
    private final HUDPanel panel;
    private final Font labelFont;
    private final Font scoreFont;

    public ScorePanel(double x, double y, double width, double height) {
        this.panel = new HUDPanel(x, y, width, height);
        this.labelFont = Font.font("Consolas", FontWeight.NORMAL, 14);
        this.scoreFont = Font.font("Consolas", FontWeight.BOLD, 24);
    }

    public void render(GraphicsContext gc, double time, int score, Color themeColor) {
        panel.render(gc, time, themeColor);

        double panelX = panel.getX();
        double panelY = panel.getY();

        gc.setFont(labelFont);
        gc.setFill(themeColor);
        gc.fillText("SCORE", panelX + 15, panelY + 20);

        gc.setFont(scoreFont);
        String scoreText = String.format("%06d", score);

        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(themeColor.deriveColor(0, 1, 1, 0.3));
        gc.fillText(scoreText, panelX + 14, panelY + 48);
        gc.fillText(scoreText, panelX + 16, panelY + 48);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(Color.WHITE);
        gc.fillText(scoreText, panelX + 15, panelY + 48);
    }

    public void render(GraphicsContext gc, double time, int score) {
        render(gc, time, score, Color.CYAN);
    }
}