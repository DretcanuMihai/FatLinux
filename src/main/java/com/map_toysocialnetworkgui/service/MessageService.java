package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.validators.MessageValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.CRUDException;
import com.map_toysocialnetworkgui.repository.with_db.MessageDBRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * a class that incorporates a service that works with message administration
 */
public class MessageService {
    /**
     * associated message repo
     */
    private final MessageDBRepository messageRepo;

    /**
     * associated message validator
     */
    private final MessageValidator messageValidator;

    /**
     * constructs a messageService having a specified message repo and validator
     *
     * @param messageRepo      - said message repo
     * @param messageValidator - said message validator
     */
    public MessageService(MessageDBRepository messageRepo, MessageValidator messageValidator) {
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
    public Message getMessageBy(Integer id) throws ValidationException, AdministrationException {
        messageValidator.validateID(id);
        Message message = messageRepo.tryGet(id);
        if (message == null)
            throw new AdministrationException("No message with given parent message id exists!;\n");
        return message;
    }

    /**
     * saves a root message
     *
     * @param fromEmail   - the sender email
     * @param toEmails    - a list of the recipients of the message
     * @param messageText - the message's text
     * @throws ValidationException - if the message is invalid
     * @throws CRUDException       - if the id is already in use
     */
    public void addRootMessage(String fromEmail, List<String> toEmails, String messageText) throws ValidationException, CRUDException {
        Message message = new Message(0, fromEmail, toEmails, messageText, LocalDateTime.now(), null);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
    }

    /**
     * saves a reply message
     *
     * @param fromEmail      - the sender email
     * @param messageText    - the message's text
     * @param replyMessageID - the parent message's id
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not one of the recipients
     * @throws CRUDException           - if the id is already in use
     */
    public void addReplyMessage(String fromEmail, String messageText, Integer replyMessageID) throws ValidationException,
            AdministrationException, CRUDException {

        Message parentMessage = getMessageBy(replyMessageID);
        if (!parentMessage.getToEmails().contains(fromEmail))
            throw new AdministrationException("Error: User is not one of the recipients of the message!;\n");
        Message message = new Message(0, fromEmail, List.of(parentMessage.getFromEmail()), messageText, LocalDateTime.now(),
                replyMessageID);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
    }

    /**
     * saves a reply message sent to every person who can see the original message
     *
     * @param fromEmail      - the sender email
     * @param messageText    - the message's text
     * @param replyMessageID - the parent message's id
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not one of the recipients
     * @throws CRUDException           - if the id is already in use
     */
    public void addReplyAllMessage(String fromEmail, String messageText, Integer replyMessageID) throws ValidationException,
            AdministrationException, CRUDException {

        Message parentMessage = getMessageBy(replyMessageID);
        if (!parentMessage.getToEmails().contains(fromEmail))
            throw new AdministrationException("Error: User is not one of the recipients of the message!;\n");
        List<String> receivers = parentMessage.getToEmails();
        receivers.add(parentMessage.getFromEmail());
        receivers.remove(fromEmail);
        Message message = new Message(0, fromEmail, receivers, messageText, LocalDateTime.now(),
                replyMessageID);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
    }

    /**
     * gets a list of all the messages between two users sorted chronologically
     *
     * @param userEmail1 - first user email
     * @param userEmail2 - second user email
     * @return said list of messages
     * @throws ValidationException - if user emails are the same
     */
    public List<Message> getMessagesBetweenUsersChronologically(String userEmail1, String userEmail2) throws ValidationException {
        if (userEmail1.equals(userEmail2))
            throw new ValidationException("Error: user emails must be different;\n");
        return messageRepo.getAll().stream().filter(message -> {
            String sender = message.getFromEmail();
            List<String> toEmails = message.getToEmails();
            return (sender.equals(userEmail1) && toEmails.contains(userEmail2)) ||
                    (sender.equals(userEmail2) && toEmails.contains(userEmail1));
        }).sorted(Comparator.comparing(Message::getSendTime)).toList();
    }
}
