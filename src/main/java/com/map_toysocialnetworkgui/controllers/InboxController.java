package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Collection;

public class InboxController extends AbstractController {
    // Data
    UserUIDTO loggedUser;
    // List
    ObservableList<String> modelReceivedMessages = FXCollections.observableArrayList();
    ObservableList<String> modelSentMessages = FXCollections.observableArrayList();
    // FXML
    @FXML
    ListView<String> receivedMessagesList;
    @FXML
    ListView<String> sentMessagesList;
    @FXML
    Button replyButton;
    @FXML
    Button replyAllButton;
    @FXML
    TextField fromTextField;
    @FXML
    TextField toTextField;
    @FXML
    TextField subjectTextField;
    @FXML
    TextArea messageTextArea;

    public void setCustomCell(ListView<String> list) {
        list.setCellFactory(lst -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setPrefHeight(45.0);
                    setFont(new Font(15));
                    setText(item);
                }
            }
        });
    }

    @FXML
    public void initialize() {
        setCustomCell(receivedMessagesList);
        setCustomCell(sentMessagesList);
        receivedMessagesList.setItems(modelReceivedMessages);
        sentMessagesList.setItems(modelSentMessages);
    }

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void clearAllFields() {
        fromTextField.clear();
        toTextField.clear();
        subjectTextField.clear();
        messageTextArea.clear();
    }

    public void updateModelReceivedMessages() {
        Collection<MessageDTO> receivedMessages = service.getConversation(loggedUser.getEmail(), "test@yahoo.com");
        Collection<String> receivedMessagesSubjects = receivedMessages.stream()
                .map(MessageDTO::getMessageSubject).toList();

        receivedMessagesList.setOnMouseClicked(event -> {
            for (MessageDTO receivedMessage : receivedMessages) {
                if (receivedMessagesList.getSelectionModel().getSelectedItem() == null) {
                    clearAllFields();
                } else if (receivedMessagesList.getSelectionModel().getSelectedItem().equals(receivedMessage.getMessageSubject())) {
                    fromTextField.setText(receivedMessage.getFromEmail());
                    toTextField.clear();
                    for (String receivers : receivedMessage.getToEmails()) {
                        toTextField.appendText(receivers);
                        toTextField.appendText(", ");
                    }
                    toTextField.deleteText(toTextField.getText().length() - 2, toTextField.getText().length());
                    subjectTextField.setText(receivedMessage.getMessageSubject());
                    messageTextArea.setText(receivedMessage.getMessageText());
                    replyButton.setVisible(true);
                    replyAllButton.setVisible(true);
                    return;
                }
            }
        });
        modelReceivedMessages.setAll(receivedMessagesSubjects);
    }

    public void updateModelSentMessages() {
        // TODO
    }

    public void viewReceivedMessages() {
        this.receivedMessagesList.setVisible(true);
        this.sentMessagesList.setVisible(false);
        clearAllFields();
        replyButton.setVisible(false);
        replyAllButton.setVisible(false);
    }

    public void viewSentMessages() {
        this.receivedMessagesList.setVisible(false);
        this.sentMessagesList.setVisible(true);
        clearAllFields();
        replyButton.setVisible(false);
        replyAllButton.setVisible(false);
    }

    public void openComposeMessageWindow() throws IOException {
        FXMLLoader composeMessageWindowLoader = new FXMLLoader(getClass()
                .getResource("/com/map_toysocialnetworkgui/views/composeMessage-view.fxml"));
        composeMessageWindowLoader.load();
        ComposeMessageController composeMessageController = composeMessageWindowLoader.getController();
        composeMessageController.setService(this.service);
        composeMessageController.setLoggedUser(this.loggedUser);
        composeMessageController.init();

        Parent composeMessageWindowParent = composeMessageWindowLoader.getRoot();
        Scene scene = new Scene(composeMessageWindowParent);
        Stage composeMessageStage = new Stage();
        composeMessageStage.setScene(scene);
        composeMessageStage.initStyle(StageStyle.UNDECORATED);
        composeMessageStage.centerOnScreen();
        composeMessageStage.show();
    }

    public void init() {
        updateModelReceivedMessages();
        updateModelSentMessages();
        this.receivedMessagesList.setVisible(true);
        this.sentMessagesList.setVisible(false);
        clearAllFields();
        replyButton.setVisible(false);
        replyAllButton.setVisible(false);
    }
}
