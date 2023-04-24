module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.dotenv;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}