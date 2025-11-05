package com.arkanoid.view.Coin;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.coin.Coin;
import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinRenderer {
    private final GameModel gameModel;
    private final Pane coinPane;
    private final Map<Coin, CoinView> coinViews;

    private static class CoinView {
        ImageView imageView;
        SpriteAnimator animator;

        CoinView(ImageView imageView, SpriteAnimator animator) {
            this.imageView = imageView;
            this.animator = animator;
        }
    }

    public CoinRenderer(GameModel gameModel) {
        this.gameModel = gameModel;
        this.coinPane = new Pane();
        this.coinPane.setMouseTransparent(true); // Không chặn input
        this.coinViews = new HashMap<>();
    }

    public void render() {
        List<Coin> coins = gameModel.getCoinManager().getCoins();

        coinViews.entrySet().removeIf(entry -> {
            Coin coin = entry.getKey();
            if (!coins.contains(coin) || !coin.isActive()) {
                coinPane.getChildren().remove(entry.getValue().imageView);
                return true;
            }
            return false;
        });

        for (Coin coin : coins) {
            if (!coin.isActive()) continue;

            if (!coinViews.containsKey(coin)) {
                createCoinView(coin);
            }

            updateCoinView(coin);
        }
    }

    private void createCoinView(Coin coin) {
        javafx.scene.image.Image[] frames = AssetsManager.getFrames("Coin");

        if (frames == null || frames.length == 0) {
            System.err.println("⚠️ Coin frames not loaded!");
            return;
        }

        ImageView imageView = new ImageView();
        imageView.setFitWidth(coin.getWidth());
        imageView.setFitHeight(coin.getHeight());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        SpriteAnimator animator = new SpriteAnimator(frames, 10);

        CoinView coinView = new CoinView(imageView, animator);
        coinViews.put(coin, coinView);
        coinPane.getChildren().add(imageView);
    }


    private void updateCoinView(Coin coin) {
        CoinView coinView = coinViews.get(coin);
        if (coinView == null) return;

        coinView.animator.update();

        javafx.scene.image.Image currentFrame = coinView.animator.getCurrentFrame();
        if (currentFrame != null) {
            coinView.imageView.setImage(currentFrame);
        }


        coinView.imageView.setX(coin.getX());
        coinView.imageView.setY(coin.getY());
    }

    public Node getNode() {
        return coinPane;
    }

    public void cleanup() {
        coinPane.getChildren().clear();
        coinViews.clear();
    }
}