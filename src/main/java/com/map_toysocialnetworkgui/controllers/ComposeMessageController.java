package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ComposeMessageController extends AbstractControllerWithTitleBar {
    // Data
    UserUIDTO loggedUser;
    // FXML
    @FXML
    Button closeComposeMessageWindowButton;

    @Override
    public void initAppExitButton() {
        Image exitButtonImage = appExitButton.getImage();
        appExitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) appExitButton.getScene().getWindow();
            stage.close();
        });
        appExitButton.setOnMouseEntered(event -> {
            appExitButton.setImage(appExitHoveredButton);
        });
        appExitButton.setOnMouseExited(event -> {
            appExitButton.setImage(exitButtonImage);
        });
    }

    public void setLoggedUser(UserUIDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /*
    public void sendMessage() {
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

     */

    public void close() {
        ((Stage) closeComposeMessageWindowButton.getScene().getWindow()).close();
    }
}
