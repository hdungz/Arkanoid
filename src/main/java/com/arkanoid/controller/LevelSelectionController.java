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
        // Play button - chuyển đến gameplay với level đã chọn
        levelSelectionView.getPlayButton().setOnAction(e -> {
            if (levelSelectionView.getSelectedLevel() != null) {
                int selectedLevelId = levelSelectionView.getSelectedLevel().getId();
                System.out.println("Playing level: " + selectedLevelId);
                
                // Load level được chọn vào GameModel
                levelManager.selectLevel(selectedLevelId);
                SceneManager.getInstance().switchTo(SceneType.GamePlay);
            }
        });

        // Back button - quay về menu chính
        levelSelectionView.getBackButton().setOnAction(e -> {
            System.out.println("Back to main menu");
            SceneManager.getInstance().switchTo(SceneType.Menu);
        });

        // Leaderboard button - hiển thị bảng xếp hạng
        levelSelectionView.getLeaderboardButton().setOnAction(e -> {
            System.out.println("Show leaderboard for level: " + 
                (levelSelectionView.getSelectedLevel() != null ? 
                 levelSelectionView.getSelectedLevel().getId() : "none"));
            // TODO: Implement leaderboard functionality
        });
    }

    @Override
    public void onEnterScene() {
        System.out.println("Enter level selection");
        // Có thể thêm logic khởi tạo khi vào màn hình
    }

    @Override
    public void onExitScene() {
        System.out.println("Exit level selection");
        // Có thể thêm logic cleanup khi rời màn hình
    }

    // Method để lấy level ID hiện tại được chọn
    public int getSelectedLevelId() {
        if (levelSelectionView.getSelectedLevel() != null) {
            return levelSelectionView.getSelectedLevel().getId();
        }
        return 1; // Default level
    }
}
