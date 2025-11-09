package com.arkanoid.utils;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.state.*;

import java.util.Random;


public class LevelTransitionManager {
    private final GameModel gameModel;
    private LevelTransitionState currentState;
    private boolean transitionComplete = false;
    private boolean readyForNextLevel = false;

    private boolean introActive = false;
    private double introAlpha = 0;
    private int currentLevel = 1;

    private boolean clearActive = false;
    private double clearAlpha = 0;

    private double glitchTime = 0;
    private double glitchIntensity = 0;
    private final Random random = new Random();

    public LevelTransitionManager(GameModel gameModel) {
        this.gameModel = gameModel;
    }


    public void startLevelTransition(int level) {
        this.currentLevel = level;
        this.transitionComplete = false;
        this.readyForNextLevel = false;
        setState(new LevelIntroState(gameModel, this));
    }


    public void startLevelClear() {
        this.transitionComplete = false;
        this.readyForNextLevel = false;
        setState(new LevelClearState(gameModel, this));
    }

    public void update(double deltaTime) {
        if (currentState == null) return;

        currentState.update(deltaTime);

        if (currentState.isComplete()) {
            transitionToNextState();
        }
    }

    private void transitionToNextState() {
        String currentStateName = currentState.getStateName();
        currentState.exit();

        switch (currentStateName) {
            case "LEVEL_INTRO":
                setState(new PreBrickFallState(gameModel, this));
                break;

            case "PRE_BRICK_FALL":
                setState(new BrickFallingState(gameModel, this));
                break;

            case "BRICK_FALLING":
                setState(new ReadyToPlayState(gameModel, this));
                break;

            case "LEVEL_CLEAR":
                currentState = null;
                break;

            default:
                currentState = null;
                break;
        }
    }


    private void setState(LevelTransitionState newState) {
        this.currentState = newState;
        if (newState != null) {
            newState.enter();
        }
    }


    public void updateGlitch(double deltaTime) {
        glitchTime += deltaTime;

        if (random.nextDouble() < 0.15) {
            glitchIntensity = random.nextDouble() * 0.6;
        } else {
            glitchIntensity *= 0.85;
        }
    }


    public void reset() {
        currentState = null;
        transitionComplete = false;
        readyForNextLevel = false;
        introActive = false;
        clearActive = false;
        introAlpha = 0;
        clearAlpha = 0;
    }

    public boolean isIntroActive() { return introActive; }
    public void setIntroActive(boolean active) { this.introActive = active; }

    public double getIntroAlpha() { return introAlpha; }
    public void setIntroAlpha(double alpha) { this.introAlpha = Math.max(0, Math.min(1, alpha)); }

    public boolean isClearActive() { return clearActive; }
    public void setClearActive(boolean active) { this.clearActive = active; }

    public double getClearAlpha() { return clearAlpha; }
    public void setClearAlpha(double alpha) { this.clearAlpha = Math.max(0, Math.min(1, alpha)); }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int level) { this.currentLevel = level; }

    public double getGlitchIntensity() { return glitchIntensity; }

    public boolean isTransitionComplete() { return transitionComplete; }
    public void setTransitionComplete(boolean complete) { this.transitionComplete = complete; }

    public boolean isReadyForNextLevel() { return readyForNextLevel; }
    public void setReadyForNextLevel(boolean ready) { this.readyForNextLevel = ready; }

    public boolean isInTransition() {
        return currentState != null && !transitionComplete;
    }

    public boolean canPlayerControl() {
        return currentState != null &&
                currentState.getStateName().equals("READY_TO_PLAY");
    }

    public String getCurrentStateName() {
        return currentState != null ? currentState.getStateName() : "NONE";
    }
}