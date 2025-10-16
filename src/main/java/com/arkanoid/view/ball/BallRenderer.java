package com.arkanoid.view.ball;

import com.arkanoid.utils.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
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

    // Hiệu ứng mở rộng
    private final LinkedList<Circle> trailList = new LinkedList<>();
    private static final int MAX_TRAIL = 40; // số vệt sáng
    private double lastX, lastY;
    private double rotation = 0;

    private final Group group; // để chứa bóng + hiệu ứng vệt sáng

    public BallRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.ball = gameModel.getBall();

        images = AssetsManager.getFrames("EnBallRed");
        animator = new SpriteAnimator(images, images.length);
        ballSprite = new ImageView(animator.getCurrentFrame());

        double diameter = ball.getRadius() * 2;
        ballSprite.setFitWidth(diameter);
        ballSprite.setFitHeight(diameter);

        // Hiệu ứng sáng nhẹ
        Glow glow = new Glow(0.5);
        ballSprite.setEffect(glow);

        group = new Group();
        group.getChildren().add(ballSprite);

        lastX = ball.getX();
        lastY = ball.getY();
    }

    public void render() {
        animator.update();
        ballSprite.setImage(animator.getCurrentFrame());

        updateTrail();
        updateRotation();
        updatePosition();
    }

    private void updateTrail() {
        double x = ball.getX();
        double y = ball.getY();

        Circle trail = new Circle(x, y, ball.getRadius() * 0.7, Color.rgb(255, 200, 50, 0.3));
        trailList.addFirst(trail);
        group.getChildren().add(0, trail);

        if (trailList.size() > MAX_TRAIL) {
            Circle old = trailList.removeLast();
            group.getChildren().remove(old);
        }

        for (int i = 0; i < trailList.size(); i++) {
            Circle c = trailList.get(i);
            c.setOpacity(Math.max(0, 0.4 - (i * 0.008)));
            c.setRadius(ball.getRadius() * (0.8 - i * 0.01));
            double ratio = (double) i / trailList.size();
            Color color = Color.rgb(
                    (int) (255 - 100 * ratio),
                    (int) (150 - 100 * ratio),
                    0,
                    1.0
            );
            c.setFill(color);

        }

        lastX = x;
        lastY = y;
    }

    private void updateRotation() {
        double dx = ball.getX() - lastX;
        double dy = ball.getY() - lastY;
        rotation += Math.sqrt(dx * dx + dy * dy) * 2; // quay nhanh hơn khi bóng di chuyển nhanh
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
