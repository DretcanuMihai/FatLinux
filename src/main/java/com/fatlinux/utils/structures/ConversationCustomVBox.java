package com.fatlinux.utils.structures;

import com.fatlinux.utils.styling.ButtonStyling;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * class that describes a custom VBox for showing messages in a conversation
 */
public class ConversationCustomVBox extends VBox {
    private final Button replyButton;
    private final Button replyAllButton;
    private final TextFlow fromFlow;
    private final TextFlow sentFlow;
    private final TextFlow toFlow;
    private final TextFlow subjectFlow;
    private final TextFlow parentMessageIdFlow;
    private final TextArea messageTextArea;

    /**
     * creates a custom VBox having with a spacing
     *
     * @param spacing - spacing of the VBox
     */
    public ConversationCustomVBox(double spacing) {
        this.setSpacing(spacing);
        this.setPrefHeight(201);
        this.setMinHeight(201);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));

        ButtonStyling buttonStyling = new ButtonStyling();
        HBox hBox = new HBox();
        Region region = new Region();
        replyButton = new Button("Reply");
        replyAllButton = new Button("Reply All");
        ImageView replyIcon = new ImageView("com/fatlinux/images/replyIcon1.png");
        ImageView replyAllIcon = new ImageView("com/fatlinux/images/replyAllIcon1.png");
        fromFlow = new TextFlow();
        sentFlow = new TextFlow();
        toFlow = new TextFlow();
        subjectFlow = new TextFlow();
        parentMessageIdFlow = new TextFlow();
        messageTextArea = new TextArea();

        parentMessageIdFlow.setStyle("-fx-font-size: 1");
        Text fromText = new Text("From: ");
        fromText.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-fill: white");
        Text sentText = new Text("Sent: ");
        sentText.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-fill: white");
        Text toText = new Text("To: ");
        toText.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-fill: white");
        Text subjectText = new Text("Subject: ");
        subjectText.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-fill: white");
        messageTextArea.setWrapText(true);

        replyButton.setFont(new Font(0));
        replyAllButton.setFont(new Font(0));
        buttonStyling.setButtonOrangeSquareBorder(replyButton);
        buttonStyling.setButtonOrangeSquareBorder(replyAllButton);
        replyButton.setGraphic(replyIcon);
        replyAllButton.setGraphic(replyAllIcon);
        hBox.getChildren().add(fromFlow);
        HBox.setHgrow(region, Priority.ALWAYS);
        hBox.getChildren().add(region);
        hBox.getChildren().addAll(replyButton, replyAllButton);
        fromFlow.getChildren().add(fromText);
        sentFlow.getChildren().add(sentText);
        toFlow.getChildren().add(toText);
        subjectFlow.getChildren().add(subjectText);

        messageTextArea.setPrefSize(300, 110);
        messageTextArea.setFont(new Font(16));
        messageTextArea.setEditable(false);

        this.getChildren().addAll(parentMessageIdFlow, hBox, sentFlow, toFlow, subjectFlow, messageTextArea);
    }

    /**
     * adds text to fromFlow
     *
     * @param text - said text
     */
    public void setFromFlow(Text text) {
        this.fromFlow.getChildren().add(text);
    }

    /**
     * adds text to sentFlow
     *
     * @param text - said text
     */
    public void setSentFlow(Text text) {
        this.sentFlow.getChildren().add(text);
    }

    /**
     * adds text to toFlow
     *
     * @param text - said text
     */
    public void setToFlow(Text text) {
        this.toFlow.getChildren().add(text);
    }

    /**
     * adds text to subjectFlow
     *
     * @param text - said text
     */
    public void setSubjectFlow(Text text) {
        this.subjectFlow.getChildren().add(text);
    }

    /**
     * returns the parentMessageIdFlow
     *
     * @return - said text flow
     */
    public TextFlow getParentMessageIdFlow() {
        return this.parentMessageIdFlow;
    }

    /**
     * adds text to parentMessageIdFlow
     *
     * @param text - said text
     */
    public void setParentMessageIdFlow(Text text) {
        this.parentMessageIdFlow.getChildren().add(text);
    }

    /**
     * adds text to messageTextArea
     *
     * @param text - said text
     */
    public void setMessageTextArea(String text) {
        this.messageTextArea.setText(text);
    }

    /**
     * sets the id of the text area for specific styling
     *
     * @param id - said id
     */
    public void changeTextAreaId(String id) {
        this.messageTextArea.setId(id);
    }

    /**
     * returns the reply button
     *
     * @return - said button
     */
    public Button getReplyButton() {
        return replyButton;
    }

    /**
     * returns the reply all button
     *
     * @return - said button
     */
    public Button getReplyAllButton() {
        return replyAllButton;
    }

    /**
     * disables reply button
     */
    public void disableReplyButton() {
        replyButton.setDisable(true);
    }

    /**
     * sets the button's primary functions
     *
     * @param replyEvent    - reply function for reply button
     * @param replyAllEvent - reply all function for reply all
     */
    public void setButtonsActions(EventHandler<ActionEvent> replyEvent, EventHandler<ActionEvent> replyAllEvent) {
        this.replyButton.setOnAction(replyEvent);
        this.replyAllButton.setOnAction(replyAllEvent);
    }
}
