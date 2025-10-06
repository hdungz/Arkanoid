package com.arkanoid.model.paddle;

public enum PaddleType {

    Default("Default","DefaultPaddle", 90.0, 3.0),      // Cân bằng (90 * 1.0, 3 * 1.0)
    Aegis("Aegis","AegisPaddle",117.0, 2.7),           // Rộng hơn, phòng thủ tốt, hơi chậm (90 * 1.3, 3 * 0.9)
    Swift("Swift","SwiftPaddle", 72.0, 3.6 ),            // Hẹp hơn, nhanh nhẹn (90 * 0.8, 3 * 1.2)
    Titan("Titan", "TitanPaddle",144.0, 2.1);           // Siêu rộng, phòng thủ tối thượng, rất chậm (90 * 1.6, 3 * 0.7)

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

