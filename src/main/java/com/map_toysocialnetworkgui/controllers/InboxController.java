package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
import com.map_toysocialnetworkgui.utils.structures.ConversationCustomVBox;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
import com.map_toysocialnetworkgui.utils.styling.TextColoring;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    VBox conversationVBox;
    @FXML
    ScrollPane conversationScrollPane;
    @FXML
    Button viewReceivedMessagesButton;
    @FXML
    Button viewSentMessagesButton;
    @FXML
    Button replyButton;
    @FXML
    Button replyAllButton;
    @FXML
    Button composeNewButton;
    @FXML
    Label noMessagesLabel;

    /**
     * button styling class
     */
    ButtonColoring buttonColoring;

    /**
     * compose message view controller
     */
    ComposeMessageController composeMessageWindowController;

    /**
     * compose message scene
     */
    Scene composeMessageScene;

    /**
     * compose message stage
     */
    Stage composeMessageStage;

    /**
     * text coloring class
     */
    TextColoring textColoring;

    /**
     * protected class that describes a message cell for the message list
     */
    protected class MessageCell extends ListCell<MessageDTO> {
        HBox root = new HBox(10);
        Label label = new Label("Null");
        ImageView imageView = new ImageView("com/map_toysocialnetworkgui/images/messageListIcon.png");

        /**
         * message cell that has an icon and a label with said message's subject
         */
        public MessageCell() {
            super();
            label.setFont(new Font(15));
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 10, 5, 5));
            root.getChildren().addAll(imageView, label);
        }

        @Override
        protected void updateItem(MessageDTO messageDTO, boolean empty) {
            super.updateItem(messageDTO, empty);
            if (messageDTO == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(messageDTO.getMessageSubject());
                setPrefHeight(55);
                setText(null);
                setGraphic(root);
            }
        }
    }

    /**
     * creates a string from a text flow's text
     *
     * @param textFlow - said text flow
     * @return - said string
     */
    public String getTextFromTextFlow(TextFlow textFlow) {
        StringBuilder sb = new StringBuilder();
        for (Node node : textFlow.getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }
        return sb.toString();
    }

    /**
     * initializes inbox window elements
     */
    public void initInboxElements() {
        this.buttonColoring = new ButtonColoring();
        this.receivedMessagesList.setCellFactory(param -> new MessageCell());
        this.sentMessagesList.setCellFactory(param -> new MessageCell());
        this.receivedMessagesList.setItems(this.modelReceivedMessages);
        this.sentMessagesList.setItems(this.modelSentMessages);

        this.composeNewButton.setOnAction(event -> {
            this.composeMessageWindowController.setPrimaryFunction(this.composeNewButton.getText());
            this.composeMessageWindowController.setSelectedMessage(null);
            this.composeMessageWindowController.init();
            openComposeMessageWindow();
        });
        this.replyButton.setOnAction(event -> {
            this.composeMessageWindowController.setPrimaryFunction(this.replyButton.getText());
            this.composeMessageWindowController.setSelectedMessage(receivedMessagesList.getSelectionModel().getSelectedItem());
            this.composeMessageWindowController.init();
            openComposeMessageWindow();
        });
        this.replyAllButton.setOnAction(event -> {
            this.composeMessageWindowController.setPrimaryFunction(this.replyAllButton.getText());
            this.composeMessageWindowController.setSelectedMessage(receivedMessagesList.getSelectionModel().getSelectedItem());
            this.composeMessageWindowController.init();
            openComposeMessageWindow();
        });

        this.conversationScrollPane.vvalueProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    if (newValue.doubleValue() == 0) {
                        if (!conversationVBox.getChildren().isEmpty()) {
                            ConversationCustomVBox lastParent = (ConversationCustomVBox) conversationVBox.getChildren().get(0);
                            String parentMessageId = getTextFromTextFlow(lastParent.getParentMessageIdFlow());

                            if (!parentMessageId.equals("")) {
                                MessageDTO newParentMessage = service.getMessageDTO(Integer.parseInt(parentMessageId));
                                ConversationCustomVBox root = createConversationCustomVBox(newParentMessage);
                                conversationVBox.getChildren().add(0, root);
                            }
                        }
                    }
                }
        );
    }

    /**
     * initiates the scene for message composition
     *
     * @throws IOException - if an IO error occurs
     */
    public void initComposeMessageScene() throws IOException {
        URL composeMessageURL = getClass().getResource("/com/map_toysocialnetworkgui/views/composeMessage-view.fxml");
        FXMLLoader composeMessageWindowLoader = new FXMLLoader(composeMessageURL);
        Parent composeMessageWindowRoot = composeMessageWindowLoader.load();
        this.composeMessageWindowController = composeMessageWindowLoader.getController();
        this.composeMessageScene = new Scene(composeMessageWindowRoot);
    }

    @FXML
    public void initialize() throws IOException {
        textColoring = new TextColoring();
        initInboxElements();
        initComposeMessageScene();
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
     * creates a conversation custom VBox for displaying a message in a conversation
     *
     * @param message - said message
     * @return - said custom VBox
     */
    public ConversationCustomVBox createConversationCustomVBox(MessageDTO message) {
        ConversationCustomVBox conversationCustomVBox = new ConversationCustomVBox(2);
        Text fromText = new Text(message.getFromEmail());
        Text sentText = new Text(message.getSendTime().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm")));
        Text toText = new Text(message.getToEmails().toString().replaceAll("[\\[\\]]", ""));
        Text subjectText = new Text(message.getMessageSubject());
        List<Text> messageHeader = Arrays.asList(fromText, sentText, toText, subjectText);
        messageHeader.forEach(text -> textColoring.setTextWhite(text));

        if (message.getParentMessageId() != null)
            conversationCustomVBox.setParentMessageIdFlow(new Text(message.getParentMessageId().toString()));

        conversationCustomVBox.setFromFlow(fromText);
        conversationCustomVBox.setSentFlow(sentText);
        conversationCustomVBox.setToFlow(toText);
        conversationCustomVBox.setSubjectFlow(subjectText);
        conversationCustomVBox.setMessageTextArea(message.getMessageText());

        return conversationCustomVBox;
    }

    /**
     * fills data of a message in its corresponding custom VBox
     *
     * @param message - said message
     */
    public void fillDataForMessage(MessageDTO message) {
        // Selected message highlighted in black
        ConversationCustomVBox root1 = createConversationCustomVBox(message);
        root1.setStyle("-fx-background-color: black");
        root1.changeTextAreaId("highlightedMessageTextArea");

        conversationVBox.getChildren().clear();
        if (message.getParentMessageId() != null) {
            conversationVBox.getChildren().add(0, root1);

            // Parent message
            ConversationCustomVBox root2 = createConversationCustomVBox(service.getMessageDTO(message.getParentMessageId()));
            conversationVBox.getChildren().add(0, root2);
        } else {
            conversationVBox.getChildren().add(0, root1);
        }
    }

    /**
     * updates the observable list of received messages
     * selected message will display the contents of the message and show reply and reply all buttons
     */
    public void initModelReceivedMessages() {
        Collection<MessageDTO> receivedMessages = StreamSupport
                .stream(service.getMessagesReceivedByUser(loggedUser.getEmail()).spliterator(), false).toList();

        receivedMessagesList.setOnMouseClicked(event -> {
            if (receivedMessagesList.getSelectionModel().getSelectedItem() != null) {
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
    public void initModelSentMessages() {
        Collection<MessageDTO> sentMessages = StreamSupport
                .stream(service.getMessagesSentByUser(loggedUser.getEmail()).spliterator(), false).toList();

        sentMessagesList.setOnMouseClicked(event -> {
            if (sentMessagesList.getSelectionModel().getSelectedItem() != null) {
                fillDataForMessage(sentMessagesList.getSelectionModel().getSelectedItem());
                replyButton.setVisible(false);
                replyAllButton.setVisible(false);
            }
        });
        modelSentMessages.setAll(sentMessages);
    }

    /**
     * initiates the lists
     * received messages list is by default visible
     * reply and replyAll buttons are by default hidden
     */
    public void initModels() {
        initModelReceivedMessages();
        initModelSentMessages();
    }

    /**
     * hides the sent messages list and shows the received messages list
     */
    public void viewReceivedMessages() {
        buttonColoring.setButtonOrange(viewReceivedMessagesButton);
        buttonColoring.setButtonBlack(viewSentMessagesButton);
        this.sentMessagesList.getSelectionModel().clearSelection();
        this.receivedMessagesList.getSelectionModel().clearSelection();
        if (receivedMessagesList.getItems().isEmpty()) {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(false);
            this.conversationVBox.getChildren().clear();
            this.conversationVBox.setVisible(false);
            this.conversationScrollPane.setVisible(false);
            replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
            this.noMessagesLabel.setVisible(true);
        } else {
            this.receivedMessagesList.setVisible(true);
            this.conversationVBox.getChildren().clear();
            this.conversationVBox.setVisible(true);
            this.conversationScrollPane.setVisible(true);
            this.sentMessagesList.setVisible(false);
            this.noMessagesLabel.setVisible(false);
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
        }
    }

    /**
     * hides the received messages list and shows the sent messages list
     */
    public void viewSentMessages() {
        buttonColoring.setButtonOrange(viewSentMessagesButton);
        buttonColoring.setButtonBlack(viewReceivedMessagesButton);
        this.sentMessagesList.getSelectionModel().clearSelection();
        this.receivedMessagesList.getSelectionModel().clearSelection();
        if (sentMessagesList.getItems().isEmpty()) {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(false);
            this.conversationVBox.getChildren().clear();
            this.conversationVBox.setVisible(false);
            this.conversationScrollPane.setVisible(false);
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
            this.noMessagesLabel.setVisible(true);
        } else {
            this.receivedMessagesList.setVisible(false);
            this.sentMessagesList.setVisible(true);
            this.conversationVBox.getChildren().clear();
            this.conversationScrollPane.setVisible(true);
            this.conversationVBox.setVisible(true);
            this.noMessagesLabel.setVisible(false);
            this.replyButton.setVisible(false);
            this.replyAllButton.setVisible(false);
        }
    }

    /**
     * initializes compose message window's elements
     */
    public void initComposeMessageWindow() {
        this.composeMessageStage = new Stage();
        this.composeMessageWindowController.setService(this.service);
        this.composeMessageWindowController.setLoggedUser(this.loggedUser);
        this.composeMessageStage.setScene(this.composeMessageScene);
        this.composeMessageStage.initStyle(StageStyle.UNDECORATED);
        this.composeMessageStage.centerOnScreen();
    }

    public void openComposeMessageWindow() {
        this.composeMessageStage.show();
    }

    /**
     * initiates the lists
     * received messages list is by default visible
     * reply and replyAll buttons are by default hidden
     */
    public void init() {
        viewReceivedMessages();
        initComposeMessageWindow();
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

    /**
     * method for delete (observer pattern)
     *
     * @param id - id of modified entity
     */
    public void updateForDelete(Integer id) {
        // TODO
    }

    /**
     * method for add (observer pattern)
     *
     * @param id - id of modified entity
     */
    public void updateForAdd(Integer id) {
        MessageDTO messageDTO = service.getMessageDTO(id);
        if (messageDTO.getFromEmail().equals(loggedUser.getEmail()))
            modelSentMessages.add(0, messageDTO);
        if (messageDTO.getToEmails().contains(loggedUser.getEmail()))
            modelReceivedMessages.add(0, messageDTO);
    }

    /**
     * method for update (observer pattern)
     *
     * @param id - id of modified entity
     */
    public void updateForUpdate(Integer id) {
        // TODO
    }

    @Override
    public void reset() {
        super.reset();
        modelReceivedMessages.setAll();
        modelSentMessages.setAll();
        conversationVBox.getChildren().clear();
    }
}
