package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
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
import javafx.scene.layout.VBox;
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
    UserDTO loggedUser;

    /**
     * observable lists for search
     */
    ObservableList<UserDTO> modelSearch = FXCollections.observableArrayList();
    Collection<String> friendEmails;
    Collection<String> friendRequestedEmails;

    /**
     * FXML data
     */
    @FXML
    ListView<UserDTO> searchFriendsList;
    @FXML
    Label searchFailedLabel;
    @FXML
    Button previousSearchListButton;
    @FXML
    Button nextSearchListButton;

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
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * updates the observable list of users (for searching)
     */
    public void updateModelUsers(String searchText) {
        if (searchText.equals("")) {
            searchFriendsList.setVisible(false);
            searchFailedLabel.setVisible(true);
        } else {
            try {
                Collection<UserDTO> foundUsers = StreamSupport.stream(service.filterUsers(searchText).spliterator(), false)
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
     * search given the search text
     */
    public void search(String text) {
        friendEmails = service.getAllFriendshipDTOsOfUser(loggedUser.getEmail()).stream()
                .map(friendshipDTO -> friendshipDTO.getUser2().getEmail())
                .collect(Collectors.toSet());
        friendRequestedEmails = service.getFriendRequestsSentByUser(loggedUser.getEmail()).stream()
                .map(friendRequestDTO -> friendRequestDTO.getReceiver().getEmail())
                .collect(Collectors.toSet());
        updateModelUsers(text);
    }

    @Override
    public void reset() {
        loggedUser = null;
        modelSearch.setAll();
        friendEmails = null;
        friendRequestedEmails = null;
    }

    /**
     * protected class that describes a user list cell for the users list
     */
    protected class UserCell extends ListCell<UserDTO> {
        private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
        private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
        HBox root = new HBox(10);
        VBox userDetails = new VBox();
        Label label = new Label("Null");
        Label emailLabel = new Label("Null");
        Region region = new Region();
        Button addFriendButton = new Button("Null");

        /**
         * user cell that has a label and a button that chan change its text
         */
        public UserCell() {
            super();
            label.setFont(new Font(25.0));
            emailLabel.setFont(new Font(10.0));

            userDetails.getChildren().addAll(label, emailLabel);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 10, 5, 10));
            root.getChildren().add(userDetails);
            HBox.setHgrow(region, Priority.ALWAYS);
            root.getChildren().add(region);

            addFriendButton.setFont(new Font(16.0));
            addFriendButton.setStyle(IDLE_BUTTON_STYLE);
            addFriendButton.setOnMouseEntered(event -> addFriendButton.setStyle(HOVERED_BUTTON_STYLE));
            addFriendButton.setOnMouseExited(event -> addFriendButton.setStyle(IDLE_BUTTON_STYLE));

            root.getChildren().addAll(addFriendButton);
        }

        @Override
        protected void updateItem(UserDTO user, boolean empty) {
            super.updateItem(user, empty);
            if (user == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(user.getFirstName() + " " + user.getLastName());
                emailLabel.setText(" " + user.getEmail());
                addFriendButton.setText("Add friend");
                addFriendButton.setDisable(loggedUser.getEmail().equals(user.getEmail()));
                if (friendEmails.contains(user.getEmail())) {
                    addFriendButton.setText("✔ Friends");
                    addFriendButton.setDisable(true);
                } else if (friendRequestedEmails.contains(user.getEmail())) {
                    addFriendButton.setText("Cancel friend request");
                    addFriendButton.setDisable(false);
                }
                addFriendButton.setOnAction(event -> {
                    if (addFriendButton.getText().equals("Add friend")) {
                        try {
                            service.sendFriendRequest(loggedUser.getEmail(), user.getEmail());
                            friendRequestedEmails.add(user.getEmail());
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
                        friendRequestedEmails.remove(user.getEmail());
                        addFriendButton.setText("Add friend");
                    }
                });

                setPrefHeight(80.0);
                setText(null);
                setGraphic(root);
            }
        }
    }
}
