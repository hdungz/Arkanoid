package com.arkanoid.model;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.Paddle;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import static com.arkanoid.CONSTANT.*;
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


    private final ArrayList<ExplosionEffect> effects = new ArrayList<>();
    Paddle paddle;
    Ball ball;
    int score;
    int lives;
    GameState gameState;

    public GameModel() {
        instance = this;
        this.score = 0;
        paddle = new Paddle();
        ball = new Ball();
        lives = 3;
        bricks = new ArrayList<>();
        paddle.resetPosition();
        ball.resetPosition(paddle);
        gameState = GameState.Ready;
        loadLevel(20);
    }

    private void loadLevel(int level) {
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
        paddle.move(deltaTime);
        ball.move(deltaTime);
        updateBricks(deltaTime);
        checkCollisions();
        updateEffects();
        capNhatItem(deltaTime);
    }

    void checkCollisions() {
        this.lastWallCollision = WallCollisionSide.NONE;
        ball.checkWallCollision(CONSTANT.GAME_AREA_X, CONSTANT.GAME_AREA_END_X, CONSTANT.BORDER_WIDTH, this);

        if (ball.getBoundary().intersects(paddle.getBoundary())) {
            paddle.onBallHit();
            ball.handlePaddleCollision(paddle);
        }

        if (ball.getY() > WINDOW_HEIGHT) {
            lives--;
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
                checkpierce = 1;
                pierceTimer = 2.5;
                System.out.println(checkpierce);

                vatPham.setHienThi(false);
            }
        }
    }

    // 🟢 Vẽ tất cả Item (quà)
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
    }

    public ArrayList<Brick> getBricks() { return bricks; }
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


    // Sinh vật phẩm (coin / hộp quà)
    public void spawnCoin(double x, double y) {
        items.add(new Item(x, y));
    }
}
