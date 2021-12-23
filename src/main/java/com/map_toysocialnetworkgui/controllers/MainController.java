package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainController extends AbstractController {
    //Data
    UserUIDTO loggedUser;
    /*
    ObservableList<UserUIDTO> modelUsers = FXCollections.observableArrayList();
    ObservableList<UserUIDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> modelPending = FXCollections.observableArrayList();
    // FXML
    @FXML
    Label welcomeLabel;
    @FXML
    TableView<UserUIDTO> friendsTable;
    @FXML
    TableView<UserUIDTO> usersTable;
    @FXML
    TableView<FriendRequestDTO> pendingTable;

    @FXML
    public TableColumn<UserUIDTO, String> friendsFirstNameColumn;
    @FXML
    public TableColumn<UserUIDTO, String> friendsLastNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, String> pendingFirstNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, String> pendingLastNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, LocalDateTime> pendingDateColumn;
    @FXML
    public TableColumn<UserUIDTO, String> usersFirstNameColumn;
    @FXML
    public TableColumn<UserUIDTO, String> usersLastNameColumn;

    @FXML
    public void initialize() {
        usersFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        usersLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendsLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        pendingFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("senderFirstName"));
        pendingLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("senderLastName"));
        pendingDateColumn.setCellValueFactory(new PropertyValueFactory<>("sendTime"));

        usersTable.setItems(modelUsers);
        friendsTable.setItems(modelFriends);
        pendingTable.setItems(modelPending);
    }
    */
    public void init(UserUIDTO user) {
        loggedUser=user;
        /*
        welcomeLabel.setText("Welcome, " + loggedUser.getFirstName() + "!");
        updateModelUsers();
        updateModelFriends();
        updateModelPending();
        */
    }
    /*
    private void updateModelUsers() {
        modelUsers.setAll(new ArrayList<>(service.getAllUserDTOs()));
    }

    private void updateModelFriends() {
        modelFriends.setAll(service.getAllFriendshipDTOsOfUser(loggedUser.getEmail()).stream()
                .map(FriendshipDTO::getUser2).collect(Collectors.toList()));
    }

    private void updateModelPending() {
        modelPending.setAll(service.getFriendRequestsSentToUser(loggedUser.getEmail()));
    }

    public void add() {
        String senderEmail = loggedUser.getEmail();
        UserUIDTO userUIDTO = usersTable.getSelectionModel().getSelectedItem();
        if(userUIDTO == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Empty selection error!");
            alert.setContentText("You must select an user from the list in order to add a friend!");
            alert.showAndWait();
            return;
        }
        String receiverEmail = userUIDTO.getEmail();
        try {
            service.sendFriendRequest(senderEmail, receiverEmail);
        } catch (ValidationException | AdministrationException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Friend request warning!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void delete() {
        if (friendsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Empty selection error");
            alert.setContentText("You must select a friend from friends table in order to remove it!");
            alert.showAndWait();
        } else {
            String toDelete = friendsTable.getSelectionModel().getSelectedItem().getEmail();
            service.deleteFriendship(loggedUser.getEmail(), toDelete);
            updateModelFriends();
        }
    }

    private void confirm(boolean accepted) {
        if (pendingTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Empty selection error");
            alert.setContentText("You must select a pending friend request in order to accept it!");
            alert.showAndWait();
        } else {
            try {
                String sender = pendingTable.getSelectionModel().getSelectedItem().getSender().getEmail();
                service.confirmFriendRequest(sender, loggedUser.getEmail(), accepted);
                updateModelPending();
                updateModelFriends();
            } catch (ValidationException | AdministrationException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Friend request warning");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void accept() {
        confirm(true);
    }

    public void decline() {
        confirm(false);
    }
     */
}
