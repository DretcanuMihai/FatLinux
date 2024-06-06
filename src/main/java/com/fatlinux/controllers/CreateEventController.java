package com.fatlinux.controllers;

import com.fatlinux.model.entities_dto.UserDTO;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * controller for create event view
 */
public class CreateEventController extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;
    EventsController parentController;

    /**
     * FXML data
     */
    @FXML
    Button closeCreateEventWindowButton;
    @FXML
    Button createEventWindowButton;
    @FXML
    TextField hostTextField;
    @FXML
    TextField titleTextField;
    @FXML
    DatePicker dateDatePicker;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    BorderPane createEventBorderPane;

    /**
     * makes the exit button able to only close the associated window
     */
    @Override
    public void initAppExitButton() {
        this.setAppExitButtonToOnlyCloseWindow();
    }

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setParentController(EventsController parentController) {
        this.parentController = parentController;
    }

    /**
     * initializes create event window elements
     */
    public void init() {
        createEventBorderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        this.hostTextField.setPromptText(this.loggedUser.getFirstName() + " " + this.loggedUser.getLastName());
    }

    /**
     * creates a new event with the details entered in the corresponding text fields
     */
    public void createNewEvent() {
        String eventTitle = this.titleTextField.getText();

        if (this.dateDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Create event warning!");
            alert.setContentText("Event date cannot be null!");
            alert.showAndWait();
        } else {
            LocalDateTime eventDate = this.dateDatePicker.getValue().atTime(LocalTime.now());
            String eventDescription = this.descriptionTextArea.getText();

            try {
                this.service.createEvent(eventTitle, eventDescription, this.loggedUser.getEmail(), eventDate);
                this.close();
                this.reset();
                this.parentController.init();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Event created!");
                alert.setContentText("Your event has been successfully created!");
                alert.showAndWait();

            } catch (ValidationException | AdministrationException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Create event warning!");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @Override
    public void reset() {
        this.hostTextField.setPromptText(this.loggedUser.getFirstName() + " " + this.loggedUser.getLastName());
        this.titleTextField.clear();
        this.dateDatePicker.getEditor().clear();
        this.descriptionTextArea.clear();
    }

    /**
     * closes the window
     */
    public void close() {
        ((Stage) this.closeCreateEventWindowButton.getScene().getWindow()).close();
    }
}
