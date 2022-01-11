module com.map_toysocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires com.sun.jna;
    requires org.apache.pdfbox;

    opens com.map_toysocialnetworkgui to javafx.fxml;
    exports com.map_toysocialnetworkgui;

    exports com.map_toysocialnetworkgui.controllers;
    opens com.map_toysocialnetworkgui.controllers to javafx.fxml;

    exports com.map_toysocialnetworkgui.model.entities_dto;
    opens com.map_toysocialnetworkgui.model.entities_dto to javafx.fxml;
}