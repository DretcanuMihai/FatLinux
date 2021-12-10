package com.example.map_toysocialnetworkgui.model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Integer> {
    private final String fromEmail;
    private final List<String> toEmails;
    private final String messageText;
    private final LocalDateTime sendTime;
    private final Integer parentMessageId;

    /**
     * creates a message with an id, email of sender, emails of receivers, message content and date of sending
     *
     * @param id              - message id
     * @param fromEmail       - email of sender
     * @param toEmails        - emails of receivers
     * @param messageText     - message content
     * @param sendTime        - date of sending
     * @param parentMessageId - id of parent message (can be null)
     */
    public Message(Integer id, String fromEmail, List<String> toEmails, String messageText, LocalDateTime sendTime,
                   Integer parentMessageId) {
        super(id);
        this.fromEmail = fromEmail;
        this.toEmails = new ArrayList<>(toEmails);
        this.messageText = messageText;
        this.sendTime = sendTime;
        this.parentMessageId = parentMessageId;
    }

    /**
     * gets the email of sender
     *
     * @return - said email
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * gets the emails of receivers
     *
     * @return - a list of said emails
     */
    public List<String> getToEmails() {
        return new ArrayList<>(toEmails);
    }

    /**
     * gets content of message
     *
     * @return said content
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * gets the time when message was sent
     *
     * @return said time
     */
    public LocalDateTime getSendTime() {
        return sendTime;
    }

    /**
     * gets the repliedMessageId
     *
     * @return said repliedMessageId
     */
    public Integer getParentMessageId() {
        return parentMessageId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromEmail='" + fromEmail + '\'' +
                ", toEmails=" + toEmails +
                ", messageText='" + messageText + '\'' +
                ", sendTime=" + sendTime +
                ", repliedMessageId=" + parentMessageId +
                '}';
    }
}
