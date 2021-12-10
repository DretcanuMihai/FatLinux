package com.example.map_toysocialnetworkgui.model.entities_dto;

import com.example.map_toysocialnetworkgui.model.entities.Message;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class for message entities
 */
public class MessageDTO {
    private final Integer id;
    private final String fromEmail;
    private final List<String> toEmails;
    private final String messageText;
    private final LocalDateTime sendTime;
    private final Integer parentMessageId;

    /**
     * creates a message DTO for a message
     *
     * @param message - said message
     */
    public MessageDTO(Message message) {
        id = message.getId();
        fromEmail = message.getFromEmail();
        toEmails = message.getToEmails();
        messageText = message.getMessageText();
        sendTime = message.getSendTime();
        parentMessageId = message.getParentMessageId();
    }

    /**
     * gets message id
     *
     * @return said id
     */
    public Integer getId() {
        return id;
    }

    /**
     * gets sender email
     *
     * @return said email
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * gets receiver email
     *
     * @return said email
     */
    public List<String> getToEmails() {
        return toEmails;
    }

    /**
     * gets the text of the message
     *
     * @return said text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * gets the time when the message was sent
     *
     * @return said time
     */
    public LocalDateTime getSendTime() {
        return sendTime;
    }

    /**
     * gets the id of the parent message
     *
     * @return said id
     */
    public Integer getParentMessageId() {
        return parentMessageId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ",fromEmail='" + fromEmail + '\'' +
                ", toEmails=" + toEmails +
                ", messageText='" + messageText + '\'' +
                ", sendTime=" + sendTime +
                ", repliedMessageId=" + parentMessageId +
                '}';
    }
}
