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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
     * event DTO page
     */
    Page<EventDTO> eventDTOPage;

    /**
     * events array list
     */
    ArrayList<EventDTO> events;

    /**
     * counter for current event
     */
    Integer counter = 0;

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
        buttonColoring = new ButtonColoring();
        buttonColoring.setButtonForSearchEvent(this.searchForEventsButton);
        buttonColoring.setButtonBlackWithLighterHover(this.deleteEventButton);
        this.deleteEventButton.setGraphic(deleteEventIcon);
        buttonColoring.setButtonOrange(this.subscriptionToEventButton);
        this.subscriptionToEventButton.setVisible(true);
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
     *
     * @return - true if events were loaded
     * - false otherwise
     */
    private boolean eventsLoaded() {
        this.eventDTOPage = this.service.getUserEventsChronoDesc(this.loggedUser.getEmail(), new PageableImplementation(1, 7));
        if (eventDTOPage.getContent() == null) {
            return false;
        } else {
            Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
            this.events = new ArrayList<>(eventDTOStream.toList());
            this.counter = 0;
            return true;
        }
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
        this.previousEventButton.setVisible(true);
        this.nextEventButton.setVisible(false);
        this.noEventsLabel.setVisible(false);
    }

    /**
     * fills the details for events
     */
    private void fillEventDetails() {
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
        if (eventsLoaded()) {
            showEvents();
            this.previousEventButton.setVisible(false);
            if (this.events.size() > 1)
                this.nextEventButton.setVisible(true);
            fillEventDetails();
            this.deleteEventButton.setVisible(this.events.get(counter).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
            if (this.events.get(counter).getAttendees().contains(this.loggedUser.getEmail()))
                this.subscriptionToEventButton.setText("✘ Unsubscribe");
            else
                this.subscriptionToEventButton.setText("✔ Subscribe");

        } else
            showNoEventsLabel();
    }

    /**
     * shows the previous event from the list
     */
    public void showPreviousEvent() {
        this.counter--;
        this.nextEventButton.setVisible(true);
        this.previousEventButton.setVisible(true);
        fillEventDetails();
        if (this.events.get(counter).getAttendees().contains(this.loggedUser.getEmail()))
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
        else
            this.subscriptionToEventButton.setText("✔ Subscribe");
        this.deleteEventButton.setVisible(this.events.get(counter).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
        if (counter == 0) {
            this.nextEventButton.setVisible(true);
            this.previousEventButton.setVisible(false);
        }
    }

    /**
     * shows the next event from the list
     */
    public void showNextEvent() {
        this.counter++;
        fillEventDetails();
        if (this.events.get(counter).getAttendees().contains(this.loggedUser.getEmail()))
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
        else
            this.subscriptionToEventButton.setText("✔ Subscribe");
        this.deleteEventButton.setVisible(this.events.get(counter).getHostUser().getEmail().equals(this.loggedUser.getEmail()));
        this.nextEventButton.setVisible(true);
        this.previousEventButton.setVisible(true);
        if (counter == events.size() - 1) {
            this.nextEventButton.setVisible(false);
            this.previousEventButton.setVisible(true);
        }
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
        service.deleteEvent(this.events.get(counter).getId());
    }

    /**
     * subscribes/unsubscribes the currently logged user to the currently viewed event
     */
    public void setEventSubscription() {
        if (this.subscriptionToEventButton.getText().equals("✔ Subscribe")) {
            this.service.subscribeToEvent(this.events.get(counter).getId(), this.loggedUser.getEmail());
            this.subscriptionToEventButton.setText("✘ Unsubscribe");
            Notifications.create()
                    .title("Subscribed!")
                    .text("You are now subscribed to the following event: " + this.events.get(counter).getTitle())
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
        } else if (this.subscriptionToEventButton.getText().equals("✘ Unsubscribe")) {
            this.service.unsubscribeFromEvent(this.events.get(counter).getId(), this.loggedUser.getEmail());
            this.subscriptionToEventButton.setText("✔ Subscribe");
            Notifications.create()
                    .title("Unsubscribed!")
                    .text("You are now unsubscribed to the following event: " + this.events.get(counter).getTitle())
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
        eventDTOPage = this.service.getEventsFiltered(this.eventsSearchBar.getText(), new PageableImplementation(1, 7));
        if (eventDTOPage.getContent() == null) {
            showNoEventsLabel();
        } else {
            Stream<EventDTO> eventDTOStream = eventDTOPage.getContent();
            events = new ArrayList<>(eventDTOStream.toList());
            counter = 0;

            showEvents();
            this.previousEventButton.setVisible(false);
            if (this.events.size() > 1)
                this.nextEventButton.setVisible(true);
            fillEventDetails();
            if (this.events.get(counter).getAttendees().contains(this.loggedUser.getEmail()))
                this.subscriptionToEventButton.setText("✘ Unsubscribe");
            else
                this.subscriptionToEventButton.setText("✔ Subscribe");
        }
    }

    /**
     * enables searching with text changed from search bar
     */
    public void searchForEvents() {
        buttonColoring.setButtonForCancelSearchEvent(this.searchForEventsButton);
        loadSearchedEvents();
        this.deleteEventButton.setVisible(this.events.get(counter).getHostUser().getEmail().equals(this.loggedUser.getEmail()));

        ChangeListener<String> textChangedListener = (observable, oldValue, newValue) -> loadSearchedEvents();
        eventsSearchBar.textProperty().addListener(textChangedListener);

        searchForEventsButton.setOnAction(event -> {
            eventsSearchBar.textProperty().removeListener(textChangedListener);
            eventsSearchBar.clear();
            init();
        });
    }
}