package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.EventDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserPage;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import com.map_toysocialnetworkgui.utils.structures.NoFocusModel;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import java.util.Objects;
import java.util.stream.Stream;

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
    Label eventNotificationNumberLabel;
    @FXML
    Label inboxNotificationNumberLabel;
    @FXML
    Label friendsNotificationNumberLabel;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    TextField searchBar;
    @FXML
    Button searchForFriendsButton;
    @FXML
    ImageView notificationBell;
    @FXML
    ImageView inboxNotificationBubble;
    @FXML
    ImageView friendsNotificationBubble;

    /**
     * button coloring
     */
    ButtonColoring buttonColoring;

    /**
     * page navigation button images
     */
    ImageView nextPageButtonImage = new ImageView("com/map_toysocialnetworkgui/images/nextPageIcon.png");
    ImageView previousPageButtonImage = new ImageView("com/map_toysocialnetworkgui/images/previousPageIcon.png");

    /**
     * notification bell images
     */
    Image noNewNotifications = new Image("com/map_toysocialnetworkgui/images/noNotificationsIcon.png");
    Image newNotifications = new Image("com/map_toysocialnetworkgui/images/newNotificationsIcon.png");

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
        buttonColoring = new ButtonColoring();
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

    private void initNumberOfNotifications(UserPage userPage) {
        if (userPage.getNrOfNotifications() > 0) {
            eventNotificationNumberLabel.setVisible(true);
            eventNotificationNumberLabel.setText(String.valueOf(userPage.getNrOfNotifications()));
            if (userPage.getNrOfNotifications() > 9)
                eventNotificationNumberLabel.setText("9+");
        }
        if (userPage.getNrOfNewRequests() > 0) {
            friendsNotificationBubble.setVisible(true);
            friendsNotificationNumberLabel.setVisible(true);
            friendsNotificationNumberLabel.setText(String.valueOf(userPage.getNrOfNewRequests()));
            if (userPage.getNrOfNewRequests() > 9)
                friendsNotificationNumberLabel.setText("9+");
        }
        if (userPage.getNrOfNewMessages() > 0) {
            inboxNotificationBubble.setVisible(true);
            inboxNotificationNumberLabel.setVisible(true);
            inboxNotificationNumberLabel.setText(String.valueOf(userPage.getNrOfNewMessages()));
            if (userPage.getNrOfNewMessages() > 9)
                inboxNotificationNumberLabel.setText("9+");
        }
        if (userPage.getNrOfNewFriends() > 0) {
            Notifications.create()
                    .title("FAT Linux!")
                    .text("You have " + userPage.getNrOfNewFriends() + " new friends!")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
        }
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
        notificationBell.setImage(noNewNotifications);
        eventNotificationNumberLabel.setVisible(false);
        friendsNotificationBubble.setVisible(false);
        friendsNotificationNumberLabel.setVisible(false);
        inboxNotificationBubble.setVisible(false);
        inboxNotificationNumberLabel.setVisible(false);
        initNumberOfNotifications(userPage);
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
     * initializes notifications list
     */
    private ListView<String> initNotificationsList(ObservableList<String> modelNotifications, List<String> notificationMessages) {
        modelNotifications.setAll(notificationMessages);
        ListView<String> eventDTOListView = new ListView<>(modelNotifications);
        eventDTOListView.setFocusModel(new NoFocusModel<>());
        eventDTOListView.setStyle("-fx-padding: 0px; -fx-background-insets: 0");
        eventDTOListView.setPrefWidth(500);
        eventDTOListView.setCellFactory(param -> new ListCell<>() {
            {
                setPrefHeight(60);
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

        return eventDTOListView;
    }

    /**
     * generate the content shown in notifications' pop over
     */
    private VBox generateNotificationsPopOverContent(ListView<String> eventDTOListView) {
        VBox notificationsVBox = new VBox();
        HBox pageButtonsHBox = new HBox();
        Button nextPageButton = new Button();
        Button previousPageButton = new Button();

        pageButtonsHBox.setAlignment(Pos.CENTER);
        pageButtonsHBox.setSpacing(20);
        pageButtonsHBox.setPrefHeight(40);
        buttonColoring.setButtonOrange(nextPageButton);
        buttonColoring.setButtonOrange(previousPageButton);
        nextPageButton.setGraphic(nextPageButtonImage);
        previousPageButton.setGraphic(previousPageButtonImage);
        pageButtonsHBox.getChildren().addAll(previousPageButton, nextPageButton);
        notificationsVBox.getChildren().add(eventDTOListView);
        notificationsVBox.getChildren().add(pageButtonsHBox);

        return notificationsVBox;
    }

    /**
     * initiates notifications' pop over
     */
    private void initNotificationsPopOver(Node content) {
        PopOver popOver = new PopOver(content);
        popOver.setTitle("Notifications");
        popOver.setArrowSize(20.0);
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        popOver.setHeaderAlwaysVisible(true);
        popOver.show(notificationBell);
        ((Parent)popOver.getSkin().getNode()).getStylesheets()
                .add(Objects.requireNonNull(getClass()
                        .getResource("/com/map_toysocialnetworkgui/css/style.css")).toExternalForm());
    }

    /**
     * loads the notifications for user
     */
    private void loadNotifications() {
        this.eventDTOPage = this.service.getUserNotificationEvents(this.loggedUser.getEmail(), new PageableImplementation(1, 7));
        Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
        this.events = new ArrayList<>(eventDTOStream.toList());

        if (this.events.size() != 0) {
            notificationBell.setImage(newNotifications);
            eventNotificationNumberLabel.setVisible(true);
            Notifications.create()
                    .title("FAT Linux!")
                    .text("You have " + this.events.size() + " new notifications!")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();

            EventHandler<MouseEvent> mouseClickedEvent = event -> {
                notificationBell.setImage(noNewNotifications);
                eventNotificationNumberLabel.setVisible(false);
                ObservableList<String> modelNotifications = FXCollections.observableArrayList();
                List<String> notificationMessages = new ArrayList<>();

                this.events.forEach(eventDTO -> {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), eventDTO.getDate());
                    if (daysLeft == 0)
                        notificationMessages.add(0, eventDTO.getTitle() + " takes place today!");
                    else
                        notificationMessages.add(0, daysLeft + " days left until " + eventDTO.getTitle() + " takes place!");
                });

                ListView<String> eventDTOListView = initNotificationsList(modelNotifications, notificationMessages);
                VBox notificationsVBox = generateNotificationsPopOverContent(eventDTOListView);
                initNotificationsPopOver(notificationsVBox);
            };
            notificationBell.setOnMouseClicked(mouseClickedEvent);
            eventNotificationNumberLabel.setOnMouseClicked(mouseClickedEvent);
        } else {
            notificationBell.setOnMouseClicked(event -> {
                Label noNewNotificationsLabel = new Label("No notifications!");
                noNewNotificationsLabel.setFont(new Font(20));
                noNewNotificationsLabel.setStyle("-fx-text-fill: black; -fx-padding: 10px 10px 10px 10px");
                initNotificationsPopOver(noNewNotificationsLabel);
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
        inboxNotificationBubble.setVisible(false);
        inboxNotificationNumberLabel.setVisible(false);
        inboxController.init();
        mainBorderPane.setCenter(inboxRoot);
    }

    /**
     * shows the friends view
     */
    public void showFriends() {
        friendsNotificationBubble.setVisible(false);
        friendsNotificationNumberLabel.setVisible(false);
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
