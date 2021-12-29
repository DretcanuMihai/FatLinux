package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.model.validators.FriendRequestValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendRequestRepositoryInterface;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.AbstractObservable;
import com.map_toysocialnetworkgui.utils.structures.Pair;

import java.time.LocalDateTime;

public class FriendRequestService extends AbstractObservable<EntityModificationEvent<Pair<String,String>>> {

    /**
     * associated friend request repo
     */
    private final FriendRequestRepositoryInterface friendRequestRepository;
    /**
     * associated friend request validator
     */
    private final FriendRequestValidator friendRequestValidator;

    /**
     * creates a friend request service based on a friendship repository and validator and
     * friend request repository and validator
     *
     * @param friendRequestRepository - said friend request repository
     * @param friendRequestValidator  - said friend request validator
     */
    public FriendRequestService(FriendRequestRepositoryInterface friendRequestRepository,
                                FriendRequestValidator friendRequestValidator) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRequestValidator = friendRequestValidator;
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
        FriendRequest inverse = friendRequestRepository.findOne(new Pair<>(receiverEmail, senderEmail));
        if (inverse != null)
            throw new AdministrationException("Error: Can't send request! Receiver already sent request to sender;\n");
        FriendRequest friendRequest = new FriendRequest(senderEmail, receiverEmail, LocalDateTime.now());
        FriendRequest result=friendRequestRepository.save(friendRequest);
        if(result!=null)
            throw new AdministrationException("Error: A friend request has already been sent!;\n");
        notifyObservers(new EntityModificationEvent<>(ChangeEventType.ADD,friendRequest.getId()));
    }

    /**
     * deletes the friend request sent by a sender to a receiver
     *
     * @param senderEmail   - sender's email
     * @param receiverEmail - receiver's email
     * @throws ValidationException     - if any data is invalid
     * @throws AdministrationException - if any administrative problem occurs
     */
    public void deleteFriendRequest(String senderEmail, String receiverEmail)
            throws ValidationException, AdministrationException {

        friendRequestValidator.validateEmails(senderEmail, receiverEmail);
        FriendRequest result=friendRequestRepository.delete(new Pair<>(senderEmail, receiverEmail));
        if(result==null)
            throw new AdministrationException("No friend request from sender to receiver exists;\n");
        notifyObservers(new EntityModificationEvent<>(ChangeEventType.ADD,result.getId()));
    }

    /**
     * gets all friend requests as an iterable
     *
     * @return an iterable of said friend requests
     */
    public Iterable<FriendRequest> getAllFriendRequests() {
        return friendRequestRepository.findAll();
    }

    /**
     * gets all friend requests of a user as an iterable
     *
     * @param userEmail -> said user's emails
     * @return said iterable
     */
    public Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail) {
        return friendRequestRepository.getFriendRequestsSentToUser(userEmail);
    }

    /**
     * gets a page of all the friend requests
     *
     * @param pageable - for paging
     * @return said page
     */
    public Page<FriendRequest> getAllFriendRequests(Pageable pageable) {
        return friendRequestRepository.findAll(pageable);
    }

    /**
     * gets a page of a user's friend requests
     *
     * @param userEmail -> said user's emails
     * @param pageable - for paging
     * @return said page
     */
    public Page<FriendRequest> getFriendRequestsSentToUser(String userEmail,Pageable pageable) {
        return friendRequestRepository.getFriendRequestsSentToUser(userEmail,pageable);
    }

}