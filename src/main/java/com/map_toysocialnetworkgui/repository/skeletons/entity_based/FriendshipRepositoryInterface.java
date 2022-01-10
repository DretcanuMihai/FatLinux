package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PagingRepository;
import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.time.LocalDate;

/**
 * the interface for a generic paging friendship repository
 */
public interface FriendshipRepositoryInterface extends PagingRepository<UnorderedPair<String>, Friendship> {
    /**
     * gets all existing friendships to which a user belongs
     *
     * @param userEmail - said user's email
     * @return an iterable of said friendships
     */
    Iterable<Friendship> getUserFriendships(String userEmail);

    /**
     * gets all existing friendships to which a user belongs, friendships created in a given month
     *
     * @param userEmail - said user's email
     * @param month     - said month's number
     * @return an iterable of said friendships
     */
    Iterable<Friendship> getUserFriendshipsFromMonth(String userEmail, int month);

    /**
     * gets a page of the existing friendships to which a user belongs
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return the said page
     */
    Page<Friendship> getUserFriendships(String userEmail, Pageable pageable);

    /**
     * gets a page of the existing friendships to which a user belongs, friendships created in a given month
     *
     * @param userEmail - said user's email
     * @param month     - said month's number
     * @param pageable  - for paging
     * @return the page
     */
    Page<Friendship> getUserFriendshipsFromMonth(String userEmail, int month, Pageable pageable);

    /**
     * gets a page of the existing friendships to which a user belongs, friendships created in a given interval
     *
     * @param userEmail - said user's email
     * @param begin - begin of the interval
     * @param end - end of the interval
     * @param pageable  - for paging
     * @return the page
     */
    Page<Friendship> getUserFriendshipsFromInterval(String userEmail, LocalDate begin, LocalDate end, Pageable pageable);
}
