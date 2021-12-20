package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.skeletons.CRUDRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * a class that incorporates a service that works with friendship administration
 */
public class FriendshipService {
    /**
     * associated friendship repo
     */
    private final CRUDRepository<UnorderedPair<String>, Friendship> friendshipRepo;
    /**
     * associated friendship validator
     */
    private final FriendshipValidator friendshipValidator;
    CRUDRepository<Pair<String,String>, FriendRequest> friendRequestCRUDRepository;
    FriendRequestValidator friendRequestValidator;

    /**
     * creates a friendship service based on a friendship repository and validator
     * @param friendshipRepo - said repository
     * @param friendshipValidator - said validator
     */
    public FriendshipService(CRUDRepository<UnorderedPair<String>, Friendship> friendshipRepo,
                             FriendshipValidator friendshipValidator, CRUDRepository<Pair<String,String>,
                                                                                                   FriendRequest> friendRequestCRUDRepository, FriendRequestValidator friendRequestValidator) {

        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
        this.friendRequestCRUDRepository = friendRequestCRUDRepository;
        this.friendRequestValidator=friendRequestValidator;
    }

    /**
     * adds a friendship between two users identified by email
     * Said user emails are assumed to belong to actual valid users
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException - if the emails are the same
     * @throws AdministrationException - if the friendship already exists
     */
    public void addFriendship(String userEmail1, String userEmail2) throws ValidationException,AdministrationException {
        Friendship friendship=new Friendship(userEmail1,userEmail2, LocalDate.now());
        friendshipValidator.validateDefault(friendship);
        if(friendshipRepo.tryGet(new UnorderedPair<>(userEmail1,userEmail2))!=null)
            throw new AdministrationException("Users are already friends!\n");
        friendshipRepo.save(friendship);
    }

    /**
     * gets a friendship of two users identified by email
     * said emails are assumed to belong to actual valid users
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @return said friendship
     * @throws ValidationException - if the emails are equal
     * @throws AdministrationException - if a friendship doesn't exist between the two users
     */
    public Friendship getFriendship(String userEmail1, String userEmail2)
            throws ValidationException,AdministrationException{
        friendshipValidator.validateEmails(userEmail1,userEmail2);
        Friendship friendship=friendshipRepo.tryGet(new UnorderedPair<>(userEmail1,userEmail2));
        if(friendship==null)
            throw new AdministrationException("Users aren't friends!\n");
        return friendship;
    }

    /**
     * deletes a friendship between two users identified by email
     * Said user emails are assumed to belong to actual valid users
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException - if the emails are the same
     * @throws AdministrationException - if the friendship doesn't exist
     */
    public void deleteFriendship(String userEmail1, String userEmail2) throws ValidationException,AdministrationException {
        friendshipValidator.validateEmails(userEmail1,userEmail2);
        if(friendshipRepo.tryGet(new UnorderedPair<>(userEmail1,userEmail2))==null)
            throw new AdministrationException("Users aren't friends!\n");
        friendshipRepo.delete(new UnorderedPair<>(userEmail1,userEmail2));
    }

    /**
     * gets all existing friendships
     * @return a collection of said friendships
     */
    public Collection<Friendship> getAllFriendships() {
        return friendshipRepo.getAll();
    }

    /**
     * gets all existing friendships to which a user belongs
     * the email is assumed to belong to an actual valid user
     * @param userEmail - said user's email
     * @return a collection of said friendships
     */
    public Collection<Friendship> getUserFriendships(String userEmail) {
        return friendshipRepo.getAll().stream().filter(friendship -> (friendship.getEmails().getFirst().equals(userEmail) ||
                friendship.getEmails().getSecond().equals(userEmail))).toList();
    }

    /**
     * Returns all friendships of a user that were created in a specific month
     * @param userEmail - email of user
     * @param month - month in which the friendship was created
     * @return a collection of said friendships
     * @throws ValidationException if month is invalid
     */
    public Collection<Friendship> getUserFriendshipsFromMonth(String userEmail, int month) throws ValidationException {
        if (month < 1 || month > 12)
            throw new ValidationException("Invalid month!\n");

        Predicate<Friendship> predicateMonth = friendship -> friendship.getBeginDate().getMonth().getValue() == month;
        Predicate<Friendship> predicateUser = friendship -> friendship.getEmails().getFirst().equals(userEmail)
                || friendship.getEmails().getSecond().equals(userEmail);

        return friendshipRepo.getAll().stream().filter(predicateMonth.and(predicateUser)).toList();
    }

    /**
     * saves a friend request from sender to receiver
     * @param senderEmail - sender email
     * @param receiverEmail - receiver email
     * @throws ValidationException - if sender Email and receiver are the same
     * @throws AdministrationException - if friend request already exists or if inverse exists
     */
    public void addFriendRequest(String senderEmail, String receiverEmail) throws ValidationException,AdministrationException {
        friendshipValidator.validateEmails(senderEmail,receiverEmail);
        if(friendshipRepo.tryGet(new UnorderedPair<>(senderEmail,receiverEmail))!=null)
            throw new AdministrationException("Users are already friends;\n");
        FriendRequest inverse= friendRequestCRUDRepository.tryGet(new Pair<>(receiverEmail,senderEmail));
        if(inverse!=null)
            throw new AdministrationException("Can't send request! Receiver already sent request to sender;\n");
        if(friendRequestCRUDRepository.tryGet(new Pair<>(senderEmail,receiverEmail))!=null)
            throw new AdministrationException("Friend request from sender to receiver already exists;\n");
        FriendRequest friendRequest=new FriendRequest(senderEmail,receiverEmail, LocalDateTime.now());
        friendRequestCRUDRepository.save(friendRequest);
    }

    /**
     * confirms a friend request based on accepted status
     * @param senderEmail - sender's email
     * @param receiverEmail - receiver's email
     * @param accepted - acceptance status (true is accept, false decline)
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void confirmFriendRequest(String senderEmail, String receiverEmail,boolean accepted)
            throws ValidationException,AdministrationException{

        friendshipValidator.validateEmails(senderEmail,receiverEmail);
        FriendRequest friendRequest= friendRequestCRUDRepository.tryGet(new Pair<>(senderEmail,receiverEmail));
        if(friendRequest==null)
            throw new AdministrationException("No friend request from sender to receiver exists;\n");
        friendRequestCRUDRepository.delete(new Pair<>(senderEmail,receiverEmail));
        if(accepted)
            friendshipRepo.save(new Friendship(senderEmail,receiverEmail,LocalDate.now()));
    }

    /**
     * gets all friend requests as collection
     * @return a collection of said friend requests
     */
    public Collection<FriendRequest> getAllFriendRequests() {
        return friendRequestCRUDRepository.getAll();
    }

    /**
     * gets all friend requests of a user as a collection
     * @param userEmail -> said user's emails
     * @return a collection of said friend requests
     */
    public Collection<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        return friendRequestCRUDRepository.getAll().stream()
                .filter(request->request.getReceiver().equals(userEmail))
                .toList();
    }

}
