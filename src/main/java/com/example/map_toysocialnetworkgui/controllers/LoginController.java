package com.example.map_toysocialnetworkgui.controllers;

import com.example.map_toysocialnetworkgui.model.validators.ValidationException;
import com.example.map_toysocialnetworkgui.service.AdministrationException;
import com.example.map_toysocialnetworkgui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private SuperService service;
    FXMLLoader mainLoader;


    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Label errorLabel;
    @FXML
    Button loginButton;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void login() throws IOException {
        try {
            String email=emailTextField.getText();
            service.login(email, passwordTextField.getText().hashCode());
            startMainApplication(email);
        } catch (ValidationException | AdministrationException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }
    public void startMainApplication(String userEmail) throws IOException {
        Stage stage=(Stage)loginButton.getScene().getWindow();
        stage.close();
        initMainLoader();
        Stage newStage=new Stage();
        initMainView(newStage);
        newStage.show();
    }
    private void initMainLoader(){
        mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource("main-view.fxml"));
    }

    private void initMainView(Stage stage) throws IOException {
        Scene scene = new Scene(mainLoader.load(), 450, 277);
        LoginController loginController = mainLoader.getController();
        loginController.setService(service);
        stage.setTitle("ToySocialNetworkFATLinux!");
        stage.setScene(scene);
    }

    public void register() {
    }
}