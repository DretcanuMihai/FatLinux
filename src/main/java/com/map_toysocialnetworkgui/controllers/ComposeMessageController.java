package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ComposeMessageController extends AbstractControllerWithTitleBar {
    // Data
    UserUIDTO loggedUser;
    // FXML
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

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void init() {
        fromTextField.setPromptText(loggedUser.getEmail());
    }

    public void close() {
        ((Stage) closeComposeMessageWindowButton.getScene().getWindow()).close();
    }

    public void sendMessage() {
        try {
            String fromEmail = fromTextField.getPromptText();
            List<String> toEmails = Arrays.stream(toTextField.getText().split(", ")).toList();
            String messageSubject = subjectTextField.getText();
            String messageText = messageTextArea.getText();

            Message message = new Message(null, fromEmail, toEmails, messageText, messageSubject, LocalDateTime.now(), null);
            MessageDTO messageDTO = new MessageDTO(message);
            this.service.sendRootMessage(messageDTO);

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
