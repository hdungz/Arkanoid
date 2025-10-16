// Giả định: com.arkanoid.view.effects.EffectRenderer.java
package com.arkanoid.view.effects;

import com.arkanoid.model.GameModel;
import com.arkanoid.view.Effect.ExplosionEffect;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.arkanoid.CONSTANT; // Cần import CONSTANT để lấy kích thước

public class EffectRenderer {
    private final GameModel gameModel;
    private final Canvas canvas;
    private final GraphicsContext gc;

    public EffectRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.canvas = new Canvas(CONSTANT.WINDOW_WIDTH, CONSTANT.WINDOW_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (ExplosionEffect effect : gameModel.getEffects()) {
            effect.render(gc);
        }
    }
}