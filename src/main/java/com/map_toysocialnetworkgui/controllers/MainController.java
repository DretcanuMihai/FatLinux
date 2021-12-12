package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
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
    UserDTO loggedUser;
    ObservableList<UserDTO> modelUsers = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> modelPending = FXCollections.observableArrayList();
    //FXML
    @FXML
    Label welcomeLabel;
    @FXML
    TableView<UserDTO> friendsTable;
    @FXML
    TableView<UserDTO> usersTable;
    @FXML
    TableView<FriendRequestDTO> pendingTable;
    @FXML
    public TableColumn<UserDTO, String> friendsFirstNameColumn;
    @FXML
    public TableColumn<UserDTO, String> friendsLastNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, String> pendingFirstNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, String> pendingLastNameColumn;
    @FXML
    public TableColumn<FriendRequestDTO, LocalDateTime> pendingDateColumn;
    @FXML
    public TableColumn<UserDTO, String> usersFirstNameColumn;
    @FXML
    public TableColumn<UserDTO, String> usersLastNameColumn;

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

    public void init(String userEmail) {
        loggedUser=service.getUserDTO(userEmail);
        welcomeLabel.setText("Welcome, " + loggedUser.getFirstName() + "!");
        updateModelUsers();
        updateModelFriends();
        updateModelPending();
    }

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
    }

    public void delete() {
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
}
