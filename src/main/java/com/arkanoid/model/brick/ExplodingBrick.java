package com.arkanoid.model.brick;

import com.arkanoid.model.GameModel;

import java.util.ArrayList;

public class ExplodingBrick extends Brick {
    protected final GameModel gameModel;
    private double explosionRadius = 50.0; // Bán kính nổ
    private boolean exploding = false;
    public ExplodingBrick(double x, double y, double width, double height, BrickType type) {
        super(x, y, width, height, type, 2);
        this.gameModel = GameModel.getInstance();
    }

    @Override
    public boolean takeDamage() {
        setHealth(getHealth() - 1);
        if (getHealth() <= 0) {
            // Khi bị phá, gạch nổ sẽ nổ
            setExploding(true);  // Đánh dấu đang nổ trước
            setVisible(false);
            explode();
            return true;
        } else {
            return false;
        }
    }

    public void explode() {

        // Tạo hiệu ứng nổ
        createExplosionEffect();

        // Gây sát thương cho gạch xung quanh
        damageNearbyBricks();

        // Thông báo cho GameModel về vụ nổ
        notifyExplosion();
    }

    private void createExplosionEffect() {
        double centerX = getX() + getWidth() / 2;
        double centerY = getY() + getHeight() / 2;

        // SỬA TẠI ĐÂY: Gọi GameModel thay vì EffectManager
        double explosionSize = explosionRadius*4;
        GameModel.getInstance().onExplosion(centerX, centerY, explosionSize);
        // SoundManager.getInstance().playSound("EXPLOSION_SOUND");
    }

    private void damageNearbyBricks() {
        // Lấy danh sách bricks từ GameModel
        ArrayList<Brick> bricks = GameModel.getInstance().getBricks();
        System.out.println("🔍 EXPLOSION DEBUG:");
        System.out.println("  - Total bricks: " + bricks.size());
        System.out.println("  - Explosion center: (" + (getX() + getWidth()/2) + "," + (getY() + getHeight()/2) + ")");
        System.out.println("  - Explosion radius: " + explosionRadius);
        for (Brick brick : bricks) {
            // Kiểm tra điều kiện:
            // 1. Không phải chính gạch nổ này
            // 2. Gạch vẫn còn visible
            // 3. Trong tầm nổ
            if (brick != this && brick.isVisible() && isInExplosionRange(brick)) {
                System.out.println(" Nổ trúng gạch: " + brick.getType() +
                        " tại (" + brick.getX() + "," + brick.getY() + ")");
                // Gây sát thương cho gạch
                brick.setHealth(brick.getHealth() - 2);

                // Nếu gạch bị phá hoàn toàn
                if (brick.getHealth() <= 0) {
                    brick.setVisible(false);
                    // Nếu gạch bị phá cũng là ExplodingBrick, tạo chuỗi nổ
                    if (brick instanceof ExplodingBrick) {
                        ((ExplodingBrick) brick).explode();
                    }
                }
            }
        }
    }

    private void notifyExplosion() {
        // Có thể bổ sung logic cộng điểm, kiểm tra thắng thua tại đây trong GameModel nếu cần
    }
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

    // Method để kiểm tra xem gạch có trong bán kính nổ không
    public boolean isInExplosionRange(Brick otherBrick) {
        // Tính khoảng cách từ tâm gạch nổ đến tâm gạch khác
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
}