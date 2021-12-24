package com.map_toysocialnetworkgui.controllers;

import com.jfoenix.controls.JFXButton;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class MainController extends AbstractController {
    // Data
    UserUIDTO loggedUser;
    // FXML
    @FXML
    Label userNameLabel;

    public void init(UserUIDTO user) {
        loggedUser = user;
        userNameLabel.setText(user.getFirstName() + " " + user.getFirstName());
    }

    public void logout() throws IOException {
        application.changeToLogin();
    }
}
