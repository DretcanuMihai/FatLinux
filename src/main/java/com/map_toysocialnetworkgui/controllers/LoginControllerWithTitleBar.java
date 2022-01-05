package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
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

            User user = service.login(email);
            Argon2 argon2 = Argon2Factory.create();
            if (argon2.verify(user.getPasswordHash(), password.toCharArray())) {
                UserUIDTO userUIDTO = new UserUIDTO(user);
                errorLabel.setText("");
                application.changeToMain(userUIDTO);
                reset();
            }
            else
                errorLabel.setText("Invalid email or password!\n");
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
