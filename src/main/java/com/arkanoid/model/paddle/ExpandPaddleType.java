package com.arkanoid.model.paddle;

public enum ExpandPaddleType {

    Default("Default", "VIPPaddleExtendable", 200.0, 3.0),
    GreenHell("GreenHell", "Paddle2Extendable", 200.0, 3.2),
    ForestIce("ForestIce", "Paddle1Extendable", 230.0, 3.6);

    private final String displayName;
    private final String assetKey;
    private final double width;
    private final double speed;

    ExpandPaddleType(String displayName, String assetKey, double width, double speed) {
        this.displayName = displayName;
        this.assetKey = assetKey;
        this.width = width;
        this.speed = speed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAssetKey() {
        return assetKey;
    }

    public double getWidth() {
        return width;
    }

    public double getSpeed() {
        return speed;
    }

    public static ExpandPaddleType fromPaddleType(PaddleType paddleType) {
        return switch (paddleType) {
            case Default -> ExpandPaddleType.Default;
            case GreenHell -> ExpandPaddleType.GreenHell;
            case ForestIce -> ExpandPaddleType.ForestIce;
        };
    }
}