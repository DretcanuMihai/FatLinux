package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * controller for compose message view
 */
public class ComposeMessageController extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

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
    Button closeComposeMessageWindowButton;
    @FXML
    TextField fromTextField;
    @FXML
    TextField toTextField;
    @FXML
    TextField subjectTextField;
    @FXML
    TextArea messageTextArea;
    @FXML
    BorderPane composeMessageBorderPane;

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

    @Override
    public void reset() {
        super.reset();
        this.sendMessageButton.setText("");
        this.fromTextField.setPromptText("");
        this.toTextField.clear();
        this.toTextField.setPromptText("");
        this.subjectTextField.clear();
        this.subjectTextField.setPromptText("");
        this.messageTextArea.clear();
    }

    /**
     * initiates compose message window based on its main functionality
     */
    public void init() {
        composeMessageBorderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        messageTextArea.setWrapText(true);
        switch (this.primaryFunction) {
            case "Compose New" -> {
                reset();
                this.sendMessageButton.setText("Send new message");
                this.fromTextField.setEditable(false);
                this.toTextField.setEditable(true);
                this.subjectTextField.setEditable(true);
                this.fromTextField.setPromptText(this.loggedUser.getEmail() + " (me)");
            }
            case "Reply" -> {
                reset();
                this.sendMessageButton.setText("Send reply");
                this.fromTextField.setEditable(false);
                this.toTextField.setEditable(false);
                this.subjectTextField.setEditable(false);
                this.fromTextField.setPromptText(this.loggedUser.getEmail() + " (me)");
                this.toTextField.setPromptText(this.selectedMessage.getFromEmail());
                if (this.selectedMessage.getMessageSubject().contains("Re: "))
                    this.subjectTextField.setPromptText(this.selectedMessage.getMessageSubject());
                else
                    this.subjectTextField.setPromptText("Re: " + this.selectedMessage.getMessageSubject());
            }
            case "Reply All" -> {
                reset();
                this.sendMessageButton.setText("Send reply to all");
                this.fromTextField.setEditable(false);
                this.toTextField.setEditable(false);
                this.subjectTextField.setEditable(false);
                this.fromTextField.setPromptText(this.loggedUser.getEmail() + " (me)");

                List<String> receivers = this.selectedMessage.getToEmails();
                String sendToString = receivers.toString().replaceAll("\\[", "");
                sendToString = sendToString.replaceAll("]", ", ");
                sendToString = sendToString.substring(0, sendToString.length() - 2);
                String sendTo = sendToString.replace(this.loggedUser.getEmail(), this.selectedMessage.getFromEmail());
                this.toTextField.setPromptText(sendTo);
                if (this.selectedMessage.getMessageSubject().contains("Re: "))
                    this.subjectTextField.setPromptText(this.selectedMessage.getMessageSubject());
                else
                    this.subjectTextField.setPromptText("Re: " + this.selectedMessage.getMessageSubject());
            }
        }
    }

    /**
     * closes the window
     */
    public void close() {
        ((Stage) this.closeComposeMessageWindowButton.getScene().getWindow()).close();
    }

    /**
     * sends a new message, a reply message to an existing message
     * or a reply message to all the receivers of the message (including the sender)
     * raises a warning window if there are any exceptions
     * raises an information window if the message has been successfully sent
     */
    public void sendMessage() {
        try {
            String fromEmail = fromTextField.getPromptText().replace(" (me)", "");
            List<String> toEmails = Arrays.stream(toTextField.getText().split(", ")).toList();
            String messageSubject = subjectTextField.getText();
            String messageSubjectForReply = subjectTextField.getPromptText();
            String messageText = messageTextArea.getText();

            if (sendMessageButton.getText().equals("Send new message")) {
                this.service.sendRootMessage(fromEmail, toEmails, messageText, messageSubject);
                this.close();
            } else if (sendMessageButton.getText().equals("Send reply")) {
                this.service.sendReplyMessage(fromEmail, messageText, messageSubjectForReply, selectedMessage.getId());
                this.close();
            } else if (sendMessageButton.getText().equals("Send reply to all")) {
                this.service.sendReplyAllMessage(fromEmail, messageText, messageSubjectForReply, selectedMessage.getId());
                this.close();
            }

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
