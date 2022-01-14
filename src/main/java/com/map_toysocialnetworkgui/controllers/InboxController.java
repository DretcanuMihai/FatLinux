package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import com.map_toysocialnetworkgui.service.SuperService;
import com.map_toysocialnetworkgui.utils.Constants;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationObsEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
import com.map_toysocialnetworkgui.utils.structures.ConversationCustomVBox;
import com.map_toysocialnetworkgui.utils.styling.ButtonColoring;
import com.map_toysocialnetworkgui.utils.styling.TextColoring;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * controller for inbox view
 */
public class InboxController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    /**
     * observable lists for sent and received messages
     */
    ObservableList<MessageDTO> modelMessages = FXCollections.observableArrayList();
    Observer<EntityModificationObsEvent<Integer>> eventObserver = new Observer<EntityModificationObsEvent<Integer>>() {
        @Override
        public void update(EntityModificationObsEvent<Integer> event) {
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
            currentMode="s";
            setPage(new PageableImplementation(1,10));
        }

        /**
         * method for update (observer pattern)
         *
         * @param id - id of modified entity
         */
        public void updateForUpdate(Integer id) {
            // TODO
        }
    };

    /**
     * FXML data
     */
    @FXML
    ListView<MessageDTO> messagesList;
    @FXML
    VBox conversationVBox;
    @FXML
    ScrollPane conversationScrollPane;
    @FXML
    Button viewReceivedMessagesButton;
    @FXML
    Button viewSentMessagesButton;
    @FXML
    Button composeNewButton;
    @FXML
    Button previousMessagesPageButton;
    @FXML
    Button nextMessagesPageButton;
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
     * message page
     */
    Page<MessageDTO> messageDTOPage;
    String currentMode;

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

    public void setPage(Pageable pageable){
        messageDTOPage=getPage(pageable);
        modelMessages.setAll(messageDTOPage.getContent().collect(Collectors.toList()));
        setButtonsVisibility();
    }

    public Page<MessageDTO> getPage(Pageable pageable){
        if(currentMode.equals("r")){
            return service.getMessagesReceivedByUser(loggedUser.getEmail(),pageable);
        }
        else{
            return service.getMessagesSentByUser(loggedUser.getEmail(),pageable);
        }
    }

    /**
     * initializes inbox window elements
     */
    public void initInboxElements() {
        this.buttonColoring = new ButtonColoring();
        this.messagesList.setCellFactory(param -> new MessageCell());
        this.messagesList.setItems(this.modelMessages);

        this.composeNewButton.setOnAction(event -> {
            this.composeMessageWindowController.setPrimaryFunction(this.composeNewButton.getText());
            this.composeMessageWindowController.setSelectedMessage(null);
            this.composeMessageWindowController.init();
            openComposeMessageWindow();
        });

        this.conversationScrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * 0.01;
            this.conversationScrollPane.setVvalue(this.conversationScrollPane.getVvalue() - deltaY);
        });

        this.conversationScrollPane.setOnScroll(event -> {
            if (conversationScrollPane.getVvalue() < Constants.EPSILON) {
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
        });
        this.previousMessagesPageButton.setOnAction(event -> {
            setPage(messageDTOPage.previousPageable());
        });
        this.nextMessagesPageButton.setOnAction(event -> {
            setPage(messageDTOPage.nextPageable());
        });
        /*
        this.conversationScrollPane.vvalueProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    if (newValue.doubleValue() < Constants.EPSILON) {
                        if (!conversationVBox.getChildren().isEmpty()) {
                            ConversationCustomVBox lastParent = (ConversationCustomVBox) conversationVBox.getChildren().get(0);
                            String parentMessageId = getTextFromTextFlow(lastParent.getParentMessageIdFlow());

                            if (!parentMessageId.equals("")) {
                                MessageDTO newParentMessage = service.getMessageDTO(Integer.parseInt(parentMessageId));
                                ConversationCustomVBox root = createConversationCustomVBox(newParentMessage);
                                conversationVBox.getChildren().add(0, root);
                                // conversationScrollPane.setVvalue(1.0d / (conversationVBox.getChildren().size() * lastParent.getHeight()));
                            }
                        }
                    }
                }
        );
        */
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
    public void setLoggedUser(UserDTO loggedUser) {
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

        EventHandler<ActionEvent> replyEvent = event -> {
            composeMessageWindowController.setPrimaryFunction(conversationCustomVBox.getReplyButton().getText());
            composeMessageWindowController.setSelectedMessage(message);
            composeMessageWindowController.init();
            openComposeMessageWindow();
        };
        EventHandler<ActionEvent> replyAllEvent = event -> {
            composeMessageWindowController.setPrimaryFunction(conversationCustomVBox.getReplyAllButton().getText());
            composeMessageWindowController.setSelectedMessage(message);
            composeMessageWindowController.init();
            openComposeMessageWindow();
        };
        conversationCustomVBox.setButtonsActions(replyEvent, replyAllEvent);

        if (message.getFromEmail().equals(loggedUser.getEmail()))
            conversationCustomVBox.disableReplyButton();

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

        this.conversationScrollPane.setVvalue(1);
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
     * initiates the lists
     * received messages list is by default visible
     * reply and replyAll buttons are by default hidden
     */
    public void initModels() {

    }

    public boolean hasNext(){
        return getPage(messageDTOPage.nextPageable()).getContent().count()!=0;
    }

    public boolean hasPrevious(){
        return messageDTOPage.getPageable().getPageNumber()!=1;
    }

    public void setButtonsVisibility(){

        nextMessagesPageButton.setVisible(hasNext());
        previousMessagesPageButton.setVisible(hasPrevious());
    }

    /**
     * shows the no messages label and hides everything else
     */
    public void showNoMessagesLabel() {
        this.messagesList.setVisible(false);
        this.conversationVBox.getChildren().clear();
        this.conversationVBox.setVisible(false);
        this.conversationScrollPane.setVisible(false);
        this.noMessagesLabel.setVisible(true);
    }

    /**
     * hides the sent messages list and shows the received messages list
     */
    public void viewReceivedMessages() {
        currentMode="r";
        messagesList.setOnMouseClicked(event -> {
            if (messagesList.getSelectionModel().getSelectedItem() != null) {
                fillDataForMessage(messagesList.getSelectionModel().getSelectedItem());
            }
        });
        buttonColoring.setButtonOrange(viewReceivedMessagesButton);
        buttonColoring.setButtonBlack(viewSentMessagesButton);
        setPage(new PageableImplementation(1,7));
        if (messagesList.getItems().isEmpty()) {
            showNoMessagesLabel();
        } else {
            this.messagesList.setVisible(true);
            this.conversationVBox.getChildren().clear();
            this.conversationVBox.setVisible(true);
            this.conversationScrollPane.setVisible(true);
            this.noMessagesLabel.setVisible(false);
        }
    }

    /**
     * hides the received messages list and shows the sent messages list
     */
    public void viewSentMessages() {
        currentMode="s";

        messagesList.setOnMouseClicked(event -> {
            if (messagesList.getSelectionModel().getSelectedItem() != null) {
                fillDataForMessage(messagesList.getSelectionModel().getSelectedItem());
            }
        });

        buttonColoring.setButtonOrange(viewSentMessagesButton);
        buttonColoring.setButtonBlack(viewReceivedMessagesButton);
        setPage(new PageableImplementation(1,7));
        if (messagesList.getItems().isEmpty()) {
            showNoMessagesLabel();
        } else {
            this.messagesList.setVisible(true);
            this.conversationVBox.getChildren().clear();
            this.conversationScrollPane.setVisible(true);
            this.conversationVBox.setVisible(true);
            this.noMessagesLabel.setVisible(false);
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

    /**
     * opens the message composition window
     */
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
    public void reset() {
        super.reset();
        modelMessages.setAll();
        conversationVBox.getChildren().clear();
        this.service.removeMessageObserver(eventObserver);
    }

    @Override
    public void setService(SuperService service) {
        super.setService(service);
        this.service.addMessageObserver(eventObserver);
    }

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
}
