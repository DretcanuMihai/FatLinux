package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.*;
import com.map_toysocialnetworkgui.model.validators.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * controller class - it controls all services used in the application
 */
public class SuperService {
    /**
     * associated UserService
     */
    private final UserService userService;
    /**
     * associated FriendshipService
     */
    private final FriendshipService friendshipService;
    /**
     * associated MessageService
     */
    private final MessageService messageService;

    /**
     * Creates a Controller with said services
     * @param userService - the User Service
     * @param friendshipService - the Friendship Service
     */
    public SuperService(UserService userService, FriendshipService friendshipService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService=messageService;
    }

    /**
     * adds a user to the repo
     *
     * @param dto - dto containing needed information
     * @throws ValidationException - if the user data is invalid
     * @throws AdministrationException       - if the email is already in use
     */
    public void createUserAccount(UserServiceDTO dto)
            throws ValidationException, AdministrationException {
        userService.createUserAccount(dto);
    }

    /**
     * modifies the account identified by email with the other given information
     *
     * @param dto - needed data
     * @throws ValidationException if any of the data is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void updateUser(UserServiceDTO dto)
            throws ValidationException, AdministrationException {

        userService.updateUserAccountInfo(dto);
    }

    /**
     * deletes the user with the given email
     * @param email - user email
     * @throws ValidationException if the email is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void deleteUserAccount(String email)throws ValidationException, AdministrationException {
        userService.deleteUserAccount(email);
    }

    /**
     * returns a collection of all user data in the form of UserDTOs
     * @return said collection
     */
    public Collection<UserUIDTO> getAllUserDTOs() {
        Collection<UserUIDTO> userUIDTOS =new ArrayList<>();
        userService.getAllUsers().forEach(user -> userUIDTOS.add(new UserUIDTO(user)));
        return userUIDTOS;
    }

    /**
     * ඞ
     * deletes a friendship between two users identified by their emails
     * @param requester requester's email
     * @param friendEmail friend's email
     * @throws ValidationException if emails are invalid
     * @throws AdministrationException if no users with said emails exist or if they aren't friend or
     *                                 if the requester is disabled
     */
    public void deleteFriendship(String requester,String friendEmail)throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(requester,friendEmail));
        friendshipService.deleteFriendship(requester,friendEmail);
    }

    /**
     * returns information of friendship between two users identified by email
     * @param requester - first user's email
     * @param friendEmail - second user's email
     * @return a FriendshipDTO with said information
     * @throws ValidationException if any validation error occurs
     * @throws AdministrationException if any administration error occurs
     */
    public FriendshipDTO getFriendshipDTO(String requester, String friendEmail)
            throws ValidationException,AdministrationException{

        userService.verifyEmailCollection(List.of(requester,friendEmail));
        User user1=userService.getUserInfo(requester);
        User user2=userService.getUserInfo(friendEmail);
        Friendship friendship=friendshipService.getFriendship(requester,friendEmail);
        return new FriendshipDTO(friendship,user1,user2);
    }

    /**
     * returns a collection of all friendship data in the form of FriendshipDTOs
     * @return said collection
     */
    @Deprecated
    public Collection<FriendshipDTO> getAllFriendshipDTOs() {
        Collection<FriendshipDTO> friendshipDTOS=new ArrayList<>();
        friendshipService.getAllFriendships().forEach(friendship -> {
            User u1=userService.getUserInfo(friendship.getEmails().getFirst());
            User u2=userService.getUserInfo(friendship.getEmails().getSecond());
            friendshipDTOS.add(new FriendshipDTO(friendship,u1,u2));
        });
        return friendshipDTOS;
    }

    /**
     * Returns a collection of all friendship data of one user identified by email in the form of FriendshipDTOs
     * @param userEmail - the user's email
     * @return said collection
     * @throws ValidationException if email is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOsOfUser(String userEmail) throws ValidationException, AdministrationException {
        User user = userService.getUserInfo(userEmail);

        Collection<FriendshipDTO> friendshipDTOS=new ArrayList<>();
        friendshipService.getUserFriendships(userEmail).forEach(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();

            if(friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();

            User friend = userService.getUserInfo(friendEmail);
            friendshipDTOS.add(new FriendshipDTO(friendship, user, friend));
        });
        return friendshipDTOS;
    }

    /**
     * Returns all friendships of a user that were created in a specific month as DTOs
     * @param userEmail - email of user
     * @param month - month in which the friendship was created
     * @return a collection of said friendshipDTOs
     * @throws ValidationException if month is invalid or userEmail is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOsOfUserFromMonth(String userEmail, int month) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        User user = userService.getUserInfo(userEmail);

        Collection<FriendshipDTO> friendshipDTOS=new ArrayList<>();
        friendshipService.getUserFriendshipsFromMonth(userEmail, month).forEach(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();

            if(friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();

            User friend = userService.getUserInfo(friendEmail);
            friendshipDTOS.add(new FriendshipDTO(friendship, user, friend));
        });
        return friendshipDTOS;
    }

    /**
     * sends a root message (a message that isn't a reply) to some users
     * @param dto - needed info
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendRootMessage(MessageDTO dto)
            throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {

        if(dto==null)
            throw new ValidationException("Error:dto shouldn't be null;\n");

        userService.getUserInfo(dto.getFromEmail());
        userService.verifyEmailCollection(dto.getToEmails());
        messageService.addRootMessage(dto);
    }

    /**
     * sends a reply message to another message
     * the receiver will be the sender of the original message
     * @param dto - needed info
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyMessage(MessageDTO dto)
            throws ValidationException, AdministrationException {
        userService.getUserInfo(dto.getFromEmail());
        messageService.addReplyMessage(dto);
    }

    /**
     * sends a reply message to another message
     * the receiver will be the sender of the original message and all the original receivers
     * except the replier
     * @param dto - needed data
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyAllMessage(MessageDTO dto)
            throws ValidationException, AdministrationException {
        userService.getUserInfo(dto.getFromEmail());
        messageService.addReplyAllMessage(dto);
    }

    /**
     * returns the conversation between two users sorted chronologically
     *
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return a list of DTOs for said messages
     * @throws AdministrationException if the users do not exist
     * @throws ValidationException if emails are the same
     */
    public List<MessageDTO> getConversation(String email1, String email2) throws ValidationException, AdministrationException {
        userService.getUserInfo(email1);
        userService.getUserInfo(email2);
        List<MessageDTO> messageDTOS=new ArrayList<>();
        messageService.getConversationBetweenUsers(email1, email2).
                forEach(message -> messageDTOS.add(new MessageDTO(message)));
        return messageDTOS;
    }

    /**
     * sends a friend request from sender to receiver
     * @param sender - sender's email
     * @param receiver - receiver's email
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if any administration error occurs
     */
    public void sendFriendRequest(String sender, String receiver)throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(sender,receiver));
        friendshipService.sendFriendRequest(sender,receiver);
    }

    /**
     * confirms a friend request based on accepted status
     *
     * @param sender - sender's email
     * @param receiver - receiver's email
     * @param accepted - acceptance status (true is accept, false decline)
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void confirmFriendRequest(String sender, String receiver, boolean accepted) throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(sender, receiver));
        friendshipService.confirmFriendRequest(sender, receiver, accepted);
    }

    /**
     * gets a collection of DTOs of all the friend requests sent to a user
     *
     * @param userEmail - said user's email
     * @return said collection
     * @throws ValidationException if the user email is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Collection<FriendRequestDTO> getFriendRequestsSentToUser(String userEmail)
            throws ValidationException, AdministrationException {
        User receiver = userService.getUserInfo(userEmail);
        Collection<FriendRequestDTO> friendRequestDTOS = new ArrayList<>();
        friendshipService.getFriendRequestsSentToUser(userEmail).forEach(
                request-> {
                    User sender = userService.getUserInfo(request.getSender());
                    friendRequestDTOS.add(new FriendRequestDTO(request, sender, receiver));
                });
        return friendRequestDTOS;
    }

    /**
     * logs in a user
     *
     * @param userEmail - said user's email
     * @param userPassword - said user's password
     * @throws ValidationException - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     * @return said user
     */
    public UserUIDTO login(String userEmail, int userPassword) throws ValidationException, AdministrationException {
        User user = userService.login(userEmail,userPassword);
        return new UserUIDTO(user);
    }
}
