package com.map_toysocialnetworkgui.utils.structures;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ConversationCustomVBox extends VBox {
    TextFlow fromFlow;
    TextFlow sentFlow;
    TextFlow toFlow;
    TextFlow subjectFlow;
    TextFlow parentMessageIdFlow;
    TextArea messageTextArea;

    public void changeTextAreaId(String id) {
        this.messageTextArea.setId(id);
    }

    public ConversationCustomVBox(double spacing) {
        this.setSpacing(spacing);
        this.setPrefHeight(201);
        this.setMinHeight(201);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));

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

        fromFlow.getChildren().add(fromText);
        sentFlow.getChildren().add(sentText);
        toFlow.getChildren().add(toText);
        subjectFlow.getChildren().add(subjectText);

        messageTextArea.setPrefSize(300, 110);
        messageTextArea.setFont(new Font(16));
        messageTextArea.setEditable(false);

        this.getChildren().addAll(parentMessageIdFlow, fromFlow, sentFlow, toFlow, subjectFlow, messageTextArea);
    }
}
