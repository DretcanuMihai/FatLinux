package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

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
            int passwordHash = passwordTextField.getText().hashCode();
            int confirmPasswordHash = confirmPasswordTextField.getText().hashCode();
            registerSuccessMessageLabel.setText("");
            registerPasswordMatchErrorLabel.setText("");

            if (passwordHash != confirmPasswordHash)
                registerPasswordMatchErrorLabel.setText("Passwords do not match!");
            else {
                service.createUserAccount(email, passwordHash, firstName, lastName);
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
     *
     * @throws IOException if an IO error occurs
     */
    public void back() throws IOException {
        application.changeToLogin();
    }
}