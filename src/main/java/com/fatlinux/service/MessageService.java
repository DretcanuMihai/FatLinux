package com.fatlinux.service;

import com.fatlinux.model.entities.Message;
import com.fatlinux.model.entities.User;
import com.fatlinux.model.validators.MessageValidator;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.skeletons.entity_based.MessageRepositoryInterface;
import com.fatlinux.utils.events.ChangeEventType;
import com.fatlinux.utils.events.EntityModificationObsEvent;
import com.fatlinux.utils.observer.AbstractObservable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * class that incorporates a service that works with message administration
 */
public class MessageService extends AbstractObservable<EntityModificationObsEvent<Integer>> {
    /**
     * associated message repo
     */
    private final MessageRepositoryInterface messageRepo;

    /**
     * associated message validator
     */
    private final MessageValidator messageValidator;

    /**
     * constructs a message service having a specified message repo and validator
     *
     * @param messageRepo      - said message repo
     * @param messageValidator - said message validator
     */
    public MessageService(MessageRepositoryInterface messageRepo, MessageValidator messageValidator) {
        this.messageRepo = messageRepo;
        this.messageValidator = messageValidator;
    }

    /**
     * gets a message by its id
     *
     * @param id - said id
     * @return said message
     * @throws ValidationException     - if id is invalid
     * @throws AdministrationException - if no message with requested id exists
     */
    public Message getMessage(Integer id) throws ValidationException, AdministrationException {
        messageValidator.validateID(id);
        Message message = messageRepo.findOne(id);
        if (message == null)
            throw new AdministrationException("No message with given id exists!;\n");
        return message;
    }

    /**
     * saves a root message
     *
     * @param fromEmail   - sender email
     * @param toEmails    - a list of destination emails
     * @param messageText - message's text
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the id is already in use
     */
    public void addRootMessage(String fromEmail, List<String> toEmails, String messageText, String messageSubject)
            throws ValidationException, AdministrationException {

        Message message = new Message(null, fromEmail, toEmails, messageText, messageSubject, LocalDateTime.now(), null);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.ADD, message.getId()));
    }

    /**
     * saves a reply message
     *
     * @param fromEmail   - sender email
     * @param messageText - message's text
     * @param parentID    - parent message id
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not a recipient
     */
    public void addReplyMessage(String fromEmail, String messageText, String messageSubject, Integer parentID)
            throws ValidationException, AdministrationException {

        Message parentMessage = getMessage(parentID);
        if (!parentMessage.getToEmails().contains(fromEmail))
            throw new AdministrationException("Error: User is not one of the recipients of the message!;\n");
        Message message = new Message(null, fromEmail, List.of(parentMessage.getFromEmail()), messageText,
                messageSubject, LocalDateTime.now(), parentID);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.ADD, message.getId()));
    }

    /**
     * saves a reply message sent to every person who can see the original message
     *
     * @param fromEmail   - sender email
     * @param messageText - message's text
     * @param parentID    - parent message id
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not one of the recipients
     */
    public void addReplyAllMessage(String fromEmail, String messageText, String messageSubject, Integer parentID)
            throws ValidationException, AdministrationException {

        Message parentMessage = getMessage(parentID);
        if (!parentMessage.getToEmails().contains(fromEmail) && !parentMessage.getFromEmail().equals(fromEmail))
            throw new AdministrationException("Error: User is not one of the recipients of the message!;\n");
        List<String> receivers = parentMessage.getToEmails();
        receivers.add(parentMessage.getFromEmail());
        receivers.remove(fromEmail);
        Message message = new Message(0, fromEmail, receivers, messageText, messageSubject, LocalDateTime.now(),
                parentID);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.ADD, message.getId()));
    }

    /**
     * returns the conversation between two users sorted chronologically
     *
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return an iterable of the messages in the conversation
     * @throws ValidationException if the emails are the same
     */
    public Iterable<Message> getConversationBetweenUsers(String email1, String email2) throws ValidationException {
        if (Objects.equals(email1, email2))
            throw new ValidationException("Error: user emails must be different;\n");
        return messageRepo.getMessagesBetweenUsersChronologically(email1, email2);
    }

    /**
     * returns a page of the conversation between two users sorted chronologically
     *
     * @param email1   - first user's email
     * @param email2   - second user's email
     * @param pageable - for paging
     * @return said page
     * @throws ValidationException if the emails are the same
     */
    public Page<Message> getConversationBetweenUsers(String email1, String email2, Pageable pageable) throws ValidationException {
        if (Objects.equals(email1, email2))
            throw new ValidationException("Error: user emails must be different;\n");
        return messageRepo.getMessagesBetweenUsersChronologically(email1, email2, pageable);
    }

    /**
     * returns the messages received by a user
     *
     * @param email - first user's email
     * @return an iterable of the messages
     */
    public Iterable<Message> getMessagesReceivedByUser(String email) {
        return messageRepo.getMessagesReceivedByUserChronologically(email);
    }

    /**
     * returns the messages received by a user
     *
     * @param email    - first user's email
     * @param pageable - for paging
     * @return said page
     */
    public Page<Message> getMessagesReceivedByUser(String email, Pageable pageable) {
        return messageRepo.getMessagesReceivedByUserChronologically(email, pageable);
    }

    /**
     * returns the messages sent by an user
     *
     * @param email - first user's email
     * @return an iterable of the messages
     */
    public Iterable<Message> getMessagesSentByUser(String email) {
        return messageRepo.getMessagesSentByUserChronologically(email);
    }

    /**
     * returns the messages sent by an user
     *
     * @param email    - first user's email
     * @param pageable - for paging
     * @return said page
     */
    public Page<Message> getMessagesSentByUser(String email, Pageable pageable) {
        return messageRepo.getMessagesSentByUserChronologically(email, pageable);
    }

    /**
     * gets a page of all the messages received by user in a given interval
     *
     * @param userEmail - said user's email
     * @param begin - begin of said interval
     * @param end - end of said interval
     * @param pageable  - for paging
     * @return said page
     */
    public Page<Message> getMessagesToUserInInterval(String userEmail, LocalDate begin, LocalDate end, Pageable pageable) {
        return messageRepo.getMessagesToUserInInterval(userEmail, begin, end, pageable);
    }

    /**
     * gets a page of all the messages received by user in a given interval from another user
     *
     * @param userEmail - said user's email
     * @param friendEmail - said friend's email
     * @param begin - begin of said interval
     * @param end - end of said interval
     * @param pageable  - for paging
     * @return said page
     */
    public Page<Message> getMessagesToUserFromFriendInInterval(String userEmail,String friendEmail, LocalDate begin, LocalDate end, Pageable pageable) {
        return messageRepo.getMessagesToUserFromFriendInInterval(userEmail,friendEmail, begin, end, pageable);
    }

    public int getUserNewMessagesCount(User user) {
        return messageRepo.getUserNewMessagesCount(user);
    }
}
