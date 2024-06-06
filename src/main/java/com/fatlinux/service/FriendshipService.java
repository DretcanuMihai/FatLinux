package com.fatlinux.service;

import com.fatlinux.model.entities.Friendship;
import com.fatlinux.model.entities.User;
import com.fatlinux.model.validators.FriendshipValidator;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.skeletons.entity_based.FriendshipRepositoryInterface;
import com.fatlinux.utils.events.ChangeEventType;
import com.fatlinux.utils.events.EntityModificationObsEvent;
import com.fatlinux.utils.observer.AbstractObservable;
import com.fatlinux.utils.structures.UnorderedPair;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * class that incorporates a service that works with friendship administration
 */
public class FriendshipService extends AbstractObservable<EntityModificationObsEvent<UnorderedPair<String>>> {
    /**
     * associated friendship repo
     */
    private final FriendshipRepositoryInterface friendshipRepo;

    /**
     * associated friendship validator
     */
    private final FriendshipValidator friendshipValidator;

    /**
     * creates a friendship service based on a friendship repository and validator
     *
     * @param friendshipRepo      - said friend repository
     * @param friendshipValidator - said friend validator
     */
    public FriendshipService(FriendshipRepositoryInterface friendshipRepo, FriendshipValidator friendshipValidator) {
        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
    }

    /**
     * adds a friendship between two users identified by email
     * said user emails are assumed to belong to actual valid users
     *
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException     - if the emails are the same
     * @throws AdministrationException - if the friendship already exists
     */
    public void addFriendship(String userEmail1, String userEmail2) throws ValidationException, AdministrationException {
        Friendship friendship = new Friendship(userEmail1, userEmail2, LocalDateTime.now());
        friendshipValidator.validateDefault(friendship);
        Friendship result = friendshipRepo.save(friendship);
        if (result != null)
            throw new AdministrationException("Error: users are already friends;\n");
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.ADD, friendship.getId()));
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
        Friendship friendship = friendshipRepo.findOne(new UnorderedPair<>(userEmail1, userEmail2));
        if (friendship == null)
            throw new AdministrationException("Error: Users aren't friends!\n");
        return friendship;
    }

    /**
     * deletes a friendship between two users identified by email
     * said user emails are assumed to belong to actual valid users
     *
     * @param userEmail1 - first user's email
     * @param userEmail2 - second user's email
     * @throws ValidationException     - if the emails are the same
     * @throws AdministrationException - if the friendship doesn't exist
     */
    public void deleteFriendship(String userEmail1, String userEmail2) throws ValidationException, AdministrationException {
        friendshipValidator.validateEmails(userEmail1, userEmail2);
        Friendship result = friendshipRepo.delete(new UnorderedPair<>(userEmail1, userEmail2));
        if (result == null)
            throw new AdministrationException("Error: users weren't friends;\n");
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.DELETE, new UnorderedPair<>(userEmail1, userEmail2)));
    }

    /**
     * gets all existing friendships
     *
     * @return a collection of said friendships
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
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
     * returns all friendships of a user that were created in a specific month
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
     * gets all existing friendships
     *
     * @param pageable - for paging
     * @return a collection of said friendships
     */
    public Page<Friendship> getAllFriendships(Pageable pageable) {
        return friendshipRepo.findAll(pageable);
    }

    /**
     * gets a page of all existing friendships to which a user belongs
     * the email is assumed to belong to an actual valid user
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return a page of said friendships
     */
    public Page<Friendship> getUserFriendships(String userEmail, Pageable pageable) {
        return friendshipRepo.getUserFriendships(userEmail, pageable);
    }

    /**
     * returns a page of all friendships of a user that were created in a specific month
     *
     * @param userEmail - email of user
     * @param month     - month in which the friendship was created
     * @param pageable  - for paging
     * @return a page of said friendships
     * @throws ValidationException if month is invalid
     */
    public Page<Friendship> getUserFriendshipsFromMonth(String userEmail, int month, Pageable pageable) throws ValidationException {
        if (month < 1 || month > 12)
            throw new ValidationException("Error: Invalid month!\n");
        return friendshipRepo.getUserFriendshipsFromMonth(userEmail, month, pageable);
    }

    /**
     * returns a page of all friendships of a user that were created in a specific month
     *
     * @param userEmail - email of user
     * @param begin - begin of the interval
     * @param end  - end of the interval
     * @param pageable  - for paging
     * @return a page of said friendships
     * @throws ValidationException if month is invalid
     */
    public Page<Friendship> getUserFriendshipsFromInterval(String userEmail, LocalDate begin, LocalDate end, Pageable pageable) throws ValidationException {
        return friendshipRepo.getUserFriendshipsFromInterval(userEmail,begin,end,pageable);
    }

    public int getUserNewFriendshipsCount(User user) {
        return friendshipRepo.getUserNewFriendshipsCount(user);
    }
}
