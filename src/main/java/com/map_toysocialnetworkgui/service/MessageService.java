package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Message;
import com.map_toysocialnetworkgui.model.entities_dto.MessageDTO;
import com.map_toysocialnetworkgui.model.validators.MessageValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.MessageRepositoryInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * a class that incorporates a service that works with message administration
 */
public class MessageService {
    /**
     * associated message repo
     */
    private final MessageRepositoryInterface messageRepo;

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
        Message message = messageRepo.get(id);
        if (message == null)
            throw new AdministrationException("No message with given id exists!;\n");
        return message;
    }

    /**
     * saves a root message
     *
     *
     * @param dto - needed data
     * @throws ValidationException - if the message is invalid
     * @throws AdministrationException       - if the id is already in use
     */
    public void addRootMessage(MessageDTO dto) throws ValidationException, AdministrationException {
        Message message = new Message(null, dto.getFromEmail(),dto.getToEmails(),dto.getMessageText(), LocalDateTime.now(), null);
        messageValidator.validateDefault(message);
        messageRepo.save(message);
    }

    /**
     * saves a reply message
     *
     *
     * @param dto - needed data
     * @throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not a recipient
     */
    public void addReplyMessage(MessageDTO dto)
            throws ValidationException, AdministrationException {

        Message parentMessage = getMessage(dto.getParentMessageId());
        if (!parentMessage.getToEmails().contains(dto.getFromEmail()))
            throw new AdministrationException("Error: User is not one of the recipients of the message!;\n");
        Message message = new Message(null, dto.getFromEmail(), List.of(parentMessage.getFromEmail()), dto.getMessageText(), LocalDateTime.now(),
                dto.getParentMessageId());
        messageValidator.validateDefault(message);
        messageRepo.save(message);
    }

    /**
     * saves a reply message sent to every person who can see the original message
     *
     *
     * @param dto@throws ValidationException     - if the message is invalid
     * @throws AdministrationException - if the user is not one of the recipients
     */
    public void addReplyAllMessage(MessageDTO dto)
            throws ValidationException, AdministrationException {

        String fromEmail=dto.getFromEmail();
        String messageText=dto.getMessageText();
        Integer replyMessageID=dto.getParentMessageId();

        Message parentMessage = getMessage(replyMessageID);
        if (!parentMessage.getToEmails().contains(fromEmail) && !parentMessage.getFromEmail().equals(fromEmail))
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
     * returns the conversation between two users sorted chronologically
     *
     * @param email1 - first user's email
     * @param email2 - second user's email
     * @return a list of DTOs for said messages
     * @throws ValidationException if the emails are the same
     */
    public Iterable<Message> getConversationBetweenUsers(String email1, String email2) throws ValidationException {
        if (Objects.equals(email1, email2))
            throw new ValidationException("Error: user emails must be different;\n");
        return messageRepo.getMessagesBetweenUsersChronologically(email1, email2);
    }
}
