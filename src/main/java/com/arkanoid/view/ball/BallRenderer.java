package com.arkanoid.view.ball;

import com.arkanoid.controller.StoreController;
import com.arkanoid.model.ball.BallType;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.utils.SpriteAnimator;
import com.arkanoid.utils.SpriteManager;
import com.arkanoid.view.StoreView;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import java.util.LinkedList;

public class BallRenderer {
    private final GameModel gameModel;
    private final Ball ball;
    private final ImageView ballSprite;
    private Image[] images;
    private SpriteAnimator animator;

    private final LinkedList<Circle> trailList = new LinkedList<>();
    private static final int MAX_TRAIL = 45;
    private double lastX, lastY;
    private double rotation = 0;
    private int checkPierce;

    private final Group group;


    private BallType currentBallType;
    private String currentAssetKey;


    private static final String FALLBACK_ASSET_KEY = "EnBallRed";

    public BallRenderer(GameModel gameModel) {
        this(gameModel, gameModel.getBall());
    }

    public BallRenderer(GameModel gameModel, Ball ball) {
        this.gameModel = gameModel;
        this.ball = ball;


        currentBallType = SpriteManager.getSelectedBall();
        currentAssetKey = getAssetKeyForBallType(currentBallType);


        images = loadImagesWithFallback(currentAssetKey);
        animator = new SpriteAnimator(images, images.length);
        ballSprite = new ImageView(animator.getCurrentFrame());

        double diameter = ball.getRadius() * 2;
        ballSprite.setFitWidth(diameter);
        ballSprite.setFitHeight(diameter);

        Glow glow = new Glow(0.0);
        ballSprite.setEffect(glow);

        group = new Group();
        group.getChildren().add(ballSprite);

        lastX = ball.getX();
        lastY = ball.getY();
    }

    public void render() {
        animator.update();


        BallType selectedBall = SpriteManager.getSelectedBall();
        if (!selectedBall.equals(currentBallType)) {
            updateBallSkin(selectedBall);
        }


        checkPierce = gameModel.getCheckpierce();


        Image[] currentFrames;
        if (checkPierce == 0) {
            currentFrames = loadImagesWithFallback(currentAssetKey);
        } else {
            currentFrames = loadImagesWithFallback("PurpleBall");
        }


        if (currentFrames != null && currentFrames.length > 0) {
            ballSprite.setImage(currentFrames[0]);
        }

        updateTrail();
        updateRotation();
        updatePosition();
    }

    private void updateBallSkin(BallType newBallType) {
        currentBallType = newBallType;
        currentAssetKey = getAssetKeyForBallType(newBallType);


        images = loadImagesWithFallback(currentAssetKey);
        animator = new SpriteAnimator(images, images.length);


        double diameter = ball.getRadius() * 2;
        ballSprite.setFitWidth(diameter);
        ballSprite.setFitHeight(diameter);

        System.out.println("Đã cập nhật skin bóng thành: " + newBallType.getName() + " (" + currentAssetKey + ")");
    }

    private String getAssetKeyForBallType(BallType ballType) {

        String key = ballType.getAssetKey();


        if (key == null || key.isEmpty()) {
            return FALLBACK_ASSET_KEY;
        }

        return key;
    }


    private Image[] loadImagesWithFallback(String assetKey) {
        try {
            Image[] frames = AssetsManager.getFrames(assetKey);


            if (frames == null || frames.length == 0) {
                System.err.println("WARNING: Asset '" + assetKey + "' không có frames, dùng fallback");

                if (!assetKey.equals(FALLBACK_ASSET_KEY)) {
                    frames = AssetsManager.getFrames(FALLBACK_ASSET_KEY);
                }


                if (frames == null || frames.length == 0) {
                    System.err.println("ERROR: Không thể load bất kỳ asset nào, tạo image trắng mặc định");
                    frames = createDefaultImage();
                }
            }

            return frames;

        } catch (Exception e) {
            System.err.println("ERROR: Lỗi khi load asset '" + assetKey + "': " + e.getMessage());


            try {
                if (!assetKey.equals(FALLBACK_ASSET_KEY)) {
                    return AssetsManager.getFrames(FALLBACK_ASSET_KEY);
                }
            } catch (Exception e2) {
                System.err.println("ERROR: Không thể load fallback asset");
            }


            return createDefaultImage();
        }
    }


    private Image[] createDefaultImage() {

        javafx.scene.image.WritableImage defaultImage =
                new javafx.scene.image.WritableImage((int)(ball.getRadius() * 2), (int)(ball.getRadius() * 2));


        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(ball.getRadius() * 2, ball.getRadius() * 2);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(0, 0, ball.getRadius() * 2, ball.getRadius() * 2);

        canvas.snapshot(null, defaultImage);

        return new Image[]{defaultImage};
    }

    private void updateTrail() {
        double x = ball.getX();
        double y = ball.getY();

        Circle trail = new Circle(x, y, ball.getRadius() * 0.8);
        trailList.addFirst(trail);
        group.getChildren().add(0, trail);

        if (trailList.size() > MAX_TRAIL) {
            Circle old = trailList.removeLast();
            group.getChildren().remove(old);
        }

        for (int i = 0; i < trailList.size(); i++) {
            Circle c = trailList.get(i);

            c.setOpacity(Math.max(0, 0.35 - (i * 0.006)));
            c.setRadius(ball.getRadius() * (0.85 - i * 0.008));

            double ratio = (double) i / trailList.size();
            if (checkPierce == 0) {

                c.setFill(Color.rgb(
                        (int) (255 - 100 * ratio),
                        (int) (140 - 80 * ratio),
                        0,
                        1.0
                ));
            } else {

                c.setFill(Color.rgb(
                        (int) (160 + 70 * ratio),
                        (int) (60 + 40 * ratio),
                        (int) (255 - 30 * ratio),
                        1.0
                ));
            }
        }

        lastX = x;
        lastY = y;
    }

    private void updateRotation() {
        double dx = ball.getX() - lastX;
        double dy = ball.getY() - lastY;
        rotation += Math.sqrt(dx * dx + dy * dy) * 2;
        ballSprite.setRotate(rotation);
    }

    private void updatePosition() {
        ballSprite.setX(ball.getX() - ball.getRadius());
        ballSprite.setY(ball.getY() - ball.getRadius());
    }

    public Node getNode() {
        return group;
    }
}