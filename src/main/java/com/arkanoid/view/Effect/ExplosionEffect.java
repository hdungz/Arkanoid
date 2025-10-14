package com.arkanoid.effects;

import com.arkanoid.utils.AssetsManager;
import com.arkanoid.utils.SpriteAnimator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosionEffect {
    private final SpriteAnimator animator;
    // Tọa độ tâm vụ nổ
    private final double centerX, centerY;
    // Kích thước mong muốn của hiệu ứng nổ (ví dụ 150x150 pixels)
    private final double effectSize;
    private boolean finished = false;

    // Thay đổi constructor để nhận kích thước nổ
    public ExplosionEffect(double centerX, double centerY, double size) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.effectSize = size; // Kích thước hiển thị
        Image[] frames = AssetsManager.getFrames("Explosion");
        if (frames.length == 0) {
            System.err.println("Không tìm thấy sprite explosion!");
        }
        this.animator = new SpriteAnimator(frames, 60, false); // không loop
        // Tốc độ animation: 39 frames trong 1/4 giây (khoảng 156 FPS) là quá nhanh.
        // Dùng 12 FPS sẽ làm hiệu ứng kéo dài khoảng 3 giây (quá dài).
        // Thường animation nổ nên kéo dài ~0.25 đến 0.5 giây.
        // Nếu 39 frames, để kéo dài 0.25s -> FPS = 39 / 0.25 = 156 FPS.
       // Thử 156 FPS
    }

    public void update() {
        animator.update();
        // Khi chạy hết animation (trả về null), đánh dấu kết thúc
        if (animator.isFinished()) {
            finished = true;
        }
    }

    public void render(GraphicsContext gc) {
        Image frame = animator.getCurrentFrame();
        if (frame == null) return;

        // Vẽ ảnh tại tọa độ tâm (centerX, centerY),
        // dịch ảnh về phía góc trên bên trái để căn giữa
        gc.drawImage(
                frame,
                centerX - effectSize / 2, // X mới
                centerY - effectSize / 2, // Y mới
                effectSize,               // Chiều rộng
                effectSize                // Chiều cao
        );
    }

    public boolean isFinished() {
        return finished;
    }
}