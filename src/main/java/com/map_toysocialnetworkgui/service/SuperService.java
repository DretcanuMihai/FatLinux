package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.FriendRequestDTO;
import com.map_toysocialnetworkgui.model.entities_dto.FriendshipDTO;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.entities_dto.UserUIDTO;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
import com.map_toysocialnetworkgui.utils.structures.Pair;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * class that incorporates a super service that administers the other service classes
 */
public class SuperService {
    /**
     * associated user service
     */
    private final UserService userService;

    /**
     * associated friendship service
     */
    private final FriendshipService friendshipService;

    /**
     * associated friend request service
     */
    private final FriendRequestService friendRequestService;

    /**
     * associated message service
     */
    private final MessageService messageService;

    /**
     * creates a super service with said services
     *
     * @param userService          - the user service
     * @param friendshipService    - the friendship service
     * @param friendRequestService - the friendship request service
     * @param messageService       - the message service
     */
    public SuperService(UserService userService, FriendshipService friendshipService, FriendRequestService friendRequestService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendRequestService = friendRequestService;
        this.messageService = messageService;
    }

    /**
     * validates a pageable if its page number and size are greater than 1
     *
     * @param pageable - said pageable
     * @throws ValidationException if pageable is invalid
     */
    private void validatePageable(Pageable pageable) throws ValidationException {
        if (pageable == null || pageable.getPageNumber() < 1 || pageable.getPageSize() < 1)
            throw new ValidationException("Error: pageable not valid;\n");
    }

    /**
     * adds a user to the repo
     *
     * @param email        - email info
     * @param passwordHash - password hash info
     * @param firstName    - first name info
     * @param lastName     - last name info
     * @throws ValidationException     - if the user data is invalid
     * @throws AdministrationException - if the email is already in use
     */
    public void createUserAccount(String email, String passwordHash, String firstName, String lastName)
            throws ValidationException, AdministrationException {
        userService.createUserAccount(email, passwordHash, firstName, lastName);
    }

    /**
     * modifies the account identified by email with the other given information
     *
     * @param email        - email info
     * @param passwordHash - password hash info
     * @param firstName    - first name info
     * @param lastName     - last name info
     * @throws ValidationException     if any of the data is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void updateUser(String email, String passwordHash, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        userService.updateUserAccountInfo(email, passwordHash, firstName, lastName);
    }

    /**
     * deletes the user with the given email
     *
     * @param email - user email
     * @throws ValidationException     if the email is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void deleteUserAccount(String email) throws ValidationException, AdministrationException {
        userService.deleteUserAccount(email);
    }

    /**
     * returns a collection of all user data in the form of UserUIDTO
     *
     * @return said collection
     */
    public Collection<UserUIDTO> getAllUserDTOs() {
        Collection<UserUIDTO> userUIDTOS = new ArrayList<>();
        userService.getAllUsers().forEach(user -> userUIDTOS.add(new UserUIDTO(user)));
        return userUIDTOS;
    }

    /**
     * returns a page of user data in the form of UserUIDTO
     *
     * @param pageable - for paging
     * @return said page
     * @throws ValidationException if pageable is not valid
     */
    public Page<UserUIDTO> getAllUserDTOs(Pageable pageable) throws ValidationException {
        validatePageable(pageable);
        Page<User> page = userService.getAllUsers(pageable);
        Stream<UserUIDTO> stream = page.getContent().map(UserUIDTO::new);
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * ඞ
     * deletes a friendship between two users identified by their emails
     *
     * @param requester   requester's email
     * @param friendEmail friend's email
     * @throws ValidationException     if emails are invalid
     * @throws AdministrationException if no users with said emails exist or if they aren't friend or
     *                                 if the requester is disabled
     */
    public void deleteFriendship(String requester, String friendEmail) throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(requester, friendEmail));
        friendshipService.deleteFriendship(requester, friendEmail);
    }

    /**
     * returns information of friendship between two users identified by email
     *
     * @param requester   - first user's email
     * @param friendEmail - second user's email
     * @return a FriendshipDTO with said information
     * @throws ValidationException     if any validation error occurs
     * @throws AdministrationException if any administration error occurs
     */
    public FriendshipDTO getFriendshipDTO(String requester, String friendEmail)
            throws ValidationException, AdministrationException {

        userService.verifyEmailCollection(List.of(requester, friendEmail));
        User user1 = userService.getUserInfo(requester);
        User user2 = userService.getUserInfo(friendEmail);
        Friendship friendship = friendshipService.getFriendship(requester, friendEmail);
        return new FriendshipDTO(friendship, user1, user2);
    }

    /**
     * returns information of a friend request sent by a user to another user
     *
     * @param sender   - sender email
     * @param receiver - receiver email
     * @return a FriendRequestDTO with said information
     * @throws ValidationException     if any validation error occurs
     * @throws AdministrationException if any administration error occurs
     */
    public FriendRequestDTO getFriendRequestDTO(String sender, String receiver)
            throws ValidationException, AdministrationException {

        userService.verifyEmailCollection(List.of(sender, receiver));
        User user1 = userService.getUserInfo(sender);
        User user2 = userService.getUserInfo(receiver);
        FriendRequest friendRequest = friendRequestService.getFriendRequest(sender, receiver);
        return new FriendRequestDTO(friendRequest, user1, user2);
    }

    /**
     * returns a collection of all friendship data in the form of FriendshipDTOs
     *
     * @return said collection
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOs() {
        Collection<FriendshipDTO> friendshipDTOS = new ArrayList<>();
        friendshipService.getAllFriendships().forEach(friendship -> {
            User u1 = userService.getUserInfo(friendship.getEmails().getFirst());
            User u2 = userService.getUserInfo(friendship.getEmails().getSecond());
            friendshipDTOS.add(new FriendshipDTO(friendship, u1, u2));
        });
        return friendshipDTOS;
    }

    /**
     * returns a page of friendship data in the form of FriendshipDTOs
     *
     * @param pageable - for paging
     * @return said collection
     * @throws ValidationException if pageable is invalid
     */
    public Page<FriendshipDTO> getAllFriendshipDTOs(Pageable pageable) throws ValidationException {
        validatePageable(pageable);
        Page<Friendship> page = friendshipService.getAllFriendships(pageable);
        Stream<FriendshipDTO> stream = page.getContent().map(friendship -> {
            User u1 = userService.getUserInfo(friendship.getEmails().getFirst());
            User u2 = userService.getUserInfo(friendship.getEmails().getSecond());
            return new FriendshipDTO(friendship, u1, u2);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * returns a collection of all friendship data of one user identified by email in the form of FriendshipDTOs
     *
     * @param userEmail - the user's email
     * @return said collection
     * @throws ValidationException     if email is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOsOfUser(String userEmail) throws ValidationException, AdministrationException {
        User user = userService.getUserInfo(userEmail);
        Collection<FriendshipDTO> friendshipDTOS = new ArrayList<>();
        friendshipService.getUserFriendships(userEmail).forEach(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();

            if (friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();

            User friend = userService.getUserInfo(friendEmail);
            friendshipDTOS.add(new FriendshipDTO(friendship, user, friend));
        });
        return friendshipDTOS;
    }

    /**
     * returns a page of friendship data of one user identified by email in the form of FriendshipDTOs
     *
     * @param userEmail - the user's email
     * @param pageable  - for paging
     * @return said page
     * @throws ValidationException     if email or pageable is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Page<FriendshipDTO> getAllFriendshipDTOsOfUser(String userEmail, Pageable pageable) throws ValidationException, AdministrationException {
        validatePageable(pageable);
        User user = userService.getUserInfo(userEmail);
        Page<Friendship> page = friendshipService.getUserFriendships(userEmail, pageable);
        Stream<FriendshipDTO> friendshipDTOS = page.getContent().map(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();
            if (friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();
            User friend = userService.getUserInfo(friendEmail);
            return new FriendshipDTO(friendship, user, friend);
        });
        return new PageImplementation<>(pageable, friendshipDTOS);
    }

    /**
     * returns all friendships of a user that were created in a specific month as FriendshipDTOs
     *
     * @param userEmail - email of user
     * @param month     - month in which the friendship was created
     * @return a collection of said friendshipDTOs
     * @throws ValidationException     if month is invalid or userEmail is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Collection<FriendshipDTO> getAllFriendshipDTOsOfUserFromMonth(String userEmail, int month) throws ValidationException, AdministrationException {
        User user = userService.getUserInfo(userEmail);
        Collection<FriendshipDTO> friendshipDTOS = new ArrayList<>();
        friendshipService.getUserFriendshipsFromMonth(userEmail, month).forEach(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();
            if (friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();
            User friend = userService.getUserInfo(friendEmail);
            friendshipDTOS.add(new FriendshipDTO(friendship, user, friend));
        });
        return friendshipDTOS;
    }

    /**
     * returns a page of friendships of a user that were created in a specific month as FriendshipDTOs
     *
     * @param userEmail - email of user
     * @param month     - month in which the friendship was created
     * @param pageable  - for paging
     * @return a page of said friendshipDTOs
     * @throws ValidationException     if month, userEmail or pageable is invalid
     * @throws AdministrationException if user doesn't exist
     */
    public Page<FriendshipDTO> getAllFriendshipDTOsOfUserFromMonth(String userEmail, int month, Pageable pageable) throws ValidationException, AdministrationException {
        validatePageable(pageable);
        User user = userService.getUserInfo(userEmail);
        Page<Friendship> page = friendshipService.getUserFriendshipsFromMonth(userEmail, month, pageable);
        Stream<FriendshipDTO> friendships = page.getContent().map(friendship -> {
            String friendEmail = friendship.getEmails().getFirst();
            if (friendEmail.equals(userEmail))
                friendEmail = friendship.getEmails().getSecond();
            User friend = userService.getUserInfo(friendEmail);
            return new FriendshipDTO(friendship, user, friend);
        });
        return new PageImplementation<>(page.getPageable(), friendships);
    }

    /**
     * gets a messageDTO by its id
     *
     * @param id - said id
     * @return said messageDTO
     * @throws ValidationException     - if id is invalid
     * @throws AdministrationException - if no message with requested id exists
     */
    public MessageDTO getMessageDTO(Integer id) throws ValidationException, AdministrationException {
        return new MessageDTO(messageService.getMessage(id));
    }

    /**
     * sends a root message (a message that isn't a reply) to one or more users
     *
     * @param fromEmail      - sender email
     * @param toEmails       - a list of destination emails
     * @param messageText    - message's text
     * @param messageSubject - message's subject
     * @throws ValidationException     if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendRootMessage(String fromEmail, List<String> toEmails, String messageText, String messageSubject)
            throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {

        userService.getUserInfo(fromEmail);
        userService.verifyEmailCollection(toEmails);
        messageService.addRootMessage(fromEmail, toEmails, messageText, messageSubject);
    }

    /**
     * sends a reply message to another message
     * the receiver will be the sender of the original message
     *
     * @param fromEmail      - sender email
     * @param messageText    - message's text
     * @param messageSubject - message's subject
     * @param parentID       - parent message id
     * @throws ValidationException     if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyMessage(String fromEmail, String messageText, String messageSubject, Integer parentID)
            throws ValidationException, AdministrationException {

        userService.getUserInfo(fromEmail);
        messageService.addReplyMessage(fromEmail, messageText, messageSubject, parentID);
    }

    /**
     * sends a reply all message to another message
     * the receiver will be the sender of the original message and all the original receivers
     * except the replier
     *
     * @param fromEmail   - sender email
     * @param messageText - message's text
     * @param parentID    - parent message id
     * @throws ValidationException     if any data is invalid
     * @throws AdministrationException if any administration problems are found
     */
    public void sendReplyAllMessage(String fromEmail, String messageText, String messageSubject, Integer parentID)
            throws ValidationException, AdministrationException {

        userService.getUserInfo(fromEmail);
        messageService.addReplyAllMessage(fromEmail, messageText, messageSubject, parentID);
    }

    /**
     * returns the conversation between two users sorted chronologically
     *
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return a list of DTOs for said messages
     * @throws AdministrationException if the users do not exist
     * @throws ValidationException     if emails are the same
     */
    public List<MessageDTO> getConversation(String email1, String email2) throws ValidationException, AdministrationException {
        userService.getUserInfo(email1);
        userService.getUserInfo(email2);
        List<MessageDTO> messageDTOS = new ArrayList<>();
        messageService.getConversationBetweenUsers(email1, email2).
                forEach(message -> messageDTOS.add(new MessageDTO(message)));
        return messageDTOS;
    }

    /**
     * returns a page of conversation between two users sorted chronologically
     *
     * @param email1   - first user's email
     * @param email2   - second user's email
     * @param pageable - for paging
     * @return a list of DTOs for said messages
     * @throws AdministrationException if the users do not exist
     * @throws ValidationException     if emails or the pageable is invalid
     */
    public Page<MessageDTO> getConversation(String email1, String email2, Pageable pageable)
            throws ValidationException, AdministrationException {

        validatePageable(pageable);
        userService.getUserInfo(email1);
        userService.getUserInfo(email2);
        Page<Message> page = messageService.getConversationBetweenUsers(email1, email2, pageable);
        Stream<MessageDTO> stream = page.getContent().map(MessageDTO::new);
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * sends a friend request from sender to receiver
     *
     * @param sender   - sender's email
     * @param receiver - receiver's email
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administration error occurs
     */
    public void sendFriendRequest(String sender, String receiver) throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(sender, receiver));
        Friendship friendship = null;

        try {
            friendship = friendshipService.getFriendship(sender, receiver);
        } catch (AdministrationException ignored) {
        }
        if (friendship != null) {
            throw new AdministrationException("Error: users are already friends;\n");
        }
        friendRequestService.sendFriendRequest(sender, receiver);
    }

    /**
     * confirms a friend request based on accepted status
     *
     * @param sender   - sender's email
     * @param receiver - receiver's email
     * @param accepted - acceptance status (true is accept, false decline)
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void confirmFriendRequest(String sender, String receiver, boolean accepted)
            throws ValidationException, AdministrationException {

        userService.verifyEmailCollection(List.of(sender, receiver));
        friendRequestService.deleteFriendRequest(sender, receiver);
        if (accepted)
            friendshipService.addFriendship(sender, receiver);
    }

    /**
     * gets a collection of DTOs of all the friend requests sent to a user
     *
     * @param userEmail - said user's email
     * @return said collection
     * @throws ValidationException     if the user email is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Collection<FriendRequestDTO> getFriendRequestsSentToUser(String userEmail)
            throws ValidationException, AdministrationException {

        User receiver = userService.getUserInfo(userEmail);
        Collection<FriendRequestDTO> friendRequestDTOS = new ArrayList<>();
        friendRequestService.getFriendRequestsSentToUser(userEmail).forEach(
                request -> {
                    User sender = userService.getUserInfo(request.getSender());
                    friendRequestDTOS.add(new FriendRequestDTO(request, sender, receiver));
                });
        return friendRequestDTOS;
    }

    /**
     * gets a page of DTOs of all the friend requests sent to a user
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return said page
     * @throws ValidationException     if the user email or pageable is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Page<FriendRequestDTO> getFriendRequestsSentToUser(String userEmail, Pageable pageable)
            throws ValidationException, AdministrationException {

        validatePageable(pageable);
        User receiver = userService.getUserInfo(userEmail);
        Page<FriendRequest> page = friendRequestService.getFriendRequestsSentToUser(userEmail, pageable);
        Stream<FriendRequestDTO> stream = page.getContent().map(request -> {
            User sender = userService.getUserInfo(request.getSender());
            return new FriendRequestDTO(request, sender, receiver);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * gets a collection of DTOs of all the friend requests sent to a user
     *
     * @param userEmail - said user's email
     * @return said collection
     * @throws ValidationException     if the user email is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Collection<FriendRequestDTO> getFriendRequestsSentByUser(String userEmail)
            throws ValidationException, AdministrationException {

        User sender = userService.getUserInfo(userEmail);
        Collection<FriendRequestDTO> friendRequestDTOS = new ArrayList<>();
        friendRequestService.getFriendRequestsSentByUser(userEmail).forEach(
                request -> {
                    User receiver = userService.getUserInfo(request.getReceiver());
                    friendRequestDTOS.add(new FriendRequestDTO(request, sender, receiver));
                });
        return friendRequestDTOS;
    }

    /**
     * gets a page of DTOs of all the friend requests sent to a user
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return said page
     * @throws ValidationException     if the user email or pageable is invalid
     * @throws AdministrationException - if the user doesn't exist
     */
    public Page<FriendRequestDTO> getFriendRequestsSentByUser(String userEmail, Pageable pageable)
            throws ValidationException, AdministrationException {

        validatePageable(pageable);
        User sender = userService.getUserInfo(userEmail);
        Page<FriendRequest> page = friendRequestService.getFriendRequestsSentByUser(userEmail, pageable);
        Stream<FriendRequestDTO> stream = page.getContent().map(request -> {
            User receiver = userService.getUserInfo(request.getReceiver());
            return new FriendRequestDTO(request, sender, receiver);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * logs in a user
     *
     * @param userEmail - said user's email
     * @return said user
     * @throws ValidationException     - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public User login(String userEmail) throws ValidationException, AdministrationException {
        return userService.login(userEmail);
    }

    /**
     * retracts a friend request from sender to receiver
     *
     * @param senderEmail   - sender's email
     * @param receiverEmail - receiver's email
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void retractFriendRequest(String senderEmail, String receiverEmail) throws ValidationException, AdministrationException {
        userService.verifyEmailCollection(List.of(senderEmail, receiverEmail));
        friendRequestService.deleteFriendRequest(senderEmail, receiverEmail);
    }

    /**
     * subscribes an observer to user notifications
     *
     * @param observer - said observer
     */
    public void addUserObserver(Observer<EntityModificationEvent<String>> observer) {
        userService.addObserver(observer);
    }

    /**
     * subscribes an observer to friendship notifications
     *
     * @param observer - said observer
     */
    public void addFriendshipObserver(Observer<EntityModificationEvent<UnorderedPair<String>>> observer) {
        friendshipService.addObserver(observer);
    }

    /**
     * subscribes an observer to friend request notifications
     *
     * @param observer - said observer
     */
    public void addFriendRequestObserver(Observer<EntityModificationEvent<Pair<String, String>>> observer) {
        friendRequestService.addObserver(observer);
    }

    /**
     * subscribes an observer to message notifications
     *
     * @param observer - said observer
     */
    public void addMessageObserver(Observer<EntityModificationEvent<Integer>> observer) {
        messageService.addObserver(observer);
    }

    /**
     * returns an iterable of all the users in repo with certain string inside them
     *
     * @param string - said string
     * @return said iterable
     * @throws ValidationException - if string is null
     */
    public Iterable<UserUIDTO> filterUsers(String string) throws ValidationException {
        List<UserUIDTO> userUIDTOS = new ArrayList<>();
        userService.filterUsers(string).forEach(user -> {
            userUIDTOS.add(new UserUIDTO(user));
        });
        return userUIDTOS;
    }

    /**
     * returns a page of all the users in repo that have a certain string in their names
     *
     * @param string   - said string
     * @param pageable - pageable for paging
     * @return said page
     * @throws ValidationException - if string is null
     */
    public Page<UserUIDTO> filterUsers(String string, Pageable pageable) throws ValidationException {
        validatePageable(pageable);
        Page<User> page = userService.filterUsers(string, pageable);
        Stream<UserUIDTO> stream = page.getContent().map(UserUIDTO::new);
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * returns the messages received by a user
     *
     * @param email - first user's email
     * @return an iterable of the messages
     * @throws ValidationException     - is email is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Iterable<MessageDTO> getMessagesReceivedByUser(String email) throws ValidationException, AdministrationException {
        userService.getUserInfo(email);
        List<MessageDTO> messageDTOList = new ArrayList<>();
        messageService.getMessagesReceivedByUser(email).forEach(message -> messageDTOList.add(new MessageDTO(message)));
        return messageDTOList;
    }

    /**
     * returns the messages sent to a user
     *
     * @param email    - first user's email
     * @param pageable - for paging
     * @return said page
     * @throws ValidationException     - is email or pageable is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Page<MessageDTO> getMessagesReceivedByUser(String email, Pageable pageable) throws ValidationException, AdministrationException {
        validatePageable(pageable);
        userService.getUserInfo(email);
        Page<Message> page = messageService.getMessagesReceivedByUser(email, pageable);
        Stream<MessageDTO> stream = page.getContent().map(MessageDTO::new);
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * returns the messages sent by a user
     *
     * @param email - first user's email
     * @return an iterable of the messages
     * @throws ValidationException     - is email is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Iterable<MessageDTO> getMessagesSentByUser(String email) throws ValidationException, AdministrationException {
        userService.getUserInfo(email);
        List<MessageDTO> messageDTOList = new ArrayList<>();
        messageService.getMessagesSentByUser(email).forEach(message -> messageDTOList.add(new MessageDTO(message)));
        return messageDTOList;
    }

    /**
     * returns the messages sent by a user
     *
     * @param email    - first user's email
     * @param pageable - for paging
     * @return said page
     * @throws ValidationException     - is email or pageable is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Page<MessageDTO> getMessagesSentByUser(String email, Pageable pageable) throws ValidationException, AdministrationException {
        validatePageable(pageable);
        userService.getUserInfo(email);
        Page<Message> page = messageService.getMessagesSentByUser(email, pageable);
        Stream<MessageDTO> stream = page.getContent().map(MessageDTO::new);
        return new PageImplementation<>(page.getPageable(), stream);
    }
}
