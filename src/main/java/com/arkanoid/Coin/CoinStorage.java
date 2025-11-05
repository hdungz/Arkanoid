package com.arkanoid.Coin;

import java.nio.file.*;

public class CoinStorage {
    private static final String SAVE_FILE = "arkanoid_coins.txt";
    private static String SAVE_PATH = "src/main/resources/com/arkanoid/data/arkanoid_coins.txt";

    private static int totalCoins = 0;
    private static boolean loaded = false;

    public static void load() {
        if (loaded) return;

        try {
            Path path = Paths.get(SAVE_PATH);
            if (Files.exists(path)) {
                String content = Files.readString(path);
                String[] lines = content.split("\n");
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("coins=")) {
                        String valueStr = line.substring(6).trim();
                        totalCoins = Integer.parseInt(valueStr);
                        break;
                    }
                }
            } else {
                totalCoins = 0;
                save();
            }

            loaded = true;
        } catch (Exception e) {
            System.err.println("Error loading coins: " + e.getMessage());
            totalCoins = 0;
        }
    }


    public static void save() {
        try {
            Path path = Paths.get(SAVE_PATH);
            Path dir = path.getParent();
            
            if (dir != null && !Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            
            StringBuilder content = new StringBuilder();
            content.append("coins=").append(totalCoins);
            
            Files.writeString(path, content.toString());

            System.out.println("Saved coins: " + totalCoins + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addCoins(int amount) {
        if (!loaded) load();
        totalCoins += amount;
        save();
    }

    public static boolean spendCoins(int amount) {
        if (!loaded) load();

        if (totalCoins >= amount) {
            totalCoins -= amount;
            save();
            return true;
        }
        return false;
    }

    public static int getTotalCoins() {
        if (!loaded) load();
        return totalCoins;
    }

    public static void setTotalCoins(int amount) {
        if (!loaded) load();

        totalCoins = Math.max(0, amount);
        save();
    }

    public static void reset() {
        totalCoins = 0;
        save();
    }

    public static boolean hasEnoughCoins(int amount) {
        if (!loaded) load();
        return totalCoins >= amount;
    }

    public static String getSavePath() {
        return SAVE_PATH;
    }

    public static boolean saveFileExists() {
        return Files.exists(Paths.get(SAVE_PATH));
    }

    public static void deleteSaveFile() {
        try {
            Path path = Paths.get(SAVE_PATH);
            if (Files.exists(path)) {
                Files.delete(path);
                totalCoins = 0;
                loaded = false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting save file: " + e.getMessage());
        }
    }
}