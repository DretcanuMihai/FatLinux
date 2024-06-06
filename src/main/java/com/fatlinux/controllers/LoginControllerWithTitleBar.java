package com.fatlinux.controllers;

import com.fatlinux.model.entities_dto.UserPage;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * controller for login view
 */
public class LoginControllerWithTitleBar extends AbstractControllerWithTitleBar {
    /**
     * FXML data
     */
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Label errorLabel;

    /**
     * logs in a user
     */
    public void login() {
        try {
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            UserPage userPage = service.login(email, password);
            errorLabel.setText("");
            application.changeToMain(userPage);
            reset();
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    /**
     * changes main window to register window
     */
    public void register() {
        application.changeToRegister();
        reset();
    }

    @Override
    public void reset() {
        emailTextField.setText("");
        passwordTextField.setText("");
        errorLabel.setText("");
    }
}
