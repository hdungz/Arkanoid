package com.arkanoid.utils;
import com.arkanoid.model.GameModel;
import com.arkanoid.CONSTANT;
import com.arkanoid.model.GameState;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.classfile.instruction.LoadInstruction;
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
                    double x = CONSTANT.GAME_AREA_X + j * (brickWidth + 5) + 5 ;
                    double y = CONSTANT.BORDER_WIDTH + i * (brickHeight + 5) + 5 - 2500;
                    double finaly = (CONSTANT.BORDER_WIDTH + 30) + i * (brickHeight + 5) + 5;
                    char c = line.charAt(j);
                    if (c == '1') bricks.add(new DropBrick(x, y,finaly, brickWidth, brickHeight, BrickType.DROPPER));
                    else if (c == '2') bricks.add(new SuperDurableBrick(x, y,finaly, brickWidth, brickHeight, BrickType.SUPERDURABLE));
                    else if (c == '3') bricks.add(new ExplodingBrick(x, y,finaly, brickWidth, brickHeight, BrickType.EXPLODING));
                    else if (c == '4') bricks.add(new DurableBrick(x, y,finaly, brickWidth, brickHeight, BrickType.DURABLE));
                    else if (c == '5') bricks.add(new MoveBrick(x, y,finaly, brickWidth, brickHeight, BrickType.MOVING));
                    else if (c == '6') bricks.add(new Brick(x, y,finaly, brickWidth, brickHeight, BrickType.NORMAL, 1));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
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
    public void brickfalldown(ArrayList<Brick> bricks, double deltaTime) {
        final double SPEED = 900.0;
        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                brick.setY(brick.getY() + SPEED * deltaTime);
            }
        }
    }
    public void loadCurrentLevel(GameModel gameModel) {
        int levelToLoad = gameModel.getLevelmanager().getCurrentLevel();
        gameModel.setCurrentLevel(levelToLoad);

        gameModel.setScore(0);
        gameModel.setLives(3);
        gameModel.getBricks().clear();
        gameModel.getExtraBalls().clear();
        gameModel.getPowerUpManager().clear();
        gameModel.setLevelCompleteDelay(0);
        gameModel.getEffects().clear();
        gameModel.getCoinManager().clear();
        Paddle newPaddle = new Paddle(PowerUpPaddleType.Normal);
        newPaddle.resetPosition();
        gameModel.setPaddle(newPaddle);

        gameModel.getBall().resetPosition(gameModel.getPaddle());

        gameModel.setGameState(GameState.Ready);

        loadLevelfromfile(levelToLoad, gameModel.getBricks());

        gameModel.setBrickFalling(true);
        gameModel.setNeedsViewSync(true);
        this.loaded = true;
    }
    public void loadNextLevel(GameModel gameModel) {
        int nextLevel = gameModel.getCurrentLevel() + 1;
        int currentScore = gameModel.getScore();
        int currentLives = gameModel.getLives();
        if (nextLevel <= 20) {
            gameModel.getLevelmanager().selectLevel(nextLevel);
            loadCurrentLevel(gameModel);
            gameModel.setScore(currentScore);
            gameModel.setLives(currentLives);
        } else {
            gameModel.setGameState(GameState.Win);
            System.out.println("Game Completed! All levels finished!");
        }
    }
    public static LoadLevelManager  getInstance() {
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
