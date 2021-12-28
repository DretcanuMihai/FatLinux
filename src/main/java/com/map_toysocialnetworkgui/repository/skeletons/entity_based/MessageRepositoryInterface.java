package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PagingRepository;

public interface MessageRepositoryInterface extends PagingRepository<Integer, Message> {

    /**
     * gets a list of all the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @return said list of messages
     */
    Iterable<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2);

    /**
     * gets a list of all the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @param pageable - pageable for pagination
     * @return said list of messages
     */
    Page<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2, Pageable pageable);
}
