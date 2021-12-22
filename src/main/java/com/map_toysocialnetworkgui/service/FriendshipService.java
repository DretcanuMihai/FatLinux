package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.with_db.FriendRequestDBRepository;
import com.map_toysocialnetworkgui.repository.with_db.FriendshipDBRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * a class that incorporates a service that works with friendship administration
 */
public class FriendshipService {
    /**
     * associated friendship repo
     */
    private final FriendshipDBRepository friendshipRepo;
    /**
     * associated friendship validator
     */
    private final FriendshipValidator friendshipValidator;
    /**
     * associated friend request repo
     */
    private final FriendRequestDBRepository friendRequestRepository;
    /**
     * associated friend request validator
     */
    private final FriendRequestValidator friendRequestValidator;

    /**
     * creates a friendship service based on a friendship repository and validator and
     * friend request repository and validator
     *
     * @param friendshipRepo          - said friend repository
     * @param friendshipValidator     - said friend validator
     * @param friendRequestRepository - said friend request repository
     * @param friendRequestValidator  - said friend request validator
     */
    public FriendshipService(FriendshipDBRepository friendshipRepo, FriendshipValidator friendshipValidator,
                             FriendRequestDBRepository friendRequestRepository,
                             FriendRequestValidator friendRequestValidator) {

        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
        this.friendRequestRepository = friendRequestRepository;
        this.friendRequestValidator = friendRequestValidator;
    }

    /**
     * adds a friendship between two users identified by email
     * Said user emails are assumed to belong to actual valid users
     *
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException     - if the emails are the same
     * @throws AdministrationException - if the friendship already exists
     */
    public void addFriendship(String userEmail1, String userEmail2) throws ValidationException, AdministrationException {
        Friendship friendship = new Friendship(userEmail1, userEmail2, LocalDate.now());
        friendshipValidator.validateDefault(friendship);
        boolean success=friendshipRepo.save(friendship);
        if(!success)
            throw new AdministrationException("Error: users are already friends;\n");
    }

    /**
     * gets a friendship of two users identified by email
     * said emails are assumed to belong to actual valid users
     *
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @return said friendship
     * @throws ValidationException     - if the emails are equal
     * @throws AdministrationException - if a friendship doesn't exist between the two users
     */
    public Friendship getFriendship(String userEmail1, String userEmail2)
            throws ValidationException, AdministrationException {
        friendshipValidator.validateEmails(userEmail1, userEmail2);
        Friendship friendship = friendshipRepo.get(new UnorderedPair<>(userEmail1, userEmail2));
        if (friendship == null)
            throw new AdministrationException("Error: Users aren't friends!\n");
        return friendship;
    }

    /**
     * deletes a friendship between two users identified by email
     * Said user emails are assumed to belong to actual valid users
     *
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException     - if the emails are the same
     * @throws AdministrationException - if the friendship doesn't exist
     */
    public void deleteFriendship(String userEmail1, String userEmail2) throws ValidationException, AdministrationException {
        friendshipValidator.validateEmails(userEmail1, userEmail2);
        boolean success=friendshipRepo.delete(new UnorderedPair<>(userEmail1, userEmail2));
        if(!success)
            throw new AdministrationException("Error: users weren't friends;\n");
    }

    /**
     * gets all existing friendships
     *
     * @return a collection of said friendships
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.getAll();
    }

    /**
     * gets all existing friendships to which a user belongs
     * the email is assumed to belong to an actual valid user
     *
     * @param userEmail - said user's email
     * @return a collection of said friendships
     */
    public Iterable<Friendship> getUserFriendships(String userEmail) {
        return friendshipRepo.getUserFriendships(userEmail);
    }

    /**
     * Returns all friendships of a user that were created in a specific month
     *
     * @param userEmail - email of user
     * @param month     - month in which the friendship was created
     * @return a collection of said friendships
     * @throws ValidationException if month is invalid
     */
    public Iterable<Friendship> getUserFriendshipsFromMonth(String userEmail, int month) throws ValidationException {
        if (month < 1 || month > 12)
            throw new ValidationException("Error: Invalid month!\n");

        return friendshipRepo.getUserFriendshipsFromMonth(userEmail, month);
    }

    /**
     * saves a friend request from sender to receiver
     *
     * @param senderEmail   - sender email
     * @param receiverEmail - receiver email
     * @throws ValidationException     - if sender Email and receiver are the same
     * @throws AdministrationException - if friend request already exists or if inverse exists
     */
    public void sendFriendRequest(String senderEmail, String receiverEmail) throws ValidationException, AdministrationException {
        friendRequestValidator.validateEmails(senderEmail, receiverEmail);
        if (friendshipRepo.get(new UnorderedPair<>(senderEmail, receiverEmail)) != null)
            throw new AdministrationException("Error: Users are already friends;\n");
        FriendRequest inverse = friendRequestRepository.get(new Pair<>(receiverEmail, senderEmail));
        if (inverse != null)
            throw new AdministrationException("Error: Can't send request! Receiver already sent request to sender;\n");
        FriendRequest friendRequest = new FriendRequest(senderEmail, receiverEmail, LocalDateTime.now());
        boolean success=friendRequestRepository.save(friendRequest);
        if(!success)
            throw new AdministrationException("Error: A friend request has already been sent!;\n");
    }

    /**
     * confirms a friend request based on accepted status
     *
     * @param senderEmail   - sender's email
     * @param receiverEmail - receiver's email
     * @param accepted      - acceptance status (true is accept, false decline)
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void confirmFriendRequest(String senderEmail, String receiverEmail, boolean accepted)
            throws ValidationException, AdministrationException {

        friendRequestValidator.validateEmails(senderEmail, receiverEmail);
        boolean success=friendRequestRepository.delete(new Pair<>(senderEmail, receiverEmail));
        if(!success)
            throw new AdministrationException("No friend request from sender to receiver exists;\n");
        if (accepted)
            friendshipRepo.save(new Friendship(senderEmail, receiverEmail, LocalDate.now()));
    }

    /**
     * retracts the friend request sent by a sender to a receiver
     *
     * @param senderEmail   - sender's email
     * @param receiverEmail - receiver's email
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void retractFriendRequest(String senderEmail, String receiverEmail)
            throws ValidationException, AdministrationException {

        friendRequestValidator.validateEmails(senderEmail, receiverEmail);
        boolean success=friendRequestRepository.delete(new Pair<>(senderEmail, receiverEmail));
        if(!success)
            throw new AdministrationException("No friend request from sender to receiver exists;\n");
    }

    /**
     * gets all friend requests as collection
     *
     * @return a collection of said friend requests
     */
    public Iterable<FriendRequest> getAllFriendRequests() {
        return friendRequestRepository.getAll();
    }

    /**
     * gets all friend requests of a user as a collection
     *
     * @param userEmail -> said user's emails
     * @return a collection of said friend requests
     */
    public Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        return friendRequestRepository.getFriendRequestsSentToUser(userEmail);
    }

}
