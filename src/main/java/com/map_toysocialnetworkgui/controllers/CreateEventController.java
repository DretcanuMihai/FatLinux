package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateEventController extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

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

    /**
     * initializes create event window elements
     */
    public void init() {
        this.hostTextField.setPromptText(this.loggedUser.getFirstName() + " " + this.loggedUser.getLastName());
    }

    public void createNewEvent() {
        String eventTitle = this.titleTextField.getText();
        LocalDateTime eventDate = this.dateDatePicker.getValue().atTime(LocalTime.now());
        String eventDescription = this.descriptionTextArea.getText();

        this.service.createEvent(eventTitle, eventDescription, this.loggedUser.getEmail(), eventDate);
        this.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Event created!");
        alert.setContentText("Your event has been successfully created!");
        alert.showAndWait();
    }

    /**
     * closes the window
     */
    public void close() {
        ((Stage) this.closeCreateEventWindowButton.getScene().getWindow()).close();
    }
}
