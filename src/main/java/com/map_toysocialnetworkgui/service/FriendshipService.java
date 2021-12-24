package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.validators.FriendshipValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.FriendshipRepositoryInterface;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.time.LocalDate;

/**
 * a class that incorporates a service that works with friendship administration
 */
public class FriendshipService {
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
     * @param friendshipRepo          - said friend repository
     * @param friendshipValidator     - said friend validator
     */
    public FriendshipService(FriendshipRepositoryInterface friendshipRepo, FriendshipValidator friendshipValidator)
    {

        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
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
        Friendship result=friendshipRepo.save(friendship);
        if(result!=null)
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
        Friendship friendship = friendshipRepo.findOne(new UnorderedPair<>(userEmail1, userEmail2));
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
        Friendship result=friendshipRepo.delete(new UnorderedPair<>(userEmail1, userEmail2));
        if(result==null)
            throw new AdministrationException("Error: users weren't friends;\n");
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


}
