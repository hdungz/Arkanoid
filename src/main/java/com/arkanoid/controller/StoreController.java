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

    private boolean[] paddleUnlocked;
    private int selectedPaddleIndex;
    private int currentPaddleViewIndex;

    private boolean[] ballUnlocked;
    private int selectedBallIndex;
    private int currentBallViewIndex;

    private static final int TOTAL_PADDLE_SKINS = 3;
    private static final int TOTAL_BALL_SKINS = 2;
    private static final int MIN_COINS = 0;
    private static final int MAX_COINS = 999999;

    private final int[] paddlePrices = {0, 100, 150};
    private final int[] ballPrices = {0, 80};

    private final String[] paddleNames = {
            "Classic",
            "GreenHell",
            "Forest Ice"
    };

    private final String[] ballNames = {
            "Classic",
            "BasketBall"
    };

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
        StoreSaveSystem.saveStoreData(
                selectedPaddleIndex,
                selectedBallIndex,
                paddleUnlocked,
                ballUnlocked
        );


        CoinStorage.save();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void initializeButtons() {
        storeView.setOnBackButtonClicked(() -> {
            // Save data when leaving store
            saveData();
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
        int newIndex = (currentPaddleViewIndex - 1 + TOTAL_PADDLE_SKINS) % TOTAL_PADDLE_SKINS;
        currentPaddleViewIndex = newIndex;
        updatePaddleDisplay();
    }

    private void navigateToNextPaddle() {
        int newIndex = (currentPaddleViewIndex + 1) % TOTAL_PADDLE_SKINS;
        currentPaddleViewIndex = newIndex;
        updatePaddleDisplay();
    }

    private void handlePaddlePurchaseOrSelect(int skinIndex) {
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

        // Auto-save after selection
        saveData();
    }

    private void purchasePaddle(int skinIndex) {
        if (!isValidPaddleIndex(skinIndex)) {
            return;
        }

        if (paddleUnlocked[skinIndex]) {
            return;
        }

        int price = paddlePrices[skinIndex];

        if (CoinStorage.hasEnoughCoins(price)) {
            CoinStorage.spendCoins(price);
            paddleUnlocked[skinIndex] = true;
            selectedPaddleIndex = skinIndex;

            SpriteManager.setPaddleByIndex(skinIndex);

            storeView.playPurchaseSuccessAnimation(() -> {
                storeView.updateCoins(CoinStorage.getTotalCoins());
                updateDisplay();

                // Auto-save after purchase
                saveData();
            });
        } else {
            storeView.playNotEnoughCoinsAnimation();
        }
    }

    private void updatePaddleDisplay() {
        if (!isValidPaddleIndex(currentPaddleViewIndex)) {
            return;
        }

        String name = paddleNames[currentPaddleViewIndex];
        int price = paddlePrices[currentPaddleViewIndex];
        boolean isUnlocked = paddleUnlocked[currentPaddleViewIndex];
        boolean isSelected = (currentPaddleViewIndex == selectedPaddleIndex);

        storeView.displayPaddle(currentPaddleViewIndex, name, price, isUnlocked, isSelected);
    }

    private void navigateToPreviousBall() {
        int newIndex = (currentBallViewIndex - 1 + TOTAL_BALL_SKINS) % TOTAL_BALL_SKINS;
        currentBallViewIndex = newIndex;
        updateBallDisplay();
    }

    private void navigateToNextBall() {
        int newIndex = (currentBallViewIndex + 1) % TOTAL_BALL_SKINS;
        currentBallViewIndex = newIndex;
        updateBallDisplay();
    }

    private void handleBallPurchaseOrSelect(int skinIndex) {
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

        // Auto-save after selection
        saveData();
    }

    private void purchaseBall(int skinIndex) {
        if (!isValidBallIndex(skinIndex)) {
            return;
        }

        if (ballUnlocked[skinIndex]) {
            return;
        }

        int price = ballPrices[skinIndex];

        if (CoinStorage.hasEnoughCoins(price)) {
            CoinStorage.spendCoins(price);
            ballUnlocked[skinIndex] = true;
            selectedBallIndex = skinIndex;

            SpriteManager.setBallByIndex(skinIndex);

            storeView.playPurchaseSuccessAnimation(() -> {
                storeView.updateCoins(CoinStorage.getTotalCoins());
                updateDisplay();

                // Auto-save after purchase
                saveData();
            });
        } else {
            storeView.playNotEnoughCoinsAnimation();
        }
    }

    private void updateBallDisplay() {
        if (!isValidBallIndex(currentBallViewIndex)) {
            return;
        }

        String name = ballNames[currentBallViewIndex];
        int price = ballPrices[currentBallViewIndex];
        boolean isUnlocked = ballUnlocked[currentBallViewIndex];
        boolean isSelected = (currentBallViewIndex == selectedBallIndex);

        storeView.displayBall(currentBallViewIndex, name, price, isUnlocked, isSelected);
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


        saveData();
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

        saveData();
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