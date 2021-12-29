package com.map_toysocialnetworkgui.utils.structures;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 * class that describes a friend list cell for the friends list
 */
public class FriendCell extends ListCell<String> {
    private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
    private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
    HBox root = new HBox(10);
    Label label = new Label("Null");
    Region region = new Region();
    Button removeFriendButton = new Button("Remove friend");

    /**
     * creates a cell that has a label and a button
     */
    public FriendCell() {
        super();
        label.setFont(new Font(25.0));

        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(5, 10, 5, 10));
        root.getChildren().add(label);
        HBox.setHgrow(region, Priority.ALWAYS);
        root.getChildren().add(region);

        removeFriendButton.setFont(new Font(16.0));
        removeFriendButton.setStyle(IDLE_BUTTON_STYLE);
        removeFriendButton.setOnMouseEntered(event -> removeFriendButton.setStyle(HOVERED_BUTTON_STYLE));
        removeFriendButton.setOnMouseExited(event -> removeFriendButton.setStyle(IDLE_BUTTON_STYLE));
        removeFriendButton.setOnAction(event -> System.out.println(label.getText()));

        root.getChildren().addAll(removeFriendButton);
    }

    @Override
    protected void updateItem(String user, boolean empty) {
        super.updateItem(user, empty);
        if (user == null || empty) {
            setText(null);
        } else {
            setPrefHeight(80.0);
            label.setText(user);
            setText(null);
            setGraphic(root);
        }
    }
}
