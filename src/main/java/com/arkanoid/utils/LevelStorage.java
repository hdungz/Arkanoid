package com.arkanoid.utils; // Đặt vào package .utils (hoặc package bạn muốn)

import java.nio.file.*;

public class LevelStorage {

    private static final String SAVE_FILE = "arkanoid_levels.txt";
    private static String SAVE_PATH = "src/main/resources/com/arkanoid/data/arkanoid_levels.txt";
    private static int unlockedLevels = 1;
    private static boolean loaded = false;


    private static void load() {
        if (loaded) return;

        try {
            Path path = Paths.get(SAVE_PATH);
            if (Files.exists(path)) {
                String content = Files.readString(path);
                String[] lines = content.split("\n");
                for (String line : lines) {
                    line = line.trim();

                    if (line.startsWith("unlockedLevels=")) {
                        String valueStr = line.substring(15).trim();
                        unlockedLevels = Integer.parseInt(valueStr);
                        break;
                    }
                }
            } else {
                unlockedLevels = 1;
                save();
            }

            loaded = true;
        } catch (Exception e) {
            System.err.println("Error loading unlocked levels: " + e.getMessage());
            unlockedLevels = 1;
        }
    }


    private static void save() {
        try {
            Path path = Paths.get(SAVE_PATH);
            Path dir = path.getParent();

            if (dir != null && !Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            StringBuilder content = new StringBuilder();
            content.append("unlockedLevels=").append(unlockedLevels); // <-- Đổi "coins="

            Files.writeString(path, content.toString());

            System.out.println("Saved unlocked levels: " + unlockedLevels); // <-- Log message

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int loadUnlockedLevels() {
        if (!loaded) {
            load();
        }
        return unlockedLevels;
    }

    public static void saveUnlockedLevels(int levelsToSave) {
        unlockedLevels = Math.max(1, levelsToSave);
        save();
        loaded = true;
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
                unlockedLevels = 1;
                loaded = false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting progress save file: " + e.getMessage());
        }
    }

    public static void reset() {
        unlockedLevels = 1;
        loaded = true;
        save();
    }
}