package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public class UserProfileController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    /**
     * FXML data
     */
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField joinDateTextField;
    @FXML
    TextField friendEmailTextField;
    @FXML
    DatePicker startDatePickerGeneral;
    @FXML
    DatePicker endDatePickerGeneral;
    @FXML
    DatePicker startDatePickerFriend;
    @FXML
    DatePicker endDatePickerFriend;

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void initProfileDetails() {
        this.firstNameTextField.setText(this.loggedUser.getFirstName());
        this.lastNameTextField.setText(this.loggedUser.getLastName());
        this.emailTextField.setText(this.loggedUser.getEmail());
        this.joinDateTextField.setText(this.loggedUser.getJoinDate().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy")));
    }

    public void generateGeneralReport() {

    }

    public void generateFriendReport() {

    }

    /**
     * initiates the user profile view
     */
    public void init() {
        initProfileDetails();
        reset();
    }

    @Override
    public void reset() {
        this.startDatePickerGeneral.getEditor().clear();
        this.endDatePickerGeneral.getEditor().clear();
        this.startDatePickerFriend.getEditor().clear();
        this.endDatePickerFriend.getEditor().clear();
        this.friendEmailTextField.clear();
    }
}
