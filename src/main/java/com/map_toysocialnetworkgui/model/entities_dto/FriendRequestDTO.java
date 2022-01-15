package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;
import com.map_toysocialnetworkgui.model.entities.User;

import java.time.LocalDateTime;

/**
 * DTO class for friend request entity
 */
public class FriendRequestDTO {
    /**
     * send date info
     */
    private final LocalDateTime sendTime;

    /**
     * users info
     */
    private final UserDTO sender, receiver;

    /**
     * creates a FriendRequest DTO for a friendship between two users
     * sender and receiver must be the friend request's users - this isn't verified
     *
     * @param friendRequest - said friend request
     * @param sender        - sender user
     * @param receiver      - receiver user
     */
    public FriendRequestDTO(FriendRequest friendRequest, User sender, User receiver) {
        this.sendTime = friendRequest.getSendTime();
        this.sender = new UserDTO(sender);
        this.receiver = new UserDTO(receiver);
    }

    /**
     * gets the date the request was sent
     *
     * @return said date
     */
    public LocalDateTime getSendTime() {
        return sendTime;
    }

    /**
     * gets the sender's DTO
     *
     * @return said DTO
     */
    public UserDTO getSender() {
        return sender;
    }

    /**
     * gets the receiver's DTO
     *
     * @return said DTO
     */
    public UserDTO getReceiver() {
        return receiver;
    }

    /**
     * gets the sender's first name
     *
     * @return said first name
     */
    public String getSenderFirstName() {
        return sender.getFirstName();
    }

    /**
     * gets the sender's last name
     *
     * @return said last name
     */
    public String getSenderLastName() {
        return sender.getLastName();
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "beginDate=" + sendTime +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
