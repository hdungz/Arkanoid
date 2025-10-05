package com.arkanoid.view.brick;

import com.arkanoid.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.brick.Brick;
import com.arkanoid.view.SpriteAnimator;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrickRenderer {
    private final GameModel gameModel;
    private final Map<Brick, ImageView> spriteMap;
    private final Map<Brick, SpriteAnimator> animatorMap;


    public BrickRenderer(GameModel model) {
        this.gameModel = model;
        this.spriteMap = new HashMap<>();
        this.animatorMap = new HashMap<>();
        for (Brick brick : model.getBricks()) {
            Image[] frames = getFramesForBrick(brick);

            SpriteAnimator animator = new SpriteAnimator(frames, 5);

            ImageView sprite = new ImageView(animator.getCurrentFrame());
            sprite.setX(brick.getX());
            sprite.setY(brick.getY());
            sprite.setFitWidth(brick.getWidth());
            sprite.setFitHeight(brick.getHeight());

            spriteMap.put(brick, sprite);
            animatorMap.put(brick, animator);
        }
    }

    public void render() {
        for (Brick brick : gameModel.getBricks()) {
            ImageView sprite = spriteMap.get(brick);
            SpriteAnimator animator = animatorMap.get(brick);

            if (sprite == null || animator == null) continue;

            if (brick.isVisible()) {
                sprite.setVisible(true);
                animator.update();
                sprite.setImage(animator.getCurrentFrame());

                // Hiệu ứng dựa trên máu: gạch sẽ mờ dần khi mất máu
                // Sau nếu sửa hiệu ứng thì sẽ sửa ở đây
                sprite.setOpacity((double) brick.getHealth() / 3);

            } else {
                sprite.setVisible(false);
            }
        }
    }

    private Image[] getFramesForBrick(Brick brick) {
        switch (brick.getType()) {
            case NORMAL:
                return AssetsManager.getFrames("Brick1_4");
//            case DURABLE:
//                return AssetsManager.getFrames("brick_durable");

            // case EXPLODING:
            //     return AssetManager.getFrames("brick_exploding");
            default:
                return AssetsManager.getFrames("Brick1_4");
        }
    }

    public List<Node> getNodes() {
        return new ArrayList<>(spriteMap.values());
    }
}
