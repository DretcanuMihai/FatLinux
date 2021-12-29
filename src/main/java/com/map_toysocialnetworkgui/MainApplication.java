package com.map_toysocialnetworkgui;

import com.map_toysocialnetworkgui.controllers.AbstractController;
import com.map_toysocialnetworkgui.controllers.MainController;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.map_toysocialnetworkgui.model.validators.MessageValidator;
import com.map_toysocialnetworkgui.model.validators.UserValidator;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendRequestRepositoryInterface;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendshipRepositoryInterface;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.MessageRepositoryInterface;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.UserRepositoryInterface;
import com.map_toysocialnetworkgui.repository.with_db.FriendRequestDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.FriendshipDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.MessageDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.UserDBRepository;
import com.map_toysocialnetworkgui.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    // Business
    SuperService service;
    // Loader files
    URL loginFXMLURL;
    URL mainFXMLURL;
    // Stages
    Stage primaryStage;

    @Override
    public void init() {
        initService();
        initURLs();
    }

    private void initService() {
        // Repositories
        UserRepositoryInterface userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        FriendshipRepositoryInterface friendshipRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        MessageRepositoryInterface messageDBRepository=new MessageDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");
        FriendRequestRepositoryInterface friendRequestRepository=new FriendRequestDBRepository("jdbc:postgresql://localhost:5432/SocialMediaDB",
                "postgres","postgres");

        //Validators
        UserValidator userValidator=new UserValidator();
        FriendshipValidator friendshipValidator=new FriendshipValidator();
        MessageValidator messageValidator=new MessageValidator();
        FriendRequestValidator friendRequestValidator=new FriendRequestValidator();

        //Services
        UserService userService=new UserService(userRepo, userValidator);
        FriendshipService friendshipService=new FriendshipService(friendshipRepo, friendshipValidator);
        FriendRequestService friendRequestService =new FriendRequestService(friendRequestRepository, friendRequestValidator);
        MessageService messageService=new MessageService(messageDBRepository,messageValidator);
        this.service = new SuperService(userService, friendshipService,friendRequestService, messageService);
    }

    private void initURLs(){
        loginFXMLURL=getClass().getResource("login-view.fxml");
        mainFXMLURL=getClass().getResource("main-view.fxml");
    }

    private FXMLLoader initLoader(URL url) throws IOException {
        FXMLLoader loader=new FXMLLoader(url);
        loader.load();
        AbstractController controller=loader.getController();
        controller.setService(service);
        controller.setApplication(this);
        return loader;
    }

    private void modifyMainWindowWith(FXMLLoader loader){
        Parent parent=loader.getRoot();
        Scene scene=new Scene(parent);
        primaryStage.setScene(scene);
    }

    public void changeToMain(UserUIDTO user) throws IOException {
        FXMLLoader mainLoader=initLoader(mainFXMLURL);
        MainController controller=mainLoader.getController();
        controller.init(user);
        modifyMainWindowWith(mainLoader);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage=stage;
        primaryStage.setTitle("FatLinuxApplication");
        FXMLLoader loginLoader=initLoader(loginFXMLURL);
        modifyMainWindowWith(loginLoader);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
