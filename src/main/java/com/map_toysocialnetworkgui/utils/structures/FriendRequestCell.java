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
 * class that describes a friend request list cell for the friend requests list
 */
public class FriendRequestCell extends ListCell<String> {
    private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
    private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
    HBox root = new HBox(10);
    Label label = new Label("Null");
    Region region = new Region();
    Button acceptButton = new Button("Accept");
    Button declineButton = new Button("Decline");

    /**
     * creates a cell that has a label and 2 buttons
     */
    public FriendRequestCell() {
        super();
        label.setFont(new Font(25.0));

        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(5, 10, 5, 10));
        root.getChildren().add(label);
        HBox.setHgrow(region, Priority.ALWAYS);
        root.getChildren().add(region);

        acceptButton.setFont(new Font(16.0));
        acceptButton.setStyle(IDLE_BUTTON_STYLE);
        acceptButton.setOnMouseEntered(event -> acceptButton.setStyle(HOVERED_BUTTON_STYLE));
        acceptButton.setOnMouseExited(event -> acceptButton.setStyle(IDLE_BUTTON_STYLE));
        acceptButton.setOnAction(event -> System.out.println("Accepted"));

        declineButton.setFont(new Font(16.0));
        declineButton.setStyle(IDLE_BUTTON_STYLE);
        declineButton.setOnMouseEntered(event -> declineButton.setStyle(HOVERED_BUTTON_STYLE));
        declineButton.setOnMouseExited(event -> declineButton.setStyle(IDLE_BUTTON_STYLE));
        declineButton.setOnAction(event -> System.out.println("Declined"));

        root.getChildren().addAll(acceptButton, declineButton);
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
