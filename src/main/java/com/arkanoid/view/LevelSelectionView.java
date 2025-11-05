package com.arkanoid.view;

import com.arkanoid.utils.LevelManager;
import com.arkanoid.utils.LevelManager.LevelInfo;
import com.arkanoid.utils.AssetsManager;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private Pane levelMap;
    private Pane backgroundPane;

    private Map<Integer, Circle> levelNodes;
    private Timeline selectedNodePulse;
    private Translate mapParallaxTransform;
    private Timeline parallaxTimeline;

    public LevelSelectionView() {
        this.levelManager = LevelManager.getInstance();
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);


        this.levelNodes = new HashMap<>();
        this.mapParallaxTransform = new Translate();
        this.parallaxTimeline = new Timeline();

        Image[] backgroundFrames = AssetsManager.getFrames("backgroundchooselevel");
        if (backgroundFrames != null && backgroundFrames.length > 0) {
            ImageView backgroundView = new ImageView(backgroundFrames[0]);
            backgroundView.setFitWidth(WINDOW_WIDTH);
            backgroundView.setFitHeight(WINDOW_HEIGHT);
            backgroundView.setPreserveRatio(false);
            getChildren().add(backgroundView);
        }

        createBackground();
        createLevelMap();
        createLevelInfoPanel();
        createBackButton();

        animateBackgroundStars(backgroundPane);
        setupParallaxEffect();


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

    private void createBackground() {
        backgroundPane = new Pane();

        backgroundPane.setStyle("-fx-background-color: transparent;");
        backgroundPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        getChildren().add(backgroundPane);
    }

    private void animateBackgroundStars(Pane container) {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Circle star = new Circle(1, Color.rgb(255, 255, 255, 0.6));
            star.setCenterX(rand.nextDouble() * WINDOW_WIDTH);
            star.setCenterY(rand.nextDouble() * WINDOW_HEIGHT);
            FadeTransition ft = new FadeTransition(Duration.millis(rand.nextInt(3000) + 1000), star);
            ft.setFromValue(0.1);
            ft.setToValue(0.8);
            ft.setAutoReverse(true);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setDelay(Duration.millis(rand.nextInt(1000)));
            ft.play();
            container.getChildren().add(star);
        }
    }


    private void createLevelMap() {
        levelMap = new Pane();
        levelMap.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        int cols = 5; // 5 node mỗi hàng
        int rows = 4; // 4 hàng
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

            if (r % 2 == 1) {
                c = (cols - 1) - c;
            }

            double x = paddingX + c * xSpacing;
            double y = paddingTop + r * ySpacing;

            if (i > 0) {
                x += (jitter.nextDouble() - 0.5) * (xSpacing * 0.2);
                y += (jitter.nextDouble() - 0.5) * (ySpacing * 0.2);
            }

            nodePositions[i][0] = x;
            nodePositions[i][1] = y;
        }

        levelMap.getTransforms().add(mapParallaxTransform);


        createConnections(levelMap, nodePositions);
        for (int i = 0; i < Math.min(nodePositions.length, 20); i++) {
            LevelInfo level = levelManager.getLevel(i + 1);
            if (level != null) {
                createLevelNode(levelMap, level, nodePositions[i][0], nodePositions[i][1]);
            }
        }

        getChildren().add(levelMap);
    }

    private void createLevelNode(Pane container, LevelInfo level, double x, double y) {
        Circle node = new Circle(x, y, 20);
        DropShadow nodeShadow = new DropShadow();
        nodeShadow.setRadius(20);

        if (level.isUnlocked()) {
            node.setFill(Color.rgb(70, 70, 110, 0.9));
            node.setStroke(Color.rgb(150, 200, 255));
            node.setStrokeWidth(2);
            node.setEffect(new Glow(0.6));
            nodeShadow.setColor(Color.rgb(150, 200, 255, 0.7));
        } else {
            node.setFill(Color.rgb(30, 30, 30, 0.7));
            node.setStroke(Color.rgb(200, 80, 80));
            node.setStrokeWidth(2);
            node.getStrokeDashArray().addAll(5.0, 5.0);
            nodeShadow.setColor(Color.rgb(200, 80, 80, 0.7));
        }
        node.setEffect(nodeShadow);

        Label levelNumber = new Label(String.valueOf(level.getId()));
        levelNumber.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        levelNumber.setTextFill(level.isUnlocked() ? Color.WHITE : Color.rgb(200, 100, 100));
        levelNumber.setLayoutX(x - (level.getId() < 10 ? 4 : 8));
        levelNumber.setLayoutY(y - 7);
        levelNumber.setMouseTransparent(true);

        node.setOnMouseClicked(e -> {
            if (level.isUnlocked()) {
                selectLevel(level.getId());
                showLevelInfoPanel(true);
            }
        });

        node.setOnMouseEntered(e -> {
            if (level.isUnlocked()) {
                node.setEffect(new Glow(1.0));
                ScaleTransition scale = new ScaleTransition(Duration.millis(100), node);
                scale.setToX(1.3);
                scale.setToY(1.3);
                scale.play();
            }
        });

        node.setOnMouseExited(e -> {
            if (level.isUnlocked()) {
                if (selectedLevel == null || selectedLevel.getId() != level.getId()) {
                    node.setEffect(new Glow(0.6));
                }
                ScaleTransition scale = new ScaleTransition(Duration.millis(100), node);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            }
        });

        container.getChildren().addAll(node, levelNumber);

        levelNodes.put(level.getId(), node);
    }

    private void createConnections(Pane container, double[][] positions) {
        for (int i = 0; i < positions.length - 1; i++) {
            Line line = new Line(positions[i][0], positions[i][1], positions[i + 1][0], positions[i + 1][1]);
            LevelInfo level2 = levelManager.getLevel(i + 2);

            if (level2 != null && level2.isUnlocked()) {
                line.setStroke(Color.rgb(180, 210, 255, 0.9));
                line.setStrokeWidth(2);
                line.getStrokeDashArray().addAll(10.0, 5.0);
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(line.strokeDashOffsetProperty(), 0)),
                        new KeyFrame(Duration.seconds(1), new KeyValue(line.strokeDashOffsetProperty(), 15))
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            } else {
                line.setStroke(Color.rgb(100, 100, 140, 0.5));
                line.setStrokeWidth(1.5);
                line.getStrokeDashArray().addAll(5.0, 5.0);
            }
            container.getChildren().add(line);
        }
    }

    private void createLevelInfoPanel() {
        levelInfoPanel = new VBox(15);
        levelInfoPanel.setAlignment(Pos.CENTER);
        levelInfoPanel.setPrefSize(350, 280);

        levelInfoPanel.setLayoutX(WINDOW_WIDTH / 2 - 175);
        levelInfoPanel.setLayoutY(WINDOW_HEIGHT / 2 - 140);

        levelInfoPanel.setStyle("-fx-background-color: rgba(40,40,60,0.95); -fx-background-radius: 12; -fx-border-color: #aaccff; -fx-border-radius: 12; -fx-border-width: 1.5;");
        levelInfoPanel.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));
        levelInfoPanel.setVisible(false);
        levelInfoPanel.setOpacity(0);

        Label title = new Label();
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);
        Label goal = new Label();
        goal.setFont(Font.font("Arial", 16));
        goal.setTextFill(Color.LIGHTGRAY);
        goal.setWrapText(true);
        goal.setMaxWidth(300);
        Label current = new Label();
        current.setFont(Font.font("Arial", 16));
        current.setTextFill(Color.LIGHTGRAY);
        Label timeLimit = new Label();
        timeLimit.setFont(Font.font("Arial", 16));
        timeLimit.setTextFill(Color.LIGHTGRAY);

        playButton = createImageButton("playbutton1", "playbutton2");
        leaderboardButton = createImageButton("BtnHighNormal", "BtnHighHover");
        infoBackButton = createImageButton("backbutton1", "backbutton2");

        HBox buttons = new HBox(15, leaderboardButton, playButton, infoBackButton);
        buttons.setAlignment(Pos.CENTER);
        infoBackButton.setOnAction(e -> showLevelInfoPanel(false));

        levelInfoPanel.getChildren().addAll(title, goal, current, timeLimit, buttons);
        getChildren().add(levelInfoPanel);
    }

    private void createBackButton() {
        backButton = createImageButton("backtomenu2", "backtomenu1");

        backButton.setOnAction(e -> {
            System.out.println("Return to main menu!");
        });

        getChildren().add(backButton);
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backButton, new Insets(0, 0, 20, 20));
    }

    private void setupParallaxEffect() {
        this.setOnMouseMoved(e -> {
            double parallaxX = -1 * ((e.getX() / WINDOW_WIDTH) - 0.5) * 30;
            double parallaxY = -1 * ((e.getY() / WINDOW_HEIGHT) - 0.5) * 20;

            parallaxTimeline.stop();
            parallaxTimeline = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(mapParallaxTransform.xProperty(), parallaxX, Interpolator.EASE_OUT),
                            new KeyValue(mapParallaxTransform.yProperty(), parallaxY, Interpolator.EASE_OUT)
                    )
            );
            parallaxTimeline.play();
        });
    }

    public void selectLevel(int id) {
        LevelInfo info = levelManager.getLevel(id);
        if (info == null || !info.isUnlocked()) return;

        // Dừng và reset node cũ
        if (selectedNodePulse != null) {
            selectedNodePulse.stop();
        }
        if (selectedLevel != null) {
            Circle oldNode = levelNodes.get(selectedLevel.getId());
            if (oldNode != null) {
                oldNode.setScaleX(1.0);
                oldNode.setScaleY(1.0);
                oldNode.setEffect(new Glow(0.6));
            }
        }

        selectedLevel = info;
        levelManager.selectLevel(id);
        updateLevelInfoPanel();


        Circle newNode = levelNodes.get(selectedLevel.getId());
        if (newNode != null) {
            newNode.setEffect(new Glow(1.0));
            selectedNodePulse = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(newNode.scaleXProperty(), 1.0),
                            new KeyValue(newNode.scaleYProperty(), 1.0)
                    ),
                    new KeyFrame(Duration.millis(500),
                            new KeyValue(newNode.scaleXProperty(), 1.2),
                            new KeyValue(newNode.scaleYProperty(), 1.2)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(newNode.scaleXProperty(), 1.0),
                            new KeyValue(newNode.scaleYProperty(), 1.0)
                    )
            );
            selectedNodePulse.setCycleCount(Timeline.INDEFINITE);
            selectedNodePulse.play();
        }
    }


    private void showLevelInfoPanel(boolean visible) {
        levelMap.setMouseTransparent(visible);

        FadeTransition mapFade = new FadeTransition(Duration.millis(200), levelMap);
        mapFade.setToValue(visible ? 0.2 : 1.0);
        mapFade.play();

        FadeTransition panelFade = new FadeTransition(Duration.millis(200), levelInfoPanel);
        ScaleTransition panelScale = new ScaleTransition(Duration.millis(200), levelInfoPanel);
        TranslateTransition panelTranslate = new TranslateTransition(Duration.millis(200), levelInfoPanel);

        if (visible) {
            levelInfoPanel.setVisible(true);
            panelFade.setFromValue(0);
            panelFade.setToValue(1.0);
            panelScale.setFromX(0.9);
            panelScale.setFromY(0.9);
            panelScale.setToX(1.0);
            panelScale.setToY(1.0);

            panelTranslate.setFromY(20);
            panelTranslate.setToY(0);

            new ParallelTransition(panelFade, panelScale, panelTranslate).play();
        } else {
            panelFade.setFromValue(1.0);
            panelFade.setToValue(0);
            panelScale.setFromX(1.0);
            panelScale.setFromY(1.0);
            panelScale.setToX(0.9);
            panelScale.setToY(0.9);

            panelTranslate.setFromY(0);
            panelTranslate.setToY(20);

            ParallelTransition pt = new ParallelTransition(panelFade, panelScale, panelTranslate);
            pt.setOnFinished(e -> levelInfoPanel.setVisible(false));
            pt.play();
        }
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

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(10);
        button.setEffect(shadow);

        button.setOnMouseEntered(e -> {
            imageView.setImage(hoverImg);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            imageView.setImage(normalImg);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }



    public void refreshView() {

        if (selectedNodePulse != null) {
            selectedNodePulse.stop();
        }


        if (levelMap != null) {
            getChildren().remove(levelMap);
        }
        levelNodes.clear();


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


        if (levelInfoPanel != null) {
            levelInfoPanel.toFront();
        }
        if (backButton != null) {
            backButton.toFront();
        }
    }



    public Button getPlayButton() {
        return playButton;
    }

    public Button getLeaderboardButton() {
        return leaderboardButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public LevelInfo getSelectedLevel() {
        return selectedLevel;
    }
}