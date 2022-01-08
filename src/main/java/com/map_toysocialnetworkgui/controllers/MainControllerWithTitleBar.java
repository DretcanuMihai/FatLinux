package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    UserDTO loggedUser;

    /**
     * controllers for child views
     */
    SearchFriendsController searchFriendsController;
    EventsController eventsController;
    InboxController inboxController;
    FriendsViewController friendsViewController;

    /**
     * child views' parents
     */
    Parent mainPageRoot;
    Parent searchForFriendRoot;
    Parent eventsRoot;
    Parent inboxRoot;
    Parent showFriendsRoot;

    /**
     * FXML data
     */
    @FXML
    AnchorPane mainWindowTopAnchorPane;
    @FXML
    Label userNameLabel;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    TextField searchBar;
    @FXML
    Button searchForFriendsButton;

    @FXML
    @Override
    public void initialize() throws IOException {
        super.initialize();
        initLoadersAndControllers();
    }

    /**
     * initiates loaders and controllers for child views
     *
     * @throws IOException if an IO error occurs
     */
    private void initLoadersAndControllers() throws IOException {
        URL mainPageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/mainPage-view.fxml");
        URL searchForFriendURL = getClass().getResource("/com/map_toysocialnetworkgui/views/searchFriend-view.fxml");
        URL eventsURL = getClass().getResource("/com/map_toysocialnetworkgui/views/events-view.fxml");
        URL inboxURL = getClass().getResource("/com/map_toysocialnetworkgui/views/inbox-view.fxml");
        URL showFriendsURL = getClass().getResource("/com/map_toysocialnetworkgui/views/friends-view.fxml");

        FXMLLoader mainPageLoader = new FXMLLoader(mainPageURL);
        FXMLLoader searchForFriendLoader = new FXMLLoader(searchForFriendURL);
        FXMLLoader eventsLoader = new FXMLLoader(eventsURL);
        FXMLLoader inboxLoader = new FXMLLoader(inboxURL);
        FXMLLoader showFriendsLoader = new FXMLLoader(showFriendsURL);

        this.mainPageRoot = mainPageLoader.load();
        this.searchForFriendRoot = searchForFriendLoader.load();
        this.eventsRoot = eventsLoader.load();
        this.inboxRoot = inboxLoader.load();
        this.showFriendsRoot = showFriendsLoader.load();

        this.searchFriendsController = searchForFriendLoader.getController();
        this.inboxController = inboxLoader.getController();
        this.eventsController = eventsLoader.getController();
        this.friendsViewController = showFriendsLoader.getController();
    }

    /**
     * initiates the main controller with the currently logged-in user
     * shows the main page view
     *
     * @param user - said user
     */
    public void init(UserDTO user) {
        loggedUser = user;
        mainWindowTopAnchorPane.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
        initEventsController();
        initSearchFriendsController();
        initInboxController();
        initFriendsController();
        showMainPage();
    }

    /**
     * initiates friends controller
     */
    private void initSearchFriendsController() {
        searchFriendsController.setLoggedUser(this.loggedUser);
        searchFriendsController.setService(this.service);
    }

    /**
     * initiates events controller
     */
    private void initEventsController() {
        eventsController.setLoggedUser(this.loggedUser);
        eventsController.setService(this.service);
    }

    /**
     * initiates inbox controller
     */
    private void initInboxController() {
        inboxController.setLoggedUser(loggedUser);
        inboxController.setService(this.service);
        this.service.addMessageObserver(inboxController);
        inboxController.initModels();
    }

    /**
     * initiates friends controller
     */
    private void initFriendsController() {
        friendsViewController.setLoggedUser(loggedUser);
        friendsViewController.setService(this.service);
    }

    /**
     * shows the main page view
     */
    public void showMainPage() {
        mainBorderPane.setCenter(mainPageRoot);
    }

    /**
     * shows searched users
     */
    public void showSearchFriends() {
        searchFriendsController.search(searchBar.getText());
        mainBorderPane.setCenter(searchForFriendRoot);
    }

    /**
     * shows the events view
     */
    public void showEvents() {
        eventsController.init();
        mainBorderPane.setCenter(eventsRoot);
    }

    /**
     * shows the inbox view
     */
    public void showInbox() {
        inboxController.init();
        mainBorderPane.setCenter(inboxRoot);
    }

    /**
     * shows the friends view
     */
    public void showFriends() {
        friendsViewController.init();
        mainBorderPane.setCenter(showFriendsRoot);
    }

    /**
     * logs out the currently logged-in user
     */
    public void logout() {
        application.changeToLogin();
        reset();
    }

    @Override
    public void reset() {
        searchFriendsController.reset();
        inboxController.reset();
        friendsViewController.reset();
        this.service.removeMessageObserver(inboxController);
        loggedUser = null;
        userNameLabel.setText("");
        searchBar.setText("");
    }
}
