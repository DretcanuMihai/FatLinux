package com.fatlinux.controllers;

import com.fatlinux.model.entities_dto.EventDTO;
import com.fatlinux.model.entities_dto.UserDTO;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PageableImplementation;
import com.fatlinux.utils.structures.Pair;
import com.fatlinux.utils.styling.ButtonStyling;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * controller for events view
 */
public class EventsController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    /**
     * FXML data
     */
    @FXML
    Button previousEventButton;
    @FXML
    Button nextEventButton;
    @FXML
    Button searchForEventsButton;
    @FXML
    Button deleteEventButton;
    @FXML
    Button subscriptionToEventButton;
    @FXML
    Button eventNotificationsControlButton;
    @FXML
    TextField eventsSearchBar;
    @FXML
    AnchorPane eventsViewBottomAnchorPane;
    @FXML
    AnchorPane eventsViewTopAnchorPane;
    @FXML
    StackPane eventImageStackPane;
    @FXML
    Pane eventDetailsPane;
    @FXML
    Label noEventsLabel;
    @FXML
    ImageView eventImageView;
    @FXML
    Label eventTitleLabel;
    @FXML
    Label eventHostedByLabel;
    @FXML
    Label eventDateLabel;
    @FXML
    Label eventPeopleAttendingLabel;
    @FXML
    TextArea eventDescriptionTextArea;

    /**
     * create event view controller
     */
    CreateEventController createEventWindowController;

    /**
     * create event scene
     */
    Scene createEventScene;

    /**
     * create event stage
     */
    Stage createEventStage;

    /**
     * button coloring class
     */
    ButtonStyling buttonStyling;

    /**
     * event DTO pages
     */
    Page<EventDTO> eventDTOPage;

    /**
     * events array list
     */
    ArrayList<EventDTO> events;

    /**
     * text changed listener for event search bar
     */
    ChangeListener<String> textChangedListener = (observable, oldValue, newValue) -> loadSearchedEvents();

    /**
     * current mode of the events view
     */
    String currentMode;

    /**
     * initiates the scene for event creation
     *
     * @throws IOException - if an IO error occurs
     */
    public void initCreateEventScene() throws IOException {
        URL createEventURL = getClass().getResource("/com/fatlinux/views/createEvent-view.fxml");
        FXMLLoader createEventWindowLoader = new FXMLLoader(createEventURL);
        Parent createEventWindowRoot = createEventWindowLoader.load();
        this.createEventWindowController = createEventWindowLoader.getController();
        this.createEventScene = new Scene(createEventWindowRoot);
    }

    /**
     * initializes create event window's elements
     */
    public void initCreateEventWindow() {
        this.createEventStage = new Stage();
        this.createEventWindowController.setService(this.service);
        this.createEventWindowController.setLoggedUser(this.loggedUser);
        this.createEventWindowController.setParentController(this);
        this.createEventWindowController.init();
        this.createEventStage.setScene(this.createEventScene);
        this.createEventStage.initStyle(StageStyle.UNDECORATED);
        this.createEventStage.centerOnScreen();
    }

    /**
     * opens the create event window
     */
    public void openCreateEventWindow() {
        this.createEventStage.show();
    }

    @FXML
    public void initialize() throws IOException {
        buttonStyling = new ButtonStyling();
        initCreateEventScene();
    }

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * initializes components for events view
     */
    private void initComponents() {
        ImageView deleteEventIcon = new ImageView("com/fatlinux/images/deleteEventIcon.png");

        initCreateEventWindow();
        eventsSearchBar.textProperty().removeListener(textChangedListener);
        buttonStyling.setButtonForSearchEvent(this.searchForEventsButton);
        buttonStyling.setButtonBlackWithLighterHover(this.deleteEventButton);
        this.deleteEventButton.setGraphic(deleteEventIcon);
        buttonStyling.setButtonOrange(this.subscriptionToEventButton);
        this.subscriptionToEventButton.setVisible(false);
        this.deleteEventButton.setVisible(false);
        this.searchForEventsButton.setOnAction(event -> searchForEvents());
        ImageView nextDoubleArrow = new ImageView("com/fatlinux/images/nextDoubleArrow.png");
        ImageView previousDoubleArrow = new ImageView("com/fatlinux/images/previousDoubleArrow.png");
        nextDoubleArrow.setFitHeight(20);
        nextDoubleArrow.setFitWidth(20);
        previousDoubleArrow.setFitHeight(20);
        previousDoubleArrow.setFitWidth(20);
        this.nextEventButton.setGraphic(nextDoubleArrow);
        this.previousEventButton.setGraphic(previousDoubleArrow);
        this.eventsViewBottomAnchorPane.setStyle("-fx-border-color: black; -fx-border-width: 1px 0px 0px 0px");
        this.eventsViewTopAnchorPane.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
        this.eventImageStackPane.setStyle("-fx-border-color: black; -fx-border-width: 8px 8px 8px 8px; -fx-border-radius: 10; -fx-background-radius: 10");
        this.eventDetailsPane.setStyle("-fx-border-color: black; -fx-border-width: 8px 8px 8px 0px; -fx-border-radius: 10; -fx-background-color: #424043; -fx-background-radius: 10");
        this.nextEventButton.setVisible(false);
        this.previousEventButton.setVisible(false);
        this.eventNotificationsControlButton.setVisible(false);
    }

    /**
     * gets the eventDTO page for a pageable
     *
     * @param pageable - said pageable
     * @return said page
     */
    private Page<EventDTO> getEventDTOPage(Pageable pageable) {
        if (currentMode.equals("normal"))
            return this.service.getUserEventsChronoDesc(this.loggedUser.getEmail(), pageable);
        return this.service.getEventsFiltered(eventsSearchBar.getText(), pageable);
    }

    /**
     * verifies if current page has next page
     *
     * @return true if it has next page, false otherwise
     */
    private boolean pageHasNext() {
        return getEventDTOPage(eventDTOPage.nextPageable()).getContent().toList().size() != 0;
    }

    /**
     * verifies if current page has previous page
     *
     * @return true if it has next page, false otherwise
     */
    private boolean pageHasPrevious() {
        return eventDTOPage.getPageable().getPageNumber() != 1;
    }

    /**
     * loads events
     */
    private void loadEventPage() {
        loadEventPage(new PageableImplementation(1, 1));
    }

    /**
     * loads events
     */
    private void loadEventPage(Pageable pageable) {
        this.eventDTOPage = getEventDTOPage(pageable);
        this.events = new ArrayList<>(eventDTOPage.getContent().toList());
    }

    /**
     * shows the no events label
     */
    private void showNoEventsLabel() {
        this.eventDetailsPane.setVisible(false);
        this.eventImageStackPane.setVisible(false);
        this.eventImageView.setVisible(false);
        this.previousEventButton.setVisible(false);
        this.nextEventButton.setVisible(false);
        this.eventNotificationsControlButton.setVisible(false);
        this.noEventsLabel.setVisible(true);
    }

    /**
     * sets the notification button for events
     */
    public void setEventsNotificationsButton() {
        if (this.events.get(0).getAttendees().contains(new Pair<>(this.loggedUser.getEmail(), true))) {
            this.events.get(0).getAttendees().remove(new Pair<>(this.loggedUser.getEmail(), true));
            this.events.get(0).getAttendees().add(new Pair<>(this.loggedUser.getEmail(), false));
            service.unrequestNotificationsFromEvent(this.events.get(0).getId(), this.loggedUser.getEmail());
            buttonStyling.setButtonToEnableNotifications(this.eventNotificationsControlButton);
        } else if (this.events.get(0).getAttendees().contains(new Pair<>(this.loggedUser.getEmail(), false))) {
            this.events.get(0).getAttendees().remove(new Pair<>(this.loggedUser.getEmail(), false));
            this.events.get(0).getAttendees().add(new Pair<>(this.loggedUser.getEmail(), true));
            service.requestNotificationsFromEvent(this.events.get(0).getId(), this.loggedUser.getEmail());
            buttonStyling.setButtonToDisableNotifications(this.eventNotificationsControlButton);
        }
    }

    /**
     * shows the event details components
     */
    public void showEvents() {
        if (this.events.size() != 0) {
            fillEventDetails();
            if (this.events.get(0).getAttendees().contains(new Pair<>(this.loggedUser.getEmail(), true)) ||
                    this.events.get(0).getAttendees().contains(new Pair<>(this.loggedUser.getEmail(), false))) {
                this.subscriptionToEventButton.setText("✘ Unsubscribe");

                if (this.events.get(0).getAttendees().contains(new Pair<>(this.loggedUser.getEmail(), true)))
                    buttonStyling.setButtonToDisableNotifications(this.eventNotificationsControlButton);
                else
                    buttonStyling.setButtonToEnableNotifications(this.eventNotificationsControlButton);

                this.eventNotificationsControlButton.setVisible(true);
            } else {
                this.subscriptionToEventButton.setText("✔ Subscribe");
                this.eventNotificationsControlButton.setVisible(false);
            }
            this.eventDetailsPane.setVisible(true);
            this.eventImageStackPane.setVisible(true);
            this.eventImageView.setVisible(true);
            this.noEventsLabel.setVisible(false);
            this.subscriptionToEventButton.setVisible(true);
            this.nextEventButton.setVisible(pageHasNext());
            this.previousEventButton.setVisible(pageHasPrevious());
            this.deleteEventButton.setVisible(this.events.get(0).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
        } else {
            showNoEventsLabel();
        }
    }

    /**
     * fills the details for events
     */
    private void fillEventDetails() {
        int counter = 0;
        this.eventTitleLabel.setText(events.get(counter).getTitle());
        this.eventHostedByLabel.setText(events.get(counter).getHostUser().getFirstName() + " " + events.get(counter).getHostUser().getLastName());
        this.eventDateLabel.setText(events.get(counter).getDate().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy")));
        this.eventPeopleAttendingLabel.setText(String.valueOf(events.get(counter).getAttendees().size()));
        this.eventDescriptionTextArea.setText(events.get(counter).getDescription());
    }

    /**
     * initiates the events view
     */
    public void init() {
        currentMode = "normal";
        initComponents();
        loadEventPage();
        showEvents();
    }

    /**
     * shows the previous event from the list
     */
    public void showPreviousEvent() {
        loadEventPage(eventDTOPage.previousPageable());
        showEvents();
    }

    /**
     * shows the next event from the list
     */
    public void showNextEvent() {
        loadEventPage(eventDTOPage.nextPageable());
        showEvents();
    }

    /**
     * opens the window for creating a new event
     */
    public void createNewEvent() {
        openCreateEventWindow();
    }

    /**
     * deletes the currently viewed event
     */
    public void deleteEvent() {
        service.deleteEvent(this.events.get(0).getId());
        loadEventPage(eventDTOPage.getPageable());
        if (events.size() == 0) {
            if (eventDTOPage.getPageable().getPageNumber() != 1)
                loadEventPage(eventDTOPage.previousPageable());
        }
        showEvents();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Event deleted!");
        alert.setContentText("Your event has been successfully deleted!");
        alert.showAndWait();
    }

    /**
     * subscribes/unsubscribes the currently logged user to the currently viewed event
     */
    public void setEventSubscription() {
        if (this.subscriptionToEventButton.getText().equals("✔ Subscribe")) {
            this.service.subscribeToEvent(this.events.get(0).getId(), this.loggedUser.getEmail());
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
            this.events.get(0).getAttendees().add(new Pair<>(loggedUser.getEmail(), true));
            buttonStyling.setButtonToDisableNotifications(this.eventNotificationsControlButton);
            this.eventNotificationsControlButton.setVisible(true);
            Notifications.create()
                    .title("Subscribed!")
                    .text("You are now subscribed to the following event: " + this.events.get(0).getTitle())
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
        } else if (this.subscriptionToEventButton.getText().equals("✘ Unsubscribe")) {
            this.service.unsubscribeFromEvent(this.events.get(0).getId(), this.loggedUser.getEmail());
            this.events.get(0).getAttendees().remove(new Pair<>(loggedUser.getEmail(), true));
            this.events.get(0).getAttendees().remove(new Pair<>(loggedUser.getEmail(), false));
            this.eventNotificationsControlButton.setVisible(false);
            Notifications.create()
                    .title("Unsubscribed!")
                    .text("You are now unsubscribed to the following event: " + this.events.get(0).getTitle())
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
            if (currentMode.equals("normal")) {
                loadEventPage(eventDTOPage.getPageable());
                if (events.size() == 0) {
                    if (eventDTOPage.getPageable().getPageNumber() != 1)
                        loadEventPage(eventDTOPage.previousPageable());
                }
                showEvents();
            } else
                this.subscriptionToEventButton.setText("✔ Subscribe");
        }
    }

    /**
     * loads found events from searching
     */
    public void loadSearchedEvents() {
        if (this.eventsSearchBar.getText().equals("")) {
            showNoEventsLabel();
        } else {
            loadEventPage();
            showEvents();
        }
    }

    /**
     * enables searching with text changed from search bar
     */
    public void searchForEvents() {
        currentMode = "search";
        buttonStyling.setButtonForCancelSearchEvent(this.searchForEventsButton);
        eventsSearchBar.textProperty().addListener(textChangedListener);
        searchForEventsButton.setOnAction(event -> {
            eventsSearchBar.textProperty().removeListener(textChangedListener);
            eventsSearchBar.clear();
            init();
        });
        if (this.eventsSearchBar.getText().equals(""))
            showNoEventsLabel();
        else {
            loadEventPage();
            showEvents();
        }
    }
}
