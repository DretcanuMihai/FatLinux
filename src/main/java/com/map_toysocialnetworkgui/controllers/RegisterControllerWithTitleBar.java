package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * controller for register view
 */
public class RegisterControllerWithTitleBar extends AbstractControllerWithTitleBar {
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
    PasswordField passwordTextField;
    @FXML
    PasswordField confirmPasswordTextField;
    @FXML
    Label registerSuccessMessageLabel;
    @FXML
    Label registerPasswordMatchErrorLabel;
    @FXML
    Label passwordLengthErrorLabel;

    /**
     * clears all text fields and text areas
     */
    public void clearAllFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        emailTextField.clear();
        passwordTextField.clear();
        confirmPasswordTextField.clear();
    }

    /**
     * signs up a user
     * raises a warning window if there are any exceptions
     */
    public void signUp() {
        try {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            registerSuccessMessageLabel.setText("");
            registerPasswordMatchErrorLabel.setText("");
            passwordLengthErrorLabel.setText("");

            if (password.length() < 8)
                passwordLengthErrorLabel.setText("Password has to be at least 8 characters long!");
            else if (!password.equals(confirmPassword))
                registerPasswordMatchErrorLabel.setText("Passwords do not match!");
            else {
                service.createUserAccount(email, password, firstName, lastName);
                registerSuccessMessageLabel.setText("Account created successfully!");
                clearAllFields();
            }
        } catch (ValidationException | AdministrationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Register warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * changes the register window to log in window
     */
    public void back() {
        application.changeToLogin();
        reset();
    }

    @Override
    public void reset() {
        clearAllFields();
    }
}
