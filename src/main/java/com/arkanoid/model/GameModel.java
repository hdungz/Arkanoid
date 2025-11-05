package com.arkanoid.model;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.Laser;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.utils.SceneManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import static com.arkanoid.CONSTANT.*;


import java.util.function.Consumer;

import com.arkanoid.model.paddle.PowerUpPaddleType;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.Effect.ExplosionEffect;
import com.arkanoid.view.GameView;
import javafx.scene.canvas.GraphicsContext;

public class GameModel {
    public enum WallCollisionSide { NONE, TOP, LEFT, RIGHT }
    public int checkpierce = 0;
    private double pierceTimer = 0;
    private static GameModel instance;
    private WallCollisionSide lastWallCollision = WallCollisionSide.NONE;
    ArrayList<Brick> bricks;
    ArrayList<Item> items = new ArrayList<>();
    private final LevelManager levelmanager;
    private int currentLevel = 0;
    private final ArrayList<ExplosionEffect> effects = new ArrayList<>();
    Paddle paddle;
    Ball ball;
    int score;
    int lives;
    GameState gameState;

    public GameModel() {
        instance = this;
        this.score = 0;
        paddle = new Paddle(PowerUpPaddleType.Normal);
        this.levelmanager=LevelManager.getInstance();
        ball = new Ball();
        lives = 3;
        bricks = new ArrayList<>();
        paddle.resetPosition();
        ball.resetPosition(paddle);
        gameState = GameState.Ready;
        loadCurrentLevel();
    }
    public void loadCurrentLevel() {
        // Lấy level ID từ Singleton
        this.currentLevel = levelmanager.getCurrentLevel();

        // Reset trạng thái game
        this.score = 0;
        this.lives = 3;
        this.bricks.clear();
        this.items.clear();
        this.effects.clear();

        // Khởi tạo lại paddle và ball
        this.paddle = new Paddle(PowerUpPaddleType.Normal);
        paddle.resetPosition();
        ball.resetPosition(paddle);
        gameState = GameState.Ready;
        levelmanager.loadLevel(this.currentLevel,bricks);
    }
    /*private void loadLevel(int level) {
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

     */

    public void update(double deltaTime) {
        if (checkpierce == 1) {
            pierceTimer -= deltaTime;
            if (pierceTimer <= 0) {
                checkpierce = 0;
                pierceTimer = 0;
            }
        }

        if (gameState != GameState.Running) {
            if (gameState == GameState.Ready) {
                paddle.move(deltaTime);
                ball.resetPosition(paddle);
            }
            return;
        }

        updateBricks(deltaTime);
        updateEffects();
        capNhatItem(deltaTime);
        paddle.move(deltaTime);

        if (paddle instanceof StickyPaddle) {
            StickyPaddle sp = (StickyPaddle) paddle;
            sp.updateArrow(deltaTime);

            if (sp.isBallStuck()) {
                ball.setX(sp.getStuckBallX());
                ball.setY(sp.getStuckBallY());
                ball.setVelocityX(0);
                ball.setVelocityY(0);
            } else {
                ball.move(deltaTime);
            }
        } else {
            ball.move(deltaTime);
        }

        checkCollisions();
        checkLaserCollisions();

        if (paddle instanceof LaserPaddle) {
            ((LaserPaddle) paddle).updateLasers(deltaTime);
        }
        if(levelmanager.WinLevels(bricks)) {
            gameState = GameState.Win;
            // Gọi hàm hoàn thành level
            LevelManager.getInstance().completeLevel(LevelManager.getInstance().getCurrentLevel());

            // Có thể chuyển sang màn "Level Complete"
            SceneManager.getInstance().switchTo(SceneType.LevelSelection);
        }
    }

    void checkCollisions() {
        this.lastWallCollision = WallCollisionSide.NONE;

        if (paddle instanceof StickyPaddle && ((StickyPaddle) paddle).isBallStuck()) {
            return;
        }

        ball.checkWallCollision(CONSTANT.GAME_AREA_X, CONSTANT.GAME_AREA_END_X, CONSTANT.BORDER_WIDTH, this);

        if (ball.getBoundary().intersects(paddle.getBoundary())) {
            paddle.onBallHit();

            if (paddle instanceof StickyPaddle) {
                StickyPaddle sp = (StickyPaddle) paddle;
                if (!sp.isBallStuck()) {
                    sp.stickBall();
                }
            } else {
                ball.handlePaddleCollision(paddle);
            }
        }

        if (ball.getY() > WINDOW_HEIGHT) {
            lives--;
            stopCurrentPaddleTimer();
            if(paddle instanceof StickyPaddle || paddle instanceof ExpandablePaddle || paddle instanceof LaserPaddle) {
                boolean isMovingRight = paddle.isMovingRight();
                boolean isMovingLeft = paddle.isMovingLeft();
                double x = paddle.getX();
                double y = paddle.getY();
                this.paddle = new Paddle(PowerUpPaddleType.Normal);
                paddle.setX(x);
                paddle.setY(y);
                paddle.setMovingLeft(isMovingLeft);
                paddle.setMovingRight(isMovingRight);
            }
            if (lives == 0) gameState = GameState.GameOver;
            else {
                ball.resetPosition(paddle);
                gameState = GameState.Ready;
            }
        }

        ListIterator<Brick> iterator = bricks.listIterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();

            if (!brick.isVisible()) continue;
            
            if (brick.getBoundary().intersects(ball.getBoundary())) {
                brick.playHitSound();
                if (brick.getHealth() == 1) score += 10;
                if (brick.getHealth() == 2) score += 20;
                if (brick.getHealth() == 3) score += 30;
                brick.takeDamage();

                if (!brick.isVisible()) {
                    if (Math.random() < 0.3) { // 30% xác suất
                        items.add(new Item(brick.getX() + brick.getWidth() / 2, brick.getY()));
                    }
                }

                double ballPrevY = ball.getPrevY();
                double ballRadius = ball.getRadius();
                double brickTop = brick.getY();
                double brickBottom = brick.getY() + brick.getHeight();
                boolean isVerticalCollision;
                if (ballPrevY < brickTop - ballRadius) isVerticalCollision = true;
                else if (ballPrevY > brickBottom + ballRadius) isVerticalCollision = true;
                else isVerticalCollision = false;

                if(checkpierce == 0) {
                    ball.handleBrickCollision(isVerticalCollision);

                    if (isVerticalCollision) {
                        ball.setY(ball.getVelocityY() < 0 ? brickTop - ballRadius : brickBottom + ballRadius);
                        break;
                    } else {
                        ball.setX(ball.getVelocityX() < 0 ? brick.getX() - ballRadius : brick.getX() + brick.getWidth() + ballRadius);
                        break;
                    }
                }
            }
        }
    }

    void checkLaserCollisions() {
        if (!(paddle instanceof LaserPaddle)) {
            return;
        }

        LaserPaddle laserPaddle = (LaserPaddle) paddle;

        for (Laser laser : laserPaddle.getLasers()) {
            if (!laser.isActive()) continue;

            ListIterator<Brick> brickIterator = bricks.listIterator();
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();

                if (!brick.isVisible()) continue;

                if (laser.getBoundary().intersects(brick.getBoundary())) {
                    laser.setActive(false);
                    brick.playHitSound();
                    if (brick.getHealth() == 1) score += 10;
                    else if (brick.getHealth() == 2) score += 20;
                    else if (brick.getHealth() == 3) score += 30;
                    brick.takeDamage();

                    if (!brick.isVisible()) {
                        if (Math.random() < 0.3) {
                            items.add(new Item(brick.getX() + brick.getWidth() / 2, brick.getY()));
                        }
                    }

                    break;
                }
            }
        }
    }

    private void capNhatItem(double deltaTime) {
        for (int i = 0; i < items.size(); i++) {
            Item vatPham = items.get(i);
            vatPham.capNhat(deltaTime);
            if (!vatPham.isHienThi()) {
                items.remove(i--);
                continue;
            }
            if (vatPham.getBoundary().intersects(paddle.getBoundary())) {
                score += 50;

                // Random chọn power-up (33.3% mỗi loại)
                double rand = Math.random();
                if (rand < 0.33) {
                    activateSpecialPaddle(PowerUpPaddleType.ExpandablePaddle);
                    System.out.println("Power-up: EXPAND!");
                } else if (rand < 0.66) {
                    activateSpecialPaddle(PowerUpPaddleType.LaserPaddle);
                    System.out.println("Power-up: LASER!");
                } else {
                    activateSpecialPaddle(PowerUpPaddleType.StickyPaddle);
                    System.out.println("Power-up: STICKY!");
                }

                vatPham.setHienThi(false);
            }
        }
    }

    public void veItem(GraphicsContext gc) {
        for (Item vatPham : items) {
            vatPham.ve(gc);
        }
    }

    private void updateBricks(double deltaTime) {
        for (Brick brick : bricks) {
            if (brick instanceof MoveBrick) ((MoveBrick) brick).update(deltaTime);
        }
    }

    private void updateEffects() {
        effects.removeIf(effect -> {
            effect.update();
            return effect.isFinished();
        });
    }

    public java.util.List<ExplosionEffect> getEffects() { return effects; }

    public void launchBall() {
        if (gameState == GameState.Ready) {
            gameState = GameState.Running;
            ball.launch();
        }

        if (paddle instanceof StickyPaddle) {
            StickyPaddle sp = (StickyPaddle) paddle;
            if (sp.isBallStuck()) {
                double[] velocity = sp.getLaunchVelocity();
                ball.setVelocityX(velocity[0]);
                ball.setVelocityY(velocity[1]);
                sp.launchBall();
                Paddle normalPaddle = new Paddle(PowerUpPaddleType.Normal);
                normalPaddle.setX(sp.getX());
                normalPaddle.setY(sp.getY());
                normalPaddle.setMovingLeft(sp.isMovingLeft());
                normalPaddle.setMovingRight(sp.isMovingRight());
                this.paddle = normalPaddle;
            }
        }
    }

    private void stopCurrentPaddleTimer() {
        if (paddle instanceof ExpandablePaddle) {
            ((ExpandablePaddle) paddle).stopTimer();
            ((ExpandablePaddle) paddle).stopBlinking();
        }
        else if (paddle instanceof LaserPaddle) {
            ((LaserPaddle) paddle).stopTimer();
            ((LaserPaddle) paddle).stopBlinking();
        }
        else if (paddle instanceof StickyPaddle) {
            StickyPaddle sp = (StickyPaddle) paddle;

            if (sp.isBallStuck()) {
                double[] velocity = sp.getLaunchVelocity();
                ball.setVelocityX(velocity[0]);
                ball.setVelocityY(velocity[1]);
                System.out.println("Auto-launched at " + sp.getArrowAngle() + "°");
            }

            sp.stopTimer();
            sp.stopBlinking();
        }
    }

    public void activateSpecialPaddle(PowerUpPaddleType type) {
        stopCurrentPaddleTimer();

        double currentX = paddle.getX();
        double currentY = paddle.getY();
        boolean isMovingLeft = paddle.isMovingLeft();
        boolean isMovingRight = paddle.isMovingRight();

        switch (type) {
            case ExpandablePaddle:
                ExpandablePaddle newExpandPaddle = new ExpandablePaddle(
                        currentX,
                        currentY,
                        isMovingLeft,
                        isMovingRight,
                        PowerUpPaddleType.ExpandablePaddle,
                        (normalPaddle) -> {
                            this.paddle = normalPaddle;
                            System.out.println("Back to normal paddle.");
                        }
                );
                this.paddle = newExpandPaddle;
                break;

            case LaserPaddle:
                LaserPaddle newLaserPaddle = new LaserPaddle(
                        currentX,
                        currentY,
                        isMovingLeft,
                        isMovingRight,
                        PowerUpPaddleType.LaserPaddle,
                        (normalPaddle) -> {
                            this.paddle = normalPaddle;
                            System.out.println("Back to normal paddle.");
                        }
                );
                this.paddle = newLaserPaddle;
                break;

            case StickyPaddle:
                StickyPaddle newStickyPaddle = new StickyPaddle(
                        currentX,
                        currentY,
                        isMovingLeft,
                        isMovingRight,
                        PowerUpPaddleType.StickyPaddle,
                        (normalPaddle) -> {
                            this.paddle = normalPaddle;
                            System.out.println("Back to normal paddle.");
                        }
                );
                this.paddle = newStickyPaddle;
                break;

            case Normal:
                this.paddle = new Paddle(PowerUpPaddleType.Normal);
                this.paddle.setX(currentX);
                this.paddle.setY(currentY);
                break;
        }
    }

    public ArrayList<Brick> getBricks() { return bricks; }
    public void setBricks(ArrayList<Brick> bricks) { this.bricks = bricks; }
    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getCheckpierce() { return checkpierce; }
    public GameState getGameState() { return gameState; }
    public WallCollisionSide getLastWallCollision() { return lastWallCollision; }
    public void setLastWallCollision(WallCollisionSide side) { this.lastWallCollision = side; }
    public static GameModel getInstance() { if (instance == null) instance = new GameModel(); return instance; }
    public ArrayList<Item> getItems() { return items; }

    public void onExplosion(double centerX, double centerY, double size) {
        effects.add(new ExplosionEffect(centerX, centerY, size));
    }
    public void setCurrentLevel(int level) { this.currentLevel = level; return; }
    public int getCurrentLevel() { return currentLevel; }
    public void spawnCoin(double x, double y) {
        items.add(new Item(x, y));
    }
    /*public void loadLevelFromManager() {
        // Load level hiện tại được chọn từ LevelManager
        // Sử dụng reflection để tránh circular dependency
        try {
            Class<?> levelManagerClass = Class.forName("com.arkanoid.model.LevelManager");
            Object levelManager = levelManagerClass.getMethod("getInstance").invoke(null);
            int selectedLevel = (Integer) levelManagerClass.getMethod("getCurrentLevel").invoke(levelManager);
            setCurrentLevel(selectedLevel);
        } catch (Exception e) {
            System.out.println("Error loading level from manager: " + e.getMessage());
            setCurrentLevel(1);
        }
    }

     */
}