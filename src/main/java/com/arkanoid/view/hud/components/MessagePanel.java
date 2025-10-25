package com.arkanoid.view.hud.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.*;
import javafx.geometry.VPos;

public class MessagePanel {
    private final Font messageFont;
    private final double windowWidth;
    private final double windowHeight;

    public MessagePanel(double windowWidth, double windowHeight) {
        this.messageFont = Font.font("Consolas", FontWeight.BOLD, 50);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void render(GraphicsContext gc, String message, Color color, double pulse) {
        double messageY = windowHeight / 2.0;

        gc.setFont(messageFont);
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(message);
        tempText.setFont(messageFont);
        double textWidth = tempText.getLayoutBounds().getWidth();

        double panelWidth = textWidth + 80;
        double panelHeight = 80;
        double panelX = (windowWidth - panelWidth) / 2;
        double panelY = messageY - (panelHeight / 2);

        drawBackground(gc, panelX, panelY, panelWidth, panelHeight);

        drawBorder(gc, panelX, panelY, panelWidth, panelHeight, color, pulse);

        drawCornerDecorations(gc, panelX, panelY, panelWidth, panelHeight, color, pulse);

        drawText(gc, message, textWidth, messageY, color, pulse);
    }

    private void drawBackground(GraphicsContext gc, double x, double y, double width, double height) {
        Stop[] bgStops = new Stop[] {
                new Stop(0, Color.rgb(10, 20, 40, 0.95)),
                new Stop(0.5, Color.rgb(15, 25, 50, 0.98)),
                new Stop(1, Color.rgb(10, 20, 40, 0.95))
        };
        LinearGradient bgGradient = new LinearGradient(
                0, y, 0, y + height,
                false, CycleMethod.NO_CYCLE, bgStops
        );
        gc.setFill(bgGradient);
        gc.fillRoundRect(x, y, width, height, 12, 12);
    }

    private void drawBorder(GraphicsContext gc, double x, double y, double width, double height,
                            Color color, double pulse) {
        gc.setStroke(color.deriveColor(0, 1, 1, pulse));
        gc.setLineWidth(3);
        gc.strokeRoundRect(x + 2, y + 2, width - 4, height - 4, 12, 12);
    }

    private void drawCornerDecorations(GraphicsContext gc, double x, double y, double width, double height,
                                       Color color, double pulse) {
        gc.setStroke(color.deriveColor(0, 1, 1.3, pulse));
        gc.setLineWidth(4);
        double cornerSize = 25;

        gc.strokeLine(x + 8, y + 2, x + 8 + cornerSize, y + 2);
        gc.strokeLine(x + 2, y + 8, x + 2, y + 8 + cornerSize);

        gc.strokeLine(x + width - 8 - cornerSize, y + 2, x + width - 8, y + 2);
        gc.strokeLine(x + width - 2, y + 8, x + width - 2, y + 8 + cornerSize);

        gc.strokeLine(x + 8, y + height - 2, x + 8 + cornerSize, y + height - 2);
        gc.strokeLine(x + 2, y + height - 8 - cornerSize, x + 2, y + height - 8);

        gc.strokeLine(x + width - 8 - cornerSize, y + height - 2, x + width - 8, y + height - 2);
        gc.strokeLine(x + width - 2, y + height - 8 - cornerSize, x + width - 2, y + height - 8);
    }

    private void drawText(GraphicsContext gc, String message, double textWidth, double messageY,
                          Color color, double pulse) {
        gc.setFont(messageFont);
        gc.setTextBaseline(VPos.CENTER);
        double textX = (windowWidth - textWidth) / 2;

        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(color.deriveColor(0, 1, 1, pulse * 0.3));
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                gc.fillText(message, textX + i, messageY + j);
            }
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(color);
        gc.fillText(message, textX, messageY);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, pulse * 0.5));
        gc.fillText(message, textX - 1, messageY - 1);

        gc.setTextBaseline(VPos.BASELINE);
    }
}