package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

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
    Button viewSentMessagesButton;
    @FXML
    Button viewReceivedMessagesButton;
    @FXML
    Button replyButton;
    @FXML
    Button replyAllButton;
    @FXML
    Button composeButton;
    @FXML
    Label fromLabel;
    @FXML
    Label toLabel;
    @FXML
    Label subjectLabel;
    @FXML
    TextArea messageTextArea;

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void updateModelReceivedMessages() {

    }

    public void updateModelSentMessages() {

    }

    public void viewReceivedMessages() {
        this.receivedMessagesList.setVisible(true);
        this.sentMessagesList.setVisible(false);
    }

    public void viewSentMessages() {
        this.receivedMessagesList.setVisible(false);
        this.sentMessagesList.setVisible(true);
    }

    public void init() {
        updateModelReceivedMessages();
        updateModelSentMessages();
        this.receivedMessagesList.setVisible(true);
        this.sentMessagesList.setVisible(false);
    }
}
