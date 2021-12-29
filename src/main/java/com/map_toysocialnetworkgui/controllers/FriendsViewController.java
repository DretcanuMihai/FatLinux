package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.structures.FriendCell;
import com.map_toysocialnetworkgui.utils.structures.FriendRequestCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collection;

public class FriendsViewController extends AbstractController {
    // Data
    UserUIDTO loggedUser;
    // List
    ObservableList<String> modelFriends = FXCollections.observableArrayList();
    ObservableList<String> modelRequests = FXCollections.observableArrayList();
    // FXML
    @FXML
    ListView<String> friendsList;
    @FXML
    ListView<String> requestsList;
    @FXML
    Button viewFriendsButton;
    @FXML
    Button viewFriendRequestsButton;

    @FXML
    public void initialize() {
        friendsList.setCellFactory(param -> new FriendCell());
        friendsList.setItems(modelFriends);
        requestsList.setCellFactory(param -> new FriendRequestCell());
        requestsList.setItems(modelRequests);
    }

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void updateModelFriends() {
        Collection<String> friendCompleteName = service.getAllFriendshipDTOsOfUser(loggedUser.getEmail()).stream()
                .map(FriendshipDTO::getUser2).toList().stream()
                .map(userUIDTO -> userUIDTO.getFirstName() + " " + userUIDTO.getLastName()).toList();
         modelFriends.setAll(friendCompleteName);
    }

    public void updateModelRequests() {
        Collection<String> senderCompleteName = service.getFriendRequestsSentToUser(loggedUser.getEmail()).stream()
                .map(FriendRequestDTO::getSender).toList().stream()
                .map(userUIDTO -> userUIDTO.getFirstName() + " " + userUIDTO.getLastName()).toList();
        modelRequests.setAll(senderCompleteName);
    }

    public void viewAllFriends() {
        this.friendsList.setVisible(true);
        this.requestsList.setVisible(false);
    }

    public void viewFriendRequests() {
        this.friendsList.setVisible(false);
        this.requestsList.setVisible(true);
    }

    public void init() {
        updateModelFriends();
        updateModelRequests();
        this.friendsList.setVisible(true);
        this.requestsList.setVisible(false);
    }
}
