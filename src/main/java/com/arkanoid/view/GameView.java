package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.Item;
import com.arkanoid.view.background.BackgroundRenderer;
import com.arkanoid.view.background.PlayGroundRenderer;
import com.arkanoid.view.ball.BallRenderer;
import com.arkanoid.view.border.BorderRenderer;
import com.arkanoid.view.brick.BrickRenderer;
import com.arkanoid.view.hud.HUDRenderer;
import com.arkanoid.view.paddle.PaddleRenderer;
import com.arkanoid.view.effects.EffectRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class GameView extends Pane {
    private final GameModel gameModel;
    private final BallRenderer ballRenderer;
    private final PaddleRenderer paddleRenderer;
    private final BrickRenderer brickRenderer;
    private final HUDRenderer hudRenderer;
    private final BorderRenderer borderRenderer;
    private final PlayGroundRenderer playGroundRenderer;
    private final BackgroundRenderer backgroundRenderer;
    private final EffectRenderer effectRenderer;

    // ✅ Canvas riêng để vẽ Item
    private final Canvas itemCanvas;
    private final GraphicsContext gcItem;

    public GameView(GameModel gameModel) {
        this.setStyle("-fx-background-color: black;");
        this.gameModel = gameModel;

        ballRenderer = new BallRenderer(gameModel);
        paddleRenderer = new PaddleRenderer(gameModel);
        brickRenderer = new BrickRenderer(gameModel);
        hudRenderer = new HUDRenderer(gameModel);
        borderRenderer = new BorderRenderer(gameModel);
        effectRenderer = new EffectRenderer(gameModel);
        playGroundRenderer = new PlayGroundRenderer();
        backgroundRenderer = new BackgroundRenderer();

        // ✅ Tạo canvas Item
        itemCanvas = new Canvas(800, 800); // kích thước canvas game
        gcItem = itemCanvas.getGraphicsContext2D();

        // Thêm các node vào Pane
        getChildren().add(backgroundRenderer.getNode());
        getChildren().add(playGroundRenderer.getNode());
        getChildren().add(ballRenderer.getNode());
        getChildren().add(paddleRenderer.getNode());
        getChildren().addAll(brickRenderer.getNodes());
        getChildren().addAll(hudRenderer.getNodes());
        getChildren().addAll(borderRenderer.getNode());
        getChildren().add(effectRenderer.getCanvas());
        getChildren().add(itemCanvas);
    }

    public void render() {
        ballRenderer.render();
        paddleRenderer.render();
        brickRenderer.render();
        effectRenderer.render();
        hudRenderer.render();
        borderRenderer.render();

        // ✅ Vẽ tất cả Item trực tiếp trên canvas
        gcItem.clearRect(0, 0, itemCanvas.getWidth(), itemCanvas.getHeight());
        for (Item vatPham : gameModel.getItems()) {
            vatPham.ve(gcItem);
        }
    }
}
