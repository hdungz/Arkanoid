package com.arkanoid.view.paddle;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import static com.arkanoid.CONSTANT.BALL_RADIUS;

public class StickyPaddleRenderer extends BasePaddleRenderer {
    private final SpriteAnimator animator;
    private final Group renderGroup;
    private final Polygon arrow;
    private final Rotate arrowRotate;
    private static final double ARROW_DISTANCE = 18;

    public StickyPaddleRenderer(GameModel gameModel) {
        super(gameModel);

        Image[] images;

        images = AssetsManager.getFrames("VIPPaddle");
        this.animator = new SpriteAnimator(images, 2);
        this.paddleSprite.setImage(animator.getCurrentFrame());

        this.arrow = createArrow();
        this.arrowRotate = new Rotate(0, 0, 0);
        this.arrow.getTransforms().add(arrowRotate);

        this.renderGroup = new Group();
        this.renderGroup.getChildren().add(paddleSprite);
        this.renderGroup.getChildren().add(arrow);
    }

    private Polygon createArrow() {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, -20.0,
                -6.0, -8.0,
                -2.5, -8.0,
                -2.5, 0.0,
                2.5, 0.0,
                2.5, -8.0,
                6.0, -8.0
        );

        arrow.setFill(Color.rgb(255, 215, 0));
        arrow.setStroke(Color.rgb(255, 140, 0));
        arrow.setStrokeWidth(2);

        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(10);
        glow.setSpread(0.6);
        arrow.setEffect(glow);

        arrow.setVisible(false);

        return arrow;
    }

    @Override
    protected void customRender() {
        animator.update();
        paddleSprite.setImage(animator.getCurrentFrame());

        Paddle paddle = gameModel.getPaddle();

        if (paddle instanceof StickyPaddle) {
            StickyPaddle sp = (StickyPaddle) paddle;
            if (sp.isBallStuck()) {
                arrow.setVisible(true);

                double ballCenterX = sp.getArrowX();
                double ballCenterY = sp.getArrowY();

                double angleRad = Math.toRadians(sp.getArrowAngle());

                double arrowX = ballCenterX + Math.cos(angleRad) * ARROW_DISTANCE;
                double arrowY = ballCenterY + Math.sin(angleRad) * ARROW_DISTANCE;

                arrow.setLayoutX(arrowX);
                arrow.setLayoutY(arrowY);

                arrowRotate.setAngle(sp.getArrowAngle() + 90);
            }
            else {
                arrow.setVisible(false);
            }

            if (sp.isBlinking()) {
                paddleSprite.setVisible(false);
            }
            else {
                paddleSprite.setVisible(true);
            }
        }
        else {
            paddleSprite.setVisible(true);
            arrow.setVisible(false);
        }
    }

    @Override
    public Node getNode() {
        return renderGroup;
    }
}