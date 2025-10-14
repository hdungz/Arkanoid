package com.arkanoid.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class MenuView extends VBox {
    private final Button playButton;
    private final Button storeButton;
    private final Button highscoreButton;
    private final Button exitButton;

    public MenuView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setStyle("-fx-background-color: #1a1a2e;");

        Text title = new Text("ARKANOID");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        title.setStyle("-fx-fill: #00d9ff;");

        playButton = createMenuButton("PLAY");
        storeButton = createMenuButton("STORE");
        highscoreButton = createMenuButton("HIGHSCORE");
        exitButton = createMenuButton("EXIT");

        this.getChildren().addAll(title, playButton, storeButton, highscoreButton, exitButton);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(250, 60);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        button.setStyle(
            "-fx-background-color: #0f3460; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #00d9ff; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #16537e; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #00d9ff; " +
            "-fx-border-width: 3; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #0f3460; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #00d9ff; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10;"
        ));

        return button;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getStoreButton() {
        return storeButton;
    }

    public Button getHighscoreButton() {
        return highscoreButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}
