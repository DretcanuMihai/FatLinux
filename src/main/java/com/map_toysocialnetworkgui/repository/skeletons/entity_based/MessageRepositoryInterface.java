package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PagingRepository;

public interface MessageRepositoryInterface extends PagingRepository<Integer, Message> {

    /**
     * gets an iterable of all the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @return said iterable
     */
    Iterable<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2);

    /**
     * gets a page of the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @param pageable - pageable for pagination
     * @return said page
     */
    Page<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2, Pageable pageable);
}
