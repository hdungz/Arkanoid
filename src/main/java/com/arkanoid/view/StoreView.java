package com.arkanoid.view;

import com.arkanoid.model.GameModel;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.view.paddle.LaserPaddleRenderer;
import com.arkanoid.view.paddle.NormalPaddleRenderer;
import com.arkanoid.view.paddle.StickyPaddleRenderer;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.arkanoid.CONSTANT.WINDOW_HEIGHT;
import static com.arkanoid.CONSTANT.WINDOW_WIDTH;

public class StoreView extends StackPane {

    private final Button backButton;

    private Button leftArrowPaddleButton;
    private Button rightArrowPaddleButton;
    private Button purchasePaddleButton;
    private ImageView currentPaddleDisplay;
    private Label paddleNameLabel;
    private Label paddlePriceLabel;
    private Label paddleStatusLabel;
    private ImageView paddleLockIcon;

    private Button leftArrowBallButton;
    private Button rightArrowBallButton;
    private Button purchaseBallButton;
    private ImageView currentBallDisplay;
    private Label ballNameLabel;
    private Label ballPriceLabel;
    private Label ballStatusLabel;
    private ImageView ballLockIcon;

    private Label coinLabel;
    private ImageView coinIcon;

    private int currentPaddleIndex = 0;
    private int currentBallIndex = 0;
    private boolean isAnimating = false;

    private Runnable onBackButtonClicked;
    private Runnable onLeftArrowPaddleClicked;
    private Runnable onRightArrowPaddleClicked;
    private Consumer<Integer> onPurchasePaddleClicked;
    private Runnable onLeftArrowBallClicked;
    private Runnable onRightArrowBallClicked;
    private Consumer<Integer> onPurchaseBallClicked;

    private Timeline paddleGlowAnimation;
    private Timeline ballGlowAnimation;

    public StoreView() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setupBackground();

        HBox mainContainer = createMainContainer();
        getChildren().add(mainContainer);

        backButton = createBackButton();
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backButton, new javafx.geometry.Insets(0, 0, 20, 20));
        getChildren().add(backButton);

        HBox coinDisplay = createCoinDisplay();
        StackPane.setAlignment(coinDisplay, Pos.TOP_RIGHT);
        StackPane.setMargin(coinDisplay, new javafx.geometry.Insets(20, 20, 0, 0));
        getChildren().add(coinDisplay);

        setupButtonHandlers();
    }

    private void setupBackground() {
        Image[] backgroundFrames = AssetsManager.getFrames("store");
        if (backgroundFrames != null && backgroundFrames.length > 0) {
            ImageView backgroundView = new ImageView(backgroundFrames[0]);
            backgroundView.setFitWidth(WINDOW_WIDTH);
            backgroundView.setFitHeight(WINDOW_HEIGHT);
            backgroundView.setPreserveRatio(false);
            getChildren().add(backgroundView);
        } else {
            setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");
        }
    }

    private HBox createMainContainer() {
        HBox container = new HBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(60, 40, 100, 40));
        container.setPickOnBounds(false);

        VBox paddleSection = createPaddleSection();
        VBox ballSection = createBallSection();

        container.getChildren().addAll(paddleSection, ballSection);
        return container;
    }

    private VBox createPaddleSection() {
        VBox section = new VBox(12);
        section.setAlignment(Pos.CENTER);
        section.setMaxWidth(450);

        // Nền trong suốt với viền xanh phát sáng
        section.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.3); " +
                        "-fx-border-color: rgba(96, 165, 250, 1); " +
                        "-fx-border-width: 3; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-radius: 0; " +
                        "-fx-padding: 20;"
        );

        // Tạo hiệu ứng glow cho viền paddle
        DropShadow paddleGlow = new DropShadow();
        paddleGlow.setColor(Color.rgb(96, 165, 250, 0.8));
        paddleGlow.setRadius(20);
        paddleGlow.setSpread(0.3);

        paddleGlowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(paddleGlow.radiusProperty(), 15),
                        new KeyValue(paddleGlow.spreadProperty(), 0.2)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(paddleGlow.radiusProperty(), 25),
                        new KeyValue(paddleGlow.spreadProperty(), 0.4)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(paddleGlow.radiusProperty(), 15),
                        new KeyValue(paddleGlow.spreadProperty(), 0.2))
        );
        paddleGlowAnimation.setCycleCount(Timeline.INDEFINITE);
        paddleGlowAnimation.play();

        section.setEffect(paddleGlow);

        Label titleLabel = new Label("PADDLE");
        titleLabel.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #60a5fa; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0, 0, 3);"
        );

        HBox contentBox = new HBox(13);
        contentBox.setAlignment(Pos.CENTER);

        leftArrowPaddleButton = createArrowButton(true);
        VBox centerDisplay = createPaddleDisplay();
        rightArrowPaddleButton = createArrowButton(false);

        contentBox.getChildren().addAll(leftArrowPaddleButton, centerDisplay, rightArrowPaddleButton);
        section.getChildren().addAll(titleLabel, contentBox);

        return section;
    }

    private VBox createPaddleDisplay() {
        VBox display = new VBox(10);
        display.setAlignment(Pos.CENTER);
        display.setMaxWidth(300);

        paddleNameLabel = new Label("Classic Blue");
        paddleNameLabel.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 5, 0, 0, 2);"
        );

        StackPane displayArea = new StackPane();
        displayArea.setPrefSize(200, 200);
        displayArea.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5); " +
                        "-fx-border-color: rgba(96, 165, 250, 0.5); " +
                        "-fx-border-width: 2;"
        );

        currentPaddleDisplay = new ImageView();
        currentPaddleDisplay.setFitWidth(150);
        currentPaddleDisplay.setFitHeight(40);
        currentPaddleDisplay.setPreserveRatio(false);

        paddleLockIcon = createLockIcon();
        paddleLockIcon.setVisible(false);

        displayArea.getChildren().addAll(currentPaddleDisplay, paddleLockIcon);

        VBox infoPanel = new VBox(5);
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPadding(new javafx.geometry.Insets(8));
        infoPanel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.6); " +
                        "-fx-background-radius: 10;"
        );
        infoPanel.setMaxWidth(192);

        paddleStatusLabel = new Label("OWNED");
        paddleStatusLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #4ade80;"
        );

        paddlePriceLabel = new Label("Price: 100 coins");
        paddlePriceLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: gold;"
        );

        purchasePaddleButton = createPurchaseButton("select");

        infoPanel.getChildren().addAll(paddleStatusLabel, paddlePriceLabel, purchasePaddleButton);
        display.getChildren().addAll(paddleNameLabel, displayArea, infoPanel);

        return display;
    }

    private VBox createBallSection() {
        VBox section = new VBox(12);
        section.setAlignment(Pos.CENTER);
        section.setMaxWidth(450);

        // Nền trong suốt với viền vàng phát sáng
        section.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.3); " +
                        "-fx-border-color: rgba(245, 158, 11, 1); " +
                        "-fx-border-width: 3; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-radius: 0; " +
                        "-fx-padding: 20;"
        );

        // Tạo hiệu ứng glow cho viền ball
        DropShadow ballGlow = new DropShadow();
        ballGlow.setColor(Color.rgb(245, 158, 11, 0.8));
        ballGlow.setRadius(20);
        ballGlow.setSpread(0.3);

        ballGlowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(ballGlow.radiusProperty(), 15),
                        new KeyValue(ballGlow.spreadProperty(), 0.2)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(ballGlow.radiusProperty(), 25),
                        new KeyValue(ballGlow.spreadProperty(), 0.4)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(ballGlow.radiusProperty(), 15),
                        new KeyValue(ballGlow.spreadProperty(), 0.2))
        );
        ballGlowAnimation.setCycleCount(Timeline.INDEFINITE);
        ballGlowAnimation.play();

        section.setEffect(ballGlow);

        Label titleLabel = new Label("BALL");
        titleLabel.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #f59e0b; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0, 0, 3);"
        );

        HBox contentBox = new HBox(20);
        contentBox.setAlignment(Pos.CENTER);

        leftArrowBallButton = createArrowButton(true);
        VBox centerDisplay = createBallDisplay();
        rightArrowBallButton = createArrowButton(false);

        contentBox.getChildren().addAll(leftArrowBallButton, centerDisplay, rightArrowBallButton);
        section.getChildren().addAll(titleLabel, contentBox);

        return section;
    }

    private VBox createBallDisplay() {
        VBox display = new VBox(10);
        display.setAlignment(Pos.CENTER);
        display.setMaxWidth(300);

        ballNameLabel = new Label("Classic White");
        ballNameLabel.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 5, 0, 0, 2);"
        );

        StackPane displayArea = new StackPane();
        displayArea.setPrefSize(200, 200);

        displayArea.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5); " +
                        "-fx-border-color: rgba(245, 158, 11, 0.5); " +
                        "-fx-border-width: 2;"
        );

        currentBallDisplay = new ImageView();
        currentBallDisplay.setFitWidth(70);
        currentBallDisplay.setFitHeight(70);
        currentBallDisplay.setPreserveRatio(true);

        ballLockIcon = createLockIcon();
        ballLockIcon.setVisible(false);

        displayArea.getChildren().addAll(currentBallDisplay, ballLockIcon);

        VBox infoPanel = new VBox(5);
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPadding(new javafx.geometry.Insets(8));
        infoPanel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.6); " +
                        "-fx-background-radius: 10;"
        );
        infoPanel.setMaxWidth(192);

        ballStatusLabel = new Label("OWNED");
        ballStatusLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #4ade80;"
        );

        ballPriceLabel = new Label("Price: 80 coins");
        ballPriceLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: gold;"
        );

        purchaseBallButton = createPurchaseButton("select");

        infoPanel.getChildren().addAll(ballStatusLabel, ballPriceLabel, purchaseBallButton);
        display.getChildren().addAll(ballNameLabel, displayArea, infoPanel);

        return display;
    }

    private Button createPurchaseButton(String buttonType) {
        String normalKey = "Btn" + capitalize(buttonType) + "Normal";
        String hoverKey = "Btn" + capitalize(buttonType) + "Hover";

        Image[] normalFrames = AssetsManager.getFrames(normalKey);
        Image[] hoverFrames = AssetsManager.getFrames(hoverKey);

        if (normalFrames != null && normalFrames.length > 0) {
            return createImageButton(
                    normalFrames[0],
                    hoverFrames != null && hoverFrames.length > 0 ? hoverFrames[0] : normalFrames[0],
                    150, 45
            );
        }

        Button button = new Button(buttonType.toUpperCase());
        button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 30; " +
                        "-fx-background-color: #22c55e; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.rgb(0, 0, 0, 0.6));
        buttonShadow.setRadius(10);
        button.setEffect(buttonShadow);

        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }

    private Button createArrowButton(boolean isLeft) {
        String direction = isLeft ? "Left" : "Right";
        String normalKey = "BtnArrow" + direction + "Normal";
        String hoverKey = "BtnArrow" + direction + "Hover";

        Image[] normalFrames = AssetsManager.getFrames(normalKey);
        Image[] hoverFrames = AssetsManager.getFrames(hoverKey);

        if (normalFrames != null && normalFrames.length > 0) {
            return createImageButton(
                    normalFrames[0],
                    hoverFrames != null && hoverFrames.length > 0 ? hoverFrames[0] : normalFrames[0],
                    60, 60
            );
        }

        Button button = new Button();
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-cursor: hand;");

        javafx.scene.shape.Polygon arrow = new javafx.scene.shape.Polygon();
        if (isLeft) {
            arrow.getPoints().addAll(20.0, 0.0, 0.0, 15.0, 20.0, 30.0);
        } else {
            arrow.getPoints().addAll(0.0, 0.0, 20.0, 15.0, 0.0, 30.0);
        }
        arrow.setFill(Color.WHITE);
        arrow.setOpacity(0.8);

        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(25);
        circle.setFill(Color.rgb(0, 0, 0, 0.5));
        circle.setStroke(Color.rgb(255, 255, 255, 0.5));
        circle.setStrokeWidth(2);

        StackPane graphic = new StackPane(circle, arrow);
        button.setGraphic(graphic);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.7));
        shadow.setRadius(15);
        button.setEffect(shadow);

        button.setOnMouseEntered(e -> {
            arrow.setOpacity(1.0);
            circle.setFill(Color.rgb(255, 255, 255, 0.2));
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.15);
            scale.setToY(1.15);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            arrow.setOpacity(0.8);
            circle.setFill(Color.rgb(0, 0, 0, 0.5));
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }

    private Button createBackButton() {
        Image[] normalFrames = AssetsManager.getFrames("BtnBackNormal");
        Image[] hoverFrames = AssetsManager.getFrames("BtnBackHover");

        return createImageButton(normalFrames[0],
                hoverFrames != null && hoverFrames.length > 0 ? hoverFrames[0] : normalFrames[0],
                144, 40);
    }

    private Button createImageButton(Image normalImg, Image hoverImg, double width, double height) {
        ImageView imageView = new ImageView(normalImg);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

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

    private HBox createCoinDisplay() {
        HBox coinBox = new HBox(6);
        coinBox.setAlignment(Pos.CENTER_RIGHT);
        coinBox.setMaxWidth(Region.USE_PREF_SIZE);
        coinBox.setMaxHeight(Region.USE_PREF_SIZE);
        coinBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.7); " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 6 14;"
        );

        Image[] coinFrames = AssetsManager.getFrames("coin");
        coinIcon = new ImageView(coinFrames[0]);
        coinIcon.setFitWidth(28);
        coinIcon.setFitHeight(28);
        coinIcon.setPreserveRatio(true);

        coinLabel = new Label("0");
        coinLabel.setStyle(
                "-fx-font-size: 19px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: gold; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 5, 0, 0, 2);"
        );

        coinBox.getChildren().addAll(coinIcon, coinLabel);
        return coinBox;
    }

    private ImageView createLockIcon() {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(40, 52);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRoundRect(10, 24, 19, 26, 3, 3);

        gc.setStroke(Color.rgb(50, 50, 50));
        gc.setLineWidth(4);
        gc.strokeArc(14, 10, 14, 18, 0, 180, javafx.scene.shape.ArcType.OPEN);

        gc.setFill(Color.rgb(100, 100, 100));
        gc.fillOval(18, 30, 5, 5);
        gc.fillRect(19, 34, 3, 8);

        javafx.scene.image.WritableImage img = new javafx.scene.image.WritableImage(40, 52);
        canvas.snapshot(null, img);

        ImageView lockView = new ImageView(img);

        DropShadow lockShadow = new DropShadow();
        lockShadow.setColor(Color.rgb(0, 0, 0, 0.8));
        lockShadow.setRadius(12);
        lockView.setEffect(lockShadow);

        return lockView;
    }

    private void setupButtonHandlers() {
        backButton.setOnAction(e -> {
            if (onBackButtonClicked != null) {
                onBackButtonClicked.run();
            }
        });

        leftArrowPaddleButton.setOnAction(e -> {
            if (!isAnimating && onLeftArrowPaddleClicked != null) {
                onLeftArrowPaddleClicked.run();
            }
        });

        rightArrowPaddleButton.setOnAction(e -> {
            if (!isAnimating && onRightArrowPaddleClicked != null) {
                onRightArrowPaddleClicked.run();
            }
        });

        purchasePaddleButton.setOnAction(e -> {
            if (onPurchasePaddleClicked != null) {
                onPurchasePaddleClicked.accept(currentPaddleIndex);
            }
        });

        leftArrowBallButton.setOnAction(e -> {
            if (!isAnimating && onLeftArrowBallClicked != null) {
                onLeftArrowBallClicked.run();
            }
        });

        rightArrowBallButton.setOnAction(e -> {
            if (!isAnimating && onRightArrowBallClicked != null) {
                onRightArrowBallClicked.run();
            }
        });

        purchaseBallButton.setOnAction(e -> {
            if (onPurchaseBallClicked != null) {
                onPurchaseBallClicked.accept(currentBallIndex);
            }
        });
    }

    public void setOnLeftArrowPaddleClicked(Runnable callback) {
        this.onLeftArrowPaddleClicked = callback;
    }

    public void setOnRightArrowPaddleClicked(Runnable callback) {
        this.onRightArrowPaddleClicked = callback;
    }

    public void setOnPurchasePaddleClicked(Consumer<Integer> callback) {
        this.onPurchasePaddleClicked = callback;
    }

    public void displayPaddle(int skinIndex, String name, int price, boolean isUnlocked, boolean isSelected) {
        currentPaddleIndex = skinIndex;

        String skinKey = "BtnSkin" + (skinIndex + 1) + "Normal";
        Image[] skinFrames = AssetsManager.getFrames(skinKey);
        if (skinFrames != null && skinFrames.length > 0) {
            currentPaddleDisplay.setImage(skinFrames[0]);
        }

        paddleNameLabel.setText(name);
        paddleLockIcon.setVisible(!isUnlocked);

        updateButtonStyle(purchasePaddleButton, paddleStatusLabel, paddlePriceLabel,
                price, isUnlocked, isSelected);
    }

    public void setOnLeftArrowBallClicked(Runnable callback) {
        this.onLeftArrowBallClicked = callback;
    }

    public void setOnRightArrowBallClicked(Runnable callback) {
        this.onRightArrowBallClicked = callback;
    }

    public void setOnPurchaseBallClicked(Consumer<Integer> callback) {
        this.onPurchaseBallClicked = callback;
    }

    public void displayBall(int skinIndex, String name, int price, boolean isUnlocked, boolean isSelected) {
        currentBallIndex = skinIndex;

        String skinKey = "BtnBall" + (skinIndex + 1) + "Normal";
        Image[] skinFrames = AssetsManager.getFrames(skinKey);
        if (skinFrames != null && skinFrames.length > 0) {
            currentBallDisplay.setImage(skinFrames[0]);
        }

        ballNameLabel.setText(name);
        ballLockIcon.setVisible(!isUnlocked);

        updateButtonStyle(purchaseBallButton, ballStatusLabel, ballPriceLabel,
                price, isUnlocked, isSelected);
    }

    private void updateButtonStyle(Button button, Label statusLabel, Label priceLabel,
                                   int price, boolean isUnlocked, boolean isSelected) {
        String buttonType;
        String statusText;
        String statusColor;
        boolean showPrice;
        boolean disableButton;

        if (isSelected) {
            buttonType = "equipped";
            statusText = "✓ SELECTED";
            statusColor = "#4ade80";
            showPrice = false;
            disableButton = true;
        } else if (isUnlocked) {
            buttonType = "select";
            statusText = "OWNED";
            statusColor = "#60a5fa";
            showPrice = false;
            disableButton = false;
        } else {
            buttonType = "purchase";
            statusText = "LOCKED";
            statusColor = "#ef4444";
            showPrice = true;
            disableButton = false;
        }

        String normalKey = "Btn" + capitalize(buttonType) + "Normal";
        String hoverKey = "Btn" + capitalize(buttonType) + "Hover";

        Image[] normalFrames = AssetsManager.getFrames(normalKey);
        Image[] hoverFrames = AssetsManager.getFrames(hoverKey);

        if (normalFrames != null && normalFrames.length > 0 && button.getGraphic() instanceof ImageView) {
            ImageView imageView = (ImageView) button.getGraphic();
            imageView.setImage(normalFrames[0]);

            final Image normalImg = normalFrames[0];
            final Image hoverImg = (hoverFrames != null && hoverFrames.length > 0)
                    ? hoverFrames[0] : normalFrames[0];

            button.setOnMouseEntered(e -> {
                if (!button.isDisabled()) {
                    imageView.setImage(hoverImg);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                    scale.setToX(1.1);
                    scale.setToY(1.1);
                    scale.play();
                }
            });

            button.setOnMouseExited(e -> {
                imageView.setImage(normalImg);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            });
        }

        statusLabel.setText(statusText);
        statusLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + statusColor + ";"
        );

        priceLabel.setVisible(showPrice);
        if (showPrice) {
            priceLabel.setText("Price: " + price + " coins");
        }

        button.setDisable(disableButton);
        if (disableButton) {
            button.setOpacity(0.7);
        } else {
            button.setOpacity(1.0);
        }
    }

    public void setOnBackButtonClicked(Runnable callback) {
        this.onBackButtonClicked = callback;
    }

    public void updateCoins(int coins) {
        coinLabel.setText(String.valueOf(coins));
    }

    public void playNotEnoughCoinsAnimation() {
        String originalStyle = coinLabel.getStyle();
        coinLabel.setStyle(originalStyle.replace("gold", "red"));

        TranslateTransition shake = new TranslateTransition(Duration.millis(50), coinLabel);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> coinLabel.setStyle(originalStyle));
        pause.play();
    }

    public void playPurchaseSuccessAnimation(Runnable onComplete) {
        DropShadow goldGlow = new DropShadow();
        goldGlow.setColor(Color.GOLD);
        goldGlow.setRadius(50);
        goldGlow.setSpread(0.8);

        Glow glow = new Glow(0.8);
        glow.setInput(goldGlow);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), coinLabel);
        scale.setToX(1.3);
        scale.setToY(1.3);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);

        scale.setOnFinished(e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });

        scale.play();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public Label getCoinLabel() {
        return coinLabel;
    }

    public Button getBackButton() {
        return backButton;
    }

    public int getCurrentPaddleIndex() {
        return currentPaddleIndex;
    }

    public int getCurrentBallIndex() {
        return currentBallIndex;
    }
}