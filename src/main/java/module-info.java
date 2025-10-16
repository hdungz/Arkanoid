module com.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires jdk.compiler;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
//    requires com.arkanoid;
//    requires com.arkanoid;

    opens com.arkanoid to javafx.fxml;
    exports com.arkanoid;
    exports com.arkanoid.utils;
    opens com.arkanoid.utils to javafx.fxml;
    exports com.arkanoid.model.ball;
    opens com.arkanoid.model.ball to javafx.fxml;
}