package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.EventDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void initComponents() {
        buttonColoring = new ButtonColoring();
        buttonColoring.setButtonForSearchEvent(this.searchForEventsButton);
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

    private void showNoEventsLabel() {
        this.eventDetailsPane.setVisible(false);
        this.eventImageStackPane.setVisible(false);
        this.eventImageView.setVisible(false);
        this.previousEventButton.setVisible(false);
        this.nextEventButton.setVisible(false);
        this.noEventsLabel.setVisible(true);
    }

    private void showEvents() {
        this.eventDetailsPane.setVisible(true);
        this.eventImageStackPane.setVisible(true);
        this.eventImageView.setVisible(true);
        this.previousEventButton.setVisible(true);
        this.nextEventButton.setVisible(false);
        this.noEventsLabel.setVisible(false);
    }

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
        if(eventsLoaded()) {
            showEvents();
            this.previousEventButton.setVisible(false);
            if (this.events.size() > 1)
                this.nextEventButton.setVisible(true);
            fillEventDetails();
        } else
            showNoEventsLabel();
    }

    public void showPreviousEvent() {
        this.counter--;
        this.nextEventButton.setVisible(true);
        this.previousEventButton.setVisible(true);
        fillEventDetails();
        if (counter == 0) {
            this.nextEventButton.setVisible(true);
            this.previousEventButton.setVisible(false);
        }
    }

    public void showNextEvent() {
        this.counter++;
        fillEventDetails();
        this.nextEventButton.setVisible(true);
        this.previousEventButton.setVisible(true);
        if (counter == events.size() - 1) {
            this.nextEventButton.setVisible(false);
            this.previousEventButton.setVisible(true);
        }
    }

    public void createNewEvent() {

    }

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
        }
    }

    public void searchForEvents() {
        buttonColoring.setButtonForCancelSearchEvent(this.searchForEventsButton);
        loadSearchedEvents();

        ChangeListener<String> textChangedListener = (observable, oldValue, newValue) -> loadSearchedEvents();
        eventsSearchBar.textProperty().addListener(textChangedListener);

        searchForEventsButton.setOnAction(event -> {
            eventsSearchBar.textProperty().removeListener(textChangedListener);
            eventsSearchBar.clear();
            init();
        });
    }
}