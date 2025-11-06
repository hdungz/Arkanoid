package com.arkanoid.utils;

import javafx.scene.paint.Color;

public class ThemeManager {
    private static ThemeManager instance;
    private LevelTheme currentTheme;

    public enum ThemeColor {
        CYAN,
        MAGENTA,
        ORANGE,
        GREEN,
        PURPLE
    }

    public static class LevelTheme {
        private final ThemeColor colorType;
        private final Color primary;
        private final Color secondary;
        private final Color glow;
        private final Color background1;
        private final Color background2;
        private final Color background3;

        public LevelTheme(ThemeColor colorType, Color primary, Color secondary, Color glow,
                          Color bg1, Color bg2, Color bg3) {
            this.colorType = colorType;
            this.primary = primary;
            this.secondary = secondary;
            this.glow = glow;
            this.background1 = bg1;
            this.background2 = bg2;
            this.background3 = bg3;
        }

        public ThemeColor getColorType() { return colorType; }
        public Color getPrimary() { return primary; }
        public Color getSecondary() { return secondary; }
        public Color getGlow() { return glow; }
        public Color getBackground1() { return background1; }
        public Color getBackground2() { return background2; }
        public Color getBackground3() { return background3; }
    }

    private ThemeManager() {
        currentTheme = createTheme(ThemeColor.CYAN);
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void setThemeForLevel(int level) {
        ThemeColor[] colors = ThemeColor.values();
        int colorIndex = (level - 1) % colors.length;
        ThemeColor color = colors[colorIndex];

        currentTheme = createTheme(color);
        System.out.println("🎨 Theme changed to: " + color + " for level " + level);
    }

    private LevelTheme createTheme(ThemeColor color) {
        switch (color) {
            case CYAN:
                return new LevelTheme(
                        ThemeColor.CYAN,
                        Color.CYAN,
                        Color.rgb(100, 200, 255),
                        Color.rgb(0, 255, 255),
                        Color.rgb(5, 10, 30),
                        Color.rgb(15, 20, 50),
                        Color.rgb(8, 12, 35)
                );

            case MAGENTA:
                return new LevelTheme(
                        ThemeColor.MAGENTA,
                        Color.rgb(150, 100, 255),
                        Color.rgb(180, 150, 255),
                        Color.rgb(170, 120, 255),
                        Color.rgb(15, 10, 30),
                        Color.rgb(25, 20, 50),
                        Color.rgb(20, 15, 40)
                );

            case ORANGE:
                return new LevelTheme(
                        ThemeColor.ORANGE,
                        Color.rgb(255, 180, 120),
                        Color.rgb(255, 200, 150),
                        Color.rgb(255, 190, 130),
                        Color.rgb(25, 20, 15),
                        Color.rgb(40, 30, 20),
                        Color.rgb(30, 25, 18)
                );

            case GREEN:
                return new LevelTheme(
                        ThemeColor.GREEN,
                        Color.rgb(0, 255, 100),
                        Color.rgb(100, 255, 150),
                        Color.rgb(50, 255, 100),
                        Color.rgb(5, 20, 10),
                        Color.rgb(10, 40, 20),
                        Color.rgb(8, 30, 15)
                );

            case PURPLE:
                return new LevelTheme(
                        ThemeColor.PURPLE,
                        Color.rgb(150, 50, 255),
                        Color.rgb(180, 100, 255),
                        Color.rgb(170, 80, 255),
                        Color.rgb(15, 5, 30),
                        Color.rgb(30, 10, 50),
                        Color.rgb(20, 8, 40)
                );

            default:
                return createTheme(ThemeColor.CYAN);
        }
    }

    public LevelTheme getCurrentTheme() {
        return currentTheme;
    }

    public Color getPrimaryColor() {
        return currentTheme.getPrimary();
    }

    public Color getSecondaryColor() {
        return currentTheme.getSecondary();
    }

    public Color getGlowColor() {
        return currentTheme.getGlow();
    }

    public Color getPrimaryWithAlpha(double alpha) {
        Color c = currentTheme.getPrimary();
        return Color.rgb(
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255),
                alpha
        );
    }

    public Color getPrimaryDerived(double brightness, double alpha) {
        return currentTheme.getPrimary().deriveColor(0, 1, brightness, alpha);
    }
}