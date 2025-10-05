package com.arkanoid.view.hud;

import com.arkanoid.model.GameModel;
import com.arkanoid.main;
import com.arkanoid.model.GameState;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class HUDRenderer {
    GameModel gameModel;
    private final Text scoreText;
    private final Text livesText;
    private final Text messageText;

    public HUDRenderer(GameModel gameModel) {
        this.gameModel = gameModel;

        scoreText = new Text(10, 30, "Score: 0");
        scoreText.setFont((Font.font("Verdana", FontWeight.BOLD, 20)));
        scoreText.setFill(Color.WHITE);

        livesText = new Text(main.WINDOW_WIDTH - 120, 30, "Lives: 3");
        livesText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        livesText.setFill(Color.WHITE);
        livesText.setTextAlignment(TextAlignment.RIGHT);

        messageText = new Text();
        messageText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        messageText.setFill(Color.YELLOW);
        messageText.setTextAlignment(TextAlignment.CENTER);
        messageText.setWrappingWidth(main.WINDOW_WIDTH);
        messageText.setY(main.WINDOW_HEIGHT / 2.0);
    }

    public void render() {
        scoreText.setText("Score: " + gameModel.getScore());
        livesText.setText("Lives: " + gameModel.getLives());

        GameState currentState = gameModel.getGameState();

        if (currentState == GameState.GameOver) {
            messageText.setText("GAME OVER");
            messageText.setVisible(true);
        } else if (currentState == GameState.Ready) {
            messageText.setText("PRESS SPACE TO START");
            messageText.setVisible(true);
        } else {
            messageText.setVisible(false);
        }
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(scoreText);
        nodes.add(livesText);
        nodes.add(messageText);
        return nodes;
    }
}
