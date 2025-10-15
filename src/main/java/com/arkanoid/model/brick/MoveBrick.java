package com.arkanoid.model.brick;

import com.arkanoid.CONSTANT;

import java.util.ArrayList;

public class MoveBrick extends Brick {
    private double moveRange = 50.0;
    private double moveSpeed = 20.0;
    private double initialX;
    private int direction = 1;

    public MoveBrick(double x, double y, double width, double height, BrickType type) {
        super(x, y, width, height, type, 1);
        this.initialX = x;
    }

    public void update(double deltaTime) {
        if (!isVisible()) return;

        double deltaX = moveSpeed * direction * deltaTime;
        double nextX = getX() + deltaX;
        boolean atRightLimit = nextX >= initialX + moveRange;
        boolean atLeftLimit = nextX <= initialX - moveRange;
        double screenLeftLimit = CONSTANT.GAME_AREA_X;
        double screenRightLimit = CONSTANT.GAME_AREA_END_X - getWidth();

        if (direction == 1 && (atRightLimit || nextX >= screenRightLimit)) {
            setX(Math.min(initialX + moveRange, screenRightLimit));
            direction = -1;
        }
        else if (direction == -1 && (atLeftLimit || nextX <= screenLeftLimit)) {
            setX(Math.max(initialX - moveRange, screenLeftLimit));
            direction = 1;
        } else {
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
