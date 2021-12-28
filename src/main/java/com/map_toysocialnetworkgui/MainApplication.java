package com.map_toysocialnetworkgui;

import com.map_toysocialnetworkgui.controllers.AbstractControllerWithTitleBar;
import com.map_toysocialnetworkgui.controllers.MainControllerWithTitleBar;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.map_toysocialnetworkgui.model.validators.MessageValidator;
import com.map_toysocialnetworkgui.model.validators.UserValidator;
import com.map_toysocialnetworkgui.repository.with_db.FriendRequestDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.FriendshipDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.MessageDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.UserDBRepository;
import com.map_toysocialnetworkgui.service.FriendshipService;
import com.map_toysocialnetworkgui.service.MessageService;
import com.map_toysocialnetworkgui.service.SuperService;
import com.map_toysocialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    // Business
    SuperService service;
    // Loader files
    URL loginFXMLURL;
    URL mainFXMLURL;
    URL registerFXMLURL;
    // Stages
    Stage primaryStage;

    private void initService() {
        // Repositories
        UserDBRepository userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        FriendshipDBRepository friendshipRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        MessageDBRepository messageDBRepository=new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");
        FriendRequestDBRepository friendRequestRepository=new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres", "postgres");

        // Validators
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();
        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();

        // Services
        UserService userService = new UserService(userRepo, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, friendshipValidator,
                friendRequestRepository, friendRequestValidator);
        MessageService messageService = new MessageService(messageDBRepository, messageValidator);
        this.service = new SuperService(userService, friendshipService, messageService);
    }

    private void initURLs() {
        loginFXMLURL = getClass().getResource("views/login-view.fxml");
        mainFXMLURL = getClass().getResource("views/main-view.fxml");
        registerFXMLURL = getClass().getResource("views/register-view.fxml");
    }

    @Override
    public void init() {
        initService();
        initURLs();
    }

    private FXMLLoader initLoader(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        loader.load();
        AbstractControllerWithTitleBar controller = loader.getController();
        controller.setService(service);
        controller.setApplication(this);
        return loader;
    }

    private void modifyMainWindowWith(FXMLLoader loader){
        Parent parent = loader.getRoot();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void changeToMain(UserUIDTO user) throws IOException {
        FXMLLoader mainLoader = initLoader(mainFXMLURL);
        MainControllerWithTitleBar controller = mainLoader.getController();
        controller.init(user);
        modifyMainWindowWith(mainLoader);
    }

    public void changeToRegister() throws IOException {
        FXMLLoader registerLoader = initLoader(registerFXMLURL);
        modifyMainWindowWith(registerLoader);
    }

    public void changeToLogin() throws IOException {
        FXMLLoader loginLoader = initLoader(loginFXMLURL);
        modifyMainWindowWith(loginLoader);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loginLoader = initLoader(loginFXMLURL);
        modifyMainWindowWith(loginLoader);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
