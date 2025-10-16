package com.arkanoid.model;
import com.arkanoid.CONSTANT;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.Paddle;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import java.io.*;
import static com.arkanoid.CONSTANT.*;

public class GameModel {
    public enum WallCollisionSide {
        NONE, TOP, LEFT, RIGHT
    }
    private static GameModel instance;
    private WallCollisionSide lastWallCollision = WallCollisionSide.NONE;
    ArrayList<Brick> bricks;
    private final ArrayList<com.arkanoid.effects.ExplosionEffect> effects = new ArrayList<>();
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
        score = 0;
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
            String line1=br.readLine();
            String line2=br.readLine();
            if (line1 == null||line2==null) {
                System.out.println("File rỗng hoặc sai định dạng");
                return;
            }
                int rows = (Integer.parseInt(line1));
                int cols = (Integer.parseInt(line1));
                double brickWidth = (CONSTANT.GAME_AREA_WIDTH - (cols + 1) * 5.0) / cols;
                double brickHeight = 20;

                for (int i = 0; i < rows; i++) {
                    String line = br.readLine();
                    if (line == null) break;
                    for (int j = 0; j < cols; j++) {
                        double x = CONSTANT.GAME_AREA_X + j * (brickWidth + 5) + 5;
                        double y = CONSTANT.BORDER_WIDTH + i * (brickHeight + 5) + 5;
                        //bricks.add(new ExplodingBrick(x, y, brickWidth, brickHeight, BrickType.EXPLODING));
                        if (line.charAt(j) == '1') {
                            bricks.add(new DropBrick(x, y, brickWidth, brickHeight, BrickType.DROPPER));
                        } else if (line.charAt(j) == '2') {
                            bricks.add(new SuperDurableBrick(x, y, brickWidth, brickHeight, BrickType.SUPERDURABLE));
                        } else if (line.charAt(j) == '3') {
                            bricks.add(new ExplodingBrick(x, y, brickWidth, brickHeight, BrickType.EXPLODING));
                        }else if (line.charAt(j) == '4'){
                            bricks.add(new DurableBrick(x, y, brickWidth, brickHeight, BrickType.DURABLE));
                        } else if (line.charAt(j) == '5') {
                            bricks.add(new MoveBrick(x, y, brickWidth, brickHeight, BrickType.MOVING));
                        }else if  (line.charAt(j) == '6') {
                            bricks.add(new Brick(x,y,brickWidth,brickHeight,BrickType.NORMAL,1));

                        }else{
                            continue;
                        }
                    }
                }
                br.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(double deltaTime) {
        if(gameState != GameState.Running) {
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
    }

    void checkCollisions() {
        // Va chạm với tường
        this.lastWallCollision = WallCollisionSide.NONE;
        ball.checkWallCollision(CONSTANT.GAME_AREA_X, CONSTANT.GAME_AREA_END_X, CONSTANT.BORDER_WIDTH, this);
        // Va chạm với paddle
        if (ball.getBoundary().intersects(paddle.getBoundary())) {
            ball.handlePaddleCollision(paddle);
        }
        // Bóng rơi ra ngoài
        if (ball.getY() > WINDOW_HEIGHT) {
            lives--;
            if (lives == 0) {
                gameState = GameState.GameOver;
            } else {
                ball.resetPosition(paddle);
                gameState = GameState.Ready;
            }
        }
        // Va chạm với brick
        ListIterator<Brick> iterator = bricks.listIterator();
        while (iterator.hasNext()) {

            Brick brick = iterator.next();
            if (!brick.isVisible()) continue;
            if (brick.getBoundary().intersects(ball.getBoundary())) {
                System.out.println("collision with brick");
                if(brick.getHealth() == 1) score += 10;
                if(brick.getHealth() == 2) score += 20;
                if(brick.getHealth() == 3) score += 20;
                brick.takeDamage();
                double ballPrevY = ball.getPrevY();
                double ballRadius = ball.getRadius();
                double brickTop = brick.getY();
                double brickBottom = brick.getY() + brick.getHeight();
                boolean isVerticalCollision;
                // Kiểm tra trước khi va chạm bóng có đang hoàn toàn ở trên brick hay ở
                // dưới brick không, từ đó suy ra được là va chạm ngang hay va chạm dọc
                if (ballPrevY < brickTop - ballRadius) {
                    isVerticalCollision = true;
                } else if (ballPrevY > brickBottom + ballRadius) {
                    isVerticalCollision = true;
                } else {
                    isVerticalCollision = false;
                }
                ball.handleBrickCollision(isVerticalCollision);
                // Đẩy bóng ra khỏi gạch để tránh bị kẹt
                if (isVerticalCollision) {
                    ball.setY(ball.getVelocityY() < 0 ? brickTop - ballRadius : brickBottom + ballRadius );
                    System.out.println("vertical collision " + brickTop + " " + brickBottom + " " + ballRadius + " " + ball.getVelocityY());
                    break;
                } else {
                    ball.setX(ball.getVelocityX() < 0 ? brick.getX() - ballRadius  : brick.getX() + brick.getWidth() + ballRadius);
                    break;
                }
                // Chỉ xử lý va chạm với 1 gạch mỗi frame
            }
        }
    }
    private void updateBricks(double deltaTime) {
        for (Brick brick : bricks) {
            if (brick instanceof MoveBrick) {
                ((MoveBrick) brick).update(deltaTime);
            }
        }
    }
    private void updateEffects() {
        effects.removeIf(effect -> {
            effect.update();
            return effect.isFinished();
        });
    }

    public void onExplosion(double centerX, double centerY) {
        effects.add(new com.arkanoid.effects.ExplosionEffect(centerX, centerY,50));
    }

    public java.util.List<com.arkanoid.effects.ExplosionEffect> getEffects() {
        return effects;
    }

    public void launchBall() {
        if (gameState == GameState.Ready) {
            gameState = GameState.Running;
            ball.launch();
        }
    }
    public void spawnCoin(double x, double y) {
    }
    public ArrayList<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public WallCollisionSide getLastWallCollision() {
        return lastWallCollision;
    }
    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }
    public void setLastWallCollision(WallCollisionSide lastWallCollision) {
        this.lastWallCollision = lastWallCollision;
    }
    public void onExplosion(double centerX, double centerY, double size) {
        effects.add(new com.arkanoid.effects.ExplosionEffect(centerX, centerY, size));
    }
}
