package com.arkanoid.model.state;
//Design pattern State
public interface LevelTransitionState {
    void enter();
    void update(double deltaTime);
    void exit();
    boolean isComplete();
    String getStateName();
}