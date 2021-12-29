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
import java.net.URL;

public class MainControllerWithTitleBar extends AbstractControllerWithTitleBar {
    // Data
    UserUIDTO loggedUser;
    // Controllers
    SearchFriendsController searchFriendsController;
    InboxController inboxController;
    FriendsViewController friendsViewController;
    // View parents
    Parent mainPageRoot;
    Parent searchForFriendRoot;
    Parent inboxRoot;
    Parent showFriendsRoot;
    // FXML
    @FXML
    Label userNameLabel;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    TextField searchBar;

    private void initLoadersAndControllers() throws IOException {
        URL mainPageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/mainPage-view.fxml");
        URL searchForFriendURL = getClass().getResource("/com/map_toysocialnetworkgui/views/searchFriend-view.fxml");
        URL inboxURL = getClass().getResource("/com/map_toysocialnetworkgui/views/inbox-view.fxml");
        URL showFriendsURL = getClass().getResource("/com/map_toysocialnetworkgui/views/friends-view.fxml");

        FXMLLoader mainPageLoader = new FXMLLoader(mainPageURL);
        FXMLLoader searchForFriendLoader = new FXMLLoader(searchForFriendURL);
        FXMLLoader inboxLoader = new FXMLLoader(inboxURL);
        FXMLLoader showFriendsLoader = new FXMLLoader(showFriendsURL);

        this.mainPageRoot = mainPageLoader.load();
        this.searchForFriendRoot = searchForFriendLoader.load();
        this.inboxRoot = inboxLoader.load();
        this.showFriendsRoot = showFriendsLoader.load();

        this.searchFriendsController = searchForFriendLoader.getController();
        this.inboxController = inboxLoader.getController();
        this.friendsViewController = showFriendsLoader.getController();
    }

    private void initSearchBar() {
        this.searchBar.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchFriendsController.setSearchText(searchBar.getText());
                searchFriendsController.init();
                mainBorderPane.setCenter(searchForFriendRoot);
            }
        });
    }

    public void init(UserUIDTO user) throws IOException {
        initLoadersAndControllers();
        initSearchBar();
        loggedUser = user;
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
        showMainPage();
    }

    public void showMainPage() {
        mainBorderPane.setCenter(mainPageRoot);
    }

    public void searchForFriend() {
        searchFriendsController.setLoggedUser(loggedUser);
        searchFriendsController.setService(this.service);
    }

    public void showInbox() {
        inboxController.setLoggedUser(loggedUser);
        inboxController.setService(this.service);
        inboxController.init();
        mainBorderPane.setCenter(inboxRoot);
    }

    public void showFriends() {
        friendsViewController.setLoggedUser(loggedUser);
        friendsViewController.setService(this.service);
        friendsViewController.init();
        mainBorderPane.setCenter(showFriendsRoot);
    }

    public void logout() throws IOException {
        application.changeToLogin();
    }
}
