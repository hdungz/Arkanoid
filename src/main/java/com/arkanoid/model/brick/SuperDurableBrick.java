package com.arkanoid.model.brick;

public class SuperDurableBrick extends Brick {
    public SuperDurableBrick(double x, double y,double finaly, double width, double height, BrickType type) {
        super(x, y,finaly, width, height, type,-1);
    }
    @Override
    public boolean takeDamage() {

       return true;
    }
}
