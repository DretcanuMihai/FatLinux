package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.util.Collection;

public class FriendsViewController extends AbstractController {
    // Data
    UserUIDTO loggedUser;
    // List
    ObservableList<String> modelFriends = FXCollections.observableArrayList();
    // FXML
    @FXML
    ListView<String> friendsList;

    static class FriendCell extends ListCell<String> {
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

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    public void initialize() {
        friendsList.setCellFactory(param -> new FriendCell());
        friendsList.setItems(modelFriends);
    }

    public void updateModelFriends() {
        Collection<String> friendCompleteName = service.getAllFriendshipDTOsOfUser(loggedUser.getEmail()).stream()
                .map(FriendshipDTO::getUser2).toList().stream()
                .map(userUIDTO -> userUIDTO.getFirstName() + " " + userUIDTO.getLastName()).toList();

         modelFriends.setAll(friendCompleteName);
    }

    public void init() {
        updateModelFriends();
    }
}
