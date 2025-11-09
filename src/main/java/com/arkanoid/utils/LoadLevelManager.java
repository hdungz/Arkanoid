package com.arkanoid.utils;

import com.arkanoid.model.GameModel;
import com.arkanoid.CONSTANT;
import com.arkanoid.model.GameState;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoadLevelManager {
    private static LoadLevelManager instance;
    private boolean loaded = false;

    public LoadLevelManager(){
        instance = this;
    }

    public void loadLevelfromfile(int level, ArrayList<Brick> bricks) {
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
                    // Y bắt đầu từ ngoài màn hình (âm) để rơi xuống
                    double y = CONSTANT.BORDER_WIDTH + i * (brickHeight + 5) + 5 - 2500;
                    // finalY là vị trí cuối cùng của brick
                    double finaly = (CONSTANT.BORDER_WIDTH + 30) + i * (brickHeight + 5) + 5;
                    char c = line.charAt(j);
                    if (c == '1') bricks.add(new DropBrick(x, y, finaly, brickWidth, brickHeight, BrickType.DROPPER));
                    else if (c == '2') bricks.add(new SuperDurableBrick(x, y, finaly, brickWidth, brickHeight, BrickType.SUPERDURABLE));
                    else if (c == '3') bricks.add(new ExplodingBrick(x, y, finaly, brickWidth, brickHeight, BrickType.EXPLODING));
                    else if (c == '4') bricks.add(new DurableBrick(x, y, finaly, brickWidth, brickHeight, BrickType.DURABLE));
                    else if (c == '5') bricks.add(new MoveBrick(x, y, finaly, brickWidth, brickHeight, BrickType.MOVING));
                    else if (c == '6') bricks.add(new Brick(x, y, finaly, brickWidth, brickHeight, BrickType.NORMAL, 1));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update animation gạch rơi xuống
     * @return true nếu vẫn còn gạch đang rơi, false nếu đã rơi xong
     */
    public boolean updateBrickFallAnimation(ArrayList<Brick> bricks, double deltaTime) {
        double fallSpeed = 2000.0;
        boolean stillFalling = false;

        for (Brick brick : bricks) {
            if (brick.getY() < brick.getFinalY()) {
                double newY = brick.getY() + fallSpeed * deltaTime;
                brick.setY(Math.min(newY, brick.getFinalY()));
                stillFalling = true;
            }
        }
        return stillFalling;
    }

    /**
     * Gạch rơi xuống khi win level (effect)
     */
    public void brickfalldown(ArrayList<Brick> bricks, double deltaTime) {
        final double SPEED = 900.0;
        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                brick.setY(brick.getY() + SPEED * deltaTime);
            }
        }
    }

    /**
     * Load level hiện tại
     */
    public void loadCurrentLevel(GameModel gameModel) {
        int levelToLoad = gameModel.getLevelmanager().getCurrentLevel();
        gameModel.setCurrentLevel(levelToLoad);

        // Reset game state
        gameModel.setScore(0);
        gameModel.setLives(3);
        gameModel.getBricks().clear();
        gameModel.getExtraBalls().clear();
        gameModel.getPowerUpManager().clear();
        gameModel.setLevelCompleteDelay(0);
        gameModel.getEffects().clear();
        gameModel.getCoinManager().clear();

        // Reset paddle
        Paddle newPaddle = new Paddle(PowerUpPaddleType.Normal);
        newPaddle.resetPosition();
        gameModel.setPaddle(newPaddle);

        // Reset ball
        gameModel.getBall().resetPosition(gameModel.getPaddle());

        // Set game state
        gameModel.setGameState(GameState.Ready);

        // Load bricks từ file
        loadLevelfromfile(levelToLoad, gameModel.getBricks());

        // Set flags
        gameModel.setBrickFalling(true);
        gameModel.setNeedsViewSync(true);
        this.loaded = true;

        // BẮT ĐẦU TRANSITION SAU KHI LOAD XONG
        gameModel.getTransitionManager().startLevelTransition(levelToLoad);

        System.out.println("Loaded level " + levelToLoad + " with " + gameModel.getBricks().size() + " bricks");
    }

    /**
     * Load level tiếp theo
     */
    public void loadNextLevel(GameModel gameModel) {
        int nextLevel = gameModel.getCurrentLevel() + 1;
        int currentScore = gameModel.getScore();
        int currentLives = gameModel.getLives();

        if (nextLevel <= 20) {
            gameModel.getLevelmanager().selectLevel(nextLevel);
            loadCurrentLevel(gameModel);

            // Giữ lại score và lives
            gameModel.setScore(currentScore);
            gameModel.setLives(currentLives);

            System.out.println("Loading next level: " + nextLevel);
        } else {
            gameModel.setGameState(GameState.Win);
            System.out.println("Game Completed! All levels finished!");
        }
    }

    public static LoadLevelManager getInstance() {
        if(instance == null){
            instance = new LoadLevelManager();
        }
        return instance;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}