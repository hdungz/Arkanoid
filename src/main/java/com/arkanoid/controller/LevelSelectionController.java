package com.arkanoid.controller;

import com.arkanoid.model.LevelManager;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.LevelSelectionView;
import javafx.application.Platform;

public class LevelSelectionController implements BaseController {
    private final LevelSelectionView levelSelectionView;
    private final LevelManager levelManager;

    public LevelSelectionController(LevelSelectionView levelSelectionView) {
        this.levelSelectionView = levelSelectionView;
        this.levelManager = LevelManager.getInstance();
        initializeButtons();
    }

    private void initializeButtons() {
        levelSelectionView.getPlayButton().setOnAction(e -> {
            if (levelSelectionView.getSelectedLevel() != null) {
                int selectedLevelId = levelSelectionView.getSelectedLevel().getId();
                System.out.println("Playing level: " + selectedLevelId);

                levelManager.selectLevel(selectedLevelId);
                SceneManager.getInstance().switchTo(SceneType.GamePlay);
            }
        });

        levelSelectionView.getBackButton().setOnAction(e -> {
            System.out.println("Back to main menu");
            SceneManager.getInstance().switchTo(SceneType.Menu);
        });
    }

    @Override
    public void onEnterScene() {
        System.out.println("Enter level selection");
        levelSelectionView.refreshView();
    }

    @Override
    public void onExitScene() {
        System.out.println("Exit level selection");

    }


    public int getSelectedLevelId() {
        if (levelSelectionView.getSelectedLevel() != null) {
            return levelSelectionView.getSelectedLevel().getId();
        }
        return 1;
    }
}
