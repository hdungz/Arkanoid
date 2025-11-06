package com.arkanoid.model.ball;

public enum BallType {
    Default("EnBallRed", "EnBallRed",10, 2.5, 1, 1),
    BasketBall("BasketBall", "Basketball",15, 1.8, 1.5, 1);


    private final String name;
    private final double radius;
    private final double initialSpeed;
    private final double damage;
    private final int hitspot;
    private final String assetKey;

    BallType(String name, String assetKey, double radius, double initialSpeed, double damage, int hitspot) {
        this.name = name;
        this.radius = radius;
        this.initialSpeed = initialSpeed;
        this.damage = damage;
        this.hitspot = hitspot;
        this.assetKey = assetKey;
    }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public double getInitialSpeed() {
        return initialSpeed;
    }

    public double getDamage() {
        return damage;
    }

    public int getHitspot() {
        return hitspot;
    }

    public String getAssetKey() {
        return assetKey;
    }
}
