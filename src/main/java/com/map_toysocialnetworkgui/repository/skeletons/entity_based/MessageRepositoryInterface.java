package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.paging.PagingRepository;

/**
 * the interface for a generic paging message repository
 */
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
     * @param pageable   - pageable for pagination
     * @return said page
     */
    Page<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2, Pageable pageable);

    /**
     * gets an iterable of all the messages sent to a user sorted chronologically
     *
     * @param userEmail - said user's email
     * @return said iterable
     */
    Iterable<Message> getMessagesReceivedByUserChronologically(String userEmail);

    /**
     * gets a page of all the messages sent to a user sorted chronologically
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return said page
     */
    Page<Message> getMessagesReceivedByUserChronologically(String userEmail, Pageable pageable);

    /**
     * gets an iterable of all the messages sent by a user sorted chronologically
     *
     * @param userEmail - said user's email
     * @return said iterable
     */
    Iterable<Message> getMessagesSentByUserChronologically(String userEmail);

    /**
     * gets a page of all the messages sent by a user sorted chronologically
     *
     * @param userEmail - said user's email
     * @param pageable  - for paging
     * @return said page
     */
    Page<Message> getMessagesSentByUserChronologically(String userEmail, Pageable pageable);
}
