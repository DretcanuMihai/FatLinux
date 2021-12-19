package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;
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
     * Creates a user with the given information
     * @param email - the user's email
     * @param firstName - the first name of the user
     * @param passwordHash - the hash of the user's password
     * @param lastName - the last name of the user
     * @throws ValidationException if any of the data is invalid
     * @throws AdministrationException if a user already exists with the given email
     */
    public void addUser(String email, String firstName, int passwordHash, String lastName)
            throws ValidationException,AdministrationException {
        userService.createUser(email, firstName, lastName, passwordHash);
    }

    /**
     * deletes the user with the given email
     * @param email - user email
     * @throws ValidationException if the email is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void deleteUser(String email)throws ValidationException,AdministrationException {
        userService.deleteUser(email);
    }

    /**
     * modifies the account identified by email with the other given information
     * @param email - the user's email
     * @param firstName - the user's first name
     * @param passwordHash - the hash of the user's password
     * @param lastName - the user's last name
     * @throws ValidationException if any of the data is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void updateUser(String email, String firstName, int passwordHash, String lastName)
            throws ValidationException,AdministrationException {
        userService.updateUser(email, firstName, passwordHash, lastName);
    }

    /**
     * returns the UserDTO with the information of the user identified by email
     * @param email - the user's email
     * @return the UserDTO
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public UserDTO getUserDTO(String email)throws AdministrationException {
        return new UserDTO(userService.getUser(email));
    }

    /**
     * returns a collection of all user data in the form of UserDTOs
     * @return said collection
     */
    public Collection<UserDTO> getAllUserDTOs() {
        Collection<UserDTO> userDTOS=new ArrayList<>();
        userService.getAllUsers().forEach(user -> userDTOS.add(new UserDTO(user)));
        return userDTOS;
    }

    /**
     * creates a friendship between two users identified by their emails
     * @param email1 first user's email
     * @param email2 second user's email
     * @throws ValidationException if emails are invalid
     * @throws AdministrationException if no users with said emails exist or if a friendship already
     * exists between them
     */
    public void addFriendship(String email1, String email2)
            throws ValidationException,AdministrationException {
        userService.getUser(email1);
        userService.getUser(email2);
        friendshipService.addFriendship(email1,email2);
    }

    /**
     * ඞ
     * deletes a friendship between two users identified by their emails
     * @param email1 first user's email
     * @param email2 second user's email
     * @throws ValidationException if emails are invalid
     * @throws AdministrationException if no users with said emails exist or if they aren't friends
     */
    public void deleteFriendship(String email1,String email2)throws ValidationException,AdministrationException {
        userService.getUser(email1);
        userService.getUser(email2);
        friendshipService.deleteFriendship(email1,email2);
    }

    /**
     * returns information of friendship between two users identified by email
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return a FriendshipDTO with said information
     */
    public FriendshipDTO getFriendshipDTO(String email1, String email2) {
        User user1=userService.getUser(email1);
        User user2=userService.getUser(email2);
        Friendship friendship=friendshipService.getFriendship(email1,email2);
        return new FriendshipDTO(friendship,user1,user2);
    }

    /**
     * returns a collection of all friendship data in the form of FriendshipDTOs
     * @return said collection
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOs() {
        Collection<FriendshipDTO> friendshipDTOS=new ArrayList<>();
        friendshipService.getAllFriendships().forEach(friendship -> {
            User u1=userService.getUser(friendship.getEmails().getFirst());
            User u2=userService.getUser(friendship.getEmails().getSecond());
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
        User user = userService.getUser(userEmail);

        return friendshipService.getUserFriendships(userEmail).stream().map(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();

            if(friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();

            User friend = userService.getUser(friendEmail);
            return new FriendshipDTO(friendship, user, friend);
        }).toList();
    }

    /**
     * Returns all friendships of a user that were created in a specific month as DTOs
     * @param userEmail - email of user
     * @param month - month in which the friendship was created
     * @return a collection of said friendshipDTOs
     * @throws ValidationException if month is invalid or userEmail is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOsOfUserFromMonth(String userEmail, int month) throws ValidationException, AdministrationException {
        User user = userService.getUser(userEmail);

        return friendshipService.getUserFriendshipsFromMonth(userEmail, month).stream().map(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();

            if(friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();

            User friend = userService.getUser(friendEmail);
            return new FriendshipDTO(friendship, user, friend);
        }).toList();
    }

    /**
     * sends a root message (a message that isn't a reply) to some users
     * @param fromEmail - sender user's email
     * @param toEmails - recipient users' emails
     * @param messageText - the text of the message
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendRootMessage(String fromEmail, List<String> toEmails, String messageText)
            throws ValidationException,AdministrationException{
        userService.getUser(fromEmail);
        userService.verifyEmailList(toEmails);
        messageService.addRootMessage(fromEmail,toEmails,messageText);
    }

    /**
     * sends a reply message to another message
     * the receiver will be the sender of the original message
     * @param fromEmail - sender user's email
     * @param messageText - the text of the message
     * @param parentID - the id of the parent message
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyMessage(String fromEmail, String messageText,Integer parentID)
            throws ValidationException,AdministrationException{
        userService.getUser(fromEmail);
        messageService.addReplyMessage(fromEmail,messageText,parentID);
    }

    /**
     * sends a reply message to another message
     * the receiver will be the sender of the original message and all of the original receivers
     * with the exception of the replier
     * @param fromEmail - sender user's email
     * @param messageText - the text of the message
     * @param parentID - the id of the parent message
     * @throws ValidationException if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyAllMessage(String fromEmail, String messageText,Integer parentID)
            throws ValidationException,AdministrationException{
        userService.getUser(fromEmail);
        messageService.addReplyAllMessage(fromEmail,messageText,parentID);
    }

    /**
     * returns the conversation between two users sorted chronologically
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return a list of DTOs for said messages
     * @throws ValidationException if emails are invalid
     * @throws AdministrationException if no user with such emails exist
     */
    public List<MessageDTO> getConversationBetweenUsers(String email1, String email2)
            throws ValidationException,AdministrationException {
        userService.getUser(email1);
        userService.getUser(email2);
        return messageService.getMessagesBetweenUsersChronologically(email1, email2).stream()
                .map(MessageDTO::new).toList();
    }
    /**
     * sends a friend request from sender to receiver
     * @param sender - sender's email
     * @param receiver - receiver's email
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if any administration error occurs
     */
    public void sendFriendRequest(String sender, String receiver)throws ValidationException,AdministrationException{
        userService.getUser(sender);
        userService.getUser(receiver);
        friendshipService.addFriendRequest(sender,receiver);
    }

    /**
     * confirms a friend request based on accepted status
     * @param sender - sender's email
     * @param receiver - receiver's email
     * @param accepted - acceptance status (true is accept, false decline)
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void confirmFriendRequest(String sender, String receiver, boolean accepted)throws ValidationException,AdministrationException{
        userService.getUser(sender);
        userService.getUser(receiver);
        friendshipService.confirmFriendRequest(sender,receiver,accepted);
    }

    /**
     * gets a collection of DTOs of all the friend requests sent to a user
     * @param userEmail - said user's email
     * @return said collection
     * @throws ValidationException if the user email is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Collection<FriendRequestDTO> getFriendRequestsSentToUser(String userEmail)
            throws ValidationException,AdministrationException{
        userService.getUser(userEmail);
        return friendshipService.getFriendRequestsSentToUser(userEmail).stream()
                .map(request-> {
                    User sender= userService.getUser(request.getSender());
                    User receiver= userService.getUser(request.getReceiver());
                    return new FriendRequestDTO(request, sender,receiver);
                }).toList();
    }

    /**
     * logs in an user
     * @param userEmail - said user's email
     * @param userPassword - said user's password
     * @throws ValidationException - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public void login(String userEmail, int userPassword) throws ValidationException, AdministrationException {
        userService.userLogin(userEmail, userPassword);
    }
}
