package com.arkanoid.model.paddle;

public enum PaddleType {

    Default("Default","VIPPaddle", 90.0, 3.0),
    GreenHell("GreenHell","Paddle2",120.0, 2.7),
    ForestIce("ForestIce","Paddle1", 150.0, 3.6 );

    private final String displayName;
    private final double width;
    private final double speed;
    private final String assetKey;

    PaddleType(String displayName, String assetKey, double width, double speed) {
        this.displayName = displayName;
        this.width = width;
        this.speed = speed;
        this.assetKey = assetKey;
    }


    public String getDisplayName() {
        return displayName;
    }

    public double getWidth() {
        return width;
    }

    public double getSpeed() {
        return speed;
    }

    public String getAssetKey() {
        return assetKey;
    }
}

