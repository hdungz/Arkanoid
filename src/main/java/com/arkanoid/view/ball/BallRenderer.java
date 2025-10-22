package com.arkanoid.view.ball;

import com.arkanoid.utils.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.utils.SpriteAnimator;
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
    private final Image[] images;
    private final SpriteAnimator animator;

    private final LinkedList<Circle> trailList = new LinkedList<>();
    private static final int MAX_TRAIL = 45; // tăng số vệt sáng để mượt hơn
    private double lastX, lastY;
    private double rotation = 0;
    private int checkPierce;

    private final Group group;


    public BallRenderer(GameModel gameModel) {
        this(gameModel, gameModel.getBall());
    }

    public BallRenderer(GameModel gameModel, Ball ball) {
        this.gameModel = gameModel;
        this.ball = ball;

        checkPierce = ball.getPierceBall();
        System.out.println(checkPierce);
        if (checkPierce == 0) {
            images = AssetsManager.getFrames("EnBallRed");
        } else {
            images = AssetsManager.getFrames("PurpleBall");
        }

        animator = new SpriteAnimator(images, images.length);
        ballSprite = new ImageView(animator.getCurrentFrame());

        double diameter = ball.getRadius() * 2;
        ballSprite.setFitWidth(diameter);
        ballSprite.setFitHeight(diameter);

        Glow glow = new Glow(0.8);
        ballSprite.setEffect(glow);

        group = new Group();
        group.getChildren().add(ballSprite);

        lastX = ball.getX();
        lastY = ball.getY();
    }

    public void render() {
        animator.update();
        // cập nhật trạng thái xuyên gạch
        checkPierce = gameModel.getCheckpierce();
        // nếu bóng thay đổi loại thì cập nhật ảnh mới
        if (checkPierce == 0)
            ballSprite.setImage(AssetsManager.getFrames("EnBallRed")[0]);
        else
            ballSprite.setImage(AssetsManager.getFrames("PurpleBall")[0]);
        updateTrail();
        updateRotation();
        updatePosition();
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

            // giảm độ mờ mượt dần hơn
            c.setOpacity(Math.max(0, 0.35 - (i * 0.006)));
            c.setRadius(ball.getRadius() * (0.85 - i * 0.008));

            double ratio = (double) i / trailList.size();
            if (checkPierce == 0) {
                // đỏ cam
                c.setFill(Color.rgb(
                        (int) (255 - 100 * ratio),
                        (int) (140 - 80 * ratio),
                        0,
                        1.0
                ));
            } else {
                // tím plasma
                c.setFill(Color.rgb(
                        (int) (160 + 70 * ratio),  // đỏ tím
                        (int) (60 + 40 * ratio),   // xanh lam
                        (int) (255 - 30 * ratio),  // xanh tím
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
