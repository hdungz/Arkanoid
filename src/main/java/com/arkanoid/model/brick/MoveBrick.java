package com.arkanoid.model.brick;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.LevelManager;

import java.util.ArrayList;

public class MoveBrick extends Brick {
    private double moveRange = 50.0;
    private double moveSpeed = 20.0;
    private double initialX;
    private int direction;

    public MoveBrick(double x, double y,double finaly, double width, double height, BrickType type) {
        super(x, y,finaly, width, height, type, 1);
        this.initialX = x;
        this.direction = this.SetDirection(x);
    }
    public int SetDirection(double x) {
        if (x >CONSTANT.GAME_AREA_X +CONSTANT.GAME_AREA_WIDTH/2) {
            return -1;
        }
        return 1;
    }
    public void update(double deltaTime) {
        if (!isVisible()) return;
        GameModel gm = GameModel.getInstance();
        if(gm.isBrickFalling()){
            return;
        }
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
        if (checkCollision(this, GameModel.getInstance().getBricks())){
            direction *= -1;
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
        return super.takeDamage();
    }
}
