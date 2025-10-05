package com.arkanoid.view;

import com.arkanoid.model.GameModel;

public class GameView {
    private final GameModel gameModel;

    private final BallRenderer ballRenderer;

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
    }
}
