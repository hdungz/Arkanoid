package com.arkanoid.model;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.brick.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static LevelManager instance;
    private List<LevelInfo> levels;
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
            level.setGoal("Goal: Under 00:05");
            level.setTimeLimit("00:30");
            level.setUnlocked(i == 1); // chỉ mở level 1 ban đầu
            level.setStars(0);
            level.setBestTime("N/A");

            // Thêm thưởng khác nhau
            if (i % 3 == 0) {
                level.setRewardType(RewardType.COIN);
                level.setRewardAmount(5);
            } else {
                level.setRewardType(RewardType.STAR);
                level.setRewardAmount(50);
            }
            levels.add(level);
        }
    }

    // Kiểm tra thắng (mọi viên gạch vỡ trừ loại đặc biệt)
    public boolean WinLevels(ArrayList<Brick> bricks) {
        for (Brick brick : bricks) {
            if (!(brick instanceof SuperDurableBrick) && brick.isVisible()) {
                return false;
            }
        }
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

    public void loadLevel(int level,ArrayList<Brick> bricks) {
        bricks.clear();
        String path = String.format("/com/arkanoid/Levels/level%d.txt", level);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
            if (br == null) {
                System.out.println("Không tìm thấy file trong resources");
                return;
            }
            String line1 = br.readLine();
            String line2 = br.readLine();
            if (line1 == null || line2 == null) {
                System.out.println("File rỗng hoặc sai định dạng");
                return;
            }
            int rows = Integer.parseInt(line1);
            int cols = Integer.parseInt(line2);
            double brickWidth = (CONSTANT.GAME_AREA_WIDTH - (cols + 1) * 5.0) / cols;
            double brickHeight = 20;

            for (int i = 0; i < rows; i++) {
                String line = br.readLine();
                if (line == null) break;
                for (int j = 0; j < cols; j++) {
                    double x = CONSTANT.GAME_AREA_X + j * (brickWidth + 5) + 5;
                    double y = CONSTANT.BORDER_WIDTH + i * (brickHeight + 5) + 5;
                    char c = line.charAt(j);
                    if (c == '1') bricks.add(new DropBrick(x, y, brickWidth, brickHeight, BrickType.DROPPER));
                    else if (c == '2') bricks.add(new SuperDurableBrick(x, y, brickWidth, brickHeight, BrickType.SUPERDURABLE));
                    else if (c == '3') bricks.add(new ExplodingBrick(x, y, brickWidth, brickHeight, BrickType.EXPLODING));
                    else if (c == '4') bricks.add(new DurableBrick(x, y, brickWidth, brickHeight, BrickType.DURABLE));
                    else if (c == '5') bricks.add(new MoveBrick(x, y, brickWidth, brickHeight, BrickType.MOVING));
                    else if (c == '6') bricks.add(new Brick(x, y, brickWidth, brickHeight, BrickType.NORMAL, 1));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    // ==========================
    // INNER CLASS + ENUM
    // ==========================
    public static class LevelInfo {
        private int id;
        private String name;
        private String goal;
        private String timeLimit;
        private boolean unlocked;
        private int stars;
        private String bestTime;
        private RewardType rewardType;
        private int rewardAmount;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getGoal() { return goal; }
        public void setGoal(String goal) { this.goal = goal; }

        public String getTimeLimit() { return timeLimit; }
        public void setTimeLimit(String timeLimit) { this.timeLimit = timeLimit; }

        public boolean isUnlocked() { return unlocked; }
        public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }

        public int getStars() { return stars; }
        public void setStars(int stars) { this.stars = stars; }

        public String getBestTime() { return bestTime; }
        public void setBestTime(String bestTime) { this.bestTime = bestTime; }

        public RewardType getRewardType() { return rewardType; }
        public void setRewardType(RewardType rewardType) { this.rewardType = rewardType; }

        public int getRewardAmount() { return rewardAmount; }
        public void setRewardAmount(int rewardAmount) { this.rewardAmount = rewardAmount; }
    }

    public enum RewardType {
        STAR, COIN
    }
}
