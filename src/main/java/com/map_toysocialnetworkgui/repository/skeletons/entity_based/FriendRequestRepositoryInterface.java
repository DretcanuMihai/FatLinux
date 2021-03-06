package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PagingRepository;
import com.map_toysocialnetworkgui.utils.structures.Pair;

/**
 * the interface for a generic paging friend request repository
 */
public interface FriendRequestRepositoryInterface extends PagingRepository<Pair<String, String>, FriendRequest> {
    /**
     * gets all friend requests sent to a user as an iterable
     *
     * @param userEmail -> said user's emails
     * @return an iterable of said friend requests
     */
    Iterable<FriendRequest> getFriendRequestsSentToUser(String userEmail);

    /**
     * gets page of user friend requests
     *
     * @param userEmail -> said user's emails
     * @param pageable  -> pageable for paging
     * @return a page of said friend requests
     */
    Page<FriendRequest> getFriendRequestsSentToUser(String userEmail, Pageable pageable);

    /**
     * gets all friend requests sent by a user as an iterable
     *
     * @param userEmail -> said user's emails
     * @return an iterable of said friend requests
     */
    Iterable<FriendRequest> getFriendRequestsSentByUser(String userEmail);

    /**
     * gets page of user's sent friend requests
     *
     * @param userEmail -> said user's emails
     * @param pageable  -> pageable for paging
     * @return a page of said friend requests
     */
    Page<FriendRequest> getFriendRequestsSentByUser(String userEmail, Pageable pageable);
}
