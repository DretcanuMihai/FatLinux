package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MainController extends AbstractController{

    //Data
    UserDTO loggedUser;

    //FXML
    @FXML
    Label welcomeLabel;
    @FXML
    TableView<UserDTO> friendsTable;
    @FXML
    TableView<UserDTO> usersTable;
    @FXML
    TableView<UserDTO> pendingTable;

    public void init(String userEmail){
        loggedUser=service.getUserDTO(userEmail);
        welcomeLabel.setText("Welcome, "+loggedUser.getLastName()+"!");
    }

    public void add() {
    }

    public void delete() {
    }

    public void accept() {
    }

    public void decline() {
    }
}
