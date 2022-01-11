package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.EventDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
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
import java.util.stream.Stream;

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
    ButtonColoring buttonColoring;

    /**
     * event DTO pages
     */
    Page<EventDTO> eventDTOPage;
    Page<EventDTO> previousPage;
    Page<EventDTO> nextPage;

    /**
     * events array list
     */
    ArrayList<EventDTO> events;
    ArrayList<EventDTO> previousEvents;
    ArrayList<EventDTO> nextEvents;

    /**
     * text changed listener for event search bar
     */
    ChangeListener<String> textChangedListener = (observable, oldValue, newValue) -> loadSearchedEvents();

    /**
     * initiates the scene for event creation
     *
     * @throws IOException - if an IO error occurs
     */
    public void initCreateEventScene() throws IOException {
        URL createEventURL = getClass().getResource("/com/map_toysocialnetworkgui/views/createEvent-view.fxml");
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
        buttonColoring = new ButtonColoring();
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
        ImageView deleteEventIcon = new ImageView("com/map_toysocialnetworkgui/images/deleteEventIcon.png");

        initCreateEventWindow();
        eventsSearchBar.textProperty().removeListener(textChangedListener);
        eventsSearchBar.clear();
        buttonColoring.setButtonForSearchEvent(this.searchForEventsButton);
        buttonColoring.setButtonBlackWithLighterHover(this.deleteEventButton);
        this.deleteEventButton.setGraphic(deleteEventIcon);
        buttonColoring.setButtonOrange(this.subscriptionToEventButton);
        this.subscriptionToEventButton.setVisible(false);
        this.deleteEventButton.setVisible(false);
        this.searchForEventsButton.setOnAction(event -> searchForEvents());
        ImageView nextDoubleArrow = new ImageView("com/map_toysocialnetworkgui/images/nextDoubleArrow.png");
        ImageView previousDoubleArrow = new ImageView("com/map_toysocialnetworkgui/images/previousDoubleArrow.png");
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
    }

    /**
     * loads events
     */
    private void eventsLoaded() {
        this.eventDTOPage = this.service.getUserEventsChronoDesc(this.loggedUser.getEmail(), new PageableImplementation(1, 1));
        this.nextPage = this.service.getUserEventsChronoDesc(this.loggedUser.getEmail(), new PageableImplementation(2, 1));
        this.previousEvents = new ArrayList<>();
        this.events = new ArrayList<>(eventDTOPage.getContent().toList());
        this.nextEvents = new ArrayList<>(nextPage.getContent().toList());
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
        this.noEventsLabel.setVisible(true);
    }

    /**
     * shows the event details components
     */
    private void showEvents() {
        this.eventDetailsPane.setVisible(true);
        this.eventImageStackPane.setVisible(true);
        this.eventImageView.setVisible(true);
        this.previousEventButton.setVisible(false);
        this.nextEventButton.setVisible(false);
        this.noEventsLabel.setVisible(false);
        this.subscriptionToEventButton.setVisible(true);
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
        initComponents();
        eventsLoaded();
        showEvents();
        if (this.nextEvents.size() != 0)
            this.nextEventButton.setVisible(true);
        if (this.events.size() != 0) {
            fillEventDetails();
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
            this.deleteEventButton.setVisible(this.events.get(0).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
        } else
            showNoEventsLabel();
    }

    /**
     * shows the navigation buttons
     */
    private void setUpEventDetails() {
        this.nextEventButton.setVisible(true);
        this.previousEventButton.setVisible(true);
        fillEventDetails();
        if (this.events.get(0).getAttendees().contains(this.loggedUser.getEmail()))
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
        else
            this.subscriptionToEventButton.setText("✔ Subscribe");
        this.deleteEventButton.setVisible(this.events.get(0).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
    }

    /**
     * shows the previous event from the list
     */
    public void showPreviousEvent() {
        this.nextPage = this.eventDTOPage;
        this.eventDTOPage = this.previousPage;
        if (this.previousPage.getPageable().getPageNumber() != 1)
            this.previousPage = this.service.getUserEventsChronoDesc(loggedUser.getEmail(), this.previousPage.previousPageable());
        else
            this.previousEvents = new ArrayList<>();
        this.events = new ArrayList<>(this.eventDTOPage.getContent().toList());
        this.nextEvents = new ArrayList<>(this.nextPage.getContent().toList());

        setUpEventDetails();
        if (this.previousEvents.size() == 0)
            this.previousEventButton.setVisible(false);

        this.nextEventButton.setVisible(true);
    }

    /**
     * shows the next event from the list
     */
    public void showNextEvent() {
        this.previousPage = this.eventDTOPage;
        this.eventDTOPage = this.nextPage;
        this.nextPage = this.service.getUserEventsChronoDesc(loggedUser.getEmail(), this.nextPage.nextPageable());
        this.events = new ArrayList<>(this.eventDTOPage.getContent().toList());
        this.nextEvents = new ArrayList<>(this.nextPage.getContent().toList());
        this.previousEvents = new ArrayList<>(this.previousPage.getContent().toList());

        setUpEventDetails();
        if (this.nextEvents.size() == 0)
            this.nextEventButton.setVisible(false);

        this.previousEventButton.setVisible(true);
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
        init();
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
            Notifications.create()
                    .title("Subscribed!")
                    .text("You are now subscribed to the following event: " + this.events.get(0).getTitle())
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
        } else if (this.subscriptionToEventButton.getText().equals("✘ Unsubscribe")) {
            this.service.unsubscribeFromEvent(this.events.get(0).getId(), this.loggedUser.getEmail());
            this.subscriptionToEventButton.setText("✔ Subscribe");
            Notifications.create()
                    .title("Unsubscribed!")
                    .text("You are now unsubscribed to the following event: " + this.events.get(0).getTitle())
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
        }
    }

    /**
     * loads found events from searching
     */
    public void loadSearchedEvents() {
        if (this.eventsSearchBar.getText().equals("")) {
            showNoEventsLabel();
        } else {
            eventDTOPage = this.service.getEventsFiltered(this.eventsSearchBar.getText(), new PageableImplementation(1, 7));
            if (eventDTOPage.getContent() == null) {
                showNoEventsLabel();
            } else {
                Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
                this.events = new ArrayList<>(eventDTOStream.toList());
                if (this.events.size() == 0)
                    showNoEventsLabel();
                else {
                    showEvents();
                    if (this.events.size() > 1)
                        this.nextEventButton.setVisible(true);
                    fillEventDetails();
                    if (this.events.get(0).getAttendees().contains(this.loggedUser.getEmail()))
                        this.subscriptionToEventButton.setText("✘ Unsubscribe");
                    else
                        this.subscriptionToEventButton.setText("✔ Subscribe");
                    this.deleteEventButton.setVisible(this.events.get(0).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
                }
            }
        }
    }

    /**
     * enables searching with text changed from search bar
     */
    public void searchForEvents() {
        loadSearchedEvents();
        buttonColoring.setButtonForCancelSearchEvent(this.searchForEventsButton);
        eventsSearchBar.textProperty().addListener(textChangedListener);

        searchForEventsButton.setOnAction(event -> {
            eventsSearchBar.textProperty().removeListener(textChangedListener);
            eventsSearchBar.clear();
            init();
        });
    }
}
