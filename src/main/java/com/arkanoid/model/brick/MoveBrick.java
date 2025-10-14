package com.arkanoid.model.brick;

import com.arkanoid.CONSTANT;

import java.util.ArrayList;

public class MoveBrick extends Brick {
    private double moveRange = 50.0; // Phạm vi di chuyển tối đa (50 pixels)
    private double moveSpeed = 20.0; // Tốc độ di chuyển (40 pixels/giây)
    private double initialX;
    private int direction = 1; // 1: sang phải, -1: sang trái

    public MoveBrick(double x, double y, double width, double height, BrickType type) {
        // Brick di chuyển nên có 1 máu
        super(x, y, width, height, type, 1);
        this.initialX = x;
    }

    //  PHƯƠNG THỨC MỚI: CẬP NHẬT VỊ TRÍ
    public void update(double deltaTime) {
        if (!isVisible()) return;

        double deltaX = moveSpeed * direction * deltaTime;
        double nextX = getX() + deltaX;
        boolean atRightLimit = nextX >= initialX + moveRange;
        boolean atLeftLimit = nextX <= initialX - moveRange;
        double screenLeftLimit = CONSTANT.GAME_AREA_X;
        double screenRightLimit = CONSTANT.GAME_AREA_END_X - getWidth();

        // Nếu đang đi sang phải (direction == 1) VÀ (đạt giới hạn phạm vi HOẶC chạm biên phải)
        if (direction == 1 && (atRightLimit || nextX >= screenRightLimit)) {
            // Đặt vị trí tại điểm giới hạn nhỏ hơn (đảm bảo không vượt quá cả hai)
            setX(Math.min(initialX + moveRange, screenRightLimit));
            direction = -1; // Đảo chiều sang trái
        }
        // Nếu đang đi sang trái (direction == -1) VÀ (đạt giới hạn phạm vi HOẶC chạm biên trái)
        else if (direction == -1 && (atLeftLimit || nextX <= screenLeftLimit)) {
            // Đặt vị trí tại điểm giới hạn lớn hơn (đảm bảo không vượt quá cả hai)
            setX(Math.max(initialX - moveRange, screenLeftLimit));
            direction = 1; // Đảo chiều sang phải
        } else {
            // Nếu không chạm giới hạn nào, tiếp tục di chuyển
            setX(nextX);
        }
    }
    public boolean checkCollision(Brick currentbrick, ArrayList<Brick> bricks) {
        for (Brick brick : bricks) {
            if (brick == this || !brick.isVisible()) {
                continue;
            }
            if (currentbrick.getBoundary().intersects(brick.getBoundary())) {
                return true;
            }
        }
        return false;
    }
    // Ghi đè setX/setY để cập nhật tọa độ
    @Override
    public void setX(double x) {
        super.setX(x);
    }

    @Override
    public boolean takeDamage() {
        // Dùng logic takeDamage của Brick cơ sở
        return super.takeDamage();
    }
}
