package com.map_toysocialnetworkgui.model.entities;

import com.map_toysocialnetworkgui.utils.structures.Pair;

import java.time.LocalDateTime;

/**
 * Class that describes a friend request
 */
public class FriendRequest extends Entity<Pair<String, String>> {
    /**
     * time at which message was sent
     */
    LocalDateTime sendTime;

    /**
     * Constructor
     *
     * @param sender   - user that sent the friend request
     * @param receiver - user that will receive the friend request
     * @param sendTime - the time when the request was sent
     */
    public FriendRequest(String sender, String receiver, LocalDateTime sendTime) {
        super(new Pair<>(sender, receiver));
        this.sendTime = sendTime;
    }

    /**
     * @return the user that sent the request
     */
    public String getSender() {
        return getId().getFirst();
    }

    /**
     * @return the user that received the request
     */
    public String getReceiver() {
        return getId().getSecond();
    }

    /**
     * @return the time when the request was sent
     */
    public LocalDateTime getSendTime() {
        return sendTime;
    }
}
