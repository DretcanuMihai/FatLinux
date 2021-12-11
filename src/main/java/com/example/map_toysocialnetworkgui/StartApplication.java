package com.example.map_toysocialnetworkgui.gui;

import com.example.map_toysocialnetworkgui.gui.LoginController;
import com.example.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.example.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.example.map_toysocialnetworkgui.model.validators.MessageValidator;
import com.example.map_toysocialnetworkgui.model.validators.UserValidator;
import com.example.map_toysocialnetworkgui.repository.with_db.FriendRequestDBRepository;
import com.example.map_toysocialnetworkgui.repository.with_db.FriendshipDBRepository;
import com.example.map_toysocialnetworkgui.repository.with_db.MessageDBRepository;
import com.example.map_toysocialnetworkgui.repository.with_db.UserDBRepository;
import com.example.map_toysocialnetworkgui.service.FriendshipService;
import com.example.map_toysocialnetworkgui.service.MessageService;
import com.example.map_toysocialnetworkgui.service.SuperService;
import com.example.map_toysocialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StartApplication extends Application {
    // Business
    SuperService service;
    // Loaders
    FXMLLoader loginLoader;

    public void initService() {
        // Repositories
        UserDBRepository userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        FriendshipDBRepository friendshipRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        MessageDBRepository messageDBRepository=new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        FriendRequestDBRepository friendRequestRepository=new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");

        //Validators
        UserValidator userValidator=new UserValidator();
        FriendshipValidator friendshipValidator=new FriendshipValidator();
        MessageValidator messageValidator=new MessageValidator();
        FriendRequestValidator friendRequestValidator=new FriendRequestValidator();

        //Services
        UserService userService=new UserService(userRepo, userValidator);
        FriendshipService friendshipService=new FriendshipService(friendshipRepo, friendshipValidator,
                friendRequestRepository, friendRequestValidator);
        MessageService messageService=new MessageService(messageDBRepository,messageValidator);
        this.service = new SuperService(userService, friendshipService, messageService);
    }

    private void initLoaders() {
        loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("login-view.fxml"));
    }

    private void initView(Stage stage) throws IOException {
        Scene scene = new Scene(loginLoader.load(), 450, 277);
        LoginController loginController = loginLoader.getController();
        loginController.setService(service);
        stage.setTitle("ToySocialNetworkFATLinux!");
        stage.setScene(scene);
    }

    @Override
    public void start(Stage stage) throws IOException {
        initService();
        initLoaders();
        initView(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
