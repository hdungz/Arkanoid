package com.arkanoid.controller;

import com.arkanoid.Coin.CoinStorage;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.utils.SpriteManager;
import com.arkanoid.utils.StoreSaveSystem;
import com.arkanoid.view.StoreView;

import java.util.function.Consumer;

public class StoreController implements BaseController {

    private final StoreView storeView;
    private final Object saveLock = new Object();

    private boolean[] paddleUnlocked;
    private int selectedPaddleIndex;
    private int currentPaddleViewIndex;

    private boolean[] ballUnlocked;
    private int selectedBallIndex;
    private int currentBallViewIndex;

    private boolean isDirty = false;

    private static final int TOTAL_PADDLE_SKINS = 3;
    private static final int TOTAL_BALL_SKINS = 2;

    public enum PaddleSkin {
        CLASSIC(0, "Classic", 0),
        GREEN_HELL(1, "GreenHell", 100),
        FOREST_ICE(2, "Forest Ice", 150);

        public final int index;
        public final String name;
        public final int price;

        PaddleSkin(int index, String name, int price) {
            this.index = index;
            this.name = name;
            this.price = price;
        }

        public static PaddleSkin fromIndex(int index) {
            for (PaddleSkin skin : values()) {
                if (skin.index == index) return skin;
            }
            return CLASSIC;
        }
    }

    public enum BallSkin {
        CLASSIC(0, "Classic", 0),
        BASKETBALL(1, "BasketBall", 80);

        public final int index;
        public final String name;
        public final int price;

        BallSkin(int index, String name, int price) {
            this.index = index;
            this.name = name;
            this.price = price;
        }

        public static BallSkin fromIndex(int index) {
            for (BallSkin skin : values()) {
                if (skin.index == index) return skin;
            }
            return CLASSIC;
        }
    }

    private GameController gameController;

    public StoreController(StoreView storeView) {
        if (storeView == null) {
            throw new IllegalArgumentException("StoreView cannot be null");
        }

        this.storeView = storeView;
        loadSavedData();
        initializeButtons();
        updateDisplay();
    }

    private void loadSavedData() {
        StoreSaveSystem.StoreData data = StoreSaveSystem.loadStoreData();
        CoinStorage.load();

        if (data.unlockedPaddles.length != TOTAL_PADDLE_SKINS) {
            System.err.println("Invalid paddle data, reinitializing");
            data = StoreSaveSystem.getDefaultData();
        }
        if (data.unlockedBalls.length != TOTAL_BALL_SKINS) {
            System.err.println("Invalid ball data, reinitializing");
            data = StoreSaveSystem.getDefaultData();
        }

        this.selectedPaddleIndex = data.selectedPaddle;
        this.currentPaddleViewIndex = data.selectedPaddle;
        this.paddleUnlocked = data.unlockedPaddles.clone();

        this.selectedBallIndex = data.selectedBall;
        this.currentBallViewIndex = data.selectedBall;
        this.ballUnlocked = data.unlockedBalls.clone();

        SpriteManager.initialize(selectedPaddleIndex, selectedBallIndex);

        System.out.println("Loaded store data: " + data);
    }

    private void saveData() {
        synchronized(saveLock) {
            try {
                StoreSaveSystem.saveStoreData(
                        selectedPaddleIndex,
                        selectedBallIndex,
                        paddleUnlocked,
                        ballUnlocked
                );
                CoinStorage.save();
                isDirty = false;
            } catch (Exception e) {
                System.err.println("Failed to save data: " + e.getMessage());

            }
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void initializeButtons() {
        storeView.setOnBackButtonClicked(() -> {
            if (isDirty) {
                saveData();
            }
            SceneManager.getInstance().switchTo(SceneType.Menu);
        });

        storeView.setOnLeftArrowPaddleClicked(this::navigateToPreviousPaddle);
        storeView.setOnRightArrowPaddleClicked(this::navigateToNextPaddle);
        storeView.setOnPurchasePaddleClicked(this::handlePaddlePurchaseOrSelect);

        storeView.setOnLeftArrowBallClicked(this::navigateToPreviousBall);
        storeView.setOnRightArrowBallClicked(this::navigateToNextBall);
        storeView.setOnPurchaseBallClicked(this::handleBallPurchaseOrSelect);
    }

    private void navigateToPreviousPaddle() {
        currentPaddleViewIndex = (currentPaddleViewIndex - 1 + TOTAL_PADDLE_SKINS) % TOTAL_PADDLE_SKINS;
        updatePaddleDisplay();
    }

    private void navigateToNextPaddle() {
        currentPaddleViewIndex = (currentPaddleViewIndex + 1) % TOTAL_PADDLE_SKINS;
        updatePaddleDisplay();
    }

    private void handlePaddlePurchaseOrSelect(int ignored) {
        int skinIndex = currentPaddleViewIndex;

        if (!isValidPaddleIndex(skinIndex)) {
            return;
        }

        if (skinIndex == selectedPaddleIndex) {
            return;
        }

        if (paddleUnlocked[skinIndex]) {
            selectPaddle(skinIndex);
        } else {
            purchasePaddle(skinIndex);
        }
    }

    private void selectPaddle(int skinIndex) {
        if (!isValidPaddleIndex(skinIndex) || !paddleUnlocked[skinIndex]) {
            return;
        }

        selectedPaddleIndex = skinIndex;
        SpriteManager.setPaddleByIndex(skinIndex);
        updatePaddleDisplay();
        isDirty = true;
    }

    private void purchasePaddle(int skinIndex) {
        if (!isValidPaddleIndex(skinIndex) || paddleUnlocked[skinIndex]) {
            return;
        }

        PaddleSkin skin = PaddleSkin.fromIndex(skinIndex);
        int price = skin.price;

        if (!CoinStorage.hasEnoughCoins(price)) {
            storeView.playNotEnoughCoinsAnimation();
            return;
        }

        boolean[] oldUnlocked = paddleUnlocked.clone();
        int oldSelected = selectedPaddleIndex;

        try {
            CoinStorage.spendCoins(price);
            paddleUnlocked[skinIndex] = true;
            selectedPaddleIndex = skinIndex;

            isDirty = true;
            saveData();

            SpriteManager.setPaddleByIndex(skinIndex);

            storeView.playPurchaseSuccessAnimation(() -> {
                storeView.updateCoins(CoinStorage.getTotalCoins());
                updateDisplay();
            });
        } catch (Exception e) {
            paddleUnlocked = oldUnlocked;
            selectedPaddleIndex = oldSelected;
            CoinStorage.addCoins(price);

            System.err.println("Purchase failed: " + e.getMessage());
        }
    }

    private void updatePaddleDisplay() {
        if (!isValidPaddleIndex(currentPaddleViewIndex)) {
            return;
        }

        PaddleSkin skin = PaddleSkin.fromIndex(currentPaddleViewIndex);
        boolean isUnlocked = paddleUnlocked[currentPaddleViewIndex];
        boolean isSelected = (currentPaddleViewIndex == selectedPaddleIndex);

        storeView.displayPaddle(currentPaddleViewIndex, skin.name, skin.price, isUnlocked, isSelected);
    }

    private void navigateToPreviousBall() {
        currentBallViewIndex = (currentBallViewIndex - 1 + TOTAL_BALL_SKINS) % TOTAL_BALL_SKINS;
        updateBallDisplay();
    }

    private void navigateToNextBall() {
        currentBallViewIndex = (currentBallViewIndex + 1) % TOTAL_BALL_SKINS;
        updateBallDisplay();
    }

    private void handleBallPurchaseOrSelect(int ignored) {
        int skinIndex = currentBallViewIndex;

        if (!isValidBallIndex(skinIndex)) {
            return;
        }

        if (skinIndex == selectedBallIndex) {
            return;
        }

        if (ballUnlocked[skinIndex]) {
            selectBall(skinIndex);
        } else {
            purchaseBall(skinIndex);
        }
    }

    private void selectBall(int skinIndex) {
        if (!isValidBallIndex(skinIndex) || !ballUnlocked[skinIndex]) {
            return;
        }

        selectedBallIndex = skinIndex;
        SpriteManager.setBallByIndex(skinIndex);
        updateBallDisplay();
        isDirty = true;
    }

    private void purchaseBall(int skinIndex) {
        if (!isValidBallIndex(skinIndex) || ballUnlocked[skinIndex]) {
            return;
        }

        BallSkin skin = BallSkin.fromIndex(skinIndex);
        int price = skin.price;

        if (!CoinStorage.hasEnoughCoins(price)) {
            storeView.playNotEnoughCoinsAnimation();
            return;
        }

        boolean[] oldUnlocked = ballUnlocked.clone();
        int oldSelected = selectedBallIndex;

        try {
            CoinStorage.spendCoins(price);
            ballUnlocked[skinIndex] = true;
            selectedBallIndex = skinIndex;

            isDirty = true;
            saveData();

            SpriteManager.setBallByIndex(skinIndex);

            storeView.playPurchaseSuccessAnimation(() -> {
                storeView.updateCoins(CoinStorage.getTotalCoins());
                updateDisplay();
            });
        } catch (Exception e) {
            ballUnlocked = oldUnlocked;
            selectedBallIndex = oldSelected;
            CoinStorage.addCoins(price);
            System.err.println("Purchase failed: " + e.getMessage());
        }
    }

    private void updateBallDisplay() {
        if (!isValidBallIndex(currentBallViewIndex)) {
            return;
        }

        BallSkin skin = BallSkin.fromIndex(currentBallViewIndex);
        boolean isUnlocked = ballUnlocked[currentBallViewIndex];
        boolean isSelected = (currentBallViewIndex == selectedBallIndex);

        storeView.displayBall(currentBallViewIndex, skin.name, skin.price, isUnlocked, isSelected);
    }

    private void updateDisplay() {
        updatePaddleDisplay();
        updateBallDisplay();
        storeView.updateCoins(CoinStorage.getTotalCoins());
    }

    public void addCoins(int amount) {
        if (amount < 0) {
            return;
        }

        CoinStorage.addCoins(amount);
        storeView.updateCoins(CoinStorage.getTotalCoins());
        isDirty = true;
    }

    private boolean isValidPaddleIndex(int index) {
        return index >= 0 && index < TOTAL_PADDLE_SKINS;
    }

    private boolean isValidBallIndex(int index) {
        return index >= 0 && index < TOTAL_BALL_SKINS;
    }

    @Override
    public void onEnterScene() {
        currentPaddleViewIndex = selectedPaddleIndex;
        currentBallViewIndex = selectedBallIndex;
        SpriteManager.initialize(selectedPaddleIndex, selectedBallIndex);
        updateDisplay();
    }

    @Override
    public void onExitScene() {
        if (isDirty) {
            saveData();
        }
    }

    public int getSelectedPaddle() {
        return selectedPaddleIndex;
    }

    public int getSelectedBall() {
        return selectedBallIndex;
    }

    public void forceSave() {
        saveData();
        System.out.println("Force saved store data");
    }

    public void resetToDefaults() {
        StoreSaveSystem.deleteSaveData();
        loadSavedData();
        updateDisplay();
        System.out.println("Store data reset to defaults");
    }
}