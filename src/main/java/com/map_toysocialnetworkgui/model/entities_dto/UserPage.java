package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.repository.paging.Page;

public class UserPage {
    UserDTO userInfo;
    Page<EventDTO> currentNotification;
    Page<EventDTO> currentEvents;
    Page<MessageDTO> currentSent;
    Page<MessageDTO> currentReceived;
    Page<FriendshipDTO> currentFriend;
    Page<FriendRequestDTO> currentRequest;

    /**
     * creates a page dto for a user's account
     * @param userInfo - dto with user's info
     * @param currentNotification - first notification page
     * @param currentEvents - first events page
     * @param currentSent - first sent message page
     * @param currentReceived - first received message page
     * @param currentFriend - first friend page
     * @param currentRequest - first request page
     */
    public UserPage(UserDTO userInfo, Page<EventDTO> currentNotification, Page<EventDTO> currentEvents,
                    Page<MessageDTO> currentSent,Page<MessageDTO> currentReceived, Page<FriendshipDTO> currentFriend,
                    Page<FriendRequestDTO> currentRequest) {
        this.userInfo = userInfo;
        this.currentNotification = currentNotification;
        this.currentEvents = currentEvents;
        this.currentSent = currentSent;
        this.currentReceived = currentReceived;
        this.currentFriend = currentFriend;
        this.currentRequest = currentRequest;
    }

    /**
     * gets user info
     * @return - said user's info
     */
    public UserDTO getUserInfo() {
        return userInfo;
    }

    /**
     * gets current notifications page
     * @return - said page
     */
    public Page<EventDTO> getCurrentNotification() {
        return currentNotification;
    }

    /**
     * gets current events page
     * @return - said page
     */
    public Page<EventDTO> getCurrentEvents() {
        return currentEvents;
    }

    /**
     * gets current sent messages page
     * @return - said page
     */
    public Page<MessageDTO> getCurrentSent() {
        return currentSent;
    }

    /**
     * gets current received messages page
     * @return - said page
     */
    public Page<MessageDTO> getCurrentReceived() {
        return currentReceived;
    }

    /**
     * gets current friend page
     * @return - said page
     */
    public Page<FriendshipDTO> getCurrentFriend() {
        return currentFriend;
    }

    /**
     * gets current requests page
     * @return - said page
     */
    public Page<FriendRequestDTO> getCurrentRequest() {
        return currentRequest;
    }

    /**
     * sets user's info
     * @param userInfo - new info
     */
    public void setUserInfo(UserDTO userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * sets current notification page
     * @param currentNotification - new page
     */
    public void setCurrentNotification(Page<EventDTO> currentNotification) {
        this.currentNotification = currentNotification;
    }

    /**
     * sets current events page
     * @param currentEvents - new page
     */
    public void setCurrentEvents(Page<EventDTO> currentEvents) {
        this.currentEvents = currentEvents;
    }

    /**
     * sets current sent message page
     * @param currentSent - new page
     */
    public void setCurrentSent(Page<MessageDTO> currentSent) {
        this.currentSent = currentSent;
    }

    /**
     * sets current received page
     * @param currentReceived - new page
     */
    public void setCurrentReceived(Page<MessageDTO> currentReceived) {
        this.currentReceived = currentReceived;
    }

    /**
     * sets current friend page
     * @param currentFriend - new page
     */
    public void setCurrentFriend(Page<FriendshipDTO> currentFriend) {
        this.currentFriend = currentFriend;
    }

    /**
     * sets current request page
     * @param currentRequest - new page
     */
    public void setCurrentRequest(Page<FriendRequestDTO> currentRequest) {
        this.currentRequest = currentRequest;
    }
}
