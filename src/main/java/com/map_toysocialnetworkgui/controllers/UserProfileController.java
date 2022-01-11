package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class UserProfileController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    /**
     * save report controller
     */
    SaveReportController saveReportController;

    /**
     * save report scene
     */
    Scene saveReportScene;

    /**
     * save report stage
     */
    Stage saveReportStage;

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
    TextField joinDateTextField;
    @FXML
    TextField friendEmailTextField;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * initiates the scene for report saving
     *
     * @throws IOException - if an IO error occurs
     */
    public void initSaveReportScene() throws IOException {
        URL saveReportURL = getClass().getResource("/com/map_toysocialnetworkgui/views/saveReport-view.fxml");
        FXMLLoader saveReportLoader = new FXMLLoader(saveReportURL);
        Parent saveReportRoot = saveReportLoader.load();
        this.saveReportController = saveReportLoader.getController();
        this.saveReportScene = new Scene(saveReportRoot);
    }

    /**
     * initializes save report window's elements
     */
    public void initSaveReportWindow() {
        this.saveReportStage = new Stage();
        this.saveReportController.setService(this.service);
        this.saveReportController.setLoggedUser(this.loggedUser);
        this.saveReportStage.setScene(this.saveReportScene);
        this.saveReportStage.initStyle(StageStyle.UNDECORATED);
        this.saveReportStage.centerOnScreen();
    }

    @FXML
    public void initialize() throws IOException {
        initSaveReportScene();
    }

    private void initProfileDetails() {
        this.firstNameTextField.setText(this.loggedUser.getFirstName());
        this.lastNameTextField.setText(this.loggedUser.getLastName());
        this.emailTextField.setText(this.loggedUser.getEmail());
        this.joinDateTextField.setText(this.loggedUser.getJoinDate().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy")));
    }

    /**
     * opens the save report window
     */
    public void openSaveReportWindow() {
        this.saveReportStage.show();
    }

    public void generateGeneralReport() {
        this.saveReportController.setReportType("general");
        this.saveReportController.setStartDate(this.startDatePicker.getValue());
        this.saveReportController.setEndDate(this.endDatePicker.getValue());
        this.saveReportController.init();
        openSaveReportWindow();
    }

    public void generateFriendReport() {
        this.saveReportController.setReportType("friend");
        this.saveReportController.setStartDate(this.startDatePicker.getValue());
        this.saveReportController.setEndDate(this.endDatePicker.getValue());
        this.saveReportController.setFriendEmail(this.friendEmailTextField.getText());
        this.saveReportController.init();
        openSaveReportWindow();
    }

    /**
     * initiates the user profile view
     */
    public void init() {
        initProfileDetails();
        initSaveReportWindow();
        reset();
    }

    @Override
    public void reset() {
        this.startDatePicker.getEditor().clear();
        this.endDatePicker.getEditor().clear();
        this.friendEmailTextField.clear();
    }
}
