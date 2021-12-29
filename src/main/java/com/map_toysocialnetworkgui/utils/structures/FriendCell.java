package com.map_toysocialnetworkgui.utils.structures;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class FriendCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("Null");
    Pane pane = new Pane();
    Button removeFriendButton = new Button("Remove friend");
    String lastItem;
    private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
    private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
    public FriendCell() {
        super();
        hbox.getChildren().addAll(label, pane, removeFriendButton);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(pane, Priority.ALWAYS);
        label.setFont(new Font(25.0));
        removeFriendButton.setFont(new Font(16.0));
        removeFriendButton.setStyle(IDLE_BUTTON_STYLE);
        removeFriendButton.setOnMouseEntered(event -> removeFriendButton.setStyle(HOVERED_BUTTON_STYLE));
        removeFriendButton.setOnMouseExited(event -> removeFriendButton.setStyle(IDLE_BUTTON_STYLE));
        removeFriendButton.setOnAction(event -> System.out.println(label.getText()));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            setPrefHeight(80.0);
            lastItem = item;
            label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }
}
