package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.structures.FriendCell;
import com.map_toysocialnetworkgui.utils.structures.FriendRequestCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.Collection;

/**
 * controller for friends view
 */
public class FriendsViewController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserUIDTO loggedUser;

    /**
     * observable lists for friends and friend requests
     */
    ObservableList<String> modelFriends = FXCollections.observableArrayList();
    ObservableList<String> modelRequests = FXCollections.observableArrayList();

    /**
     * FXML data
     */
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

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * updates the observable list of friends
     */
    public void updateModelFriends() {
        Collection<String> friendCompleteName = service.getAllFriendshipDTOsOfUser(loggedUser.getEmail()).stream()
                .map(FriendshipDTO::getUser2).toList().stream()
                .map(userUIDTO -> userUIDTO.getFirstName() + " " + userUIDTO.getLastName()).toList();
        modelFriends.setAll(friendCompleteName);
    }

    /**
     * updates the observable list of friend requests
     */
    public void updateModelRequests() {
        Collection<String> senderCompleteName = service.getFriendRequestsSentToUser(loggedUser.getEmail()).stream()
                .map(FriendRequestDTO::getSender).toList().stream()
                .map(userUIDTO -> userUIDTO.getFirstName() + " " + userUIDTO.getLastName()).toList();
        modelRequests.setAll(senderCompleteName);
    }

    /**
     * hides the friend request list and shows the friends list
     */
    public void viewAllFriends() {
        this.friendsList.setVisible(true);
        this.requestsList.setVisible(false);
    }

    /**
     * hides the friends list and shows the friend requests list
     */
    public void viewFriendRequests() {
        this.friendsList.setVisible(false);
        this.requestsList.setVisible(true);
    }

    /**
     * initiates the lists
     * friends list is by default visible
     */
    public void init() {
        updateModelFriends();
        updateModelRequests();
        this.friendsList.setVisible(true);
        this.requestsList.setVisible(false);
    }
}
