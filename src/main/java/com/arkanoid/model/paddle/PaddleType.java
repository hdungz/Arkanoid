package com.arkanoid.model.paddle;

public enum PaddleType {

    Default("Default","DefaultPaddle", 90.0, 3.0),
    Aegis("Aegis","AegisPaddle",117.0, 2.7),
    Swift("Swift","SwiftPaddle", 72.0, 3.6 ),
    Titan("Titan", "TitanPaddle",144.0, 2.1);

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

