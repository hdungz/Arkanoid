package com.arkanoid.model.brick;

import com.arkanoid.model.GameModel;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;

public class ExplodingBrick extends Brick {
    protected final GameModel gameModel;
    private double explosionRadius = 50.0;
    private boolean exploding = false;
    private boolean firstHit = true; // Chỉ kêu va chạm lần đầu

    public ExplodingBrick(double x, double y,double finaly, double width, double height, BrickType type) {
        super(x, y,finaly, width, height, type, 2);
        this.gameModel = GameModel.getInstance();
    }

    @Override
    public boolean takeDamage() {
        // ✅ Phát tiếng va chạm lần đầu tiên
        if (firstHit) {
            playHitSound();
            firstHit = false;
        }

        setHealth(getHealth() - 1);
        if (getHealth() <= 0) {
            setExploding(true);
            setVisible(false);
            explode();
            return true;
        } else {
            return false;
        }
    }

    public void explode() {
        playExplosionSound();
        createExplosionEffect();
        damageNearbyBricks();
        notifyExplosion();
    }

    private void createExplosionEffect() {
        double centerX = getX() + getWidth() / 2;
        double centerY = getY() + getHeight() / 2;

        double explosionSize = explosionRadius * 4;
        GameModel.getInstance().onExplosion(centerX, centerY, explosionSize);
    }

    private void damageNearbyBricks() {
        ArrayList<Brick> bricks = GameModel.getInstance().getBricks();
        System.out.println("EXPLOSION DEBUG:");
        System.out.println("  - Total bricks: " + bricks.size());
        System.out.println("  - Explosion center: (" + (getX() + getWidth()/2) + "," + (getY() + getHeight()/2) + ")");
        System.out.println("  - Explosion radius: " + explosionRadius);

        for (Brick brick : bricks) {
            if (brick != this && brick.isVisible() && isInExplosionRange(brick)) {
                System.out.println(" Nổ trúng gạch: " + brick.getType() +
                        " tại (" + brick.getX() + "," + brick.getY() + ")");
                if (brick instanceof SuperDurableBrick) continue;
                else brick.setHealth(brick.getHealth() - 2);
                if (brick.getHealth() <= 0) {
                    brick.setVisible(false);
                    if (brick instanceof ExplodingBrick) {
                        ((ExplodingBrick) brick).explode();
                    }
                }
            }
        }
    }

    private void notifyExplosion() {}

    public boolean getExploding() {
        return exploding;
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double radius) {
        this.explosionRadius = radius;
    }

    public boolean isInExplosionRange(Brick otherBrick) {
        double centerX1 = this.getX() + this.getWidth() / 2;
        double centerY1 = this.getY() + this.getHeight() / 2;
        double centerX2 = otherBrick.getX() + otherBrick.getWidth() / 2;
        double centerY2 = otherBrick.getY() + otherBrick.getHeight() / 2;

        double distance = Math.sqrt(
                Math.pow(centerX1 - centerX2, 2) +
                        Math.pow(centerY1 - centerY2, 2)
        );

        return distance <= explosionRadius;
    }


    private void playExplosionSound() {
        try {
            String soundPath = getClass().getResource("/com/arkanoid/music/explosion-47821.mp3").toExternalForm();
            AudioClip explosionSound = new AudioClip(soundPath);
            explosionSound.play();
        } catch (Exception e) {
            System.out.println("Không thể phát âm thanh vụ nổ: " + e.getMessage());
        }
    }


    public void playHitSound() {
        try {
            String hitPath = getClass().getResource("/com/arkanoid/music/animated-cartoon-explosion-impact-352744.mp3").toExternalForm();
            AudioClip hitSound = new AudioClip(hitPath);
            hitSound.play();
        } catch (Exception e) {
            System.out.println("Không thể phát âm thanh va chạm: " + e.getMessage());
        }
    }
}
