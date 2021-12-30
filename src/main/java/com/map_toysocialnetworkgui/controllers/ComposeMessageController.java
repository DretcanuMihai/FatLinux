package com.map_toysocialnetworkgui.controllers;

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

import java.util.ArrayList;
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
     * primary function of the window
     */
    String primaryFunction;

    /**
     * selected message for reply or reply all
     */
    MessageDTO selectedMessage;

    /**
     * FXML data
     */
    @FXML
    Button sendMessageButton;
    @FXML
    Button sendReplyMessageButton;
    @FXML
    Button sendReplyAllMessageButton;
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
     * sets the primary function of the window
     *
     * @param primaryFunction - said primary function
     */
    public void setPrimaryFunction(String primaryFunction) {
        this.primaryFunction = primaryFunction;
    }

    /**
     * sets the selected message for reply or reply all
     *
     * @param selectedMessage - said selectedMessage
     */
    public void setSelectedMessage(MessageDTO selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    /**
     * sets the sender as the currently logged-in user
     */
    public void init() {
        switch (this.primaryFunction) {
            case "composeNew" -> {
                this.sendMessageButton.setVisible(true);
                this.sendReplyMessageButton.setVisible(false);
                this.sendReplyAllMessageButton.setVisible(false);
                fromTextField.setEditable(false);
                toTextField.setEditable(true);
                subjectTextField.setEditable(true);
                fromTextField.setPromptText(loggedUser.getEmail());
            }
            case "reply" -> {
                this.sendMessageButton.setVisible(false);
                this.sendReplyMessageButton.setVisible(true);
                this.sendReplyAllMessageButton.setVisible(false);
                fromTextField.setEditable(false);
                toTextField.setEditable(false);
                subjectTextField.setEditable(true);
                fromTextField.setPromptText(loggedUser.getEmail());
                toTextField.setPromptText(selectedMessage.getFromEmail());
                subjectTextField.setText("Re:" + selectedMessage.getMessageSubject());
            }
            case "replyAll" -> {
                this.sendMessageButton.setVisible(false);
                this.sendReplyMessageButton.setVisible(false);
                this.sendReplyAllMessageButton.setVisible(true);
                fromTextField.setEditable(false);
                toTextField.setEditable(false);
                subjectTextField.setEditable(true);
                fromTextField.setPromptText(loggedUser.getEmail());

                List<String> receivers = selectedMessage.getToEmails();
                receivers.add(selectedMessage.getFromEmail());
                receivers.remove(loggedUser.getEmail());

                String sendTo = receivers.toString().replaceAll("\\[", "");
                sendTo = sendTo.replaceAll("]", ", ");
                sendTo = sendTo.substring(0, sendTo.length() - 2);
                toTextField.setPromptText(sendTo);
                subjectTextField.setText("Re:" + selectedMessage.getMessageSubject());
            }
        }
    }

    /**
     * closes the window
     */
    public void close() {
        ((Stage) closeComposeMessageWindowButton.getScene().getWindow()).close();
    }

    /**
     * sends a new message
     * raises a warning window if there are any exceptions
     * raises an information window if the message has been successfully sent
     */
    public void sendNewMessage() {
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
            alert.setHeaderText("Compose new message warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * sends a reply message to an existing message
     * raises a warning window if there are any exceptions
     * raises an information window if the message has been successfully sent
     */
    public void sendReplyMessage() {
        try {
            String fromEmail = fromTextField.getPromptText();
            String messageSubject = subjectTextField.getText();
            String messageText = messageTextArea.getText();

            this.service.sendReplyMessage(fromEmail, messageText, messageSubject, selectedMessage.getId());
            this.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Message sent!");
            alert.setContentText("Your message has been successfully sent!");
            alert.showAndWait();
        } catch (ValidationException | AdministrationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Compose reply message warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void sendReplyAllMessage() {
        try {
            String fromEmail = fromTextField.getPromptText();
            String messageSubject = subjectTextField.getText();
            String messageText = messageTextArea.getText();

            this.service.sendReplyAllMessage(fromEmail, messageText, messageSubject, selectedMessage.getId());
            this.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Message sent!");
            alert.setContentText("Your message has been successfully sent!");
            alert.showAndWait();
        } catch (ValidationException | AdministrationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Compose reply message warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
}
