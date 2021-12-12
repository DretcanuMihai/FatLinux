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
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainController extends AbstractController {
    // Data
    UserDTO loggedUser;
    ObservableList<UserDTO> modelUsers = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelPending = FXCollections.observableArrayList();
    // FXML
    @FXML
    Label welcomeLabel;
    @FXML
    TableView<UserDTO> friendsTable;
    @FXML
    TableView<UserDTO> usersTable;
    @FXML
    TableView<UserDTO> pendingTable;
    @FXML
    public TableColumn<UserDTO,String> friendsFirstNameColumn;
    @FXML
    public TableColumn<UserDTO,String> friendsLastNameColumn;
    @FXML
    public TableColumn<UserDTO,String> pendingFirstNameColumn;
    @FXML
    public TableColumn<UserDTO,String> pendingLastNameColumn;
    @FXML
    public TableColumn<UserDTO,String> usersFirstNameColumn;
    @FXML
    public TableColumn<UserDTO,String> usersLastNameColumn;

    @FXML
    public void initialize() {
        usersFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        usersLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendsLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        pendingFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        pendingLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        usersTable.setItems(modelUsers);
        friendsTable.setItems(modelFriends);
        pendingTable.setItems(modelPending);
    }

    public void init(String userEmail) {
        loggedUser=service.getUserDTO(userEmail);
        welcomeLabel.setText("Welcome, "+loggedUser.getFirstName()+"!");
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
        modelPending.setAll(service.getFriendRequestsSentToUser(loggedUser.getEmail()).stream()
                .map(FriendRequestDTO::getSender).collect(Collectors.toList()));
    }

    public void add() {
        String senderEmail=loggedUser.getEmail();
        UserDTO userDTO=usersTable.getSelectionModel().getSelectedItem();
        if(userDTO==null){
            Alert alert=new Alert(Alert.AlertType.WARNING,"No user selected!\n");
            alert.showAndWait();
            return;
        }
        String receiverEmail=userDTO.getEmail();
        try {
            service.sendFriendRequest(senderEmail, receiverEmail);
        }
        catch(ValidationException | AdministrationException e){
            Alert alert=new Alert(Alert.AlertType.WARNING,e.getMessage());
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

    public void accept() {

    }

    public void decline() {

    }
}
