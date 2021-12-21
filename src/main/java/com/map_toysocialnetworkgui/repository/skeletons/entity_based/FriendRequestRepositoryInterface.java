package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.repository.skeletons.CRUDRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;

public interface FriendRequestRepositoryInterface extends
        CRUDRepository<Pair<String, String>, FriendRequest> {


    /**
     * gets all friend requests sent to a user as a collection
     *
     * @param userEmail -> said user's emails
     * @return a collection of said friend requests
     */
    Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail);
}
