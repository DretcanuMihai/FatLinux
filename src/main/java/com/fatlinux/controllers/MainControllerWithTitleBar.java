package com.fatlinux.controllers;

import com.jfoenix.controls.JFXButton;
import com.fatlinux.model.entities_dto.EventDTO;
import com.fatlinux.model.entities_dto.UserDTO;
import com.fatlinux.model.entities_dto.UserPage;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PageableImplementation;
import com.fatlinux.utils.structures.NoFocusModel;
import com.fatlinux.utils.styling.ButtonStyling;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Collections;
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

    PopOver popOverMain = null;

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
    BorderPane mainBorderPane;
    @FXML
    TextField searchBar;
    @FXML
    Button searchForFriendsButton;
    @FXML
    ImageView notificationBell;
    @FXML
    JFXButton inboxButton;
    @FXML
    JFXButton friendsButton;

    /**
     * button coloring
     */
    ButtonStyling buttonStyling;

    /**
     * button images
     */
    ImageView inboxButtonIcon = new ImageView("com/fatlinux/images/FATLookIcon.png");
    ImageView friendsButtonIcon = new ImageView("com/fatlinux/images/friendsIcon.png");

    /**
     * page navigation button images
     */
    ImageView nextPageButtonImage = new ImageView("com/fatlinux/images/nextPageIcon.png");
    ImageView previousPageButtonImage = new ImageView("com/fatlinux/images/previousPageIcon.png");

    /**
     * notification bell images
     */
    Image noNewNotifications = new Image("com/fatlinux/images/noNotificationsIcon.png");
    Image newNotifications = new Image("com/fatlinux/images/newNotificationsIcon.png");

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
        buttonStyling = new ButtonStyling();
        initLoadersAndControllers();
    }

    /**
     * initiates loaders and controllers for child views
     *
     * @throws IOException if an IO error occurs
     */
    private void initLoadersAndControllers() throws IOException {
        URL mainPageURL = getClass().getResource("/com/fatlinux/views/mainPage-view.fxml");
        URL searchForFriendURL = getClass().getResource("/com/fatlinux/views/searchFriend-view.fxml");
        URL userProfileURL = getClass().getResource("/com/fatlinux/views/userProfile-view.fxml");
        URL eventsURL = getClass().getResource("/com/fatlinux/views/events-view.fxml");
        URL inboxURL = getClass().getResource("/com/fatlinux/views/inbox-view.fxml");
        URL showFriendsURL = getClass().getResource("/com/fatlinux/views/friends-view.fxml");

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
        buttonStyling.setJFXButtonWithNotificationBubble(inboxButton, "FATLook", inboxButtonIcon, "", false);
        buttonStyling.setJFXButtonWithNotificationBubble(friendsButton, "Friends", friendsButtonIcon, "", false);
        if (userPage.getNrOfNotifications() > 0) {
            notificationBell.setImage(newNotifications);
            eventNotificationNumberLabel.setVisible(true);
            Notifications.create()
                    .title("FAT Linux!")
                    .text("You have " + userPage.getNrOfNotifications() + " new notifications!")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();

            eventNotificationNumberLabel.setVisible(true);
            if (userPage.getNrOfNotifications() > 9)
                eventNotificationNumberLabel.setText("9+");
            else
                eventNotificationNumberLabel.setText(String.valueOf(userPage.getNrOfNotifications()));

        }
        if (userPage.getNrOfNewRequests() > 0) {
            String noOfRequests = String.valueOf(userPage.getNrOfNewRequests());
            buttonStyling.setJFXButtonWithNotificationBubble(friendsButton, "Friends", friendsButtonIcon, noOfRequests, true);
            if (userPage.getNrOfNewRequests() > 9)
                buttonStyling.setJFXButtonWithNotificationBubble(friendsButton, "Friends", friendsButtonIcon, "9+", true);
        }
        if (userPage.getNrOfNewMessages() > 0) {
            String noOfMessages = String.valueOf(userPage.getNrOfNewMessages());
            buttonStyling.setJFXButtonWithNotificationBubble(inboxButton, "FATLook", inboxButtonIcon, noOfMessages, true);
            if (userPage.getNrOfNewMessages() > 9)
                buttonStyling.setJFXButtonWithNotificationBubble(inboxButton, "FATLook", inboxButtonIcon, "9+", true);
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
        initNumberOfNotifications(userPage);
        initEventsController();
        initSearchFriendsController();
        initUserProfileController();
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

    private List<String> generateNotifications() {
        List<String> notificationMessages = new ArrayList<>();
        this.events.forEach(eventDTO -> {
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), eventDTO.getDate());
            if (daysLeft == 0)
                notificationMessages.add(0, eventDTO.getTitle() + " takes place today!");
            else
                notificationMessages.add(0, daysLeft + " days left until " + eventDTO.getTitle() + " takes place!");
        });
        Collections.reverse(notificationMessages);
        return notificationMessages;
    }

    /**
     * initializes notifications list
     */
    private ListView<String> initNotificationsList() {

        ObservableList<String> modelNotifications = FXCollections.observableArrayList();

        modelNotifications.setAll(generateNotifications());

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

    private boolean hasNextNotification() {
        return service.getUserNotificationEvents(loggedUser.getEmail(), eventDTOPage.nextPageable())
                .getContent().findAny().isPresent();
    }

    private boolean hasPreviousNotification() {
        return eventDTOPage.getPageable().getPageNumber() != 1;
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
        buttonStyling.setButtonOrange(nextPageButton);
        buttonStyling.setButtonOrange(previousPageButton);
        nextPageButton.setGraphic(nextPageButtonImage);
        previousPageButton.setGraphic(previousPageButtonImage);
        pageButtonsHBox.getChildren().addAll(previousPageButton, nextPageButton);
        notificationsVBox.getChildren().add(eventDTOListView);
        notificationsVBox.getChildren().add(pageButtonsHBox);
        nextPageButton.setOnAction(event -> {
            loadNotifications3(eventDTOPage.nextPageable());

        });
        previousPageButton.setOnAction(event -> {
            loadNotifications3(eventDTOPage.previousPageable());
        });
        previousPageButton.setVisible(hasPreviousNotification());
        nextPageButton.setVisible(hasNextNotification());

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
        ((Parent) popOver.getSkin().getNode()).getStylesheets()
                .add(Objects.requireNonNull(getClass()
                        .getResource("/com/fatlinux/css/style.css")).toExternalForm());
        popOverMain = popOver;
    }

    /**
     * loads events for notifications
     */
    public void loadEvents() {
        loadEvents(new PageableImplementation(1, 7));
    }

    /**
     * loads events for notifications with page
     *
     * @param pageable - said page's pageable
     */
    public void loadEvents(Pageable pageable) {
        this.eventDTOPage = this.service.getUserNotificationEvents(this.loggedUser.getEmail(), pageable);
        Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
        this.events = new ArrayList<>(eventDTOStream.toList());
    }

    /**
     * loads the notifications for user
     */
    public void loadNotifications() {
        loadNotifications2(new PageableImplementation(1, 6));
    }

    /**
     * loads the notifications for user
     */
    public void loadNotifications2(Pageable pageable) {
        loadEvents(pageable);
        notificationBell.setImage(noNewNotifications);
        eventNotificationNumberLabel.setVisible(false);
        if (events.size() == 0) {
            if (popOverMain == null) {
                Label noNewNotificationsLabel = new Label("No notifications!");
                noNewNotificationsLabel.setFont(new Font(20));
                noNewNotificationsLabel.setStyle("-fx-text-fill: black; -fx-padding: 10px 10px 10px 10px");
                initNotificationsPopOver(noNewNotificationsLabel);
            } else {
                if (popOverMain.isFocused()) {
                    popOverMain.hide();
                } else {
                    Label noNewNotificationsLabel = new Label("No notifications!");
                    noNewNotificationsLabel.setFont(new Font(20));
                    noNewNotificationsLabel.setStyle("-fx-text-fill: black; -fx-padding: 10px 10px 10px 10px");
                    initNotificationsPopOver(noNewNotificationsLabel);
                }
            }
        } else {
            if (popOverMain == null) {
                ListView<String> eventDTOListView = initNotificationsList();
                VBox notificationsVBox = generateNotificationsPopOverContent(eventDTOListView);
                initNotificationsPopOver(notificationsVBox);
            } else {
                if (popOverMain.isFocused()) {
                    popOverMain.hide();
                } else {
                    ListView<String> eventDTOListView = initNotificationsList();
                    VBox notificationsVBox = generateNotificationsPopOverContent(eventDTOListView);
                    initNotificationsPopOver(notificationsVBox);
                }
            }
        }
    }

    /**
     * loads the notifications for user
     */
    public void loadNotifications3(Pageable pageable) {
        loadEvents(pageable);
        ListView<String> eventDTOListView = initNotificationsList();
        VBox notificationsVBox = generateNotificationsPopOverContent(eventDTOListView);
        popOverMain.setContentNode(notificationsVBox);
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
        buttonStyling.setJFXButtonWithNotificationBubble(inboxButton, "FATLook", inboxButtonIcon, "", false);
        inboxController.init();
        mainBorderPane.setCenter(inboxRoot);
    }

    /**
     * shows the friends view
     */
    public void showFriends() {
        buttonStyling.setJFXButtonWithNotificationBubble(friendsButton, "Friends", friendsButtonIcon, "", false);
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
