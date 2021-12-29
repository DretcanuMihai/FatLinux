package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.structures.UserCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class SearchFriendsController extends AbstractController {
    // Data
    UserUIDTO loggedUser;
    String searchText;
    // List
    ObservableList<String> modelSearch = FXCollections.observableArrayList();
    // FXML
    @FXML
    ListView<String> searchFriendsList;
    @FXML
    Label searchFailedLabel;

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @FXML
    public void initialize() {
        searchFriendsList.setCellFactory(param -> new UserCell());
        searchFriendsList.setItems(modelSearch);
    }

    public void updateModelUsers() {
        if (searchText.equals("")) {
            searchFriendsList.setVisible(false);
            searchFailedLabel.setVisible(true);
        } else {
            searchFriendsList.setVisible(true);
            searchFailedLabel.setVisible(false);
            // TODO

            // modelSearch.setAll();
        }
    }

    public void init() {
        updateModelUsers();
    }
}
