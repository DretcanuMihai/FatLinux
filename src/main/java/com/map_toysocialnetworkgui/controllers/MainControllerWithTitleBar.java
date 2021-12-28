package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    @FXML
    TextField searchBar;

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

    public void searchForFriend() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader())
                .getResource("com/map_toysocialnetworkgui/views/searchFriend-view.fxml"));
        Parent root = loader.load();
        SearchFriendsController searchFriendsController = loader.getController();
        searchFriendsController.setLoggedUser(loggedUser);
        searchFriendsController.setService(this.service);
        searchFriendsController.setSearchText(searchBar.getText());
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchFriendsController.setSearchText(searchBar.getText());
                searchFriendsController.init();
            }
        });
        searchFriendsController.init();
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
