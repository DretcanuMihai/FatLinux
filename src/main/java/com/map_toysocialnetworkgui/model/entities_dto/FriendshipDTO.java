package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.model.entities.Friendship;
import com.map_toysocialnetworkgui.model.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO class for friendship entities
 */
public class FriendshipDTO {
    /**
     * begin date info
     */
    private final LocalDateTime beginDate;

    /**
     * users info
     */
    private final UserDTO user1, user2;

    /**
     * creates a Friendship DTO for a friendship between two users
     * user1 and user2 must be the friendship's users - this isn't verified
     *
     * @param friendship - said friendship
     * @param user1      - first user
     * @param user2      - second user
     */
    public FriendshipDTO(Friendship friendship, User user1, User user2) {
        this.beginDate = friendship.getBeginDate();
        this.user1 = new UserDTO(user1);
        this.user2 = new UserDTO(user2);
    }

    /**
     * gets the date the friendship begun
     *
     * @return said date
     */
    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    /**
     * gets the first user's DTO
     *
     * @return said DTO
     */
    public UserDTO getUser1() {
        return user1;
    }

    /**
     * gets the second user's DTO
     *
     * @return said DTO
     */
    public UserDTO getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "beginDate=" + beginDate +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
