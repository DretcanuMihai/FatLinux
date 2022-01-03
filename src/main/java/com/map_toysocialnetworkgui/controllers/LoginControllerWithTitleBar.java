package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

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
     *
     * @throws IOException if an IO error occurs
     */
    public void login() throws IOException {
        try {
            String email = emailTextField.getText();
            UserUIDTO user = service.login(email, passwordTextField.getText().hashCode());
            errorLabel.setText("");
            application.changeToMain(user);
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    /**
     * changes main window to register window
     *
     * @throws IOException if an IO error occurs
     */
    public void register() throws IOException {
        application.changeToRegister();
        this.reset();
    }

    @Override
    public void reset() {
        this.reset();
        emailTextField.setText("");
        passwordTextField.setText("");
        errorLabel.setText("");
    }
}
