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

/**
 * controller for main view
 */
public class MainControllerWithTitleBar extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserUIDTO loggedUser;

    /**
     * controllers for child views
     */
    SearchFriendsController searchFriendsController;
    InboxController inboxController;
    FriendsViewController friendsViewController;

    /**
     * child views' parents
     */
    Parent mainPageRoot;
    Parent searchForFriendRoot;
    Parent inboxRoot;
    Parent showFriendsRoot;

    /**
     * FXML data
     */
    @FXML
    Label userNameLabel;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    TextField searchBar;

    /**
     * initiates loaders and controllers for child views
     *
     * @throws IOException if an IO error occurs
     */
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

    /**
     * initiates search bar's functionality
     */
    private void initSearchBar() {
        this.searchBar.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchFriendsController.setSearchText(searchBar.getText());
                searchFriendsController.init();
                mainBorderPane.setCenter(searchForFriendRoot);
            }
        });
    }

    /**
     * initiates the main controller with the currently logged-in user
     * shows the main page view
     *
     * @param user - said user
     * @throws IOException if an IO error occurs
     */
    public void init(UserUIDTO user) throws IOException {
        initLoadersAndControllers();
        initSearchBar();
        loggedUser = user;
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
        showMainPage();
    }

    /**
     * shows the main page view
     */
    public void showMainPage() {
        mainBorderPane.setCenter(mainPageRoot);
    }

    /**
     * initiates search bar's controller
     */
    public void searchForFriend() {
        searchFriendsController.setLoggedUser(loggedUser);
        searchFriendsController.setService(this.service);
    }

    /**
     * shows the inbox view
     */
    public void showInbox() {
        inboxController.setLoggedUser(loggedUser);
        inboxController.setService(this.service);
        inboxController.init();
        mainBorderPane.setCenter(inboxRoot);
    }

    /**
     * shows the friends view
     */
    public void showFriends() {
        friendsViewController.setLoggedUser(loggedUser);
        friendsViewController.setService(this.service);
        friendsViewController.init();
        mainBorderPane.setCenter(showFriendsRoot);
    }

    /**
     * logs out the currently logged-in user
     *
     * @throws IOException if an IO error occurs
     */
    public void logout() throws IOException {
        application.changeToLogin();
    }
}