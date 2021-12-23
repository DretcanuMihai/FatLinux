package com.map_toysocialnetworkgui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController extends AbstractController {
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

    public void signUp() throws IOException {

    }

    public void cancel() throws IOException {
        application.changeToLogin();
    }
}
