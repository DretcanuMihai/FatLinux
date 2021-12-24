package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.MainApplication;
import com.map_toysocialnetworkgui.service.SuperService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AbstractController {
    protected SuperService service;
    protected MainApplication application;
    // Window control buttons and title bar
    @FXML
    ImageView appExitButton;
    @FXML
    ImageView appMaximizeButton;
    @FXML
    ImageView appMinimizeButton;
    @FXML
    Pane titleBar;
    Image appExitHoveredButton = new Image("com/map_toysocialnetworkgui/images/closeHover.png");
    Image appMaximizeHoveredButton = new Image("com/map_toysocialnetworkgui/images/maxHover.png");
    Image appMinimiseHoveredButton = new Image("com/map_toysocialnetworkgui/images/minHover.png");

    @FXML
    public void initialize() {
        titleBar.setOnMousePressed(pressEvent -> titleBar.setOnMouseDragged(dragEvent -> {
            Stage stage = (Stage) ((Pane) dragEvent.getSource()).getScene().getWindow();
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));

        Image exitButtonImage = appExitButton.getImage();
        Image maximizeButtonImage = appMaximizeButton.getImage();
        Image minimizeButtonImage = appMinimizeButton.getImage();

        appExitButton.setOnMouseClicked(event -> {
            Platform.exit();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
        appMinimizeButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });
        appMinimizeButton.setOnMouseEntered(event -> {
            appMinimizeButton.setImage(appMinimiseHoveredButton);
        });
        appMinimizeButton.setOnMouseExited(event -> {
            appMinimizeButton.setImage(minimizeButtonImage);
        });
        appMaximizeButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
            stage.setMaximized(!stage.isMaximized());
        });
        appMaximizeButton.setOnMouseEntered(event -> {
            appMaximizeButton.setImage(appMaximizeHoveredButton);
        });
        appMaximizeButton.setOnMouseExited(event -> {
            appMaximizeButton.setImage(maximizeButtonImage);
        });
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setApplication(MainApplication application) {
        this.application = application;
    }
}
