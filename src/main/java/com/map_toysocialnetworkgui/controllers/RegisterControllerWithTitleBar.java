package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserServiceDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterControllerWithTitleBar extends AbstractControllerWithTitleBar {
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
                UserServiceDTO userServiceDTO = new UserServiceDTO(email, firstName, lastName, passwordHash);
                service.createUserAccount(userServiceDTO);
                registerSuccessMessageLabel.setText("Account created successfully!");
                firstNameTextField.clear();
                lastNameTextField.clear();
                emailTextField.clear();
                passwordTextField.clear();
                confirmPasswordTextField.clear();
            }
        } catch (ValidationException | AdministrationException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Register warning!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void back() throws IOException {
        application.changeToLogin();
    }
}
