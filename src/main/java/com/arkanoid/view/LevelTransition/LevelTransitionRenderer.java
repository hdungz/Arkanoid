package com.arkanoid.view.LevelTransition;

import com.arkanoid.utils.LevelTransitionManager;
import com.arkanoid.utils.ThemeManager;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Random;

import static com.arkanoid.CONSTANT.*;

public class LevelTransitionRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final LevelTransitionManager transitionManager;
    private final ThemeManager themeManager;
    private final Random random;

    private final Font titleFont;
    private final Font subtitleFont;

    public LevelTransitionRenderer(LevelTransitionManager transitionManager) {
        this.transitionManager = transitionManager;
        this.themeManager = ThemeManager.getInstance();
        this.random = new Random();

        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);

        titleFont = Font.font("Consolas", FontWeight.BOLD, 80);
        subtitleFont = Font.font("Consolas", FontWeight.NORMAL, 24);
    }

    public void render() {
        gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        if (transitionManager.isIntroActive()) {
            renderLevelIntro();
        }

        if (transitionManager.isClearActive()) {
            renderLevelClear();
        }
    }

    private void renderLevelIntro() {
        double alpha = transitionManager.getIntroAlpha();
        double glitch = transitionManager.getGlitchIntensity();
        Color themeColor = themeManager.getPrimaryColor();

        gc.setFill(Color.rgb(0, 0, 0, alpha * 0.75));
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        String levelText = "LEVEL " + transitionManager.getCurrentLevel();

        gc.setTextAlign(TextAlignment.CENTER);
        double centerX = WINDOW_WIDTH / 2.0;
        double centerY = WINDOW_HEIGHT / 2.0;

        if (glitch > 0.15) {
            drawGlitchText(levelText, centerX, centerY, alpha, glitch, themeColor);
        }

        gc.setFont(titleFont);

        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(themeColor.deriveColor(0, 1, 1.5, alpha * 0.5));
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                gc.fillText(levelText, centerX + i, centerY + j);
            }
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setStroke(themeColor.deriveColor(0, 1, 1.2, alpha));
        gc.setLineWidth(3);
        gc.strokeText(levelText, centerX, centerY);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, alpha));
        gc.fillText(levelText, centerX, centerY);

        gc.setFont(subtitleFont);
        String subtitle = "GET READY";
        gc.setFill(themeColor.deriveColor(0, 1, 1, alpha * 0.8));
        gc.fillText(subtitle, centerX, centerY + 60);

        drawScanLines(alpha);
    }


    private void renderLevelClear() {
        double alpha = transitionManager.getClearAlpha();
        double glitch = transitionManager.getGlitchIntensity();
        Color themeColor = themeManager.getPrimaryColor();

        gc.setFill(Color.rgb(0, 0, 0, alpha * 0.8));
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        String clearText = "LEVEL CLEAR";

        gc.setTextAlign(TextAlignment.CENTER);
        double centerX = WINDOW_WIDTH / 2.0;
        double centerY = WINDOW_HEIGHT / 2.0;

        if (glitch > 0.15) {
            drawGlitchText(clearText, centerX, centerY, alpha, glitch, Color.rgb(50, 255, 100));
        }

        gc.setFont(titleFont);

        Color clearColor = Color.rgb(50, 255, 100);
        gc.setGlobalBlendMode(BlendMode.ADD);
        gc.setFill(clearColor.deriveColor(0, 1, 1.5, alpha * 0.6));
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                gc.fillText(clearText, centerX + i, centerY + j);
            }
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setStroke(clearColor.deriveColor(0, 1, 1.2, alpha));
        gc.setLineWidth(3);
        gc.strokeText(clearText, centerX, centerY);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, alpha));
        gc.fillText(clearText, centerX, centerY);

        gc.setFont(subtitleFont);
        String subtitle = "EXCELLENT!";
        gc.setFill(clearColor.deriveColor(0, 1, 0.8, alpha * 0.8));
        gc.fillText(subtitle, centerX, centerY + 60);

        drawScanLines(alpha);
    }

    private void drawGlitchText(String text, double x, double y, double alpha, double glitch, Color color) {
        gc.setFont(titleFont);

        gc.setFill(Color.rgb(255, 0, 0, alpha * glitch * 0.5));
        double redOffset = (random.nextDouble() - 0.5) * glitch * 12;
        gc.fillText(text, x + redOffset, y);

        gc.setFill(Color.rgb(0, 255, 255, alpha * glitch * 0.5));
        double cyanOffset = (random.nextDouble() - 0.5) * glitch * 12;
        gc.fillText(text, x + cyanOffset, y);

        if (random.nextDouble() < glitch * 0.7) {
            int sliceCount = random.nextInt(3) + 1;
            for (int i = 0; i < sliceCount; i++) {
                double sliceY = y + (random.nextDouble() - 0.5) * 60;
                double sliceHeight = random.nextDouble() * 10 + 5;
                double sliceOffset = (random.nextDouble() - 0.5) * 30;

                gc.save();
                gc.beginPath();
                gc.rect(0, sliceY, WINDOW_WIDTH, sliceHeight);
                gc.clip();
                gc.setFill(color.deriveColor(0, 1, 1, alpha * 0.7));
                gc.fillText(text, x + sliceOffset, y);
                gc.restore();
            }
        }
    }

    private void drawScanLines(double alpha) {
        gc.setGlobalAlpha(alpha * 0.12);
        gc.setStroke(Color.rgb(0, 255, 255));
        gc.setLineWidth(1);

        for (int y = 0; y < WINDOW_HEIGHT; y += 4) {
            gc.strokeLine(0, y, WINDOW_WIDTH, y);
        }

        gc.setGlobalAlpha(1.0);
    }

    public Node getNode() {
        return canvas;
    }

}