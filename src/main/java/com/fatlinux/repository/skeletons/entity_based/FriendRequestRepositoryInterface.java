package com.fatlinux.repository.skeletons.entity_based;

import com.fatlinux.model.entities.FriendRequest;
import com.fatlinux.model.entities.User;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PagingRepository;
import com.fatlinux.utils.structures.Pair;

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

    /**
     * gets the nr of new friend requests sent to user
     *
     * @param user -> said user
     * @return a page of said friend requests
     */
    int getNewFriendRequestCount(User user);
}
