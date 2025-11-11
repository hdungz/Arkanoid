package com.arkanoid.model;

import com.arkanoid.CONSTANT;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.ExpandablePaddle;
import com.arkanoid.model.paddle.Laser;
import com.arkanoid.model.paddle.LaserPaddle;
import com.arkanoid.model.paddle.StickyPaddle;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.utils.*;
import com.arkanoid.model.paddle.PowerUpPaddleType;
import com.arkanoid.view.Effect.ExplosionEffect;
import com.arkanoid.utils.LoadLevelManager;
import com.arkanoid.utils.LevelManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import static com.arkanoid.CONSTANT.*;
import com.arkanoid.utils.LevelTransitionManager;

public class GameModel {
    public enum WallCollisionSide { NONE, TOP, LEFT, RIGHT }

    public int checkpierce = 0;
    private double pierceTimer = 0;

    private static GameModel instance;

    private WallCollisionSide lastWallCollision = WallCollisionSide.NONE;

    ArrayList<Brick> bricks;

    private ArrayList<Ball> extraBalls = new ArrayList<>();

    private PowerUpManager powerUpManager;
    private final LevelManager levelmanager;
    private CoinManager coinManager;
    protected final LoadLevelManager loadLevelManager;
    protected double levelCompleteDelay = 0;
    protected boolean isBrickFalling = false;
    protected boolean needsViewSync = false;

    private int currentLevel = 0;
    private final ArrayList<ExplosionEffect> effects = new ArrayList<>();
    private LevelTransitionManager transitionManager ;

    Paddle paddle;
    Ball ball;
    int score;
    int lives;
    GameState gameState;
    private boolean isPaused = false;

    public GameModel() {
        instance = this;
        this.score = 0;
        paddle = new Paddle(PowerUpPaddleType.Normal);
        this.levelmanager = LevelManager.getInstance();
        this.powerUpManager = new PowerUpManager(this);
        this.coinManager = new CoinManager(this);
        this.loadLevelManager = LoadLevelManager.getInstance();
        ball = new Ball();
        lives = 3;
        bricks = new ArrayList<>();
        paddle.resetPosition();
        ball.resetPosition(paddle);
        gameState = GameState.Ready;
        transitionManager = new LevelTransitionManager(this);
        loadCurrentLevel();
    }

    public void loadCurrentLevel() {
        loadLevelManager.loadCurrentLevel(this);
    }

    public void update(double deltaTime) {
        if (isPaused) {
            return;
        }
        transitionManager.update(deltaTime);
        if (checkpierce == 1) {
            pierceTimer -= deltaTime;
            if (pierceTimer <= 0) {
                checkpierce = 0;
                pierceTimer = 0;
            }
        }

        if (isBrickFalling && loadLevelManager != null) {
            isBrickFalling = loadLevelManager.updateBrickFallAnimation(bricks, deltaTime);
        }

        if (transitionManager.isInTransition()) {
            if (transitionManager.canPlayerControl() && gameState == GameState.Ready) {
                paddle.move(deltaTime);
                ball.resetPosition(paddle);
            }
            return;
        }

        powerUpManager.update(deltaTime);
        coinManager.update(deltaTime);

        if (gameState != GameState.Running) {
            if (gameState == GameState.Ready) {
                paddle.move(deltaTime);
                ball.resetPosition(paddle);
            }
            if (gameState == GameState.Win) {
                levelCompleteDelay -= deltaTime;
                if (levelCompleteDelay <= 0) {
                    if (!transitionManager.isClearActive() && !transitionManager.isReadyForNextLevel()) {
                        transitionManager.startLevelClear();
                    }
                    if (transitionManager.isReadyForNextLevel()) {
                        loadLevelManager.loadNextLevel(this);
                        transitionManager.reset();
                        transitionManager.startLevelTransition(currentLevel);

                    }
                }
            }
            return;
        }

        updateBricks(deltaTime);
        updateEffects();

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

        updateExtraBalls(deltaTime);

        checkCollisions();
        checkExtraBallsCollisions();
        checkLaserCollisions();

        if (paddle instanceof LaserPaddle) {
            ((LaserPaddle) paddle).updateLasers(deltaTime);
        }

        if (levelmanager.WinLevels(bricks)) {
            gameState = GameState.Win;
            levelCompleteDelay = 0.1;
            this.ball.resetPosition(paddle);
            this.extraBalls.clear();
            this.effects.clear();
            this.getPowerUpManager().getPowerUps().clear();
            LevelManager.getInstance().completeLevel(LevelManager.getInstance().getCurrentLevel());
        }
    }

    public boolean checkAndConsumeViewSync() {
        if (needsViewSync) {
            needsViewSync = false;
            return true;
        }
        return false;
    }

    private void updateExtraBalls(double deltaTime) {
        for (Ball extraBall : extraBalls) {
            if (extraBall.isVisible()) {
                extraBall.move(deltaTime);
            }
        }
    }

    private void checkExtraBallsCollisions() {
        Iterator<Ball> iterator = extraBalls.iterator();

        while (iterator.hasNext()) {
            Ball extraBall = iterator.next();

            if (!extraBall.isVisible()) {
                iterator.remove();
                continue;
            }

            extraBall.checkWallCollision(CONSTANT.GAME_AREA_X, CONSTANT.GAME_AREA_END_X,
                    CONSTANT.BORDER_WIDTH, this);

            if (extraBall.getBoundary().intersects(paddle.getBoundary())) {
                paddle.onBallHit();
                extraBall.handlePaddleCollision(paddle);
            }

            if (extraBall.getY() > WINDOW_HEIGHT) {
                iterator.remove();
                System.out.println("Extra ball removed. Remaining: " + extraBalls.size());
                continue;
            }

            checkBallBrickCollision(extraBall);
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
            if (extraBalls.isEmpty()) {
                lives--;
                stopCurrentPaddleTimer();

                if (paddle instanceof StickyPaddle || paddle instanceof ExpandablePaddle || paddle instanceof LaserPaddle) {
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

                if (lives == 0) {
                    gameState = GameState.GameOver;
                } else {
                    ball.resetPosition(paddle);
                    gameState = GameState.Ready;
                }
            } else {
                ball.setVisible(false);
                ball.setY(WINDOW_HEIGHT + 100);
            }
            return;
        }

        checkBallBrickCollision(ball);
    }

    private void checkBallBrickCollision(Ball currentBall) {
        ListIterator<Brick> iterator = bricks.listIterator();

        while (iterator.hasNext()) {
            Brick brick = iterator.next();

            if (!brick.isVisible()) continue;

            if (brick.getBoundary().intersects(currentBall.getBoundary())) {
                brick.playHitSound();

                if (brick.getHealth() == 1) score += 10;
                else if (brick.getHealth() == 2) score += 20;

                brick.takeDamage();

                double ballPrevY = currentBall.getPrevY();
                double ballRadius = currentBall.getRadius();
                double brickTop = brick.getY();
                double brickBottom = brick.getY() + brick.getHeight();
                boolean isVerticalCollision;

                if (ballPrevY < brickTop - ballRadius) {
                    isVerticalCollision = true;
                } else if (ballPrevY > brickBottom + ballRadius) {
                    isVerticalCollision = true;
                } else {
                    isVerticalCollision = false;
                }

                if (checkpierce == 0) {
                    currentBall.handleBrickCollision(isVerticalCollision);

                    if (isVerticalCollision) {
                        currentBall.setY(currentBall.getVelocityY() < 0 ? brickTop - ballRadius : brickBottom + ballRadius);
                        break;
                    } else {
                        currentBall.setX(currentBall.getVelocityX() < 0 ? brick.getX() - ballRadius : brick.getX() + brick.getWidth() + ballRadius);
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

                    brick.takeDamage();

                    break;
                }
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

    public void launchBall() {
        if (transitionManager.isInTransition() && !transitionManager.canPlayerControl()) {
            return;
        }
        if (isBrickFalling) {
            return;
        }
        if (gameState == GameState.Ready) {
            gameState = GameState.Running;
            ball.launch();
            ball.setVisible(true);
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
        } else if (paddle instanceof LaserPaddle) {
            ((LaserPaddle) paddle).stopTimer();
            ((LaserPaddle) paddle).stopBlinking();
        } else if (paddle instanceof StickyPaddle) {
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

    public void setCheckpierce(int checkpierce) {
        this.checkpierce = checkpierce;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addBall(Ball newBall) {
        extraBalls.add(newBall);
        System.out.println("Ball added. Total extra balls: " + extraBalls.size());
    }

    public void removeBall(Ball ball) {
        extraBalls.remove(ball);
    }

    public ArrayList<Ball> getExtraBalls() {
        return extraBalls;
    }

    public int getTotalBallCount() {
        int count = ball.isVisible() ? 1 : 0;
        count += extraBalls.size();
        return count;
    }

    public void clearExtraBalls() {
        extraBalls.clear();
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void setCheckPierce(int value) {
        this.checkpierce = value;
    }

    public void setPierceTimer(double timer) {
        this.pierceTimer = timer;
    }

    public void onExplosion(double centerX, double centerY, double size) {
        effects.add(new ExplosionEffect(centerX, centerY, size));
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

    public Ball getBall() {
        return ball;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getCheckpierce() {
        return checkpierce;
    }

    public GameState getGameState() {
        return gameState;
    }

    public WallCollisionSide getLastWallCollision() {
        return lastWallCollision;
    }

    public void setLastWallCollision(WallCollisionSide side) {
        this.lastWallCollision = side;
    }

    public static GameModel getInstance() {
        if (instance == null) instance = new GameModel();
        return instance;
    }
    public LevelTransitionManager getTransitionManager() {
        return transitionManager;
    }

    public LevelManager getLevelmanager() {
        return levelmanager;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public PowerUpManager getPowerUpManager() {
        return powerUpManager;
    }

    public List<ExplosionEffect> getEffects() {
        return effects;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public double getLevelCompleteDelay() {
        return levelCompleteDelay;
    }

    public void setLevelCompleteDelay(double levelCompleteDelay) {
        this.levelCompleteDelay = levelCompleteDelay;
    }

    public boolean isBrickFalling() {
        return isBrickFalling;
    }

    public void setBrickFalling(boolean brickFalling) {
        isBrickFalling = brickFalling;
    }

    public boolean getNeedsViewSync() {
        return needsViewSync;
    }

    public void setNeedsViewSync(boolean needsViewSync) {
        this.needsViewSync = needsViewSync;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
}