package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.structures.FriendCell;
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
    // FXML
    @FXML
    ListView<String> friendsList;

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
