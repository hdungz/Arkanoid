package com.arkanoid;


public final class CONSTANT {
    public static final int WINDOW_HEIGHT = 720;
    public static final int WINDOW_WIDTH = 1280;

    //---Ball---
    public static final double BALL_RADIUS = 12;
    public static final double INITIAL_SPEED = 3;
    public static final double BRICK_DAMAGE = 1;
    public static final double HITSPOT_MULTIPLIER = 1;

    //---Paddle---
    public static final double PADDLE_WIDTH = 90;
    public static final double PADDLE_HEIGHT = 30;
    public static final double PADDLE_SPEED = 3;

    //---Bricks---

    public static final int GAME_AREA_WIDTH = 540;
    public static final int GAME_AREA_HEIGHT = 720;
    public static final int GAME_AREA_X = (WINDOW_WIDTH - GAME_AREA_WIDTH) / 2;
    public static final int GAME_AREA_END_X = GAME_AREA_X + GAME_AREA_WIDTH;
    public static final int BORDER_WIDTH = 5
            ;

    public static final int TARGET_FPS = 144;
    CONSTANT() {

    }
}
