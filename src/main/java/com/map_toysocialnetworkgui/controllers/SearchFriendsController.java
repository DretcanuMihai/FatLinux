package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.utils.structures.UserCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
    ObservableList<String> modelSearch = FXCollections.observableArrayList();

    /**
     * FXML data
     */
    @FXML
    ListView<String> searchFriendsList;
    @FXML
    Label searchFailedLabel;

    @FXML
    public void initialize() {
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
            searchFriendsList.setVisible(true);
            searchFailedLabel.setVisible(false);
            // TODO

            // modelSearch.setAll();
        }
    }

    /**
     * initiates the list
     */
    public void init() {
        updateModelUsers();
    }
}
