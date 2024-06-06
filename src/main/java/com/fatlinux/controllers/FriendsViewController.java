package com.fatlinux.controllers;

import com.fatlinux.model.entities_dto.FriendRequestDTO;
import com.fatlinux.model.entities_dto.FriendshipDTO;
import com.fatlinux.model.entities_dto.UserDTO;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PageableImplementation;
import com.fatlinux.utils.structures.NoFocusModel;
import com.fatlinux.utils.styling.ButtonStyling;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * controller for friends view
 */
public class FriendsViewController extends AbstractController {
    /**
     * currently logged-in user
     */
    UserDTO loggedUser;

    String currentMode;

    /**
     * observable lists for friends and friend requests
     */
    ObservableList<FriendshipDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> modelRequests = FXCollections.observableArrayList();


    /**
     * FXML data
     */
    @FXML
    ListView<FriendshipDTO> friendsList;
    @FXML
    ListView<FriendRequestDTO> requestsList;
    @FXML
    Button viewFriendsButton;
    @FXML
    Button viewFriendRequestsButton;
    @FXML
    Button nextFriendsPageButton;
    @FXML
    Button previousFriendsPageButton;
    @FXML
    Label emptyListLabel;

    /**
     * button styling class
     */
    ButtonStyling buttonStyling;

    Page<FriendshipDTO> pageFriends;
    Page<FriendRequestDTO> pageRequests;

    @FXML
    public void initialize() {
        buttonStyling = new ButtonStyling();
        friendsList.setFocusModel(new NoFocusModel<>());
        friendsList.setCellFactory(param -> new FriendCell());
        friendsList.setItems(modelFriends);
        requestsList.setFocusModel(new NoFocusModel<>());
        requestsList.setCellFactory(param -> new FriendRequestCell());
        requestsList.setItems(modelRequests);
    }

    /**
     * sets the currently logged-in user
     *
     * @param loggedUser - said user
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }
    public Page<FriendshipDTO> getFriendsPage(Pageable pageable){
        return service.getAllFriendshipDTOsOfUser(loggedUser.getEmail(),pageable);
    }

    public boolean hasNextFriends(){
        return getFriendsPage(pageFriends.nextPageable()).getContent().count()!=0;
    }

    public boolean hasPreviousFriends(){
        return pageFriends.getPageable().getPageNumber()!=1;
    }

    public void prepareButtonsFriends(){
        nextFriendsPageButton.setOnAction(event -> {
            setFriendsPage(pageFriends.nextPageable());
        });
        previousFriendsPageButton.setOnAction(event -> {
            setFriendsPage(pageFriends.previousPageable());
        });
        previousFriendsPageButton.setVisible(hasPreviousFriends());
        nextFriendsPageButton.setVisible(hasNextFriends());
    }

    public boolean hasNextRequests(){
        return getRequestsPage(pageRequests.nextPageable()).getContent().count()!=0;
    }

    public boolean hasPreviousRequests(){
        return pageRequests.getPageable().getPageNumber()!=1;
    }

    public void prepareButtonsRequests(){
        nextFriendsPageButton.setOnAction(event -> {
            setRequestsPage(pageRequests.nextPageable());
        });
        previousFriendsPageButton.setOnAction(event -> {
            setRequestsPage(pageRequests.previousPageable());
        });
        previousFriendsPageButton.setVisible(hasPreviousRequests());
        nextFriendsPageButton.setVisible(hasNextRequests());
    }

    public void setFriendsPage(Pageable pageable){
        pageFriends=getFriendsPage(pageable);
        updateModelFriends();
        viewAllFriends();
        prepareButtonsFriends();
    }
    public Page<FriendRequestDTO> getRequestsPage(Pageable pageable){
        return service.getFriendRequestsSentToUser(loggedUser.getEmail(),pageable);
    }
    public void setRequestsPage(Pageable pageable){
        pageRequests=getRequestsPage(pageable);
        updateModelRequests();
        viewFriendRequests();
        prepareButtonsRequests();
    }

    /**
     * updates the observable list of friends
     */
    public void updateModelFriends() {
        Collection<FriendshipDTO> allFriends = pageFriends.getContent().collect(Collectors.toList());
        modelFriends.setAll(allFriends);
    }

    /**
     * updates the observable list of friend requests
     */
    public void updateModelRequests() {
        Collection<FriendRequestDTO> allFriendRequests = pageRequests.getContent().collect(Collectors.toList());
        modelRequests.setAll(allFriendRequests);
    }

    /**
     * hides the friend request list and shows the friends list
     */
    public void viewAllFriends() {
        buttonStyling.setButtonOrange(viewFriendsButton);
        buttonStyling.setButtonBlack(viewFriendRequestsButton);
        if (this.friendsList.getItems().isEmpty()) {
            this.emptyListLabel.setText("No friends to show :(");
            this.friendsList.setVisible(false);
            this.requestsList.setVisible(false);
            this.emptyListLabel.setVisible(true);
        } else {
            this.emptyListLabel.setVisible(false);
            this.friendsList.setVisible(true);
            this.requestsList.setVisible(false);
        }
    }

    /**
     * hides the friends list and shows the friend requests list
     */
    public void viewFriendRequests() {
        buttonStyling.setButtonOrange(viewFriendRequestsButton);
        buttonStyling.setButtonBlack(viewFriendsButton);
        if (this.requestsList.getItems().isEmpty()) {
            this.emptyListLabel.setText("No friend requests :/");
            this.friendsList.setVisible(false);
            this.requestsList.setVisible(false);
            this.emptyListLabel.setVisible(true);
        } else {
            this.emptyListLabel.setVisible(false);
            this.friendsList.setVisible(false);
            this.requestsList.setVisible(true);
        }
    }

    /**
     * initiates the lists
     * friends list is by default visible
     */
    public void init() {
        setFriendsPage(new PageableImplementation(1,5));
    }

    @Override
    public void reset() {
        super.reset();
        modelFriends.setAll();
        modelRequests.setAll();
    }

    public void viewFriends() {
        setFriendsPage(new PageableImplementation(1,5));
    }

    public void viewRequests() {
        setRequestsPage(new PageableImplementation(1,5));
    }

    /**
     * protected class that describes a friend list cell for the friends list
     */
    protected class FriendCell extends ListCell<FriendshipDTO> {
        private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
        private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
        HBox root = new HBox(10);
        VBox userDetails = new VBox();
        ImageView userProfilePicture = new ImageView("com/fatlinux/images/defaultProfileIcon.png");
        Label label = new Label("Null");
        Label emailLabel = new Label("Null");
        Region region = new Region();
        Button removeFriendButton = new Button("Remove friend");

        /**
         * friend cell that has a label with the friend's complete name and a button
         */
        public FriendCell() {
            super();
            label.setFont(new Font(25.0));
            emailLabel.setFont(new Font(10.0));

            userDetails.getChildren().addAll(label, emailLabel);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 10, 5, 10));
            root.getChildren().addAll(userProfilePicture, userDetails);
            HBox.setHgrow(region, Priority.ALWAYS);
            root.getChildren().add(region);

            removeFriendButton.setFont(new Font(16.0));
            removeFriendButton.setStyle(IDLE_BUTTON_STYLE);
            removeFriendButton.setOnMouseEntered(event -> removeFriendButton.setStyle(HOVERED_BUTTON_STYLE));
            removeFriendButton.setOnMouseExited(event -> removeFriendButton.setStyle(IDLE_BUTTON_STYLE));

            root.getChildren().addAll(removeFriendButton);
        }

        @Override
        protected void updateItem(FriendshipDTO friendshipDTO, boolean empty) {
            super.updateItem(friendshipDTO, empty);
            if (friendshipDTO == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(friendshipDTO.getUser2().getFirstName() + " " + friendshipDTO.getUser2().getLastName());
                emailLabel.setText(" " + friendshipDTO.getUser2().getEmail());
                removeFriendButton.setOnAction(event -> {
                    service.deleteFriendship(loggedUser.getEmail(), friendshipDTO.getUser2().getEmail());
                    if(modelFriends.size()!=1)
                        setFriendsPage(pageFriends.getPageable());
                    else{
                        if(hasPreviousFriends())
                            setFriendsPage(pageFriends.previousPageable());
                        else
                            setFriendsPage(pageFriends.getPageable());
                    }
                });

                setPrefHeight(80.0);
                setText(null);
                setGraphic(root);
            }
        }
    }

    /**
     * protected class that describes a friend request list cell for the friend requests list
     */
    protected class FriendRequestCell extends ListCell<FriendRequestDTO> {
        private static final String IDLE_BUTTON_STYLE = "-fx-focus-traversable: false; -fx-background-radius: 10px; -fx-background-color: #ff7700;";
        private static final String HOVERED_BUTTON_STYLE = IDLE_BUTTON_STYLE + "-fx-background-color: #F04A00";
        HBox root = new HBox(10);
        VBox userDetails = new VBox();
        ImageView userProfilePicture = new ImageView("com/fatlinux/images/defaultProfileIcon.png");
        Label label = new Label("Null");
        Label emailLabel = new Label("Null");
        Region region = new Region();
        Button acceptButton = new Button("Accept");
        Button declineButton = new Button("Decline");

        /**
         * friend request cell that has a label with the sender's complete name and 2 buttons
         */
        public FriendRequestCell() {
            super();
            label.setFont(new Font(25.0));
            emailLabel.setFont(new Font(10.0));

            userDetails.getChildren().addAll(label, emailLabel);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 10, 5, 10));
            root.getChildren().addAll(userProfilePicture, userDetails);
            HBox.setHgrow(region, Priority.ALWAYS);
            root.getChildren().add(region);

            acceptButton.setFont(new Font(16.0));
            acceptButton.setStyle(IDLE_BUTTON_STYLE);
            acceptButton.setOnMouseEntered(event -> acceptButton.setStyle(HOVERED_BUTTON_STYLE));
            acceptButton.setOnMouseExited(event -> acceptButton.setStyle(IDLE_BUTTON_STYLE));

            declineButton.setFont(new Font(16.0));
            declineButton.setStyle(IDLE_BUTTON_STYLE);
            declineButton.setOnMouseEntered(event -> declineButton.setStyle(HOVERED_BUTTON_STYLE));
            declineButton.setOnMouseExited(event -> declineButton.setStyle(IDLE_BUTTON_STYLE));

            root.getChildren().addAll(acceptButton, declineButton);
        }

        @Override
        protected void updateItem(FriendRequestDTO friendRequestDTO, boolean empty) {
            super.updateItem(friendRequestDTO, empty);
            if (friendRequestDTO == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(friendRequestDTO.getSenderFirstName() + " " + friendRequestDTO.getSenderLastName());
                emailLabel.setText(" " + friendRequestDTO.getSender().getEmail());
                acceptButton.setOnAction(event -> {
                    service.confirmFriendRequest(friendRequestDTO.getSender().getEmail(), friendRequestDTO.getReceiver().getEmail(), true);
                    if(modelRequests.size()!=1)
                        setRequestsPage(pageRequests.getPageable());
                    else{
                        if(hasPreviousRequests())
                            setRequestsPage(pageRequests.previousPageable());
                        else
                            setRequestsPage(pageRequests.getPageable());
                    }
                });
                declineButton.setOnAction(event -> {
                    service.confirmFriendRequest(friendRequestDTO.getSender().getEmail(), friendRequestDTO.getReceiver().getEmail(), false);
                    if(modelRequests.size()!=1)
                        setRequestsPage(pageRequests.getPageable());
                    else{
                        if(hasPreviousRequests())
                            setRequestsPage(pageRequests.previousPageable());
                        else
                            setRequestsPage(pageRequests.getPageable());
                    }
                });

                setPrefHeight(80.0);
                setText(null);
                setGraphic(root);
            }
        }
    }
}
