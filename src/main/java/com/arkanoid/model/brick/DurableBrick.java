package com.arkanoid.model.brick;

public class DurableBrick extends Brick {
    private long LastHit =0;
    private static final long HIT_COOLDOWN = 200_000_000; // 0,2s
    public DurableBrick(double x, double y, double width, double height, BrickType type) {
        super(x, y, width, height, type,2);
    }
    @Override
    public boolean takeDamage() {
        long now = System.nanoTime();
        if (now - LastHit < HIT_COOLDOWN) {
            return false;
        }
        LastHit = now;
        setHealth(getHealth()-1);
        if (getHealth() <= 0) {
            this.visible = false;
            return true;
        } else {
            return false;
        }
    }
}
