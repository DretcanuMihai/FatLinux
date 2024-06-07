module com.fatlinux {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires org.apache.pdfbox;
    requires com.sun.jna;

    opens com.fatlinux to javafx.fxml;
    exports com.fatlinux;

    exports com.fatlinux.controllers;
    opens com.fatlinux.controllers to javafx.fxml;

    exports com.fatlinux.model.entities_dto;
    opens com.fatlinux.model.entities_dto to javafx.fxml;
}