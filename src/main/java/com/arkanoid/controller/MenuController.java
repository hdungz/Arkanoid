package com.arkanoid.controller;

import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.view.MenuView;
import javafx.application.Platform;

public class MenuController implements BaseController {
    private final MenuView menuView;

    public MenuController(MenuView menuView) {
        this.menuView = menuView;
        initializeButtons();
    }

    private void initializeButtons() {
        menuView.getPlayButton().setOnAction(e -> {
            System.out.println("Play button clicked");
            SceneManager.getInstance().switchTo(SceneType.GamePlay);
        });

        menuView.getStoreButton().setOnAction(e -> {
            System.out.println("Store button clicked");
        });

        menuView.getHighscoreButton().setOnAction(e -> {
            System.out.println("Highscore button clicked");
        });

        menuView.getExitButton().setOnAction(e -> {
            System.out.println("Exit button clicked");
            Platform.exit();
        });
    }

    @Override
    public void onEnterScene() {
        System.out.println("Enter menu");
    }

    @Override
    public void onExitScene() {
        System.out.println("Exit menu");
    }
}
