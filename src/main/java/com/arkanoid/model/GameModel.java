package com.arkanoid.model;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.Brick;
import com.arkanoid.model.brick.BrickType;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.main;

import java.util.ArrayList;
import java.util.ListIterator;

public class GameModel {
    ArrayList<Brick> bricks;
    Paddle paddle;
    Ball ball;
    int score;
    int lives;
    GameState gameState;

    public GameModel() {
        this.score = 0;
        paddle = new Paddle();
        ball = new Ball();
        score = 0;
        lives = 3;
        bricks = new ArrayList<>();
        paddle.resetPosition();
        ball.resetPosition(paddle);
        gameState = GameState.Ready;
        loadLevel(1);
    }

    private void loadLevel(int level) {
        bricks.clear();
        int rows = 5;
        int cols = 10;
        double brickWidth = (main.WINDOW_WIDTH - (cols + 1) * 5.0) / cols;
        double brickHeight = 20;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * (brickWidth + 5) + 5;
                double y = i * (brickHeight + 5) + 50;
                bricks.add(new Brick(x, y, brickWidth, brickHeight, BrickType.NORMAL, 3));
            }
        }

    }

    public void update() {
        if(gameState != GameState.Running) {
            if (gameState == GameState.Ready) {
                paddle.move();
                ball.resetPosition(paddle);
            }
            return;
        }
        paddle.move();
        ball.move();
        checkCollisions();
    }

    void checkCollisions() {
        // Va chạm với tường
        ball.checkWallCollision(0, main.WINDOW_WIDTH, 0);
        // Va chạm với paddle
        if (ball.getBoundary().intersects(paddle.getBoundary())) {
            ball.handlePaddleCollision(paddle);
        }
        // Bóng rơi ra ngoài
        if (ball.getY() > main.WINDOW_HEIGHT) {
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
                brick.takeDamage();
                double ballPrevY = ball.getPrevY();
                double ballRadius = ball.getRadius();
                double brickTop = brick.getY();
                double brickBottom = brick.getY() + brick.getHeight();
                boolean isVerticalCollision;
                // Kiểm tra trước khi va chạm bóng có đang hoàn toàn ở trên brick hay ở
                // dưới brick không, từ đó suy ra được là va chạm ngang hay va chạm dọc
                if (ballPrevY < brickTop + ballRadius) {
                    isVerticalCollision = true;
                } else if (ballPrevY > brickBottom - ballRadius) {
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

    public void launchBall() {
        if (gameState == GameState.Ready) {
            gameState = GameState.Running;
            ball.launch();
        }
    }

    //Getters and Setters
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
}
