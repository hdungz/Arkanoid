package com.arkanoid.model.brick;
import com.arkanoid.model.GameModel;
public class DropBrick extends Brick {

    public DropBrick(double x, double y, double width, double height, BrickType type) {

        super(x, y, width, height, type, 2);
    }
    @Override
    public boolean takeDamage() {
        setHealth(getHealth() - 1);
        if (getHealth() <= 0) {
            setVisible(false);
            double centerX = getX() + getWidth() / 2;
            double centerY = getY() + getHeight() / 2;
            GameModel.getInstance().getPowerUpManager().spawnPowerUp(centerX, centerY);
            return true;
        }
        return false;
    }


}
