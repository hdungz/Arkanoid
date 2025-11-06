package com.arkanoid.controller;

import com.arkanoid.Coin.CoinStorage;
import com.arkanoid.model.GameModel;
import com.arkanoid.utils.SceneManager;
import com.arkanoid.utils.SceneType;
import com.arkanoid.utils.SpriteManager;
import com.arkanoid.view.StoreView;
import com.arkanoid.view.paddle.LaserPaddleRenderer;
import com.arkanoid.view.paddle.NormalPaddleRenderer;

import com.arkanoid.view.paddle.StickyPaddleRenderer;
import javafx.scene.paint.Color;

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

    private final Color[] paddleColors = {
            Color.DODGERBLUE,
            Color.ORANGERED,
            Color.FORESTGREEN
    };

    private final Color[] ballColors = {
            Color.WHITE,
            Color.RED
    };

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

        CoinStorage.load();

        this.selectedPaddleIndex = 0;
        this.currentPaddleViewIndex = 0;
        this.paddleUnlocked = new boolean[TOTAL_PADDLE_SKINS];
        this.paddleUnlocked[0] = true;

        this.selectedBallIndex = 0;
        this.currentBallViewIndex = 0;
        this.ballUnlocked = new boolean[TOTAL_BALL_SKINS];
        this.ballUnlocked[0] = true;

        initializeButtons();
        updateDisplay();


    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }



    private void initializeButtons() {
        storeView.setOnBackButtonClicked(() -> {
            if (gameController != null){
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
    }

    public void setCoins(int amount) {
        int clampedAmount = Math.max(MIN_COINS, Math.min(amount, MAX_COINS));
        CoinStorage.setTotalCoins(clampedAmount);
        storeView.updateCoins(CoinStorage.getTotalCoins());
    }

    public int getPlayerCoins() {
        return CoinStorage.getTotalCoins();
    }

    public int getSelectedPaddleIndex() {
        return selectedPaddleIndex;
    }

    public Color getSelectedPaddleColor() {
        if (isValidPaddleIndex(selectedPaddleIndex)) {
            return paddleColors[selectedPaddleIndex];
        }
        return paddleColors[0];
    }

    public String getSelectedPaddleName() {
        if (isValidPaddleIndex(selectedPaddleIndex)) {
            return paddleNames[selectedPaddleIndex];
        }
        return paddleNames[0];
    }

    public boolean[] getUnlockedPaddles() {
        return paddleUnlocked.clone();
    }

    public int getSelectedBallIndex() {
        return selectedBallIndex;
    }

    public Color getSelectedBallColor() {
        if (isValidBallIndex(selectedBallIndex)) {
            return ballColors[selectedBallIndex];
        }
        return ballColors[0];
    }

    public String getSelectedBallName() {
        if (isValidBallIndex(selectedBallIndex)) {
            return ballNames[selectedBallIndex];
        }
        return ballNames[0];
    }

    public boolean[] getUnlockedBalls() {
        return ballUnlocked.clone();
    }

    public void unlockPaddle(int index) {
        if (isValidPaddleIndex(index)) {
            if (!paddleUnlocked[index]) {
                paddleUnlocked[index] = true;

                if (currentPaddleViewIndex == index) {
                    updatePaddleDisplay();
                }
            }
        }
    }

    public void unlockBall(int index) {
        if (isValidBallIndex(index)) {
            if (!ballUnlocked[index]) {
                ballUnlocked[index] = true;

                if (currentBallViewIndex == index) {
                    updateBallDisplay();
                }
            }
        }
    }

    public int getPaddlePrice(int index) {
        if (isValidPaddleIndex(index)) {
            return paddlePrices[index];
        }
        return 0;
    }

    public int getBallPrice(int index) {
        if (isValidBallIndex(index)) {
            return ballPrices[index];
        }
        return 0;
    }

    public boolean isPaddleUnlocked(int index) {
        return isValidPaddleIndex(index) && paddleUnlocked[index];
    }

    public boolean isBallUnlocked(int index) {
        return isValidBallIndex(index) && ballUnlocked[index];
    }

    public void navigateToPaddle(int index) {
        if (isValidPaddleIndex(index)) {
            currentPaddleViewIndex = index;
            updatePaddleDisplay();
        }
    }

    public void navigateToBall(int index) {
        if (isValidBallIndex(index)) {
            currentBallViewIndex = index;
            updateBallDisplay();
        }
    }

    public void navigateToSelectedSkins() {
        currentPaddleViewIndex = selectedPaddleIndex;
        currentBallViewIndex = selectedBallIndex;
        updateDisplay();
    }

    public void loadPlayerData(int coins, int selectedPaddle, int selectedBall,
                               boolean[] unlockedPaddles, boolean[] unlockedBalls) {
        int clampedCoins = Math.max(MIN_COINS, Math.min(coins, MAX_COINS));
        CoinStorage.setTotalCoins(clampedCoins);

        if (isValidPaddleIndex(selectedPaddle)) {
            this.selectedPaddleIndex = selectedPaddle;
        } else {
            this.selectedPaddleIndex = 0;
        }

        if (isValidBallIndex(selectedBall)) {
            this.selectedBallIndex = selectedBall;
        } else {
            this.selectedBallIndex = 0;
        }

        if (unlockedPaddles != null && unlockedPaddles.length == TOTAL_PADDLE_SKINS) {
            this.paddleUnlocked = unlockedPaddles.clone();
            this.paddleUnlocked[0] = true;

            if (!this.paddleUnlocked[selectedPaddleIndex]) {
                this.selectedPaddleIndex = 0;
            }
        } else {
            this.paddleUnlocked = new boolean[TOTAL_PADDLE_SKINS];
            this.paddleUnlocked[0] = true;
            this.selectedPaddleIndex = 0;
        }

        if (unlockedBalls != null && unlockedBalls.length == TOTAL_BALL_SKINS) {
            this.ballUnlocked = unlockedBalls.clone();
            this.ballUnlocked[0] = true;

            if (!this.ballUnlocked[selectedBallIndex]) {
                this.selectedBallIndex = 0;
            }
        } else {
            this.ballUnlocked = new boolean[TOTAL_BALL_SKINS];
            this.ballUnlocked[0] = true;
            this.selectedBallIndex = 0;
        }

        currentPaddleViewIndex = selectedPaddleIndex;
        currentBallViewIndex = selectedBallIndex;

        SpriteManager.initialize(selectedPaddleIndex, selectedBallIndex);
        updateDisplay();
    }

    public SaveData getSaveData() {
        return new SaveData(CoinStorage.getTotalCoins(), selectedPaddleIndex, selectedBallIndex,
                paddleUnlocked.clone(), ballUnlocked.clone());
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
    }

    public static class SaveData {
        public final int coins;
        public final int selectedPaddle;
        public final int selectedBall;
        public final boolean[] unlockedPaddles;
        public final boolean[] unlockedBalls;

        public SaveData(int coins, int selectedPaddle, int selectedBall,
                        boolean[] unlockedPaddles, boolean[] unlockedBalls) {
            this.coins = coins;
            this.selectedPaddle = selectedPaddle;
            this.selectedBall = selectedBall;
            this.unlockedPaddles = unlockedPaddles;
            this.unlockedBalls = unlockedBalls;
        }

        @Override
        public String toString() {
            return "SaveData{coins=" + coins +
                    ", selectedPaddle=" + selectedPaddle +
                    ", selectedBall=" + selectedBall + "}";
        }
    }

    public int getSelectedPaddle() {
        return selectedPaddleIndex;
    }

    public int getSelectedBall() {
        return selectedBallIndex;
    }
}