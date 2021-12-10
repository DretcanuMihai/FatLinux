module com.example.map_toysocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.map_toysocialnetworkgui to javafx.fxml;
    exports com.example.map_toysocialnetworkgui;
}