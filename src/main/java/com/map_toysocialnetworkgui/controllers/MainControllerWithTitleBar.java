package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class MainControllerWithTitleBar extends AbstractControllerWithTitleBar {
    // Data
    UserUIDTO loggedUser;
    // FXML
    @FXML
    Label userNameLabel;
    @FXML
    BorderPane mainBorderPane;

    public void init(UserUIDTO user) throws IOException {
        loggedUser = user;
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
        showMainPage();
    }

    public void showMainPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("com/map_toysocialnetworkgui/views/mainPage-view.fxml")));
        mainBorderPane.setCenter(root);
    }

    public void showFriends() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader())
                .getResource("com/map_toysocialnetworkgui/views/friends-view.fxml"));
        Parent root = loader.load();
        FriendsViewController friendsViewController = loader.getController();
        friendsViewController.setLoggedUser(loggedUser);
        friendsViewController.setService(this.service);
        friendsViewController.init();
        mainBorderPane.setCenter(root);
    }

    public void logout() throws IOException {
        application.changeToLogin();
    }
}
