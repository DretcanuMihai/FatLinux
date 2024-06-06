package com.fatlinux.repository.skeletons.entity_based;

import com.fatlinux.model.entities.User;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PagingRepository;

public interface UserRepositoryInterface extends PagingRepository<String, User> {

    /**
     * gets all users who have a string in their name
     *
     * @param string -> said string
     * @return an iterable of said users
     */
    Iterable<User> getUsersByName(String string);

    /**
     * gets a page of all users who have a string in their name
     *
     * @param string   -> said string
     * @param pageable -> for paging
     * @return said page
     */
    Page<User> getUsersByName(String string, Pageable pageable);
}
