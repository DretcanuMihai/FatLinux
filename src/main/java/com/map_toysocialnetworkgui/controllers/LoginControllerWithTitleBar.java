package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
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
            UserUIDTO userUIDTO = service.login(email, password);
            errorLabel.setText("");
            application.changeToMain(userUIDTO);
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
