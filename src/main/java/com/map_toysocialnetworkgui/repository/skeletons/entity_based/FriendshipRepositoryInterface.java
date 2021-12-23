package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.repository.skeletons.CRUDRepository;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

public interface FriendshipRepositoryInterface extends CRUDRepository<UnorderedPair<String>, Friendship> {


    /**
     * gets all existing friendships to which a user belongs
     *
     * @param userEmail - said user's email
     * @return a collection of said friendships
     */
    Iterable<Friendship> getUserFriendships(String userEmail);

    /**
     * gets all existing friendships to which a user belongs, friendships created in a given month
     *
     * @param userEmail - said user's email
     * @param month     - said month's number
     * @return a collection of said friendships
     */
    Iterable<Friendship> getUserFriendshipsFromMonth(String userEmail, int month);
}