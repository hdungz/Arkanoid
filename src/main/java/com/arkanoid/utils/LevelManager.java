package com.arkanoid.utils;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.brick.*;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static LevelManager instance;
    private final List<LevelInfo> levels;
    private int currentLevel;
    private int unlockedLevels;
    public LevelManager() {
        levels = new ArrayList<>();
        initializeLevels();
        currentLevel = 1;
        unlockedLevels = 1; // Level đầu tiên luôn mở khóa
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    // Khởi tạo danh sách level
    private void initializeLevels() {
        for (int i = 1; i <= 20; i++) {
            LevelInfo level = new LevelInfo();
            level.setId(i);
            level.setName("Level " + i);
            level.setUnlocked(i == 1); // chỉ mở level 1 ban đầu

            // Thêm thưởng khác nhau
            levels.add(level);
        }
    }
    public boolean WinLevels(ArrayList<Brick> bricks) {
        for (Brick brick : bricks) {
            if (!(brick instanceof SuperDurableBrick) && brick.isVisible()) {
                return false;
            }
        }
        LoadLevelManager loadLevelManager = LoadLevelManager.getInstance();
        loadLevelManager.setLoaded(false);
        return true;
    }

    // Chọn level
    public void selectLevel(int levelId) {
        if (levelId >= 1 && levelId <= levels.size()) {
            LevelInfo level = levels.get(levelId - 1);
            if (level.isUnlocked()) {
                currentLevel = levelId;
                System.out.println("Selected level: " + levelId);
            } else {
                System.out.println("Level " + levelId + " is locked!");
            }
        }
    }

    // Mở khoá 1 level cụ thể
    public void unlockLevel(int levelId) {
        if (levelId >= 1 && levelId <= levels.size()) {
            LevelInfo level = levels.get(levelId - 1);
            if (!level.isUnlocked()) {
                level.setUnlocked(true);
                System.out.println("Unlocked level " + levelId);
            }
            if (levelId > unlockedLevels) {
                unlockedLevels = levelId;
            }
        }
    }

    // Gọi khi người chơi thắng 1 màn
    public void completeLevel(int levelId) {
        if (levelId >= 1 && levelId <= levels.size()) {
            LevelInfo level = levels.get(levelId - 1);

            // Mở khoá level kế tiếp nếu có
            if (levelId < levels.size()) {
                unlockLevel(levelId + 1);
            }
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public LevelInfo getLevel(int levelId) {
        if (levelId >= 1 && levelId <= levels.size()) {
            return levels.get(levelId - 1);
        }
        return null;
    }

    public List<LevelInfo> getAllLevels() {
        return new ArrayList<>(levels);
    }

    public int getUnlockedLevels() {
        return unlockedLevels;
    }

    public static class LevelInfo {
        private int id;
        private String name;
        private boolean unlocked;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public boolean isUnlocked() { return unlocked; }
        public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    }
}
