package com.map_toysocialnetworkgui;

import com.map_toysocialnetworkgui.controllers.AbstractControllerWithTitleBar;
import com.map_toysocialnetworkgui.controllers.MainControllerWithTitleBar;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserPage;
import com.map_toysocialnetworkgui.model.validators.*;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.*;
import com.map_toysocialnetworkgui.repository.with_db.*;
import com.map_toysocialnetworkgui.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    /**
     * business
     */
    SuperService service;

    /**
     * URL files
     */
    URL loginFXMLURL;
    URL mainFXMLURL;
    URL registerFXMLURL;

    /**
     * scenes
     */
    Scene loginScene;
    Scene mainScene;
    Scene registerScene;

    /**
     * stages
     */
    Stage primaryStage;

    /**
     * controllers
     */
    MainControllerWithTitleBar mainController;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * initiates the Service
     */
    private void initService() {
        // Repositories
        UserRepositoryInterface userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        FriendshipRepositoryInterface friendshipRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        MessageRepositoryInterface messageDBRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        FriendRequestRepositoryInterface friendRequestRepository = new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        EventRepositoryInterface eventRepository = new EventDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");

        // Validators
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();
        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        EventValidator eventValidator = new EventValidator();

        // Services
        UserService userService = new UserService(userRepo, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, friendshipValidator);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository, friendRequestValidator);
        MessageService messageService = new MessageService(messageDBRepository, messageValidator);
        EventService eventService = new EventService(eventRepository, eventValidator);
        this.service = new SuperService(userService, friendshipService, friendRequestService, messageService, eventService);
    }

    /**
     * initiates FXML URLs
     */
    private void initURLs() {
        var link =getClass().getResource("views/login-view.fxml");
        loginFXMLURL = getClass().getResource("views/login-view.fxml");
        mainFXMLURL = getClass().getResource("views/main-view.fxml");
        registerFXMLURL = getClass().getResource("views/register-view.fxml");
    }

    /**
     * initiates all scenes
     *
     * @throws IOException - if any error occurs
     */
    private void initScenes() throws IOException {
        loginScene = initScene(loginFXMLURL);
        mainScene = initMainScene(mainFXMLURL);
        registerScene = initScene(registerFXMLURL);
    }

    @Override
    public void init() throws IOException {
        initService();
        initURLs();
        initScenes();
    }

    /**
     * initiates a scene described by an url
     *
     * @param url - said url
     * @throws IOException - if any error occurs
     */
    private Scene initScene(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        Parent parent = loader.load();
        AbstractControllerWithTitleBar controller = loader.getController();
        controller.setService(service);
        controller.setApplication(this);
        return new Scene(parent);
    }

    /**
     * initiates a main scene described by an url and sets the MainController for app
     *
     * @param url - said url
     * @throws IOException - if any error occurs
     */
    private Scene initMainScene(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        Parent parent = loader.load();
        mainController = loader.getController();
        mainController.setService(service);
        mainController.setApplication(this);
        return new Scene(parent);
    }

    /**
     * changes to main view
     *
     * @param userPage - currently logged-in user's page
     */
    public void changeToMain(UserPage userPage) {
        mainController.init(userPage);
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
    }

    /**
     * changes to register view
     */
    public void changeToRegister() {
        primaryStage.setScene(registerScene);
        primaryStage.centerOnScreen();
    }

    /**
     * changes to login view
     */
    public void changeToLogin() {
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        changeToLogin();
        primaryStage.show();
    }
}
