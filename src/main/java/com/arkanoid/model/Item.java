package com.arkanoid.model;

import com.arkanoid.model.paddle.Paddle;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Item {
    private double x, y;
    private double tocDoY = 120;
    private boolean hienThi = true;
    private static Image anhHopQua;

    static {
        try {
            anhHopQua = new Image(Item.class.getResource("/com/arkanoid/images/hopquado.png").toExternalForm());
        } catch (Exception e) {
            System.out.println("❌ Không thể tải ảnh hopquado.png: " + e.getMessage());
        }
    }

    public Item(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void capNhat(double deltaTime) {
        if (!hienThi) return;
        y += tocDoY * deltaTime;
        if (y > 800) hienThi = false;
    }

    public void ve(GraphicsContext gc) {
        if (!hienThi) return;
        if (anhHopQua != null) gc.drawImage(anhHopQua, x, y, 32, 32);
        else gc.fillRect(x, y, 32, 32);
    }

    public boolean kiemTraCham(Paddle paddle) {
        return hienThi && getBoundary().intersects(paddle.getBoundary());
    }

    public void an() {
        hienThi = false;
    }

    public boolean isHienThi() {
        return hienThi;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getChieuRong() {
        return 32;
    }

    public double getChieuCao() {
        return 32;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, 32, 32);
    }

    public void setHienThi(boolean hienThi) {
        this.hienThi = hienThi;
    }
}
