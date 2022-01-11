package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.*;
import com.map_toysocialnetworkgui.model.entities_dto.*;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.PageImplementation;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PageableImplementation;
import com.map_toysocialnetworkgui.utils.Constants;
import com.map_toysocialnetworkgui.utils.events.EntityModificationObsEvent;
import com.map_toysocialnetworkgui.utils.observer.Observer;
import com.map_toysocialnetworkgui.utils.structures.Pair;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * associated event service
     */
    private final EventService eventService;

    /**
     * creates a super service with said services
     *
     * @param userService          - the user service
     * @param friendshipService    - the friendship service
     * @param friendRequestService - the friendship request service
     * @param messageService       - the message service
     * @param eventService         - the event service
     */
    public SuperService(UserService userService, FriendshipService friendshipService, FriendRequestService friendRequestService,
                        MessageService messageService, EventService eventService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendRequestService = friendRequestService;
        this.messageService = messageService;
        this.eventService = eventService;
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
     * @param email     - email info
     * @param password  - password hash info
     * @param firstName - first name info
     * @param lastName  - last name info
     * @throws ValidationException     - if the user data is invalid
     * @throws AdministrationException - if the email is already in use
     */
    public void createUserAccount(String email, String password, String firstName, String lastName)
            throws ValidationException, AdministrationException {
        userService.createUserAccount(email, password, firstName, lastName);
    }

    /**
     * modifies the account identified by email with the other given information
     *
     * @param email     - email info
     * @param password  - password info
     * @param firstName - first name info
     * @param lastName  - last name info
     * @throws ValidationException     if any of the data is invalid
     * @throws AdministrationException if a user with said email doesn't exist
     */
    public void updateUser(String email, String password, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        userService.updateUserAccountInfo(email, password, firstName, lastName);
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
    public Collection<UserDTO> getAllUserDTOs() {
        Collection<UserDTO> userDTOS = new ArrayList<>();
        userService.getAllUsers().forEach(user -> userDTOS.add(new UserDTO(user)));
        return userDTOS;
    }

    /**
     * returns a page of user data in the form of UserUIDTO
     *
     * @param pageable - for paging
     * @return said page
     * @throws ValidationException if pageable is not valid
     */
    public Page<UserDTO> getAllUserDTOs(Pageable pageable) throws ValidationException {
        validatePageable(pageable);
        Page<User> page = userService.getAllUsers(pageable);
        Stream<UserDTO> stream = page.getContent().map(UserDTO::new);
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
     * @param userEmail    - said user's email
     * @param userPassword - said user's password
     * @return said user's info
     * @throws ValidationException     - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public UserDTO login(String userEmail, String userPassword) throws ValidationException, AdministrationException {
        User user = userService.login(userEmail, userPassword);
        return new UserDTO(user);
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
    public void addUserObserver(Observer<EntityModificationObsEvent<String>> observer) {
        userService.addObserver(observer);
    }

    /**
     * subscribes an observer to friendship notifications
     *
     * @param observer - said observer
     */
    public void addFriendshipObserver(Observer<EntityModificationObsEvent<UnorderedPair<String>>> observer) {
        friendshipService.addObserver(observer);
    }

    /**
     * subscribes an observer to friend request notifications
     *
     * @param observer - said observer
     */
    public void addFriendRequestObserver(Observer<EntityModificationObsEvent<Pair<String, String>>> observer) {
        friendRequestService.addObserver(observer);
    }

    /**
     * subscribes an observer to message notifications
     *
     * @param observer - said observer
     */
    public void addMessageObserver(Observer<EntityModificationObsEvent<Integer>> observer) {
        messageService.addObserver(observer);
    }

    /**
     * removes an observer from message notifications
     *
     * @param observer - said observer
     */
    public void removeMessageObserver(Observer<EntityModificationObsEvent<Integer>> observer) {
        messageService.removeObserver(observer);
    }

    /**
     * returns an iterable of all the users in repo with certain string inside them
     *
     * @param string - said string
     * @return said iterable
     * @throws ValidationException - if string is null
     */
    public Iterable<UserDTO> filterUsers(String string) throws ValidationException {
        List<UserDTO> userDTOS = new ArrayList<>();
        userService.filterUsers(string).forEach(user -> {
            userDTOS.add(new UserDTO(user));
        });
        return userDTOS;
    }

    /**
     * returns a page of all the users in repo that have a certain string in their names
     *
     * @param string   - said string
     * @param pageable - pageable for paging
     * @return said page
     * @throws ValidationException - if string is null
     */
    public Page<UserDTO> filterUsers(String string, Pageable pageable) throws ValidationException {
        validatePageable(pageable);
        Page<User> page = userService.filterUsers(string, pageable);
        Stream<UserDTO> stream = page.getContent().map(UserDTO::new);
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

    /**
     * gets a page of all user notification events
     *
     * @param userEmail - said user's email
     * @param pageable  - paging info
     * @return - said page
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Page<EventDTO> getUserNotificationEvents(String userEmail, Pageable pageable)
            throws ValidationException, AdministrationException {
        validatePageable(pageable);
        userService.getUserInfo(userEmail);
        Page<Event> page = eventService.getUserNotificationEvents(userEmail, pageable);
        Stream<EventDTO> stream = page.getContent().map(event -> {
            UserDTO userDTO = new UserDTO(userService.getUserInfo(event.getHostEmail()));
            return new EventDTO(event, userDTO);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * gets a page of all user events
     *
     * @param userEmail - said user's email
     * @param pageable  - paging info
     * @return - said page
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public Page<EventDTO> getUserEventsChronoDesc(String userEmail, Pageable pageable) throws
            ValidationException, AdministrationException {
        validatePageable(pageable);
        userService.getUserInfo(userEmail);
        Page<Event> page = eventService.getUsersEventsDesc(userEmail, pageable);
        Stream<EventDTO> stream = page.getContent().map(event -> {
            UserDTO userDTO = new UserDTO(userService.getUserInfo(event.getHostEmail()));
            return new EventDTO(event, userDTO);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * gets a page of all events described by a string
     *
     * @param string   - said string
     * @param pageable - paging info
     * @return - said page
     * @throws ValidationException - if data is invalid
     */
    public Page<EventDTO> getEventsFiltered(String string, Pageable pageable)
            throws ValidationException {

        validatePageable(pageable);
        Page<Event> page = eventService.getEventsFilter(string, pageable);
        Stream<EventDTO> stream = page.getContent().map(event -> {
            UserDTO userDTO = new UserDTO(userService.getUserInfo(event.getHostEmail()));
            return new EventDTO(event, userDTO);
        });
        return new PageImplementation<>(page.getPageable(), stream);
    }

    /**
     * saves an event with no participants
     *
     * @param title       - said event's title
     * @param description - said event's description
     * @param hostEmail   - said event's host's email
     * @param dateTime    - said event's date
     * @throws ValidationException     if data is invalid
     * @throws AdministrationException if no user with given email exists
     */
    public void createEvent(String title, String description, String hostEmail, LocalDateTime dateTime)
            throws ValidationException, AdministrationException {
        userService.getUserInfo(hostEmail);
        eventService.save(title, description, hostEmail, dateTime);
    }

    /**
     * deletes event identified by an id
     *
     * @param id - said id
     * @throws ValidationException     if id is null
     * @throws AdministrationException if no event exists with given id
     */
    public void deleteEvent(Integer id) {
        eventService.delete(id);
    }

    /**
     * subscribes an user to an event
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the user/event doesn't exist or it's already subscribed
     */
    public void subscribeToEvent(Integer id, String userEmail) {
        userService.getUserInfo(userEmail);
        eventService.subscribeToEvent(id, userEmail);
    }

    /**
     * unsubscribes an user to an event
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the user/event doesn't exist or if it's not subscribed
     */
    public void unsubscribeFromEvent(Integer id, String userEmail) {
        userService.getUserInfo(userEmail);
        eventService.unsubscribeFromEvent(id, userEmail);
    }

    /**
     * preapres a page with normal format
     *
     * @param cont - said page's content stream
     * @throws IOException - if any problem arrises
     */
    public void preparePage(PDPageContentStream cont) throws IOException {
        cont.setFont(PDType1Font.COURIER_BOLD, 9);
        cont.setLeading(10.5f);
        cont.newLineAtOffset(25, 700);

    }

    /**
     * writes a line on the page
     *
     * @param cont - said page's current content stream
     * @param line - said line
     * @throws IOException - if any problem arrises
     */
    public void writePageString(PDPageContentStream cont, String line) throws IOException {
        cont.showText(line);
        cont.newLine();
    }

    /**
     * creates a new page and adds it to the end of the document
     *
     * @param document - said document
     * @return - said page
     */
    public PDPage nextPage(PDDocument document) {
        PDPage page = new PDPage();
        document.addPage(page);
        return page;
    }

    /**
     * adds first page to activities for a user in an interval document
     *
     * @param document  - said document
     * @param beginDate -beginning date of interval
     * @param endDate   - end date of interval
     * @param user      - said user
     */
    public void mainPageActivities(PDDocument document, LocalDate beginDate, LocalDate endDate, User user) throws IOException {
        PDPage firstPage = nextPage(document);
        String line;

        try (PDPageContentStream cont = new PDPageContentStream(document, firstPage)) {

            cont.beginText();

            preparePage(cont);

            line = "Activities Report:" + Constants.DATE_FORMATTER.format(beginDate) + " - " + Constants.DATE_FORMATTER.format(endDate);
            writePageString(cont, line);

            line = "User: " + user.getFirstName() + " " + user.getLastName();
            writePageString(cont, line);

            cont.endText();
        }
    }

    /**
     * adds first page to activities for a user in an interval document
     *
     * @param document - said document
     */
    public void mainFriendsPageActivities(PDDocument document) throws IOException {
        PDPage page = nextPage(document);
        String line;

        try (PDPageContentStream cont = new PDPageContentStream(document, page)) {

            cont.beginText();

            preparePage(cont);

            line = "New Friends;";
            writePageString(cont, line);

            cont.endText();
        }
    }

    /**
     * writes a friendship on a pdf page
     *
     * @param cont - said page content stream
     * @param dto  - the friendship to write
     */
    public void writeOneFriendshipToPage(PDPageContentStream cont, FriendshipDTO dto) throws IOException {
        writePageString(cont, "User:");
        writePageString(cont, dto.getUser2().getFirstName() + " " + dto.getUser2().getLastName());
        writePageString(cont, dto.getUser2().getEmail());
        writePageString(cont, "The Friendship begun on:" + dto.getBeginDate()
                .format(Constants.DATE_FORMATTER));
        writePageString(cont, "");
    }

    /**
     * adds first page to activities for a user in an interval document
     *
     * @param document - said document
     */
    public void generateOneFriendsPageActivities(PDDocument document, User user, List<Friendship> friendships) throws IOException {
        PDPage page = nextPage(document);
        try (PDPageContentStream cont = new PDPageContentStream(document, page)) {

            cont.beginText();
            preparePage(cont);

            friendships.stream().map(friendship -> {
                String email = friendship.getEmails().getFirst();
                if (email.equals(user.getEmail()))
                    email = friendship.getEmails().getSecond();
                return new FriendshipDTO(friendship, user, userService.getUserInfo(email));
            }).forEach(dto -> {
                try {
                    writeOneFriendshipToPage(cont, dto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            cont.endText();
        }
    }

    /**
     * adds all the pages of new friends made by user in interval to document
     *
     * @param document  - said document
     * @param user      -said user
     * @param beginDate - the beginning of the interval
     * @param endDate   - the end of the interval
     */
    public void friendsPagesActivities(PDDocument document, User user, LocalDate beginDate, LocalDate endDate) throws IOException {


        Pageable pageable = new PageableImplementation(1, 10);
        while (true) {
            Page<Friendship> friendshipPage = friendshipService.getUserFriendshipsFromInterval(user.getEmail(),
                    beginDate, endDate, pageable);
            List<Friendship> friendships = friendshipPage.getContent().toList();
            if (friendships.size() == 0) {
                break;
            }
            generateOneFriendsPageActivities(document, user, friendships);
            pageable = friendshipPage.nextPageable();
        }
    }

    /**
     * adds first page to activities for a user in an interval document
     *
     * @param document - said document
     */
    public void mainMessagesPageActivities(PDDocument document) throws IOException {
        PDPage page = nextPage(document);
        String line;

        try (PDPageContentStream cont = new PDPageContentStream(document, page)) {

            cont.beginText();

            preparePage(cont);

            line = "Messages Received;";
            writePageString(cont, line);

            cont.endText();
        }
    }

    /**
     * writes a messages sent by a user to pdf page
     *
     * @param cont - said page content stream
     * @param uDTO - said user
     * @param mDTO - said message
     */
    public void writeOneMessageToPage(PDPageContentStream cont, UserDTO uDTO, MessageDTO mDTO) throws IOException {
        writePageString(cont, "Sender:");
        writePageString(cont, uDTO.getFirstName() + " " + uDTO.getLastName());
        writePageString(cont, uDTO.getEmail());
        writePageString(cont, "Sent on:" + mDTO.getSendTime()
                .format(Constants.DATE_TIME_FORMATTER));
        writePageString(cont, "Subject:");
        writePageString(cont, mDTO.getMessageSubject());
        writePageString(cont, "Text:");
        writePageString(cont, mDTO.getMessageText());
        writePageString(cont, "");
    }

    /**
     * adds first page to activities for a user in an interval document
     *
     * @param document - said document
     */
    public void generateOneMessagePageActivities(PDDocument document, User user, List<Message> messages) throws IOException {
        PDPage page = nextPage(document);
        try (PDPageContentStream cont = new PDPageContentStream(document, page)) {

            cont.beginText();
            preparePage(cont);

            messages.forEach(message -> {
                try {

                    UserDTO userDTO = new UserDTO(userService.getUserInfo(message.getFromEmail()));
                    MessageDTO messageDTO = new MessageDTO(message);
                    writeOneMessageToPage(cont, userDTO, messageDTO);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            cont.endText();
        }
    }

    /**
     * adds all the pages of messages received in interval to document
     *
     * @param document  - said document
     * @param user      - said user
     * @param beginDate - the beginning of the interval
     * @param endDate   - the end of the interval
     */
    public void messagesPagesActivities(PDDocument document, User user, LocalDate beginDate, LocalDate endDate) throws IOException {


        Pageable pageable = new PageableImplementation(1, 7);
        while (true) {
            Page<Message> messagePage = messageService.getMessagesToUserInInterval(user.getEmail(), beginDate, endDate, pageable);
            List<Message> messages = messagePage.getContent().toList();
            if (messages.size() == 0) {
                break;
            }
            generateOneMessagePageActivities(document, user, messages);
            pageable = messagePage.nextPageable();
        }
    }


    /**
     * generates a pdf with a user's activities in a specified date interval
     *
     * @param userEmail - said user's email
     * @param beginDate - the beginning of the interval
     * @param endDate   - the end of the interval
     * @param fileName  - name of file
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if user doesn't exist
     */
    public PDDocument reportActivities(String userEmail, LocalDate beginDate, LocalDate endDate, String fileName) throws ValidationException, AdministrationException {
        User user = userService.getUserInfo(userEmail);
        if (beginDate == null || endDate == null) {
            throw new ValidationException("Error: begin and end date shouldn't be null");
        }
        if (fileName == null || fileName.equals("")) {
            throw new ValidationException("Error: file name cannot be null!");
        }
        PDDocument toReturn = null;
        try (PDDocument pdDocument = new PDDocument()) {

            mainPageActivities(pdDocument, beginDate, endDate, user);
            mainFriendsPageActivities(pdDocument);
            friendsPagesActivities(pdDocument, user, beginDate, endDate);
            mainMessagesPageActivities(pdDocument);
            messagesPagesActivities(pdDocument, user, beginDate, endDate);
            pdDocument.save("data/" + fileName + ".pdf");
            toReturn = pdDocument;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * adds first page to conversation for a user and his friend in an interval document
     *
     * @param document  - said document
     * @param beginDate - beginning date of interval
     * @param endDate   - end date of interval
     * @param user      - said user
     * @param friend    - said friend
     */
    public void mainPageConversation(PDDocument document, LocalDate beginDate, LocalDate endDate, User user, User friend) throws IOException {
        PDPage firstPage = nextPage(document);
        String line;

        try (PDPageContentStream cont = new PDPageContentStream(document, firstPage)) {

            cont.beginText();

            preparePage(cont);

            line = "Messages Received Report:" + Constants.DATE_FORMATTER.format(beginDate) + " - " + Constants.DATE_FORMATTER.format(endDate);
            writePageString(cont, line);

            line = "User: " + user.getFirstName() + " " + user.getLastName();
            writePageString(cont, line);

            line = "Friend: " + friend.getFirstName() + " " + friend.getLastName();
            writePageString(cont, line);

            cont.endText();
        }
    }


    /**
     * adds all the pages of messages received in interval to document
     *
     * @param document  - said document
     * @param user      - said user
     * @param beginDate - the beginning of the interval
     * @param endDate   - the end of the interval
     */
    public void messagesPagesConversation(PDDocument document, User user, User friend, LocalDate beginDate, LocalDate endDate) throws IOException {


        Pageable pageable = new PageableImplementation(1, 7);
        while (true) {
            Page<Message> messagePage = messageService.getMessagesToUserFromFriendInInterval(user.getEmail(), friend.getEmail(), beginDate, endDate, pageable);
            List<Message> messages = messagePage.getContent().toList();
            if (messages.size() == 0) {
                break;
            }
            generateOneMessagePageActivities(document, user, messages);
            pageable = messagePage.nextPageable();
        }
    }

    /**
     * generates a pdf with the messages received by a user from a friend in a specified date interval
     *
     * @param userEmail   - said user's email
     * @param friendEmail - said friend's email
     * @param beginDate   - the beginning of the interval
     * @param endDate     - the end of the interval
     * @param fileName    - name of file
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if users don't exist, or they're not friends
     */
    public PDDocument reportConversation(String userEmail, String friendEmail, LocalDate beginDate,
                                         LocalDate endDate, String fileName) throws ValidationException, AdministrationException {
        User user = userService.getUserInfo(userEmail);
        User friend = userService.getUserInfo(friendEmail);
        friendshipService.getFriendship(userEmail, friendEmail);
        if (beginDate == null || endDate == null) {
            throw new ValidationException("Error: begin and end date shouldn't be null");
        }
        if (fileName == null || fileName.equals("")) {
            throw new ValidationException("Error: file name cannot be null!");
        }
        PDDocument toReturn = null;
        try (PDDocument pdDocument = new PDDocument()) {

            mainPageConversation(pdDocument, beginDate, endDate, user, friend);
            messagesPagesConversation(pdDocument, user, friend, beginDate, endDate);

            pdDocument.save("data/" + fileName + ".pdf");
            toReturn = pdDocument;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
