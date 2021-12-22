package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends AbstractController{
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Label errorLabel;


    public void login() throws IOException {
        try {
            String email=emailTextField.getText();
            UserUIDTO user=service.login(email, passwordTextField.getText().hashCode());
            errorLabel.setText("");
            application.changeToMain(user);
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    public void register() {
    }
}