package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
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
import java.net.URL;
import java.util.Collection;
import java.util.stream.StreamSupport;

/**
 * controller for inbox view
 */
public class InboxController extends AbstractController implements Observer<EntityModificationEvent<Integer>> {
    /**
     * currently logged-in user
     */
    UserUIDTO loggedUser;

    /**
     * observable lists for sent and received messages
     */
    ObservableList<MessageDTO> modelReceivedMessages = FXCollections.observableArrayList();
    ObservableList<MessageDTO> modelSentMessages = FXCollections.observableArrayList();

    /**
     * FXML data
     */
    @FXML
    ListView<MessageDTO> receivedMessagesList;
    @FXML
    ListView<MessageDTO> sentMessagesList;
    @FXML
    Button viewReceivedMessagesButton;
    @FXML
    Button viewSentMessagesButton;
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
    @FXML
    Label noMessagesLabel;

    /**
     * modifies a list view's cell height and font
     *
     * @param list - said list
     */
    public void setCustomCell(ListView<MessageDTO> list) {
        list.setCellFactory(lst -> new ListCell<>() {
            @Override
            protected void updateItem(MessageDTO messageDTO, boolean empty) {
                super.updateItem(messageDTO, empty);
                if (empty) {
                    setText(null);
                } else {
                    setPrefHeight(45.0);
                    setFont(new Font(15));
                    setText(messageDTO.getMessageSubject());
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

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * clears all text fields and text areas
     */
    private void clearAllFields() {
        fromTextField.clear();
        toTextField.clear();
        subjectTextField.clear();
        messageTextArea.clear();
    }

    public void fillDataForMessage(MessageDTO message) {
        fromTextField.setText(message.getFromEmail());
        toTextField.clear();
        for (String receivers : message.getToEmails()) {
            toTextField.appendText(receivers);
            toTextField.appendText(", ");
        }
        toTextField.deleteText(toTextField.getText().length() - 2, toTextField.getText().length());
        subjectTextField.setText(message.getMessageSubject());
        messageTextArea.setText(message.getMessageText());
    }

    /**
     * updates the observable list of received messages
     * selected message will display the contents of the message and show reply and reply all buttons
     */
    public void updateModelReceivedMessages() {
        Collection<MessageDTO> receivedMessages = StreamSupport
                .stream(service.getMessagesReceivedByUser(loggedUser.getEmail()).spliterator(), false).toList();

        receivedMessagesList.setOnMouseClicked(event -> {
            if (receivedMessagesList.getSelectionModel().getSelectedItem() == null) {
                clearAllFields();
            } else {
                fillDataForMessage(receivedMessagesList.getSelectionModel().getSelectedItem());
                replyButton.setVisible(true);
                replyAllButton.setVisible(true);
            }
        });
        modelReceivedMessages.setAll(receivedMessages);
    }

    /**
     * updates the observable list of sent messages
     */
    public void updateModelSentMessages() {
        Collection<MessageDTO> sentMessages = StreamSupport
                .stream(service.getMessagesSentByUser(loggedUser.getEmail()).spliterator(), false).toList();

        sentMessagesList.setOnMouseClicked(event -> {
            if (sentMessagesList.getSelectionModel().getSelectedItem() == null) {
                clearAllFields();
            } else {
                fillDataForMessage(sentMessagesList.getSelectionModel().getSelectedItem());
                replyButton.setVisible(false);
                replyAllButton.setVisible(false);
            }
        });
        modelSentMessages.setAll(sentMessages);
    }

    /**
     * colors a button in orange and adds hover effect
     *
     * @param button - said button
     */
    private void setButtonOrange(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #ff7700;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #F04A00;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #ff7700;
                """));
    }

    /**
     * colors a button in black and adds hover effect
     *
     * @param button - said button
     */
    private void setButtonBlack(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #424043;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """));
    }

    /**
     * hides the sent messages list and shows the received messages list
     */
    public void viewReceivedMessages() {
        setButtonOrange(viewReceivedMessagesButton);
        setButtonBlack(viewSentMessagesButton);
        if (receivedMessagesList.getItems().isEmpty()) {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(false);
            replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
            this.noMessagesLabel.setVisible(true);
        } else {
            this.receivedMessagesList.setVisible(true);
            this.sentMessagesList.setVisible(false);
            this.noMessagesLabel.setVisible(false);
            clearAllFields();
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
        }
    }

    /**
     * hides the received messages list and shows the sent messages list
     */
    public void viewSentMessages() {
        setButtonOrange(viewSentMessagesButton);
        setButtonBlack(viewReceivedMessagesButton);
        if (sentMessagesList.getItems().isEmpty()) {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(false);
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
            this.noMessagesLabel.setVisible(true);
        } else {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(true);
            this.noMessagesLabel.setVisible(false);
            clearAllFields();
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
        }
    }

    /**
     * show the window for a root of a certain loader
     *
     * @param root - said root
     */
    private void showWindow(Parent root) {
        Scene scene = new Scene(root);
        Stage composeMessageStage = new Stage();
        composeMessageStage.setScene(scene);
        composeMessageStage.initStyle(StageStyle.UNDECORATED);
        composeMessageStage.centerOnScreen();
        composeMessageStage.show();
    }

    /**
     * opens the window for composing a new message
     *
     * @throws IOException if an IO error occurs
     */
    public void openWindowForComposeNew() throws IOException {
        URL composeMessageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/composeMessage-view.fxml");
        FXMLLoader composeMessageWindowLoader = new FXMLLoader(composeMessageURL);
        Parent composeMessageWindowRoot = composeMessageWindowLoader.load();
        ComposeMessageController composeMessageWindowController = composeMessageWindowLoader.getController();
        composeMessageWindowController.setService(this.service);
        composeMessageWindowController.setLoggedUser(this.loggedUser);
        composeMessageWindowController.setPrimaryFunction("composeNew");
        composeMessageWindowController.setSelectedMessage(null);
        composeMessageWindowController.init();
        showWindow(composeMessageWindowRoot);
    }

    /**
     * opens the window for composing reply message for an existing message
     *
     * @throws IOException if an IO error occurs
     */
    public void openWindowForReply() throws IOException {
        URL composeMessageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/composeMessage-view.fxml");
        FXMLLoader composeMessageWindowLoader = new FXMLLoader(composeMessageURL);
        Parent composeMessageWindowRoot = composeMessageWindowLoader.load();
        ComposeMessageController composeMessageWindowController = composeMessageWindowLoader.getController();
        composeMessageWindowController.setService(this.service);
        composeMessageWindowController.setLoggedUser(this.loggedUser);
        composeMessageWindowController.setPrimaryFunction("reply");
        composeMessageWindowController.setSelectedMessage(receivedMessagesList.getSelectionModel().getSelectedItem());
        composeMessageWindowController.init();
        showWindow(composeMessageWindowRoot);
    }

    /**
     * opens the window for composing reply all message for an existing message thread
     *
     * @throws IOException if an IO error occurs
     */
    public void openWindowForReplyAll() throws IOException {
        URL composeMessageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/composeMessage-view.fxml");
        FXMLLoader composeMessageWindowLoader = new FXMLLoader(composeMessageURL);
        Parent composeMessageWindowRoot = composeMessageWindowLoader.load();
        ComposeMessageController composeMessageWindowController = composeMessageWindowLoader.getController();
        composeMessageWindowController.setService(this.service);
        composeMessageWindowController.setLoggedUser(this.loggedUser);
        composeMessageWindowController.setPrimaryFunction("replyAll");
        composeMessageWindowController.setSelectedMessage(receivedMessagesList.getSelectionModel().getSelectedItem());
        composeMessageWindowController.init();
        showWindow(composeMessageWindowRoot);
    }

    /**
     * initiates the lists
     * received messages list is by default visible
     * reply and replyAll buttons are by default hidden
     */
    public void init() {
        updateModelReceivedMessages();
        updateModelSentMessages();
        viewReceivedMessages();
    }

    @Override
    public void update(EntityModificationEvent<Integer> event) {
        ChangeEventType type = event.getType();
        if (type == ChangeEventType.DELETE)
            updateForDelete(event.getModifiedEntityID());
        if (type == ChangeEventType.UPDATE)
            updateForUpdate(event.getModifiedEntityID());
        if (type == ChangeEventType.ADD)
            updateForAdd(event.getModifiedEntityID());
    }

    public void updateForDelete(Integer id) {
        // TODO
    }

    public void updateForAdd(Integer id) {
        MessageDTO messageDTO = service.getMessageDTO(id);
        if (messageDTO.getFromEmail().equals(loggedUser.getEmail()))
            modelSentMessages.add(0, messageDTO);
        if (messageDTO.getToEmails().contains(loggedUser.getEmail()))
            modelReceivedMessages.add(0, messageDTO);
    }

    public void updateForUpdate(Integer id) {
        // TODO
    }
}
