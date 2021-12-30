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
 * class that describes a user list cell for the users list
 */
public class UserCell extends ListCell<String> {
    private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
    private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
    HBox root = new HBox(10);
    Label label = new Label("Null");
    Region region = new Region();
    Button addFriendButton = new Button("Add friend");

    /**
     * creates a cell that has a label and a button that chan change its text
     */
    public UserCell() {
        super();
        label.setFont(new Font(25.0));

        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(5, 10, 5, 10));
        root.getChildren().add(label);
        HBox.setHgrow(region, Priority.ALWAYS);
        root.getChildren().add(region);

        addFriendButton.setFont(new Font(16.0));
        addFriendButton.setStyle(IDLE_BUTTON_STYLE);
        addFriendButton.setOnMouseEntered(event -> addFriendButton.setStyle(HOVERED_BUTTON_STYLE));
        addFriendButton.setOnMouseExited(event -> addFriendButton.setStyle(IDLE_BUTTON_STYLE));
        addFriendButton.setOnAction(event -> {
            if (addFriendButton.getText().equals("Add friend"))
                addFriendButton.setText("Cancel friend request");
            else
                addFriendButton.setText("Add friend");
        });

        root.getChildren().addAll(addFriendButton);
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
