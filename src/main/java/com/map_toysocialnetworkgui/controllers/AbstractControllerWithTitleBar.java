package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * class that describes an abstract view controller with a custom title bar
 */
public class AbstractControllerWithTitleBar extends AbstractController {

    /**
     * Window control buttons and title bar
     */
    @FXML
    protected ImageView appExitButton;
    @FXML
    ImageView appMaximizeButton;
    @FXML
    ImageView appMinimizeButton;
    @FXML
    Pane titleBar;
    Image appExitHoveredButton = new Image("com/map_toysocialnetworkgui/images/closeHover.png");
    Image appMaximizeHoveredButton = new Image("com/map_toysocialnetworkgui/images/maxHover.png");
    Image appMinimiseHoveredButton = new Image("com/map_toysocialnetworkgui/images/minHover.png");

    /**
     * makes the title bar able to drag the window while clicked on and moved around
     */
    public void setTitleBarOnMousePressedDragWindow() {
        titleBar.setOnMousePressed(pressEvent -> titleBar.setOnMouseDragged(dragEvent -> {
            Stage stage = (Stage) ((Pane) dragEvent.getSource()).getScene().getWindow();
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
    }

    /**
     * initiates exit button look and functionality
     */
    public void initAppExitButton() {
        Image exitButtonImage = appExitButton.getImage();
        appExitButton.setOnMouseClicked(event -> {
            Platform.exit();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
    }

    /**
     * initiates minimize button look and functionality
     */
    public void initAppMinimizeButton() {
        Image minimizeButtonImage = appMinimizeButton.getImage();
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
    }

    /**
     * initiates maximize button look and functionality
     */
    public void initAppMaximizeButton() {
        Image maximizeButtonImage = appMaximizeButton.getImage();
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

    /**
     * sets the exit button to only close the current scene
     */
    public void setAppExitButtonToOnlyCloseWindow() {
        Image exitButtonImage = appExitButton.getImage();
        appExitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) appExitButton.getScene().getWindow();
            stage.close();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
    }

    /**
     * sets the exit button to log out the currently logged-in user before exiting
     *
     * @param loggedUser - said logged user
     */
    public void setAppExitButtonForUserLogout(UserDTO loggedUser) {
        Image exitButtonImage = appExitButton.getImage();
        appExitButton.setOnMouseClicked(event -> {
            service.logout(loggedUser.getEmail());
            Platform.exit();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
    }

    @FXML
    public void initialize() throws IOException {
        setTitleBarOnMousePressedDragWindow();
        initAppExitButton();
        initAppMinimizeButton();
        initAppMaximizeButton();
    }
}
