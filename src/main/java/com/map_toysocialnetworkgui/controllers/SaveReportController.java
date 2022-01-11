package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.service.AdministrationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * controller for save report view
 */
public class SaveReportController extends AbstractControllerWithTitleBar {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    /**
     * FXML data
     */
    @FXML
    Button closeSaveReportButton;
    @FXML
    Button saveReportButton;
    @FXML
    TextField fileNameTextField;
    @FXML
    BorderPane mainBorderPane;

    /**
     * type of report
     */
    String reportType;

    /**
     * friend email for friend report
     */
    String friendEmail;

    /**
     * start and end date for reports
     */
    LocalDate startDate, endDate;

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * sets the report type
     *
     * @param reportType - said report type
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * sets the friend email for report
     *
     * @param friendEmail - said friend email
     */
    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    /**
     * sets start date for report
     *
     * @param startDate - said start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * sets end date for report
     *
     * @param endDate - said end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * makes the exit button able to only close the associated window
     */
    @Override
    public void initAppExitButton() {
        this.setAppExitButtonToOnlyCloseWindow();
    }

    public void init() {
        mainBorderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        reset();
    }

    /**
     * saves a certain type of report
     */
    public void saveReport() {
        if (this.reportType.equals("general")) {
            try {
                this.service.reportActivities(this.loggedUser.getEmail(), this.startDate, this.endDate, this.fileNameTextField.getText());
                this.closeSaveReportWindow();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Report generated!");
                alert.setContentText("The report has been successfully generated! See data folder for the generated .pdf report!");
                alert.showAndWait();
            } catch (ValidationException | AdministrationException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Save report warning!");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        } else if (this.reportType.equals("friend")) {
            if (this.friendEmail.equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setHeaderText("Save report warning!");
                alert.setContentText("Friend email cannot be empty!");
                alert.showAndWait();
            } else
                try {
                    this.service.reportConversation(this.loggedUser.getEmail(), this.friendEmail, this.startDate, this.endDate, this.fileNameTextField.getText());
                    this.closeSaveReportWindow();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText("Report generated!");
                    alert.setContentText("The report has been successfully generated! See data folder for the generated .pdf report!");
                    alert.showAndWait();
                } catch (ValidationException | AdministrationException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setHeaderText("Save report warning!");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
        }
    }

    /**
     * closes the window
     */
    public void closeSaveReportWindow() {
        ((Stage) this.closeSaveReportButton.getScene().getWindow()).close();
    }

    @Override
    public void reset() {
        this.fileNameTextField.clear();
    }
}
