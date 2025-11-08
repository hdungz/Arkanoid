package com.arkanoid.view;

import com.arkanoid.utils.LevelManager;
import com.arkanoid.utils.LevelManager.LevelInfo;
import com.arkanoid.utils.AssetsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class LevelSelectionView extends StackPane {

    private final LevelManager levelManager;
    private LevelInfo selectedLevel;
    private Button playButton;
    private Button backButton;
    private Pane levelMap;
    private Map<Integer, ImageView> levelImageNodes;

    public LevelSelectionView() {
        this.levelManager = LevelManager.getInstance();
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        this.levelImageNodes = new HashMap<>();
        this.playButton = new Button();


        Image[] backgroundFrames = AssetsManager.getFrames("backgroundchooselevel");
        if (backgroundFrames != null && backgroundFrames.length > 0) {
            ImageView backgroundView = new ImageView(backgroundFrames[0]);
            backgroundView.setFitWidth(WINDOW_WIDTH);
            backgroundView.setFitHeight(WINDOW_HEIGHT);
            backgroundView.setPreserveRatio(false);
            getChildren().add(backgroundView);
        }

        createLevelMap();


        createBackButton();

        int highestUnlockedLevel = 1;
        for (int i = 20; i >= 1; i--) {
            LevelInfo info = this.levelManager.getLevel(i);
            if (info != null && info.isUnlocked()) {
                highestUnlockedLevel = i;
                break;
            }
        }
        selectLevel(highestUnlockedLevel);
    }

    private void createLevelMap() {
        levelMap = new Pane();
        levelMap.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        int cols = 5;
        int rows = 4;
        int numLevels = 20;
        double paddingX = 120.0;
        double paddingTop = 80.0;
        double paddingBottom = 150.0;

        double availableWidth = WINDOW_WIDTH - (2 * paddingX);
        double availableHeight = WINDOW_HEIGHT - paddingTop - paddingBottom;

        double xSpacing = availableWidth / (cols - 1);
        double ySpacing = availableHeight / (rows - 1);

        double[][] nodePositions = new double[numLevels][2];
        Random jitter = new Random();

        for (int i = 0; i < numLevels; i++) {
            int r = i / cols;
            int c = i % cols;
            if (r % 2 == 1) c = (cols - 1) - c;

            double x = paddingX + c * xSpacing + (jitter.nextDouble() - 0.5) * (xSpacing * 0.2);
            double y = paddingTop + r * ySpacing + (jitter.nextDouble() - 0.5) * (ySpacing * 0.2);
            nodePositions[i][0] = x;
            nodePositions[i][1] = y;
        }


        createConnections(levelMap, nodePositions);


        for (int i = 0; i < numLevels; i++) {
            LevelInfo level = levelManager.getLevel(i + 1);
            if (level != null) {
                createLevelNode(levelMap, level, nodePositions[i][0], nodePositions[i][1]);
            }
        }
        getChildren().add(levelMap);
    }

    private void createLevelNode(Pane container, LevelInfo level, double x, double y) {
        double imageSize = 100.0;

        String imageKey = "level" + level.getId();
        Image levelImg = null;
        Image[] frames = AssetsManager.getFrames(imageKey);
        if (frames != null && frames.length > 0) {
            levelImg = frames[0];
        }

        ImageView imageView = new ImageView(levelImg);
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        imageView.setLayoutX(x - imageSize / 2);
        imageView.setLayoutY(y - imageSize / 2);


        Button invisiblePlayButton = new Button();
        invisiblePlayButton.setPrefSize(imageSize, imageSize);
        invisiblePlayButton.setLayoutX(x - imageSize / 2);
        invisiblePlayButton.setLayoutY(y - imageSize / 2);
        invisiblePlayButton.setBackground(Background.EMPTY);
        invisiblePlayButton.setStyle("-fx-cursor: hand;");

        if (level.isUnlocked()) {

            invisiblePlayButton.setOnAction(e -> {
                selectLevel(level.getId());
                playButton.fire();
            });
        } else {

            imageView.setOpacity(0.4);
            invisiblePlayButton.setMouseTransparent(true);
            invisiblePlayButton.setStyle("-fx-cursor: default;");
        }

        container.getChildren().addAll(imageView, invisiblePlayButton);
        levelImageNodes.put(level.getId(), imageView);
    }

    private void createConnections(Pane container, double[][] positions) {
        for (int i = 0; i < positions.length - 1; i++) {
            Line line = new Line(positions[i][0], positions[i][1], positions[i + 1][0], positions[i + 1][1]);
            LevelInfo level2 = levelManager.getLevel(i + 2);

            if (level2 != null && level2.isUnlocked()) {
                line.setStroke(Color.rgb(180, 210, 255, 0.9));
                line.setStrokeWidth(2);
            } else {
                line.setStroke(Color.rgb(100, 100, 140, 0.5));
                line.setStrokeWidth(1.5);
                line.getStrokeDashArray().addAll(5.0, 5.0);
            }
            container.getChildren().add(line);
        }
    }

    private void createBackButton() {
        backButton = createImageButton("backtomenu2", "backtomenu1");
        backButton.setOnAction(e -> System.out.println("Return to main menu!"));

        getChildren().add(backButton);
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backButton, new Insets(0, 0, 20, 20));
    }

    public void selectLevel(int id) {
        LevelInfo info = levelManager.getLevel(id);
        if (info == null || !info.isUnlocked()) return;

        selectedLevel = info;
        levelManager.selectLevel(id);

    }

    private Button createImageButton(String normalKey, String hoverKey) {
        Image normalImg = AssetsManager.getFrames(normalKey)[0];
        Image hoverImg = AssetsManager.getFrames(hoverKey)[0];

        ImageView imageView = new ImageView(normalImg);
        imageView.setFitWidth(250);
        imageView.setFitHeight(50);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-cursor: hand;");

        button.setOnMouseEntered(e -> imageView.setImage(hoverImg));
        button.setOnMouseExited(e -> imageView.setImage(normalImg));

        return button;
    }


    public void refreshView() {
        if (levelMap != null) {
            getChildren().remove(levelMap);
        }
        levelImageNodes.clear();

        createLevelMap();

        int highestUnlockedLevel = 1;
        for (int i = 20; i >= 1; i--) {
            LevelInfo info = this.levelManager.getLevel(i);
            if (info != null && info.isUnlocked()) {
                highestUnlockedLevel = i;
                break;
            }
        }
        selectLevel(highestUnlockedLevel);

        if (backButton != null) {
            backButton.toFront();
        }
    }


    public Button getPlayButton() {
        return playButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public LevelInfo getSelectedLevel() {
        return selectedLevel;
    }
}