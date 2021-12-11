package com.example.map_toysocialnetworkgui;

import com.example.map_toysocialnetworkgui.model.validators.ValidationException;
import com.example.map_toysocialnetworkgui.service.AdministrationException;
import com.example.map_toysocialnetworkgui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private SuperService service;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Label errorLabel;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void login() {
        try {
            service.login(emailTextField.getText(), passwordTextField.getText().hashCode());
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    public void register() {
    }
}