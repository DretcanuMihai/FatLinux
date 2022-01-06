package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
import com.map_toysocialnetworkgui.utils.structures.ConversationCustomVBox;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    TextField fromTextField;
    @FXML
    TextField toTextField;
    @FXML
    TextField subjectTextField;
    @FXML
    TextArea messageTextArea;
    @FXML
    Label inboxFromLabel;
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

    /**
     * initializes inbox window elements
     */
    public void initInboxElements() {
        this.buttonColoring = new ButtonColoring();
        setCustomCell(this.receivedMessagesList);
        setCustomCell(this.sentMessagesList);
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
                        ConversationCustomVBox child = new ConversationCustomVBox(2);
                        VBox.setVgrow(child, Priority.ALWAYS);
                        conversationVBox.getChildren().add(0, child);
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
     * clears all text fields and text areas
     */
    private void clearAllFields() {
        fromTextField.clear();
        toTextField.clear();
        subjectTextField.clear();
        messageTextArea.clear();
    }

    public void fillDataForMessage(MessageDTO message) {
        /*
        if (fromTextField.isVisible() && inboxFromLabel.isVisible())
            fromTextField.setText(message.getFromEmail());
        toTextField.clear();
        for (String receivers : message.getToEmails()) {
            if (receivers.equals(loggedUser.getEmail()))
                toTextField.appendText("me");
            else
                toTextField.appendText(receivers);
            toTextField.appendText(", ");
        }
        toTextField.deleteText(toTextField.getText().length() - 2, toTextField.getText().length());
        subjectTextField.setText(message.getMessageSubject());
        messageTextArea.setText(message.getMessageText());
         */
        ConversationCustomVBox root1 = new ConversationCustomVBox(2);
        root1.setStyle("-fx-background-color: black");
        root1.changeTextAreaId("highlightedMessageTextArea");
        ConversationCustomVBox root2 = new ConversationCustomVBox(2);

        conversationVBox.getChildren().clear();
        if (message.getParentMessageId() != null) {
            conversationVBox.getChildren().add(0, root1);
            conversationVBox.getChildren().add(0, root2);
        }
        else {
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
    public void initModelSentMessages() {
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
        //inboxFromLabel.setVisible(true);
        //fromTextField.setVisible(true);
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
            //clearAllFields();
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
        //inboxFromLabel.setVisible(false);
        //fromTextField.setVisible(false);
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
        clearAllFields();
        modelReceivedMessages.setAll();
        modelSentMessages.setAll();
        conversationVBox.getChildren().clear();
    }

//    protected class ReplyCell extends ListCell<MessageDTO> {
//        HBox root = new HBox(10);
//        Region region = new Region();
//        Label label = new Label("Null");
//
//        /**
//         * user cell that has a label and a button that chan change its text
//         */
//        public ReplyCell() {
//            super();
//            label.setFont(new Font(25.0));
//
//            root.setAlignment(Pos.CENTER_LEFT);
//            root.setPadding(new Insets(5, 10, 5, 10));
//            root.getChildren().add(label);
//            HBox.setHgrow(region, Priority.ALWAYS);
//            root.getChildren().add(region);
//        }
//
//        @Override
//        protected void updateItem(MessageDTO message, boolean empty) {
//            super.updateItem(message, empty);
//            if (message == null || empty) {
//                setText(null);
//                setGraphic(null);
//            } else {
//                label.setText(message.getMessageSubject());
//
//                setPrefHeight(204.0);
//                setText(null);
//                setGraphic(root);
//            }
//        }
//    }

}
