package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.repository.paging.Page;

public class UserPage {
    UserDTO userInfo;
    int nrOfNotifications;
    int nrOfNewFriends;
    int nrOfNewRequests;
    int nrOfNewMessages;

    /**
     * creates the info shown on the user page on login
     * @param userInfo - said user's info
     * @param nrOfNotifications - said user's number of notifications
     * @param nrOfNewFriends - said user's number of new friends
     * @param nrOfNewRequests - said user's number of new requests
     * @param nrOfNewMessages - said user's number of new messages
     */
    public UserPage(UserDTO userInfo, int nrOfNotifications, int nrOfNewFriends, int nrOfNewRequests, int nrOfNewMessages) {
        this.userInfo = userInfo;
        this.nrOfNotifications = nrOfNotifications;
        this.nrOfNewFriends = nrOfNewFriends;
        this.nrOfNewRequests = nrOfNewRequests;
        this.nrOfNewMessages = nrOfNewMessages;
    }

    /**
     * gets user info
     * @return - said user's info
     */
    public UserDTO getUserInfo() {
        return userInfo;
    }

    /**
     * gets number of notifications
     * @return - said number
     */
    public int getNrOfNotifications() {
        return nrOfNotifications;
    }

    /**
     * gets number of new friends
     * @return - said number
     */
    public int getNrOfNewFriends() {
        return nrOfNewFriends;
    }

    /**
     * gets number of new requests
     * @return - said number
     */
    public int getNrOfNewRequests() {
        return nrOfNewRequests;
    }

    /**
     * gets number of new messages
     * @return - said number
     */
    public int getNrOfNewMessages() {
        return nrOfNewMessages;
    }
}
