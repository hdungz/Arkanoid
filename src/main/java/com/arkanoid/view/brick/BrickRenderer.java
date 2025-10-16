package com.arkanoid.view.brick;

import com.arkanoid.model.brick.BrickType;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.model.GameModel;
import com.arkanoid.model.brick.Brick;
import com.arkanoid.utils.SpriteAnimator;
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
                sprite.setX(brick.getX());
                sprite.setY(brick.getY());
                animator.update();
                sprite.setImage(animator.getCurrentFrame());

                // Sau nếu sửa hiệu ứng thì sẽ sửa ở đây
                if(brick.getType()==BrickType.SUPERDURABLE ||brick.getType()==BrickType.EXPLODING){
//                    sprite.setOpacity((double) brick.getHealth() / 3);
                }
                if (brick.getHealth() == 1 && brick.getType() == BrickType.DURABLE) {
                    sprite.setImage(AssetsManager.getFrames("CrackedDurableBrick")[0]);
                }else if (brick.getHealth() == 1 && brick.getType() == BrickType.SUPERDURABLE) {
                    sprite.setImage(AssetsManager.getFrames("CrackedSuperDurableBrick")[0]);
                }else if (brick.getHealth() == 1 && brick.getType() == BrickType.EXPLODING){
                    sprite.setImage(AssetsManager.getFrames("CrackedBoomBrick")[0]);
                }else if (brick.getHealth() == 1 && brick.getType() == BrickType.DROPPER) {
                    sprite.setImage(AssetsManager.getFrames("CrackedSponseBrick")[0]);
                }
            } else {
                sprite.setVisible(false);
            }
        }
    }

    private Image[] getFramesForBrick(Brick brick) {
        switch (brick.getType()) {
            case SUPERDURABLE:
                return AssetsManager.getFrames("SuperDurableBrick");
            case NORMAL:
                return AssetsManager.getFrames("NormalBrickRed");
            case DURABLE:
                 return AssetsManager.getFrames("DurableBrick");
            case EXPLODING:
                 return AssetsManager.getFrames("BoomBrick");
            case MOVING:
                return AssetsManager.getFrames("MovingBrick");
            case DROPPER:
                return AssetsManager.getFrames("SponseBrick");
            default:
                return AssetsManager.getFrames("NormalBrickRed");
        }
    }

    public List<Node> getNodes() {
        return new ArrayList<>(spriteMap.values());
    }
}
