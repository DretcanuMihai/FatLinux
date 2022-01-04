package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import com.map_toysocialnetworkgui.utils.cryptography.EncryptorAES256GCMPassword;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;

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

            // Encrypts the password using AES-256-GCM with user email as the password
            String securedPassword = EncryptorAES256GCMPassword
                    .encrypt(password.getBytes(StandardCharsets.UTF_8), email);
            UserUIDTO user = service.login(email, securedPassword);
            errorLabel.setText("");
            application.changeToMain(user);
            reset();
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
