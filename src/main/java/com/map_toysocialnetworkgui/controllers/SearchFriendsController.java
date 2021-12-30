package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import com.map_toysocialnetworkgui.utils.structures.NoFocusModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * controller for search friend view
 */
public class SearchFriendsController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserUIDTO loggedUser;

    /**
     * current text to search after
     */
    String searchText;

    /**
     * observable lists for search
     */
    ObservableList<UserUIDTO> modelSearch = FXCollections.observableArrayList();

    /**
     * FXML data
     */
    @FXML
    ListView<UserUIDTO> searchFriendsList;
    @FXML
    Label searchFailedLabel;

    /**
     * protected class that describes a user list cell for the users list
     */
    protected class UserCell extends ListCell<UserUIDTO> {
        private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
        private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
        HBox root = new HBox(10);
        Label label = new Label("Null");
        Region region = new Region();
        Button addFriendButton = new Button("Null");

        /**
         * user cell that has a label and a button that chan change its text
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

            root.getChildren().addAll(addFriendButton);
        }

        @Override
        protected void updateItem(UserUIDTO user, boolean empty) {
            super.updateItem(user, empty);
            if (user == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(user.getFirstName() + " " + user.getLastName());
                addFriendButton.setText("Add friend");
                addFriendButton.setDisable(false);
                try {
                    service.getFriendshipDTO(loggedUser.getEmail(), user.getEmail());
                    addFriendButton.setText("✔ Friends");
                    addFriendButton.setDisable(true);
                } catch (ValidationException | AdministrationException ex) {
                    service.getFriendRequestsSentToUser(user.getEmail()).forEach(FriendRequestDTO -> {
                        if (FriendRequestDTO.getSender().getEmail().equals(loggedUser.getEmail())) {
                            addFriendButton.setText("Cancel friend request");
                            addFriendButton.setDisable(false);
                        }
                    });
                }
                addFriendButton.setOnAction(event -> {
                    if (addFriendButton.getText().equals("Add friend")) {
                        try {
                            service.sendFriendRequest(loggedUser.getEmail(), user.getEmail());
                            addFriendButton.setText("Cancel friend request");
                        } catch (ValidationException | AdministrationException ex) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning!");
                            alert.setHeaderText("Friend request warning!");
                            alert.setContentText(ex.getMessage());
                            alert.showAndWait();
                        }
                    } else if (addFriendButton.getText().equals("Cancel friend request")) {
                        service.retractFriendRequest(loggedUser.getEmail(), user.getEmail());
                        addFriendButton.setText("Add friend");
                    }
                });

                setPrefHeight(80.0);
                setText(null);
                setGraphic(root);
            }
        }
    }

    @FXML
    public void initialize() {
        searchFriendsList.setFocusModel(new NoFocusModel<>());
        searchFriendsList.setCellFactory(param -> new UserCell());
        searchFriendsList.setItems(modelSearch);
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
     * sets the current text to search after
     *
     * @param searchText - said text
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * updates the observable list of users (for searching)
     */
    public void updateModelUsers() {
        if (searchText.equals("")) {
            searchFriendsList.setVisible(false);
            searchFailedLabel.setVisible(true);
        } else {
            try {
                Collection<UserUIDTO> foundUsers = StreamSupport.stream(service.filterUsers(searchText).spliterator(), false)
                        .collect(Collectors.toList());
                if (foundUsers.isEmpty()) {
                    searchFriendsList.setVisible(false);
                    searchFailedLabel.setVisible(true);
                } else {
                    modelSearch.setAll(foundUsers);
                    searchFriendsList.setVisible(true);
                    searchFailedLabel.setVisible(false);
                }
            } catch (ValidationException ex) {
                searchFriendsList.setVisible(false);
                searchFailedLabel.setVisible(true);
            }
        }
    }

    /**
     * initiates the list
     */
    public void init() {
        updateModelUsers();
    }
}
