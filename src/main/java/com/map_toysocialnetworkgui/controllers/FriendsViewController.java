package com.map_toysocialnetworkgui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class FriendsViewController {
    // List
    ObservableList<String> list = FXCollections.observableArrayList(
            "Item 1", "Item 2", "Item 3", "Item 4");
    // FXML
    @FXML
    ListView<String> friendsList;

    static class FriendCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("Null");
        Pane pane = new Pane();
        Button addButton = new Button("Add friend");
        String lastItem;

        public FriendCell() {
            super();
            hbox.getChildren().addAll(label, pane, addButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            addButton.setOnAction(event -> {
                System.out.println("lmao");
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item != null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }

    void initFriendsList() {
        friendsList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new FriendCell();
            }
        });
    }

    public void initialize() {
        friendsList.setItems(list);
        initFriendsList();
    }
}
