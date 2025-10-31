package com.arkanoid.view;

import com.arkanoid.model.LevelManager;
import com.arkanoid.model.LevelManager.LevelInfo;
import com.arkanoid.model.LevelManager.RewardType;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class LevelSelectionView extends StackPane {

    private final LevelManager levelManager;
    private LevelInfo selectedLevel;
    private VBox levelInfoPanel;
    private Button playButton;
    private Button infoBackButton;
    private Button backButton;
    private Button leaderboardButton;

    private Pane levelMap; // lưu lại để ẩn/hiện

    public LevelSelectionView() {
        this.levelManager = LevelManager.getInstance();
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        createBackground();
        createLevelMap();
        createLevelInfoPanel();
        createBackButton();

        selectLevel(1); // mặc định chọn level đầu
    }

    private void createBackground() {
        Pane background = new Pane();
        background.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0a2e, #16213e, #0f3460);");
        background.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        getChildren().add(background);
    }

    private void createLevelMap() {
        levelMap = new Pane();
        levelMap.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        double[][] nodePositions = {
                {100, 100}, {200, 80}, {300, 100}, {400, 120}, {500, 100},
                {150, 200}, {250, 180}, {350, 200}, {450, 220}, {550, 200},
                {100, 300}, {200, 280}, {300, 300}, {400, 320}, {500, 300},
                {150, 400}, {250, 380}, {350, 400}, {450, 420}, {550, 400}
        };

        for (int i = 0; i < Math.min(nodePositions.length, 20); i++) {
            LevelInfo level = levelManager.getLevel(i + 1);
            if (level != null) {
                createLevelNode(levelMap, level, nodePositions[i][0], nodePositions[i][1]);
            }
        }

        createConnections(levelMap, nodePositions);
        getChildren().add(levelMap);
    }

    private void createLevelNode(Pane container, LevelInfo level, double x, double y) {
        Circle node = new Circle(x, y, 20);

        if (level.isUnlocked()) {
            node.setFill(Color.rgb(60, 60, 90, 0.8));
            node.setStroke(Color.rgb(120, 180, 255));
            node.setStrokeWidth(2);
        } else {
            node.setFill(Color.rgb(30, 30, 30, 0.6));
            node.setStroke(Color.rgb(200, 80, 80));
            node.setStrokeWidth(2);
            node.getStrokeDashArray().addAll(5.0, 5.0);
        }

        Label levelNumber = new Label(String.valueOf(level.getId()));
        levelNumber.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        levelNumber.setTextFill(level.isUnlocked() ? Color.WHITE : Color.rgb(200, 100, 100));
        levelNumber.setLayoutX(x - 6);
        levelNumber.setLayoutY(y - 6);

        node.setOnMouseClicked(e -> {
            if (level.isUnlocked()) {
                selectLevel(level.getId());
                showLevelInfoPanel(true);
            }
        });

        // Hover effect
        node.setOnMouseEntered(e -> {
            if (level.isUnlocked()) {
                ScaleTransition scale = new ScaleTransition(Duration.millis(150), node);
                scale.setToX(1.2);
                scale.setToY(1.2);
                scale.play();
            }
        });

        node.setOnMouseExited(e -> {
            if (level.isUnlocked()) {
                ScaleTransition scale = new ScaleTransition(Duration.millis(150), node);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            }
        });

        container.getChildren().addAll(node, levelNumber);
    }

    private void createConnections(Pane container, double[][] positions) {
        for (int i = 0; i < positions.length - 1; i++) {
            Line line = new Line(positions[i][0], positions[i][1], positions[i + 1][0], positions[i + 1][1]);
            line.setStroke(Color.rgb(120, 120, 160, 0.8));
            line.setStrokeWidth(1);
            container.getChildren().add(line);
        }
    }

    private void createLevelInfoPanel() {
        levelInfoPanel = new VBox(10);
        levelInfoPanel.setAlignment(Pos.CENTER);
        levelInfoPanel.setPrefSize(320, 240);
        levelInfoPanel.setLayoutX(WINDOW_WIDTH / 2 - 160);
        levelInfoPanel.setLayoutY(WINDOW_HEIGHT / 2 - 120);
        levelInfoPanel.setStyle("-fx-background-color: rgba(40,40,60,0.9); -fx-background-radius: 12; -fx-border-color: #88a; -fx-border-radius: 12; -fx-border-width: 2;");
        levelInfoPanel.setVisible(false);

        Label title = new Label();
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Label goal = new Label();
        goal.setFont(Font.font("Arial", 14));
        goal.setTextFill(Color.LIGHTGRAY);

        Label current = new Label();
        current.setFont(Font.font("Arial", 14));
        current.setTextFill(Color.LIGHTGRAY);

        Label timeLimit = new Label();
        timeLimit.setFont(Font.font("Arial", 14));
        timeLimit.setTextFill(Color.LIGHTGRAY);

        playButton = createInfoButton("PLAY");
        leaderboardButton = createInfoButton("LEADERBOARD");
        infoBackButton = createInfoButton("BACK");

        HBox buttons = new HBox(10, leaderboardButton, playButton, infoBackButton);
        buttons.setAlignment(Pos.CENTER);

        // Khi nhấn BACK trong panel → chỉ ẩn panel
        infoBackButton.setOnAction(e -> showLevelInfoPanel(false));

        levelInfoPanel.getChildren().addAll(title, goal, current, timeLimit, buttons);
        getChildren().add(levelInfoPanel);
    }

    private void createBackButton() {
        backButton = new Button("BACK TO MENU");
        backButton.setPrefSize(140, 30);
        backButton.setLayoutX(20);
        backButton.setLayoutY(WINDOW_HEIGHT - 50);
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: #333; -fx-border-color: #666; -fx-background-radius: 6;");

        // Gọi callback sang SceneManager (tùy bạn setup)
        backButton.setOnAction(e -> {
            System.out.println("Return to main menu!");
            // SceneManager.loadScene("MainMenu.fxml");
        });

        getChildren().add(backButton);
    }

    public void selectLevel(int id) {
        LevelInfo info = levelManager.getLevel(id);
        if (info != null && info.isUnlocked()) {
            selectedLevel = info;
            levelManager.selectLevel(id);
            updateLevelInfoPanel();
        }
    }

    private void showLevelInfoPanel(boolean visible) {
        levelInfoPanel.setVisible(visible);
        levelMap.setVisible(!visible);
    }

    private void updateLevelInfoPanel() {
        if (selectedLevel == null) return;
        Label title = (Label) levelInfoPanel.getChildren().get(0);
        Label goal = (Label) levelInfoPanel.getChildren().get(1);
        Label current = (Label) levelInfoPanel.getChildren().get(2);
        Label time = (Label) levelInfoPanel.getChildren().get(3);

        title.setText(selectedLevel.getName());
        goal.setText(selectedLevel.getGoal());
        current.setText("Best: " + selectedLevel.getBestTime());
        time.setText("Time Limit: " + selectedLevel.getTimeLimit());
    }

    private Button createInfoButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(110, 35);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #555; -fx-border-color: #888; -fx-background-radius: 6;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #666; -fx-border-color: #aaa; -fx-background-radius: 6;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #555; -fx-border-color: #888; -fx-background-radius: 6;"));
        return btn;
    }

    // Getters
    public Button getPlayButton() { return playButton; }
    public Button getLeaderboardButton() { return leaderboardButton; }
    public Button getBackButton() { return backButton; }
    public LevelInfo getSelectedLevel() { return selectedLevel; }
}
