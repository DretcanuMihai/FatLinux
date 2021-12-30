package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * controller for compose message view
 */
public class ComposeMessageController extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserUIDTO loggedUser;

    /**
     * FXML data
     */
    @FXML
    Button closeComposeMessageWindowButton;
    @FXML
    TextField fromTextField;
    @FXML
    TextField toTextField;
    @FXML
    TextField subjectTextField;
    @FXML
    TextArea messageTextArea;

    /**
     * makes the exit button able to only close the associated window
     */
    @Override
    public void initAppExitButton() {
        Image exitButtonImage = appExitButton.getImage();
        appExitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) appExitButton.getScene().getWindow();
            stage.close();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
    }

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * sets the sender as the currently logged-in user
     */
    public void init() {
        fromTextField.setPromptText(loggedUser.getEmail());
    }

    /**
     * closes the window
     */
    public void close() {
        ((Stage) closeComposeMessageWindowButton.getScene().getWindow()).close();
    }

    /**
     * sends a message
     * raises a warning window if there are any exceptions
     * raises an information window if the message has been successfully sent
     */
    public void sendMessage() {
        try {
            String fromEmail = fromTextField.getPromptText();
            List<String> toEmails = Arrays.stream(toTextField.getText().split(", ")).toList();
            String messageSubject = subjectTextField.getText();
            String messageText = messageTextArea.getText();

            this.service.sendRootMessage(fromEmail, toEmails, messageText, messageSubject);
            this.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Message sent!");
            alert.setContentText("Your message has been successfully sent!");
            alert.showAndWait();
        } catch (ValidationException | AdministrationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Compose message warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
}
