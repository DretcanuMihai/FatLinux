package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.EventDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserPage;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * controller for main view
 */
public class MainControllerWithTitleBar extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;
    UserPage userPage;

    /**
     * controllers for child views
     */
    SearchFriendsController searchFriendsController;
    UserProfileController userProfileController;
    EventsController eventsController;
    InboxController inboxController;
    FriendsViewController friendsViewController;

    /**
     * child views' parents
     */
    Parent mainPageRoot;
    Parent searchForFriendRoot;
    Parent userProfileRoot;
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
    ImageView notificationBell;

    /**
     * event DTO page
     */
    Page<EventDTO> eventDTOPage;

    /**
     * events array list
     */
    ArrayList<EventDTO> events;

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
        URL userProfileURL = getClass().getResource("/com/map_toysocialnetworkgui/views/userProfile-view.fxml");
        URL eventsURL = getClass().getResource("/com/map_toysocialnetworkgui/views/events-view.fxml");
        URL inboxURL = getClass().getResource("/com/map_toysocialnetworkgui/views/inbox-view.fxml");
        URL showFriendsURL = getClass().getResource("/com/map_toysocialnetworkgui/views/friends-view.fxml");

        FXMLLoader mainPageLoader = new FXMLLoader(mainPageURL);
        FXMLLoader searchForFriendLoader = new FXMLLoader(searchForFriendURL);
        FXMLLoader userProfileLoader = new FXMLLoader(userProfileURL);
        FXMLLoader eventsLoader = new FXMLLoader(eventsURL);
        FXMLLoader inboxLoader = new FXMLLoader(inboxURL);
        FXMLLoader showFriendsLoader = new FXMLLoader(showFriendsURL);

        this.mainPageRoot = mainPageLoader.load();
        this.searchForFriendRoot = searchForFriendLoader.load();
        this.userProfileRoot = userProfileLoader.load();
        this.eventsRoot = eventsLoader.load();
        this.inboxRoot = inboxLoader.load();
        this.showFriendsRoot = showFriendsLoader.load();

        this.searchFriendsController = searchForFriendLoader.getController();
        this.userProfileController = userProfileLoader.getController();
        this.eventsController = eventsLoader.getController();
        this.inboxController = inboxLoader.getController();
        this.friendsViewController = showFriendsLoader.getController();
    }

    /**
     * initiates the main controller with the currently logged-in user
     * shows the main page view
     *
     * @param userPage - said user's page
     */
    public void init(UserPage userPage) {
        UserDTO user = userPage.getUserInfo();
        loggedUser = user;
        this.setAppExitButtonForUserLogout(this.loggedUser);
        mainWindowTopAnchorPane.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
        initEventsController();
        initSearchFriendsController();
        initUserProfileController();
        initInboxController();
        initFriendsController();
        loadNotifications();
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
     * initiates user profile controller
     */
    private void initUserProfileController() {
        userProfileController.setLoggedUser(this.loggedUser);
        userProfileController.setService(this.service);
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
     * loads the notifications for user
     */
    private void loadNotifications() {
        this.eventDTOPage = this.service.getUserNotificationEvents(this.loggedUser.getEmail(), new PageableImplementation(1, 7));
        if (eventDTOPage.getContent() == null) {
            Image noNewNotifications = new Image("com/map_toysocialnetworkgui/images/noNotificationsIcon.png");
            notificationBell.setImage(noNewNotifications);
        } else {
            Image noNewNotifications = new Image("com/map_toysocialnetworkgui/images/newNotificationsIcon.png");
            notificationBell.setImage(noNewNotifications);
            Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
            this.events = new ArrayList<>(eventDTOStream.toList());
            Notifications.create()
                    .title("FAT Linux!")
                    .text("You have new notifications!")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();

            notificationBell.setOnMouseClicked(event -> {
                Image newNotifications = new Image("com/map_toysocialnetworkgui/images/noNotificationsIcon.png");
                notificationBell.setImage(newNotifications);

                ObservableList<String> modelNotifications = FXCollections.observableArrayList();
                List<String> notificationMessages = new ArrayList<>();
                this.events.forEach(eventDTO -> {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), eventDTO.getDate());
                    notificationMessages.add(0, daysLeft + " days left until " + eventDTO.getTitle() + " takes place!");
                });

                modelNotifications.setAll(notificationMessages);
                ListView<String> eventDTOListView = new ListView<>(modelNotifications);
                eventDTOListView.setPrefWidth(500);
                eventDTOListView.setCellFactory(param -> new ListCell<>() {
                    {
                        setPrefHeight(60);
                        setFocused(false);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (item != null && !empty) {
                            this.setWrapText(true);
                            this.setFont(new Font(15));
                            this.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0");
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }
                });

                PopOver popOver = new PopOver(eventDTOListView);
                popOver.setTitle("Notifications");
                popOver.setDetachable(false);
                popOver.setAnimated(true);
                popOver.setHeaderAlwaysVisible(true);
                popOver.show(notificationBell);
            });
        }
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
     * shows the user profile view
     */
    public void showUserProfile() {
        userProfileController.init();
        mainBorderPane.setCenter(userProfileRoot);
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
        service.logout(loggedUser.getEmail());
        reset();
    }

    @Override
    public void reset() {
        searchFriendsController.reset();
        inboxController.reset();
        friendsViewController.reset();
        loggedUser = null;
        userNameLabel.setText("");
        searchBar.setText("");
    }
}
