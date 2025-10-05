package com.arkanoid.view.brick;

import com.arkanoid.model.GameModel;
import com.arkanoid.model.brick.Brick;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class BrickRenderer {
    private final GameModel gameModel;
    private final List<Rectangle> brickShapes;

    public BrickRenderer(GameModel model) {
        this.gameModel = model;
        this.brickShapes = new ArrayList<>();
        // Khởi tạo các đối tượng hình chữ nhật tương ứng với mỗi viên gạch trong model
        for (Brick brick : model.getBricks()) {
            Rectangle rect = new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            rect.setArcWidth(5);
            rect.setArcHeight(5);
            rect.setStroke(Color.BLACK);
            brickShapes.add(rect);
        }
    }

    /**
     * Cập nhật trạng thái hiển thị và màu sắc của tất cả các viên gạch.
     */
    public void render() {
        List<Brick> bricks = gameModel.getBricks();
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            Rectangle rect = brickShapes.get(i);

            if (brick.isVisible()) {
                rect.setVisible(true);
                // Quyết định màu sắc dựa trên loại và máu của gạch
                rect.setFill(getColorForBrick(brick));
            } else {
                rect.setVisible(false);
            }
        }
    }

    private Paint getColorForBrick(Brick brick) {
        switch (brick.getType()) {
            case NORMAL:
                // Với gạch nhiều máu, màu sắc sẽ thay đổi tùy theo số máu còn lại
                switch (brick.getHealth()) {
                    case 1: return Color.LIGHTSKYBLUE; // Máu yếu nhất
                    case 2: return Color.DEEPSKYBLUE;
                    case 3: return Color.ROYALBLUE;    // Máu đầy
                    default: return Color.GRAY;
                }
//            case NORMAL:
//                return Color.ORANGERED;
            // Bạn có thể thêm các case cho các loại gạch khác (EXPLODING, COIN_DROPPER...) ở đây
            default:
                return Color.WHITE; // Màu mặc định nếu có lỗi
        }
    }

    public List<Node> getNodes() {
        return new ArrayList<>(brickShapes);
    }
}
